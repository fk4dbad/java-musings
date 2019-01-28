import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class StringFormatTest {
  public static void main(String[] args) {
    double[] values = { 13.0, 3.14, 0.1234, 13.37, 133.7 };
    String[] patterns = { "0.00", "#0.00", "#.#", "#.0", "#.0#" };
    System.out.print(format("value"));
    Stream.of(patterns)
        .forEach(pattern -> System.out.print(format(pattern)));
    System.out.println("|");
    DoubleStream.of(values)
        .mapToObj(BigDecimal::new)
        .forEach(value -> {
          System.out.print(String.format("| %10.3f ", value));
          Stream.of(patterns)
              .forEach(pattern -> {
                DecimalFormat df = new DecimalFormat(pattern);
                System.out.print(format(df.format(value)));
              });
          System.out.println("|");
        });
  }

  private static String format(String value) {
    return String.format("| %10s ", value);
  }
}
