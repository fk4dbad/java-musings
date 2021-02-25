package com.dasburo.fk.javatest;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ParallelThreadLocalTest {
  public static void main(String[] args) throws Exception {
    int[] numbers = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
    Accumulator.addOne();
    Thread thread = new Thread(() -> IntStream.of(numbers).forEach(i -> {
      Accumulator.start();
      IntStream.range(0, i).parallel().forEach(j -> Accumulator.addOne());
      Accumulator.stop();
      System.out.println("expecting " + i + ", counted: " + Accumulator.getValue());
      Accumulator.reset();
    }), "another-thread");
    thread.start();
    thread.join();
    System.out.println("expecting main to be 1: " + Accumulator.getValue());
  }
}

class Accumulator {
  private static final ThreadLocal<AtomicInteger> COUNTER = ThreadLocal.withInitial(AtomicInteger::new);

  static void start() {
  }

  static void stop() {
  }

  static void reset() {
    COUNTER.get().set(0);
  }

  static void addOne() {
    COUNTER.get().incrementAndGet();
  }

  static int getValue() {
    return COUNTER.get().get();
  }
}
