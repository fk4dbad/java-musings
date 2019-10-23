import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;

public class DelegationTest {
  static final int NUM_DELEGATIONS = 90;
  static final int NUM_RETRIES = 1;
  static final int NUM_REPETITIONS = 5000000;

  @FunctionalInterface
  interface A {
    void doSomething();
  }

  static class BaseA implements A {
    AtomicInteger count = new AtomicInteger();

    @Override
    public void doSomething() {
      count.incrementAndGet();
    }
  }

  static class ADelegator implements A {
    A base;

    ADelegator(A base) {
      this.base = base;
    }

    @Override
    public void doSomething() {
      base.doSomething();
    }
  }

  static abstract class AAspect implements A {
    A base;

    AAspect(A base) {
      this.base = base;
    }

    abstract void withA(Consumer<A> func);

    @Override
    public void doSomething() {
      withA(a -> a.doSomething());
    }
  }

  private static void testDoSomething(UnaryOperator<A> newA) {
    BaseA baseA = new BaseA();
    A cut = baseA;
    for (int i = 0; i < NUM_DELEGATIONS; i++) {
      cut = newA.apply(cut);
    }
    for (int i = 0; i < NUM_REPETITIONS; i++) {
      cut.doSomething();
    }
    assertEquals(NUM_REPETITIONS * Math.pow(NUM_RETRIES, NUM_DELEGATIONS), baseA.count.get());
  }

  @Test
  public void testDelegator() throws Exception {
    testDoSomething(a -> new ADelegator(a) {
      @Override
      public void doSomething() {
        for (int i = 0; i < NUM_RETRIES; i++) {
          super.doSomething();
        }
      }
    });
  }

  @Test
  public void testAspect() throws Exception {
    testDoSomething(a -> new AAspect(a) {
      @Override
      void withA(Consumer<A> func) {
        for (int i = 0; i < NUM_RETRIES; i++) {
          func.accept(base);
        }
      }
    });
  }
}
