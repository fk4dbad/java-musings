import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ThreadsInspection {

  public static void main(String[] args) throws Exception {

    URL threadsUrl = Objects.requireNonNull(ThreadsInspection.class.getResource("/threads.txt"), "no threads");
    String threadsStr = new String(Files.readAllBytes(Paths.get(threadsUrl.toURI())), Charset.forName("UTF-8"));

    List<String> blocks = Pattern.compile("\r?\n\r?\n")
        .splitAsStream(threadsStr)
        .map(String::trim)
        .filter(ThreadsInspection::isThread)
        .collect(Collectors.toList());
    Predicate<String> unknownBlocked =
        str -> isBlocked(str) && !isNanoCache301(str) && !isNemoDataComponent(str) && !isInInterior(str) && !isTaskQueue(str) && !isSingularity(str) && !isThreadPool(str) && !isTimeoutServiceEngine(
            str) && !isMQ(str);

    //blocks.stream().filter(unknownBlocked).forEach(str -> System.out.println("####\n" + str));

    System.out.println("####\n|| Name || Count || Examples ||");
    printTableRow(blocks, "threads total", str -> true);
    printTableRow(blocks, "threads blocked", ThreadsInspection::isBlocked);
    printTableRow(blocks, "threads in interior", ThreadsInspection::isInInterior);
    printTableRow(blocks, "threads in interior (not blocked)", str -> isInInterior(str) && !isBlocked(str));
    printTableRow(blocks, "threads blocked in InteriorDataMatrix", str -> isBlocked(str) && isInInterior(str));
    printTableRow(blocks, "threads blocked in InteriorDataMatrix in NemoJSONRequestHandler", str -> isBlocked(str) && isInInterior(str) && isInNemoJSONRequest(str));
    printTableRow(blocks, "threads in InteriorDataMatrix in NemoJSONRequestHandler (not blocked)", str -> !isBlocked(str) && isInInterior(str) && isInNemoJSONRequest(str));
    printTableRow(blocks, "threads in InteriorDataMatrix not in NemoJSONRequestHandler", str -> isInInterior(str) && !isInNemoJSONRequest(str));
    printTableRow(blocks, "threads blocked in NanoCacheImpl", str -> isBlocked(str) && isNanoCache301(str));
    printTableRow(blocks, "threads blocked in NanoCacheImpl not in interior", str -> isBlocked(str) && isNanoCache301(str) && !isInInterior(str) && !isNemoDataComponent(str));
    printTableRow(blocks, "threads blocked in NemoDataComponent.getObjectsForCarline", str -> isBlocked(str) && isNemoDataComponent(str));
    printTableRow(blocks, "threads blocked not in NemoDataComponent.getObjectsForCarline", str -> isBlocked(str) && !isNemoDataComponent(str));
    printTableRow(blocks, "threads blocked in TaskQueue", str -> isBlocked(str) && isTaskQueue(str));
    printTableRow(blocks, "threads blocked in singularity", str -> isBlocked(str) && isSingularity(str));
    printTableRow(blocks, "threads in singularity (not blocked)", str -> !isBlocked(str) && isSingularity(str));
    printTableRow(blocks, "threads blocked in ThreadPoolExecutor.getTask()", str -> isBlocked(str) && isThreadPool(str));
    printTableRow(blocks, "threads blocked in TimeoutServiceEngine", str -> isBlocked(str) && isTimeoutServiceEngine(str));
    printTableRow(blocks, "threads blocked in mq", str -> isBlocked(str) && isMQ(str));
    printTableRow(blocks, "threads blocked in other", unknownBlocked);
    printTableRow(blocks, "threads handling requests", ThreadsInspection::isRequest);
    printTableRow(blocks, "requests blocked", str -> isRequest(str) && isBlocked(str));
    printTableRow(blocks, "requests not blocked", str -> isRequest(str) && !isBlocked(str));
    printTableRow(blocks, "requests blocked not in getInteriorTiles", str -> isRequest(str) && isBlocked(str) && !isNemoDataComponent(str) && !isInInteriorTiles(str));
    printTableRow(blocks, "threads blocked not requests", str -> isBlocked(str) && !isRequest(str) && !isThreadPool(str));
  }

  private static boolean isInNemoJSONRequest(String str) {
    return str.contains("com.dasburo.audi.nemo.controllers.NemoJSONRequestHandler.handleRequest");
  }

  private static boolean isTimeoutServiceEngine(String str) {
    return str.contains("com.dasburo.badutil.timeoutServiceEngines");
  }

  private static boolean isThreadPool(String str) {
    return str.contains("java.util.concurrent.ThreadPoolExecutor.getTask()");
  }

  private static boolean isSingularity(String str) {
    return str.contains("com.singularity");
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
        .map(ThreadsInspection::threadId)
        .collect(Collectors.joining(", ")));
    System.out.println(sj.toString());
  }

  private static String threadId(String str) {
    Matcher matcher = Pattern.compile("Thread (\\d+):")
        .matcher(str);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  private static boolean isThread(String str) {
    return str.startsWith("Thread ");
  }

  private static boolean isBlocked(String str) {
    return str.contains("state = BLOCKED");
  }

  private static boolean isInInterior(String str) {
    return str.contains("com.dasburo.audi.ak4.prnrs.interior45.InteriorDataMatrix");
  }

  private static boolean isRequest(String str) {
    return str.contains("com.dasburo.audi.v5.controller.Controller.handleRequest");
  }

  private static boolean isNemoDataComponent(String str) {
    return str.contains("NemoDataComponent.getObjectsForCarline");
  }

  private static boolean isNanoCache301(String str) {
    for (String line : str.split("\r?\n")) {
      if (line.contains("NanoCacheImpl") && line.contains("line=301")) {
        return true;
      }
    }
    return false;
  }

  private static boolean isTaskQueue(String str) {
    return str.contains("org.apache.tomcat.util.threads.TaskQueue.poll");
  }

  private static boolean isMQ(String str) {
    return str.contains("com.ibm.mq");
  }

  private static boolean isInInteriorTiles(String str) {
    return str.contains("SyntheticContentFacadeComponent.getInteriorTiles");
  }
}
