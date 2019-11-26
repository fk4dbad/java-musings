package com.dasburo.fk.javatest;

import java.io.IOException;

class PolyThrowTest {

  interface ThrowsInterface<E extends Exception> {
    void doSomething() throws E;
  }

  static class MyException extends Exception {
    private static final long serialVersionUID = 1L;
  }

  static void throwNpe() throws NullPointerException {
    // nothing
  }

  /**
   * @throws IOException declaration test
   * @throws MyException declaration test
   */
  static void throwSeveral() throws IOException, MyException {
    // nothing
  }

  @SuppressWarnings("unchecked")
  static <E extends Exception> E catchException(ThrowsInterface<E> thrower) {
    try {
      thrower.doSomething();
      return null;
    } catch (Exception ex) {
      return (E) ex;
    }
  }

  public static void main(String[] args) {
    ThrowsInterface<NullPointerException> i1 = PolyThrowTest::throwNpe;
    catchException(i1); // NullPointerException inferred
    ThrowsInterface<?> i2 = PolyThrowTest::throwSeveral;
    catchException(i2); // (? extends) Exception inferred
  }

}
