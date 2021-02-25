package com.dasburo.fk.javatest;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdMatchingBenchmark {
  private static final Logger LOG = LoggerFactory.getLogger(IdMatchingBenchmark.class);

  private static final List<String> TEST_IDS =
      List.of("NEDC:CO2:COMBINED:PETROL:WEIGHTED:PRIMARY", "NEDC:CONSUMPTION:COMBINED:ELECTRICAL:WEIGHTED:SECONDARY",
          "NEDC:CONSUMPTION:COMBINED:PETROL:WEIGHTED:PRIMARY", "NEDC:RANGE:COMBINED:ELECTRICAL:WEIGHTED:SECONDARY",
          "WLTP:CO2:COMBINED:PETROL:WEIGHTED:PRIMARY", "WLTP:CONSUMPTION:COMBINED:ELECTRICAL:WEIGHTED:SECONDARY",
          "WLTP:CONSUMPTION:COMBINED:PETROL:WEIGHTED:PRIMARY",
          "WLTP:RANGE:CITY:ELECTRICAL:EQUIV_ALL_EL_RANGE:SECONDARY",
          "WLTP:RANGE:COMBINED:ELECTRICAL:EQUIV_ALL_EL_RANGE:SECONDARY");

  private static final List<String> PATTERNS =
      List.of("WLTP:CO2:COMBINED:PETROL:DEPLETING:*", "WLTP:CONSUMPTION:COMBINED:PETROL:*:SECONDARY",
          "WLTP:CONSUMPTION:COMBINED:ELECTRICAL:*:SECONDARY", "WLTP:CONSUMPTION:MEDIUM:*",
          "NEDC:CONSUMPTION:COMBINED:*:SECONDARY", "NEDC-MARGIN:CONSUMPTION:EXTRA_URBAN:PETROL:*",
          "WLTP:CO2:COMBINED:PETROL:WEIGHTED:PRIMARY");

  private static final List<Pattern> REGEXP =
      PATTERNS.stream().map(pat -> pat.replaceAll("\\*", ".*")).map(Pattern::compile).collect(Collectors.toList());

  public static boolean matchesPatternRegex(String attrId, Pattern entryId) {
    return entryId.matcher(attrId).matches();
  }

  public static boolean matchesPattern(String attrId, String entryId) {
    int i = 0;
    String lastPart = null;
    for (String part : StringUtils.splitPreserveAllTokens(attrId, '*')) {
      lastPart = part;
      if (part.isEmpty()) {
        continue;
      }
      int indexOf = entryId.indexOf(part, i);
      if (indexOf == -1) {
        return false;
      }
      i = indexOf + part.length();
    }
    return lastPart == null || entryId.endsWith(lastPart);
  }

  public static void main(String[] args) throws Exception {
    Main.main(args);
  }

  @Benchmark
  public void matchesStringSplit() {
    int matches = 0;
    for (String testId : TEST_IDS) {
      for (String pattern : PATTERNS) {
        if (matchesPattern(testId, pattern)) {
          matches++;
        }
      }
    }
    //LOG.info("matches: {}", matches);
  }

  @Benchmark
  public void matchesRegExp() {
    int matches = 0;
    for (String testId : TEST_IDS) {
      for (Pattern pattern : REGEXP) {
        if (matchesPatternRegex(testId, pattern)) {
          matches++;
        }
      }
    }
    //LOG.info("matches: {}", matches);
  }
}
