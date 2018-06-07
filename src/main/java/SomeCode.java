import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;

public class SomeCode {

  public static class InputValidationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InputValidationException(String string) {
      super(string);
    }

  }

  public static void main(String[] args) throws Exception {
    ServletRequest request = getRequest();

    {
      String query = "SELECT account_balance FROM user_data WHERE user_name = " + request.getParameter("customerName");

      try (Connection connection = getConnection(); //
          Statement statement = connection.createStatement(); //
          ResultSet results = statement.executeQuery(query)) {
        while (results.next()) {
          // ...
        }
      }
    }
    /////////////////

    {
      String custname = request.getParameter("customerName");
      // TODO Validierung von custname angebracht
      String query = "SELECT account_balance FROM user_data WHERE user_name = ? ";

      try (Connection connection = getConnection(); //
          PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, custname);

        try (ResultSet results = pstmt.executeQuery()) {
          while (results.next()) {
            // ...
          }
        }
      }
    }

    {
      String custname = request.getParameter("customerName");
      // TODO validate custname
      try (Connection connection = getConnection(); //
          CallableStatement cs = connection.prepareCall("{call sp_getAccountBalance(?)}")) {
        cs.setString(1, custname);

        try (ResultSet results = cs.executeQuery()) {
          while (results.next()) {
            // ...
          }
        }
      }
    }

    {
      String PARAM = "";

      String tableName;
      switch (PARAM) {
        case "A":
          tableName = "fooTable";
          break;
        case "B":
          tableName = "barTable";
          break;
        default:
          throw new InputValidationException("unexpected value provided for table name");
      }
      String query = "select * from " + tableName;

      System.out.println(query);
    }

    {
      Codec ORACLE_CODEC = new OracleCodec();
      String query = String.format( //
          "SELECT user_id FROM user_data WHERE user_name = '%s' and user_password = '%s'", //
          ESAPI.encoder()
              .encodeForSQL(ORACLE_CODEC, request.getParameter("userID")), //
          ESAPI.encoder()
              .encodeForSQL(ORACLE_CODEC, request.getParameter("pwd")));

      System.out.println(query);
    }
  }

  private static Connection getConnection() {
    return null;
  }

  private static ServletRequest getRequest() {
    return null;
  }

}
