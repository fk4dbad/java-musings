package com.dasburo.fk.javatest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

public class TreeSetTest {

  class MyType {
    int a;
    String s;

    public MyType(int a, String s) {
      super();
      this.a = a;
      this.s = s;
    }

    public int getA() {
      return a;
    }

    public String getS() {
      return s;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getEnclosingInstance().hashCode();
      result = prime * result + a;
      result = prime * result + ((s == null) ? 0 : s.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      MyType other = (MyType) obj;
      if (!getEnclosingInstance().equals(other.getEnclosingInstance())) {
        return false;
      }
      if (a != other.a) {
        return false;
      }
      if (s == null) {
        if (other.s != null) {
          return false;
        }
      } else if (!s.equals(other.s)) {
        return false;
      }
      return true;
    }

    private TreeSetTest getEnclosingInstance() {
      return TreeSetTest.this;
    }
  }

  @Test
  public void testAdd() {
    SortedSet<MyType> set = new TreeSet<>(Comparator.comparing(MyType::getA));
    MyType t1 = new MyType(3, "abc");
    assertTrue(set.add(t1));
    assertTrue(contains(set, t1));
    MyType t2 = new MyType(4, "abc");
    assertTrue(set.add(t2));
    assertTrue(contains(set, t1));
    assertTrue(contains(set, t2));
    MyType t3 = new MyType(3, "xyz");
    assertFalse(set.add(t3));
    assertTrue(contains(set, t1));
    assertTrue(contains(set, t2));
    assertFalse(contains(set, t3));
  }

  @Test
  public void testAddWithS() {
    SortedSet<MyType> set = new TreeSet<>(Comparator.comparing(MyType::getA)
        .thenComparing(MyType::getS));
    MyType t1 = new MyType(3, "abc");
    assertTrue(set.add(t1));
    assertTrue(contains(set, t1));
    MyType t2 = new MyType(4, "abc");
    assertTrue(set.add(t2));
    assertTrue(contains(set, t1));
    assertTrue(contains(set, t2));
    MyType t3 = new MyType(3, "xyz");
    assertTrue(set.add(t3));
    assertTrue(contains(set, t1));
    assertTrue(contains(set, t2));
    assertTrue(contains(set, t3));
  }

  private static boolean contains(SortedSet<MyType> set, MyType t1) {
    return set.stream()
        .anyMatch(t -> t == t1);
  }
}
