class Scratch {
  static class A {
    int v = 10;

    public int getV() {
      return v;
    }
  }

  static class B extends A {
    int v = 100;

    @Override
    public int getV() {
      return v;
    }
  }

  public static void main(String[] args) {
    A a = new A();
    B b = new B();
    System.out.println(" a: " + a.v);
    System.out.println(" b: " + b.v);
    System.out.println("ba: " + b.v);

    System.out.println(" a(get): " + a.getV());
    System.out.println(" b(get): " + b.getV());
    System.out.println("ba(get): " + b.getV());
  }
}
