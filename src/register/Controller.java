package register;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This controller class is for the register page and it handles button clicks.
 *
 * @author Benjamin Deleuze
 */
public class Controller {

  @FXML
  private TextField schoolId;
  @FXML
  private TextField pin;
  @FXML
  private Button exitRegisterBt;
  @FXML
  private Label registerFailMessage;
  @FXML
  private TextField graduateDateTF;

  private boolean staffBoolean = false;

  /**
   * This method is for the Exit button in the register page.
   */
  public void exitRegistrationBtHandler() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/login/Login.fxml"));
      Parent root1 = fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
      Stage closeStage = (Stage) exitRegisterBt.getScene().getWindow();
      closeStage.close();
    } catch (Exception e) {
      registerFailMessage.setText("Failed, please try again or restart program.");
      e.printStackTrace();
    }
  }

  /**
   * This method sets the staff boolean so when the user is stored in the database we know whether
   * the user is a staff member.
   */
  public void setStaffBoolean() {
    staffBoolean = !staffBoolean;
  }

  /**
   * This method saves the user's register info in the database.
   */
  public void registerUserBtHandler() {
    String id = schoolId.getText().trim();
    String pin = this.pin.getText().trim();
    if ((!id.equals("")) && (!pin.equals(""))) {
      // Database driver and location
      final String Jdbc_Driver = "org.h2.Driver";
      final String db_Url = "jdbc:h2:./res/data";
      // Database credentials
      final String user = "";
      final String pass = "";
      // instance of class connection
      Connection conn;
      try {
        // Register JDBC driver
        Class.forName(Jdbc_Driver);
        // Create a connection to database
        conn = DriverManager.getConnection(db_Url, user, pass);
        String sql = "INSERT INTO user (SCHOOLID, PIN, STAFFBOOL, GROUPS, GRADUATEDATE) VALUES (?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Execute SQL string
        pstmt.setString(1, id);
        pstmt.setString(2, pin);
        pstmt.setBoolean(3, staffBoolean);
        pstmt.setString(4, null);
        pstmt.setString(5, null);
        pstmt.execute();
        String graduateDate = graduateDateTF.getText().trim();
        if (!graduateDate.equals("")) {
          SimpleDateFormat userDateFormat = new SimpleDateFormat("MM/dd/yyyy");
          Date graduationDate = userDateFormat.parse(graduateDate);
          String sql1 = "UPDATE USER SET GRADUATEDATE = ? WHERE SCHOOLID = ?";
          PreparedStatement pstmt1 = conn.prepareStatement(sql1);
          // Execute SQL string
          pstmt1.setString(1, graduateDate);
          pstmt1.setString(2, id);
          pstmt1.executeUpdate();
        }
        conn.close();
        pstmt.close();
        registerFailMessage.setText("Registration Complete, Click Exit to return to Login");
      } catch (ParseException | ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        registerFailMessage.setText("Failed To Register");
      }
    }
  }
}
