package com.dasburo.fk.javatest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.dasburo.fk.javatest.objects.CarlineGroup;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class CarlineGroupSorter {

  private final List<CarlineGroup> groupsToSort;
  private final ImmutableList<String> carlineGroupOrder;

  /**
   * sorts a list of carline groups by given order<br>
   * carlineGroupOrder contains id prefixes in the correct order, carline groups are then ordered by natural ordering
   * within one prefix range
   * 
   * @param groupsToSort list of {@link CarlineGroup} objects to sort
   * @param carlineGroupOrder String list containing the prefix ordering to apply
   */
  public CarlineGroupSorter(@Nonnull
      final List<CarlineGroup> groupsToSort, @Nonnull
      final ImmutableList<String> carlineGroupOrder) {
    Preconditions.checkArgument(groupsToSort != null, "groupsToSort must not be null");
    Preconditions.checkArgument(carlineGroupOrder != null, "carlineGroupOrder must not be null");
    Preconditions.checkArgument(!carlineGroupOrder.isEmpty(), "carlineGroupOrder must not be empty");
    this.groupsToSort = groupsToSort;
    this.carlineGroupOrder = carlineGroupOrder;
  }

  public ImmutableList<CarlineGroup> sort() {
    final LinkedHashMap<String, List<CarlineGroup>> carlineGroupsGroupedByPrefix = getCarlineGroupsGroupedByPrefix();

    List<CarlineGroup> sortedList = carlineGroupsGroupedByPrefix.values()
        .stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

    ImmutableList<CarlineGroup> unknownSortCarlineGroups = getUnknownGroups(carlineGroupsGroupedByPrefix);

    if (!unknownSortCarlineGroups.isEmpty()) {
      sortedList.addAll(unknownSortCarlineGroups);
    }
    return ImmutableList.copyOf(sortedList);
  }

  // create a map with each given carlineGroupOrder as key, and a list of carline groups ordered by natural order (id)
  // as value
  private LinkedHashMap<String, List<CarlineGroup>> getCarlineGroupsGroupedByPrefix() {
    LinkedHashMap<String, List<CarlineGroup>> groupedByPrefix = new LinkedHashMap<>();

    for (String prefix : this.carlineGroupOrder) {
      List<CarlineGroup> collectByPrefix = this.groupsToSort.stream()
          .filter(CarlineGroupsPredicates.isInGroup(prefix))
          .collect(Collectors.toList());
      List<CarlineGroup> sortInPrefix = collectByPrefix.stream()
          .sorted(Comparator.comparing(CarlineGroup::getName))
          .collect(Collectors.toList());
      groupedByPrefix.put(prefix, ImmutableList.copyOf(sortInPrefix));
    }

    return groupedByPrefix;
  }

  // compare the original groups list with list of already ordered groups to see if we have any that are not defined by
  // carlineGroupOrder
  // and return this list of "unknown" groups ordered by natural order of groupName
  private ImmutableList<CarlineGroup> getUnknownGroups(final Map<String, List<CarlineGroup>> sortedGroupsMap) {
    List<CarlineGroup> unknownSortGroups = new ArrayList<>();
    List<CarlineGroup> sortedGroups = sortedGroupsMap.values()
        .stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

    for (CarlineGroup testGroup : this.groupsToSort) {
      if (!sortedGroups.contains(testGroup)) {
        unknownSortGroups.add(testGroup);
      }
    }

    List<CarlineGroup> sortedNatural = unknownSortGroups.stream()
        .sorted(Comparator.comparing(CarlineGroup::getName))
        .collect(Collectors.toList());
    return ImmutableList.copyOf(sortedNatural);
  }

}
