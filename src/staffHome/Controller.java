package staffHome;

import static login.Controller.schoolID;

import CurrentGroup.CurrentGroup;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * This controller class is for the Staff Home screen.
 *
 * @author Benjamin Deleuze
 */
public class Controller implements Initializable {

  @FXML
  private Button staffLogoutBt;
  @FXML
  private ListView<String> currentGroupsLV;
  @FXML
  private ListView<String> updateGroupsLV;
  @FXML
  private Label groupMeetingLabel;
  @FXML
  private Label groupRolesLabel;
  @FXML
  private ListView<String> requestedRoleLV;
  @FXML
  private Label acceptRoleOutputLabel;
  @FXML
  private Label createRoleOutputLabel;
  @FXML
  private Label updateTimeAndPlaceOutputLabel;

  CurrentGroup cGroup = new CurrentGroup(schoolID);

  /**
   * This method is a handler to update the group meeting times.
   */
  public void updateTimesBtHandler() {
  }

  /**
   * This method is a handler to update the group member roles.
   */
  public void updateRolesBtHandler() {
  }

  /**
   * This method populates the the current Meeting Times and Roles Labels. It calls the
   * cGroup.listViewClick method the get the string for each label.
   */
  public void staffListViewClick() {
    acceptRoleOutputLabel.setText("");
    createRoleOutputLabel.setText("");
    updateTimeAndPlaceOutputLabel.setText("");
    groupMeetingLabel.setText(
        cGroup.listViewClick(currentGroupsLV.getSelectionModel().getSelectedItem(), "Time", true));
    groupRolesLabel.setText(cGroup
        .listViewClick(currentGroupsLV.getSelectionModel().getSelectedItem(), "TakenRoles", false));
    requestedRoleLV.getItems().clear();
    ;
    requestedRoleLV.getItems().addAll(cGroup
        .listViewClick(updateGroupsLV.getSelectionModel().getSelectedItem(), "RequestedRoles"));
  }

  public void acceptRolesBtHandler() {
    String role = requestedRoleLV.getSelectionModel().getSelectedItem();
    String[] name_role = role.split("/");
    if (role != null) {
      acceptRoleOutputLabel.setText(cGroup.acceptRoleRequest(updateGroupsLV.getSelectionModel().getSelectedItem(), name_role[1], name_role[0]));
    }
  }

  public void createRoleBtHandler() {

  }

  /**
   * This method is a handler to logout and return back to the login screen.
   */
  public void logoutBtHandler() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/login/Login.fxml"));
      Parent root1 = fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
      Stage closeStage = (Stage) staffLogoutBt.getScene().getWindow();
      closeStage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method populates the ListViews for updateGroupsLV and updateGroupsLV.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    List<String> currentGroupsList = cGroup.getCurrentGroupsList();
    currentGroupsLV.getItems().addAll(currentGroupsList);
    updateGroupsLV.getItems().addAll(currentGroupsList);

    currentGroupsLV.setEditable(false);
    updateGroupsLV.setEditable(false);
  }
}
