import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeekCounter {

  public static void main(String[] args) {
    LocalDate date = LocalDate.now()
        .withMonth(1)
        .withDayOfMonth(1);
    int thisYear = date.getYear();
    date = date.with(ChronoField.DAY_OF_WEEK, 1);
    if (date.getYear() < thisYear) {
      date = date.plusWeeks(1);
    }
    System.out.println("First: " + date);
    Map<String, Long> monthCount = Stream.iterate(date, d -> d.plusWeeks(2))
        .takeWhile(d -> d.getYear() == thisYear)
        .collect(Collectors.groupingBy(d -> d.getMonth() + ":" + d.getDayOfWeek(), Collectors.counting()));
    monthCount.entrySet()
        .stream()
        .sorted(Comparator.comparing(Entry::getKey))
        .forEach(mc -> System.out.println(String.format("%s => %d", mc.getKey(), mc.getValue())));
  }
}
