package com.dasburo.fk.javatest;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParallelStreamTest {
  private static final Logger LOG = LoggerFactory.getLogger(ParallelStreamTest.class);

  @Test
  public void testStuckParallelStream() throws Exception {
    IntStream.iterate(0, i -> i + 1)
        .parallel()
        .forEach(i -> getStuck(i));
  }

  private void getStuck(Integer i) {
    synchronized (i) {
      try {
        i.wait();
      } catch (InterruptedException ex) {
        LOG.info("interrupted", ex);
        Thread.currentThread()
            .interrupt();
      }
    }
  }
}
