package com.dasburo.fk.javatest.random;

import java.util.concurrent.atomic.AtomicLong;

public class XorShift {
  private static final AtomicLong INSTANCE_COUNT = new AtomicLong();

  private final long instanceId = INSTANCE_COUNT.incrementAndGet();

  private long x64 = 1825619348293841298L;

  public XorShift() {
    this(null);
  }

  public XorShift(Long seed) {
    x64 = seed == null ? System.nanoTime() + instanceId : seed;
  }

  public synchronized long nextLong() {
    x64 ^= x64 << 13;
    x64 ^= x64 >> 7;
    x64 ^= x64 << 17;
    return x64 < 0 ? -x64 : x64;
  }

  public int nextInt(int bound) {
    return (int) (nextLong() % bound);
  }

  public static void main(String[] args) {
    XorShift xs1 = new XorShift(3000000000000000000L);
    XorShift xs2 = new XorShift(3000000000000000001L);
    XorShift xs3 = new XorShift(3000000000000000000L);
    XorShift xs4 = new XorShift(3000000000000000001L);
    for (int i = 1; i <= 100; i++) {
      System.out.println(String.format("run #%3d: %19d, %19d, %2d, %2d", i, xs1.nextLong(), xs2.nextLong(), xs3.nextInt(100), xs4.nextInt(100)));
    }
    XorShift xs5 = new XorShift();
    int[] histo = new int[10];
    for (int i = 0; i < 1000000; i++) {
      int n = xs5.nextInt(10);
      histo[n] += 1;
    }
    for (int i = 0; i < 10; i++) {
      System.out.println(String.format("#%2d: %6d => %5.2f %%", i, histo[i], histo[i] / 10000f));
    }
  }
}
