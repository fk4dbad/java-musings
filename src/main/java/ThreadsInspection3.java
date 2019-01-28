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

public class ThreadsInspection3 {
  private interface StrPredicate extends Predicate<String> {
  }

  public static void main(String[] args) throws Exception {

    URL threadsUrl = Objects.requireNonNull(ThreadsInspection3.class.getResource("threads3.txt"), "no threads");

    List<String> blocks = Pattern.compile("\r?\n\r?\n\"")
        .splitAsStream(Resources.toString(threadsUrl, Charsets.UTF_8))
        .map(str -> "\"" + str)
        .map(String::trim)
        .filter(ThreadsInspection3::isThread)
        .collect(Collectors.toList());

    System.out.println("####\n|| Name || Count || Example Thread IDs ||");
    printTableRow(blocks, "threads total", str -> true);
    printTableRow(blocks, "threads blocked", ThreadsInspection3::isBlocked);
    printTableRow(blocks, "threads handling requests", ThreadsInspection3::isRequest);
    String techMixMix = "TechdataStructureMixerComponent.mix";
    printTableRow(blocks, "threads blocked in mixer.mix", ThreadsInspection3::isBlocked, contains(techMixMix));
    printTableRow(blocks, "threads not blocked in mixer.mix", thread -> !isBlocked(thread), contains(techMixMix));
    printTableRow(blocks, "threads blocked in mixer.mix in DCCWS", ThreadsInspection3::isBlocked, contains(techMixMix), contains("DCCWSImpl"));
    printTableRow(blocks, "threads blocked in mixer.mix in DPU", ThreadsInspection3::isBlocked, contains(techMixMix), contains("NemoJSONRequestHandler"));
    printTableRow(blocks, "threads blocked in mixer.mix in VelocityEngine", ThreadsInspection3::isBlocked, contains(techMixMix), contains("VelocityEngine"));
    printTableRow(blocks, "threads blocked in mixer.mix in Carstore-v2", ThreadsInspection3::isBlocked, contains(techMixMix), contains("v2.GetConfigurationComponent"));
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
        .map(ThreadsInspection3::threadId)
        .collect(Collectors.joining(", ")));
    System.out.println(sj.toString());
  }

  private static StrPredicate contains(String search) {
    return str -> str.contains(search);
  }

  private static StrPredicate notContains(String search) {
    return str -> !str.contains(search);
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
