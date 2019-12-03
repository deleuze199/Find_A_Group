package memberHome;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This Controller class is for the Member Home screen.
 *
 * @author Benjamin Deleuze
 * @edited: Thomas Matragrano 10/12/19; added "initialize" and "memberMouseClick" methods and FXML
 * fields for them
 * @edited: Benjamin Deleuze removed Thomas Matragrano update to utilize the groups table.
 */
public class Controller implements Initializable {

  //<editor-fold desc="FXML Declarations">
  @FXML
  private ListView<String> clubOptions;
  @FXML
  private Label meetingTimesLabel;
  @FXML
  private Label groupRolesLabel;
  @FXML
  private ListView<String> currentGroupsLV;
  @FXML
  private Label currentGroupMeetingLabel;
  @FXML
  private Label currentGroupRolesLabel;
  @FXML
  private ListView<String> currentGroupRequestRolesLV;
  @FXML
  private ListView<String> requestRolesLV;
  @FXML
  private TextField requestRoleTA;
  @FXML
  private Label actionOutputLabel;
  @FXML
  private Button memberLogoutBt;
  //</editor-fold>
  private final CurrentGroup cGroup = new CurrentGroup(schoolID);

  /**
   * This method is a handler to logout and return back to the login screen.
   */
  public void logoutBtHandler() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/login/NewLogin.fxml"));
      Parent root1 = fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
      Stage closeStage = (Stage) memberLogoutBt.getScene().getWindow();
      closeStage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method populates the the Meeting Times Labels, Roles Labels, and Request Roles ListView.
   * It calls the cGroup.listViewClick method the get the output from the database.
   */
  public void memberListViewClick() {
    currentGroupMeetingLabel.setText(
        cGroup.listViewClick(currentGroupsLV.getSelectionModel().getSelectedItem(), "Time", true));
    currentGroupRolesLabel.setText(cGroup
        .listViewClick(currentGroupsLV.getSelectionModel().getSelectedItem(), "TakenRoles", false));
    meetingTimesLabel.setText(
        cGroup.listViewClick(clubOptions.getSelectionModel().getSelectedItem(), "Time", true));
    groupRolesLabel.setText(cGroup
        .listViewClick(clubOptions.getSelectionModel().getSelectedItem(), "TakenRoles", false));
    requestRolesLV.getItems().clear();
    requestRolesLV.getItems().addAll(cGroup
        .listViewClick(currentGroupRequestRolesLV.getSelectionModel().getSelectedItem(),
            "AvailRoles"));
    actionOutputLabel.setText("");
    actionOutputLabel.setText("");
  }

  /**
   * This method is a handler to join a group that is displayed by the memberMouseClick method.
   */
  public void memberJoinGroupBtHandler() {
    String joinThisGroup = clubOptions.getSelectionModel().getSelectedItem();
    if (joinThisGroup != null) {
      actionOutputLabel.setText(cGroup.addGroup(joinThisGroup));
    }

    loadListViews();
  }

  /**
   * This method is a handler to request member roles.
   */
  public void requestRoleBtHandler() {
    String name = requestRoleTA.getText().trim();
    String role = requestRolesLV.getSelectionModel().getSelectedItem();
    if ((!name.equals("")) && (!(role == null))) {
      actionOutputLabel.setText(cGroup
          .requestRole(currentGroupRequestRolesLV.getSelectionModel().getSelectedItem(),
              role, name));
    }
  }

  /**
   * This method populates the ListViews for clubOptions, currentGroups, and
   * currentGroupRequestRoles.
   */
  public void loadListViews() {
    clubOptions.getItems().clear();
    currentGroupsLV.getItems().clear();
    currentGroupRequestRolesLV.getItems().clear();
    List<String> currentGroupsList = cGroup.getCurrentGroupsList();
    clubOptions.getItems().addAll(cGroup.getAvailableGroupsList(currentGroupsList));
    currentGroupsLV.getItems().addAll(currentGroupsList);
    currentGroupRequestRolesLV.getItems().addAll(currentGroupsList);
    clubOptions.getSelectionModel().selectFirst();
    clubOptions.setEditable(false);
    currentGroupRequestRolesLV.getSelectionModel().selectFirst();
    currentGroupRequestRolesLV.setEditable(false);
    currentGroupsLV.setEditable(false);
  }

  /**
   * This method populates the ListViews for clubOptions, currentGroups, and
   * currentGroupRequestRoles.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadListViews();
  }
}