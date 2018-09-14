package com.dasburo.fk.javatest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dasburo.fk.javatest.objects.CarlineGroup;
import com.google.common.collect.ImmutableList;

class CarlineGroupComparatorTest {
  private static final Logger LOG = LoggerFactory.getLogger(CarlineGroupComparatorTest.class);

  @RepeatedTest(100)
  void test() {
    Comparator<CarlineGroup> comp = CarlineGroupComparators.of(ImmutableList.of("c", "a", "b"));
    assertNotNull(comp);

    List<CarlineGroup> sorted = ImmutableList.of( //
        new CarlineGroup("c3", "C3"), //
        new CarlineGroup("a1", "A1"), //
        new CarlineGroup("a2", "A2"), //
        new CarlineGroup("a3", "A3"), //
        new CarlineGroup("a4", "A4"), //
        new CarlineGroup("b1", "B1"), //
        new CarlineGroup("b2", "B2"), //
        new CarlineGroup("b3", "B3"), //
        new CarlineGroup("b3", "B3"), //
        new CarlineGroup("d3", "D3"), //
        new CarlineGroup("z3", "Z3"));
    List<CarlineGroup> shuffled = new ArrayList<>(sorted);
    Collections.shuffle(shuffled);
    LOG.info(shuffled.toString());

    List<CarlineGroup> resorted = shuffled.stream()
        .sorted(comp)
        .collect(Collectors.toList());
    LOG.info(resorted.toString());
    assertEquals(sorted, resorted);
  }

}
