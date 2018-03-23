import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class StringJoinerVsStream {

	private static final int TEST_SIZE = 100;

  @Test
  void testStringJoiner() {
    List<String> joins = new ArrayList<>(TEST_SIZE);
    IntStream.range(0, TEST_SIZE)
        .forEach(i -> {
          StringJoiner sj = new StringJoiner(", ");
          IntStream.range(0, i)
              .mapToObj(String::valueOf)
              .forEach(sj::add);
          joins.add(sj.toString());
        });
    assertFalse(joins.isEmpty());
  }

	@Test
  void testStringBuilder() {
    List<String> joins = new ArrayList<>(TEST_SIZE);
    for (int i = 0; i < TEST_SIZE; i++) {
      String sb = IntStream.range(0, i)
          .mapToObj(String::valueOf)
          .collect(Collectors.joining(", "));
      joins.add(sb);
    }
    assertFalse(joins.isEmpty());
	}

	@Test
  void testStream() throws Exception {
    List<String> joins = IntStream.range(0, TEST_SIZE)
        .mapToObj(i -> IntStream.range(0, i)
            .mapToObj(Integer::toString)
            .collect(Collectors.joining(", ")))
        .collect(Collectors.toCollection(() -> new ArrayList<>(TEST_SIZE)));
    assertFalse(joins.isEmpty());
  }
}
