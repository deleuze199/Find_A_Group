package login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This controller class is for the login pages and it handles button clicks.
 *
 * @author Benjamin Deleuze
 */
public class Controller {

  //<editor-fold desc="FXML Declarations">
  @FXML
  private TextField schoolId;
  @FXML
  private PasswordField pin;
  @FXML
  private Button loginBt;
  @FXML
  private Button registerBt;
  @FXML
  private Label userIdDisplay;
  //</editor-fold>
  public static String schoolID;

  /**
   * This method uses a prepared statement object to check the database for the user name and
   * password entered.
   */
  public void loginBtHandler() {
    // Database driver and location
    final String Jdbc_Driver = "org.h2.Driver";
    final String db_Url = "jdbc:h2:./res/data";
    // Database credentials
    final String user = "";
    final String pass = "";
    // instance of class connection
    Connection conn;
    ResultSet rs;

    try {
      // Register JDBC driver
      Class.forName(Jdbc_Driver);
      // Create a connection to database
      conn = DriverManager.getConnection(db_Url, user, pass);
      String sql = "SELECT * FROM user WHERE SchoolId = ? and PIN = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, schoolId.getText());
      pstmt.setString(2, pin.getText());
      rs = pstmt.executeQuery();
      if (rs.next()) {
        userIdDisplay.setText(rs.getString("SchoolId"));
        schoolID = schoolId.getText();
        boolean staff = rs.getBoolean("StaffBool");
        if (staff) {
          try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/staffHome/NewStaffHome.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            Stage closeStage = (Stage) registerBt.getScene().getWindow();
            closeStage.close();
          } catch (Exception e) {
            e.printStackTrace();
            userIdDisplay.setText("Failed To Login");
          }
        } else {
          FXMLLoader fxmlLoader = new FXMLLoader(
              getClass().getResource("/memberHome/NewMemberHome.fxml"));
          Parent root1 = fxmlLoader.load();
          Stage stage = new Stage();
          stage.setScene(new Scene(root1));
          stage.show();
          Stage closeStage = (Stage) loginBt.getScene().getWindow();
          closeStage.close();
        }
      } else {
        userIdDisplay.setText("Failed To Login");
      }
      conn.close();
      pstmt.close();
    } catch (ClassNotFoundException | SQLException | IOException e) {
      e.printStackTrace();
      userIdDisplay.setText("Failed To Login");
    }
  }

  /**
   * This method opens the register page and closes the login page.
   */
  public void registerBtHandler() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/register/NewRegister.fxml"));
      Parent root1 = fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
      Stage closeStage = (Stage) loginBt.getScene().getWindow();
      closeStage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
