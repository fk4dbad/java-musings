import java.util.*;
import java.util.stream.*;

public class TwoSum {
  public static int[] findTwoSum(int[] list, int sum) {
    if (list == null || list.length < 2) return null;

    // [index, value]
    int[][] ordered = IntStream.range(0, list.length)
        .mapToObj(i -> new int[] { i, list[i] })
        .sorted(Comparator.comparing(i -> i[1]))
        .toArray(s -> new int[s][2]);
    System.out.println(Arrays.stream(ordered)
        .map(i -> i[0] + ":" + i[1])
        .collect(Collectors.joining(",", "[", "]")));
    for (int i = 0; i < ordered.length; i++) {
      int j = binSearch(ordered, i, sum - ordered[i][1]);
      if (j > i) {
        return new int[] { ordered[i][0], ordered[j][0] };
      }
    }

    return null;
  }

  private static int binSearch(int[][] list, int startIndex, int value) {
    int start = startIndex, end = list.length - 1;
    if (list[start][1] > value || list[end][1] < value) return -1;
    int safeCount = 0;
    while (start < end - 1 && safeCount++ < list.length) {
      int test = start + ((end - start) / 2);
      int testValue = list[test][1];
      if (testValue == value) return test;
      if (testValue > value) {
        end = test;
      } else {
        start = test;
      }
      if (safeCount >= list.length) {
        throw new IllegalStateException("binSearch did not terminate! " + start + " " + end + " " + test);
      }
    }
    if (list[end][1] == value) return end;
    return -1;
  }

  public static void main(String[] args) {
    check(12, 1, 3, 5, 7, 9);
    check(13, 1, 3, 5, 7, 9);
    check(4, 1, 3, 5, 7, 9);
    check(13, 1);
    check(2, 1);
    check(2, 1, 1);
    check(3, 1, 2);
    check(4, 1, 2);
    check(4, 1, 2, 3);
    check(10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    check(17, 1, 3, 5, 7, 9, 12, 18, 20);
    check(19, 5, 7, 9, 12, 18, 20);
    check(32, 1, 3, 5, 7, 9, 11, 13, 15, 17, 20);
    check(4, 1, 3, 5, 7, 9, 11, 13, 15, 17, 20);
    check(37, 1, 3, 5, 7, 9, 11, 13, 15, 17, 20);
    check(40, 1, 3, 5, 7, 9, 11, 13, 15, 17, 20);
    check(0, -3, 3);
    check(27, 1, 3, 5, 6, 9, 11, 13, 14, 17, 20);
    check(23, 1, 3, 5, 6, 9, 11, 13, 14, 17, 20);
    check(5, 3, 2);
  }

  private static void check(int sum, int... list) {
    int[] indices = findTwoSum(list, sum);
    System.out.print("search sum " + sum + " in " + java.util.Arrays.toString(list) + ": " + java.util.Arrays.toString(indices));
    if (indices != null) {
      System.out.print(" => " + list[indices[0]] + " + " + list[indices[1]] + " = " + sum);
    }
    System.out.println();
  }
}
