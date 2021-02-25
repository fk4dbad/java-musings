package com.dasburo.fk.javatest.exceptionhandling;

import java.util.function.Function;

public class ExceptionalTest {

  public static void main(String[] args) {

  }

  public interface ExceptionalFunction<K, V> {
    V tryApply(K key) throws Exception;
  }

  public static class Exceptional<T> {
    private final T object;
    private final Exception caught;

    private Exceptional(T object, Exception caught) {
      this.object = object;
      this.caught = caught;
    }

    public static <T> Exceptional<T> empty() {
      return new Exceptional<>(null, null);
    }

    public static <FK, FV> Function<FK, Exceptional<FV>> catching(ExceptionalFunction<FK, FV> tryFunction) {
      return key -> {
        try {
          FV value = tryFunction.tryApply(key);
          return new Exceptional<>(value, null);
        } catch (Exception fex) {
          return new Exceptional<>(null, fex);
        }
      };
    }

    public <EX extends Exception> Exceptional<T> rethrow(Class<EX> exClass) throws EX {
      if (isCaughtInstanceOf(exClass)) {
        throw exClass.cast(caught);
      }
      return this;
    }

    private <EX extends Exception> boolean isCaughtInstanceOf(Class<EX> exClass) {
      return caught != null && caught.getClass().isAssignableFrom(exClass);
    }
  }
}
