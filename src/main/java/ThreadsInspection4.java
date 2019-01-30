import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class ThreadsInspection4 {
  private interface StrPredicate extends Predicate<String> {
    @Override
    default StrPredicate negate() {
      return str -> !test(str);
    }

    default StrPredicate contains(String str) {
      return s -> test(s) && s.contains(str);
    }

    default StrPredicate containsNot(String str) {
      return s -> test(s) && !s.contains(str);
    }
  }

  public static void main(String[] args) throws Exception {

    URL threadsUrl = Objects.requireNonNull(ThreadsInspection3.class.getResource("threads4.txt"), "no threads");

    List<String> blocks = Pattern.compile("\r?\n\r?\n\"")
        .splitAsStream(Resources.toString(threadsUrl, Charsets.UTF_8))
        .map(str -> "\"" + str)
        .map(String::trim)
        .filter(ThreadsInspection4::isThread)
        .collect(Collectors.toList());

    System.out.println("####\n|| Name || Count || Example Thread IDs ||");
    printTableRow(blocks, "threads total", str -> true);
    printTableRow(blocks, "threads blocked", blocked());
    printTableRow(blocks, "threads handling requests", ThreadsInspection4::isRequest);
    printTableRow(blocks, "threads waiting for 431/57201", blocked(), contains("owned by http-bio-10571-exec-431 Id=57201"));
    printTableRow(blocks, "threads waiting for 462/57532", blocked(), contains("owned by http-bio-10571-exec-462 Id=57532"));
    printTableRow(blocks, "threads waiting for 455/57520", blocked(), contains("owned by http-bio-10571-exec-455 Id=57520"));

    printTableRow(blocks, "threads running in aoz", not(blocked()), contains("DatabaseAozProgramComponent"));

    printTableRow(blocks, "threads blocked in aoz in request", blocked(), contains("DatabaseAozProgramComponent"),
        contains("com.dasburo.audi.v5.controller.Controller.handleRequest"));
    printTableRow(blocks, "threads blocked in aoz not in request", blocked().contains("DatabaseAozProgramComponent")
        .containsNot("com.dasburo.audi.v5.controller.Controller.handleRequest"));
  }

  private static void printTableRow(List<String> blocks, String name, StrPredicate... matcher) {
    List<String> list = blocks.stream()
        .filter(str -> Arrays.stream(matcher)
            .allMatch(pred -> pred.test(str)))
        .collect(Collectors.toList());
    Collections.shuffle(list);
    StringJoiner sj = new StringJoiner(" | ", "| ", " |");
    sj.add(name);
    sj.add(String.valueOf(list.size()));
    sj.add(list.stream()
        .limit(5)
        .map(ThreadsInspection4::threadId)
        .collect(Collectors.joining(", ")));
    System.out.println(sj.toString());
  }

  private static StrPredicate contains(String search) {
    return str -> str.contains(search);
  }

  private static StrPredicate not(StrPredicate pred) {
    return str -> !pred.test(str);
  }

  private static StrPredicate blocked() {
    return str -> str.contains(" in BLOCKED");
  }

  private static String threadId(String str) {
    Matcher matcher = Pattern.compile(" Id=(\\d+) ")
        .matcher(str);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  private static boolean isThread(String str) {
    return Pattern.compile(" Id=\\d+ ")
        .matcher(str)
        .find();
  }

  private static boolean isRequest(String str) {
    return str.contains("com.dasburo.audi.v5.controller.Controller.handleRequest");
  }
}
