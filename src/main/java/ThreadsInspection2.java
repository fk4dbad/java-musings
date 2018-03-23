import java.net.URL;
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

public class ThreadsInspection2 {

  static class WeightedOwnage {
    String ownedBy;
    
  }
  
  public static void main(String[] args) throws Exception {

    URL threadsUrl = Objects.requireNonNull(ThreadsInspection2.class.getResource("threads2.txt"), "no threads");
    
    List<String> blocks = Pattern.compile("\r?\n\r?\n\"")
        .splitAsStream(Resources.toString(threadsUrl, Charsets.UTF_8))
        .map(str -> "\"" + str)
        .map(str -> str.trim())
        .filter(str -> isThread(str))
        .collect(Collectors.toList());

    System.out.println("####\n|| Name || Count || Example Thread IDs ||");
    printTableRow(blocks, "threads total", str -> true);
    printTableRow(blocks, "threads blocked", str -> isBlocked(str));
    printTableRow(blocks, "threads handling requests", str -> isRequest(str));
    printTableRow(blocks, "requests blocked", str -> isRequest(str) && isBlocked(str));
    printTableRow(blocks, "requests not blocked", str -> isRequest(str) && !isBlocked(str));

    printTableRow(blocks, "threads blocked not requests", str -> isBlocked(str) && !isRequest(str));

    printTableRow(blocks, "threads blocked in getSetupDocument", str -> isBlocked(str) && isInGetSetupDocument(str));
    printTableRow(blocks, "threads blocked not in getSetupDocument", str -> isBlocked(str) && !isInGetSetupDocument(str));
    printTableRow(blocks, "threads running in getSetupDocument", str -> !isBlocked(str) && isInGetSetupDocument(str));
    printTableRow(blocks, "threads blocked in getSetupDocument not requests", str -> isBlocked(str) && !isRequest(str) && isInGetSetupDocument(str));

    printTableRow(blocks, "threads blocked by thread Id=34330", str -> isBlocked(str) && ownedBy(str, 34330));
    printTableRow(blocks, "threads blocked by thread Id=34330", str -> isBlocked(str) && ownedBy(str, 34330) && !isRequest(str));
  }

  private static boolean ownedBy(String str, int i) {
    return Pattern.compile("owned by .+ Id=" + i).matcher(str).find();
  }

  private static boolean isInGetSetupDocument(String str) {
    return str.contains("com.dasburo.setupfwk.registry.ApplicationSetupRegistry.getSetupDocument(ApplicationSetupRegistry.java:346)");
  }

  private static void printTableRow(List<String> blocks, String name, Predicate<String> matcher) {
    List<String> list = blocks.stream()
        .filter(matcher)
        .collect(Collectors.toList());
    Collections.shuffle(list);
    StringJoiner sj = new StringJoiner(" | ", "| ", " |");
    sj.add(name);
    sj.add(String.valueOf(list.size()));
    sj.add(list.stream()
        .limit(5)
        .map(str -> threadId(str))
        .collect(Collectors.joining(", ")));
    System.out.println(sj.toString());
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

  private static boolean isBlocked(String str) {
    return str.contains(" in BLOCKED");
  }

  private static boolean isRequest(String str) {
    return str.contains("com.dasburo.audi.v5.controller.Controller.handleRequest");
  }
}
