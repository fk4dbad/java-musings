
public class MillisecondTest {

  private static final int SEC_IN_MS = 1000;

  private static final int MIN_IN_MS = SEC_IN_MS * 60;

  private static final int HOUR_IN_MS = MIN_IN_MS * 60;

  private static final int DAY_IN_MS = HOUR_IN_MS * 24;

  public static void main(String[] args) {
    System.out.println(Long.MAX_VALUE / DAY_IN_MS / 365);
  }

}
