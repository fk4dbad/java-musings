import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastTest {
  private static final Logger LOG = LoggerFactory.getLogger(CastTest.class);

  @Test
  @DisplayName("Cast by Exception")
  @RepeatedTest(2)
  public void testException(TestInfo ti) throws Exception {
    test(ob -> {
      try {
        return (String) ob;
      } catch (ClassCastException ex) {
        return null;
      }
    }, ti, 50);
  }

  @Test
  @DisplayName("Cast by Exception w/o fail")
  @RepeatedTest(2)
  public void testExceptionNoFail(TestInfo ti) throws Exception {
    test(ob -> {
      try {
        return (String) ob;
      } catch (ClassCastException ex) {
        return null;
      }
    }, ti, 0);
  }

  @Test
  @DisplayName("Cast by Instanceof")
  @RepeatedTest(2)
  public void testInstanceof(TestInfo ti) throws Exception {
    test(ob -> {
      if (ob instanceof String) {
        return (String) ob;
      }
      return null;
    }, ti, 50);
  }

  @Test
  @DisplayName("Cast by Instanceof w/o fail")
  @RepeatedTest(2)
  public void testInstanceofNoFail(TestInfo ti) throws Exception {
    test(ob -> {
      if (ob instanceof String) {
        return (String) ob;
      }
      return null;
    }, ti, 0);
  }

  private static void test(Function<Object, String> caster, TestInfo ti, int percentageFail) throws Exception {
    Random rnd = new Random(1337);
    Instant start = Instant.now();
    int count = 0;
    while (start.until(Instant.now(), ChronoUnit.MILLIS) < 3000) {
      caster.apply(rnd.nextInt(100) < percentageFail ? rnd.nextLong() : String.valueOf(rnd.nextLong()));
      count++;
    }
    LOG.warn("{}:\t{} casts", ti.getDisplayName(), count);
  }
}
