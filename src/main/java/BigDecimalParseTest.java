import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BigDecimalParseTest {

  @Test
  public void testBDParse() throws Exception {
    Assertions.assertThrows(NumberFormatException.class, () -> {
      new BigDecimal("abc123");
    });
  }
}
