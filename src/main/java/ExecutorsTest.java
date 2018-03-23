import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorsTest {
  private static final int MAX_THREADS = 5;

  private static ExecutorService POOL;

  private static AtomicInteger counter = new AtomicInteger();

  public static void main(String[] args) throws Exception {
    POOL = Executors.newFixedThreadPool(MAX_THREADS);
    try {
      List<Future<String>> futures = new ArrayList<>();
      for (int i = 0; i < 100; i++) {
        futures.add(POOL.submit(() -> doSomething()));
      }
      HashMap<String, AtomicInteger> histo = futures.stream().map(fu -> {
        return safeFutureGet(fu);
      }).collect(() -> new HashMap<>(), (map, key) -> histoAdd(map, key, 1),
          (map1, map2) -> map2.entrySet().forEach(entry -> histoAdd(map1, entry.getKey(), entry.getValue().get())));
      System.out.println(histo);
    } finally {
      POOL.shutdown();
    }
  }

  private static String safeFutureGet(Future<String> fu) {
    try {
      return fu.get();
    } catch (InterruptedException | ExecutionException ex) {
      ex.printStackTrace();
      return "failed:" + ex.getClass().getCanonicalName();
    }
  }

  private static void histoAdd(Map<String, AtomicInteger> map, String key, int delta) {
    AtomicInteger i = map.get(key);
    if (i == null) {
      i = new AtomicInteger();
      map.put(key, i);
    }
    i.addAndGet(delta);
  }

  private static String doSomething() {
    int incCount = counter.incrementAndGet();
    if (incCount > MAX_THREADS || incCount <= 0) {
      return "illegal-count:" + incCount;
    }
    try {
      Thread.sleep(100);
      return "done";
    } catch (InterruptedException ex) {
      ex.printStackTrace();
      return "failed:" + ex.getClass().getCanonicalName();
    } finally {
      counter.decrementAndGet();
    }
  }
}
