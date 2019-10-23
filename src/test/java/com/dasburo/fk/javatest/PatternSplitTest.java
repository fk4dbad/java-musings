package com.dasburo.fk.javatest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class PatternSplitTest {

  @Test
  void testPatternSplitAsStream() {
    assertSplit("a,b", "a", "b");
    assertSplit(",a,b", "", "a", "b");
    assertSplit(",,a,b", "", "", "a", "b");
    assertSplit("a,b,", "a", "b");
    assertSplit("a,b,,", "a", "b");
    assertSplit("a,,b,,", "a", "", "b");
    // => dismisses trailing empty strings, but not leading or inbetween empty strings
    // => use .filter(str -> !str.isEmpty()) to only use non-empty parts
  }

  private void assertSplit(String input, String... expectedParts) {
    assertEquals(Arrays.asList(expectedParts), Pattern.compile(",")
        .splitAsStream(input)
        .collect(Collectors.toList()));
  }

}
