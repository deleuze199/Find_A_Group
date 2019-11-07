package login;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * LoginMain is where the program begin and displays the login screen.
 *
 * @author Benjamin Deleuze
 */
public class LoginMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("NewLogin.fxml"));
    primaryStage.setTitle("Find A Group");
    primaryStage.setScene(new Scene(root, 350, 333));
    primaryStage.show();
    checkIfDeleteUser();
  }

  /**
   * This method checks if any user have already graduated(based on the date entered at
   * registration) and deletes that user form the table.
   *
   * @throws ParseException if date not properly inputted at registration
   */
  public void checkIfDeleteUser() {
    Connection conn;
    ResultSet rs;
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
      String sql = "SELECT GRADUATEDATE FROM user ";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      rs = pstmt.executeQuery();
      while (rs.next()) {
        String graduateString = rs.getString(1);
        if (graduateString != null) {
          SimpleDateFormat userDateFormat = new SimpleDateFormat("MM/dd/yyyy");
          Date graduationDate = userDateFormat.parse(graduateString);
          Date currentDate = new Date();
          if (currentDate.after(graduationDate)) {
            String sql1 = "DELETE FROM USER WHERE GRADUATEDATE = ?";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setString(1, graduateString);
            pstmt1.executeUpdate();
          }
        }
      }
    } catch (ParseException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * This method is made to execute prepared sql statements.
   *
   * @param SQL String is the statement being executed
   * @return rs is the ResultSet of the executed statement
   */
  public static ResultSet executeSqlWResultSet(String SQL) {
    // Database driver and location
    final String Jdbc_Driver = "org.h2.Driver";
    final String db_Url = "jdbc:h2:./res/data";
    // Database credentials
    final String user = "";
    final String pass = "";
    // instance of class connection
    Connection conn;
    ResultSet rs = null;

    try {
      // Register JDBC driver
      Class.forName(Jdbc_Driver);
      // Create a connection to database
      conn = DriverManager.getConnection(db_Url, user, pass);
      PreparedStatement pstmt = conn.prepareStatement(SQL);
      rs = pstmt.executeQuery();
      conn.close();
      pstmt.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return rs;
  }
}
