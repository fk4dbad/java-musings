import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConvertTest {
  public static void main(String[] args) {
    Date date = new Date();
    OffsetDateTime from = OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
    String format = DateTimeFormatter.ISO_DATE_TIME.format(from);
    System.out.println(format);
  }
}
