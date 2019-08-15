import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class TrainComposition {
  private int solution = 1;

  // solution 1
  public static class Wagon {
    private final int wagonId;
    Wagon nextLeft;
    Wagon nextRight;

    public Wagon(int wagonId) {
      this.wagonId = wagonId;
    }
  }

  private Wagon leftEnd, rightEnd;

  // solution 2
  private Deque<Integer> deque = new LinkedList<>();

  // solution 3
  private Deque<Integer> arrayDeque = new ArrayDeque<>();

  // solution 4
  private int[] arr = new int[0];

  // solution 5
  private ArrayList<Integer> arrList = new ArrayList<>();

  public void attachWagonFromLeft(int wagonId) {
    if (solution == 1) {
      Wagon newWagon = new Wagon(wagonId);
      if (leftEnd == null) {
        leftEnd = newWagon;
        rightEnd = newWagon;
      } else {
        newWagon.nextRight = leftEnd;
        leftEnd.nextLeft = newWagon;
        leftEnd = newWagon;
      }
    } else if (solution == 2) {
      deque.addFirst(wagonId);
    } else if (solution == 3) {
      arrayDeque.addFirst(wagonId);
    } else if (solution == 4) {
      int[] newArr = new int[arr.length + 1];
      newArr[0] = wagonId;
      System.arraycopy(arr, 0, newArr, 1, arr.length);
      arr = newArr;
    } else if (solution == 5) {
      arrList.add(0, wagonId);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public void attachWagonFromRight(int wagonId) {
    if (solution == 1) {
      Wagon newWagon = new Wagon(wagonId);
      if (rightEnd == null) {
        leftEnd = newWagon;
        rightEnd = newWagon;
      } else {
        newWagon.nextLeft = rightEnd;
        rightEnd.nextRight = newWagon;
        rightEnd = newWagon;
      }
    } else if (solution == 2) {
      deque.addLast(wagonId);
    } else if (solution == 3) {
      arrayDeque.addLast(wagonId);
    } else if (solution == 4) {
      int[] newArr = new int[arr.length + 1];
      System.arraycopy(arr, 0, newArr, 0, arr.length);
      newArr[arr.length] = wagonId;
      arr = newArr;
    } else if (solution == 5) {
      arrList.add(arrList.size(), wagonId);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public int detachWagonFromLeft() {
    if (solution == 1) {
      Wagon detachWagon = leftEnd;
      if (detachWagon == null) {
        return -1;
      }
      leftEnd = detachWagon.nextRight;
      if (leftEnd != null) {
        leftEnd.nextLeft = null;
      }
      detachWagon.nextRight = null;
      return detachWagon.wagonId;
    } else if (solution == 2) {
      return deque.removeFirst();
    } else if (solution == 3) {
      return arrayDeque.removeFirst();
    } else if (solution == 4) {
      int wagonId = arr[0];
      int[] newArr = new int[arr.length - 1];
      System.arraycopy(arr, 1, newArr, 0, arr.length - 1);
      arr = newArr;
      return wagonId;
    } else if (solution == 5) {
      return arrList.remove(0);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public int detachWagonFromRight() {
    if (solution == 1) {
      Wagon detachWagon = rightEnd;
      if (detachWagon == null) {
        return -1;
      }
      rightEnd = detachWagon.nextLeft;
      if (rightEnd != null) {
        rightEnd.nextRight = null;
      }
      detachWagon.nextLeft = null;
      return detachWagon.wagonId;
    } else if (solution == 2) {
      return deque.removeLast();
    } else if (solution == 3) {
      return arrayDeque.removeLast();
    } else if (solution == 4) {
      int wagonId = arr[arr.length - 1];
      int[] newArr = new int[arr.length - 1];
      System.arraycopy(arr, 0, newArr, 0, arr.length - 1);
      arr = newArr;
      return wagonId;
    } else if (solution == 5) {
      return arrList.remove(arrList.size() - 1);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    TrainComposition tree = new TrainComposition();
    tree.attachWagonFromLeft(7);
    tree.attachWagonFromLeft(13);
    System.out.println(tree.detachWagonFromRight()); // 7
    System.out.println(tree.detachWagonFromLeft()); // 13
  }
}
