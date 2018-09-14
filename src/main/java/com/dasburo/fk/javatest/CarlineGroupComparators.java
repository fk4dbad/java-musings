package com.dasburo.fk.javatest;

import java.util.Comparator;
import java.util.stream.IntStream;

import com.dasburo.fk.javatest.objects.CarlineGroup;
import com.google.common.collect.ImmutableList;

public class CarlineGroupComparators {

  private CarlineGroupComparators() {
  }

  public static Comparator<CarlineGroup> of(ImmutableList<String> carlineGroupOrder) {
    return Comparator.<CarlineGroup> comparingInt(cg -> getRank(cg, carlineGroupOrder))
        .thenComparing(Comparator.comparing(CarlineGroup::getName));
  }

  private static int getRank(CarlineGroup cg, ImmutableList<String> carlineGroupOrder) {
    return IntStream.range(0, carlineGroupOrder.size())
        .filter(i -> CarlineGroupsPredicates.isInGroup(carlineGroupOrder.get(i))
            .test(cg))
        .findFirst()
        .orElse(carlineGroupOrder.size());
  }

  private static int getRankAlternative(CarlineGroup cg, ImmutableList<String> carlineGroupOrder) {
    for (int i = 0; i < carlineGroupOrder.size(); i++) {
      String prefix = carlineGroupOrder.get(i);
      if (CarlineGroupsPredicates.isInGroup(prefix)
          .test(cg)) {
        return i;
      }
    }
    return carlineGroupOrder.size();
  }

  private void someMethod() {
    Comparator<CarlineGroup> cmp1 = new Comparator<CarlineGroup>() {
      @Override
      public int compare(CarlineGroup o1, CarlineGroup o2) {
        return o1.getName()
            .compareTo(o2.getName());
      }
    };
    cmp1 = (o1, o2) -> o1.getName()
        .compareTo(o2.getName());
    cmp1 = Comparator.comparing(CarlineGroup::getName);
  }
}
