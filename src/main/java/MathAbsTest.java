import java.util.stream.IntStream;

public class MathAbsTest {
  public static void main(String[] args) {
    IntStream.range(-7, 8)
        .forEach(i -> System.out.println(String.format("%3d: %3d, %3d, %3d, %3d", 
            i, Math.abs(i), i % 3, getRotatedIndex(i, 0, 3), getRotatedIndex(i, 2, 3))));
  }

  private static int getRotatedIndex(int index, int rotation, int length) {
    int i = index + rotation - 1;
    int step = index > 0 ? -length : length;
    while (i < 0 || i >= length) {
      i += step;
    }
    return i;
  }

}
