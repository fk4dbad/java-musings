import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class StringJoinerVsStream {

	private static final int TEST_SIZE = 100;

	@Test
	public void testStringBuilder() {
		List<String> joins = new ArrayList<>(TEST_SIZE);
		for (int i = 0; i < TEST_SIZE; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < i; j++) {
				if (j > 0) sb.append(", ");
				sb.append(String.valueOf(j));
			}
			joins.add(sb.toString());
		}
		assertFalse(joins.isEmpty());
	}

	@Test
	public void testStringJoiner() {
		List<String> joins = new ArrayList<>(TEST_SIZE);
		for (int i = 0; i < TEST_SIZE; i++) {
			StringJoiner sj = new StringJoiner(", ");
			for (int j = 0; j < i; j++) {
				sj.add(String.valueOf(j));
			}
			joins.add(sj.toString());
		}
		assertFalse(joins.isEmpty());
	}

	@Test
	public void testStream() throws Exception {
		List<String> joins = new ArrayList<>(TEST_SIZE);
		for (int i = 0; i < TEST_SIZE; i++) {
			joins.add(IntStream.range(0, i)
					.mapToObj(Integer::toString)
					.collect(Collectors.joining(", ")));
		}
		assertFalse(joins.isEmpty());
	}
}
