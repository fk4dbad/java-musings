package com.dasburo.fk.javatest.dataexcerpt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExcerptAnalysis {
  private static final Logger LOG = LoggerFactory.getLogger(DataExcerptAnalysis.class);

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    analyzeFile("/tmp/ids.txt");
    analyzeFile("/tmp/texts.txt");
  }

  private static void analyzeFile(String fileName) throws FileNotFoundException {
    String maxLine = null;
    int maxLength = 0;
    try (Scanner scan = new Scanner(new File(fileName))) {
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        if (line.length() > maxLength) {
          maxLength = line.length();
          maxLine = line;
        }
      }
    }
    LOG.info("file {}: longest line with {} chars: {}", fileName, maxLength, maxLine);
  }
}
