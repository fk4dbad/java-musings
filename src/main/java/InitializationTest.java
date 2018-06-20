
public class InitializationTest {
  protected String test = "test90";

  private static InitializationTest testDevStatic = new InitializationTest() {

  };

  public String getTest() {
    return test;
  }

  public static void main(String[] args) {
    System.out.println(new InitializationTest().getTest());
    System.out.println(new InitializationTest() {}.getTest());
    System.out.println(InitializationTest.testDevStatic.getTest());
  }
}
