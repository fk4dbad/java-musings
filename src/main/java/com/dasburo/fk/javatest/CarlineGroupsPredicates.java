package com.dasburo.fk.javatest;

import java.util.function.Predicate;

import com.dasburo.fk.javatest.objects.CarlineGroup;

public class CarlineGroupsPredicates {

  public static Predicate<CarlineGroup> isInGroup(String prefix) {
    return carlineGroup -> carlineGroup.getId().startsWith(prefix);
  }

}
