package staffHome;

import static login.Controller.schoolID;

import CurrentGroup.CurrentGroup;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
 * This controller class is for the Staff Home screen.
 *
 * @author Benjamin Deleuze
 */
public class Controller implements Initializable {

  @FXML
  private Button staffLogoutBt;
  @FXML
  private Label actionOutputLabel;
  @FXML
  private ListView<String> currentGroupsLV;
  @FXML
  private Label groupMeetingLabel;
  @FXML
  private Label groupRolesLabel;
  @FXML
  private ListView<String> updateGroupsLV;
  @FXML
  private TextField groupMeetingTime1TF;
  @FXML
  private TextField groupMeetingTime2TF;
  @FXML
  private TextField groupMeetingTime3TF;
  @FXML
  private TextField groupMeetingPlace1TF;
  @FXML
  private TextField groupMeetingPlace2TF;
  @FXML
  private TextField groupMeetingPlace3TF;
  @FXML
  private ListView<String> requestedRoleLV;
  @FXML
  private TextField createRoleTF;
  @FXML
  private TextField newGroupNameTF;
  @FXML
  private TextField newGroupRolesTF;
  @FXML
  private TextField newGroupTime1TF;
  @FXML
  private TextField newGroupPlace1TF;
  @FXML
  private TextField newGroupTime2TF;
  @FXML
  private TextField newGroupPlace2TF;
  @FXML
  private TextField newGroupTime3TF;
  @FXML
  private TextField newGroupPlace3TF;

  CurrentGroup cGroup = new CurrentGroup(schoolID);

  /**
   * This method populates the the current Meeting Times and Roles Labels. It calls the
   * cGroup.listViewClick method the get the string for each label.
   */
  public void staffListViewClick() {
    actionOutputLabel.setText("");
    groupMeetingLabel.setText(
        cGroup.listViewClick(currentGroupsLV.getSelectionModel().getSelectedItem(), "Time", true));
    groupRolesLabel.setText(cGroup
        .listViewClick(currentGroupsLV.getSelectionModel().getSelectedItem(), "TakenRoles", false));
    requestedRoleLV.getItems().clear();
    ;
    requestedRoleLV.getItems().addAll(cGroup
        .listViewClick(updateGroupsLV.getSelectionModel().getSelectedItem(), "RequestedRoles"));
  }

  /**
   * This method called the acceptRoleRequest method in the CurrentGroup class and populates the
   * acceptRoleOutputLabel with a message that say weather or not it was successful.
   */
  public void acceptRolesBtHandler() {
    String requestedRole = requestedRoleLV.getSelectionModel().getSelectedItem();
    String[] name_role = requestedRole.split("/");
    String name = name_role[0];
    String role = name_role[1];
    if (requestedRole != null) {
      actionOutputLabel.setText(cGroup
          .acceptRoleRequest(updateGroupsLV.getSelectionModel().getSelectedItem(), role, name));
    }
  }

  /**
   * This method populates the updateTimeAndPlaceOutputLabel. It calls the resetMeetingInfo method
   * in CurrentGroup class.
   */
  public void resetTimesBtHandler() {
    String resetGroupTime1 = groupMeetingTime1TF.getText().trim();
    String resetGroupTime2 = groupMeetingTime2TF.getText().trim();
    String resetGroupTime3 = groupMeetingTime3TF.getText().trim();
    String resetGroupPlace1 = groupMeetingPlace1TF.getText().trim();
    String resetGroupPlace2 = groupMeetingPlace2TF.getText().trim();
    String resetGroupPlace3 = groupMeetingPlace3TF.getText().trim();

    String groupTime = null;
    String groupPlace = null;
    // if top one time and place field filled out
    if ((!resetGroupTime1.equals("")) && (!resetGroupPlace1.equals(""))) {
      groupTime = resetGroupTime1;
      groupPlace = resetGroupPlace1;

      // if top two time and place field filled out
      if ((!resetGroupTime1.equals("")) && (!resetGroupPlace1.equals("")) && (!resetGroupTime2
          .equals(""))
          && (!resetGroupPlace2.equals(""))) {
        groupTime += ", " + resetGroupTime2;
        groupPlace += ", " + resetGroupPlace2;

        // if all time and place fields filled out
        if ((!resetGroupTime1.equals("")) && (!resetGroupPlace1.equals("")) && (!resetGroupTime2
            .equals(""))
            && (!resetGroupPlace2.equals("")) && (!resetGroupTime3.equals("")) && (!resetGroupPlace3
            .equals(""))) {
          groupTime += ", " + resetGroupTime3;
          groupPlace += ", " + resetGroupPlace3;
        }
      }
    }
    actionOutputLabel.setText(cGroup
        .resetMeetingInfo(updateGroupsLV.getSelectionModel().getSelectedItem(), groupTime,
            groupPlace));
  }

  /**
   * This method populates the updateTimeAndPlaceOutputLabel. It calls the addToMeetingInfo method
   * in CurrentGroup class.
   */
  public void addTimesBtHandler() {
    String selectedGroup = updateGroupsLV.getSelectionModel().getSelectedItem();
    String addGroupTime1 = groupMeetingTime1TF.getText().trim();
    String addGroupTime2 = groupMeetingTime2TF.getText().trim();
    String addGroupTime3 = groupMeetingTime3TF.getText().trim();
    String addGroupPlace1 = groupMeetingPlace1TF.getText().trim();
    String addGroupPlace2 = groupMeetingPlace2TF.getText().trim();
    String addGroupPlace3 = groupMeetingPlace3TF.getText().trim();

    String groupTime = null;
    String groupPlace = null;
    if (selectedGroup != null) {
      // if top one time and place field filled out
      if ((!addGroupTime1.equals("")) && (!addGroupPlace1.equals(""))) {
        groupTime = addGroupTime1;
        groupPlace = addGroupPlace1;

        // if top two time and place field filled out
        if ((!addGroupTime1.equals("")) && (!addGroupPlace1.equals("")) && (!addGroupTime2
            .equals(""))
            && (!addGroupPlace2.equals(""))) {
          groupTime += ", " + addGroupTime2;
          groupPlace += ", " + addGroupPlace2;

          // if all time and place fields filled out
          if ((!addGroupTime1.equals("")) && (!addGroupPlace1.equals("")) && (!addGroupTime2
              .equals(""))
              && (!addGroupPlace2.equals("")) && (!addGroupTime3.equals("")) && (!addGroupPlace3
              .equals(""))) {
            groupTime += ", " + addGroupTime3;
            groupPlace += ", " + addGroupPlace3;
          }
        }
      }

      System.out.println(
          updateGroupsLV.getSelectionModel().getSelectedItem() + " " + groupTime + " "
              + groupPlace);
      boolean addTime = cGroup
          .addToMeetingInfo(updateGroupsLV.getSelectionModel().getSelectedItem(),
              groupTime, groupPlace, true);
      boolean addPlace = cGroup
          .addToMeetingInfo(updateGroupsLV.getSelectionModel().getSelectedItem(), groupTime,
              groupPlace, false);
      if (addTime && addPlace) {
        actionOutputLabel.setText(" Meeting Info Added");
      } else if (addTime) {
        actionOutputLabel.setText(" Failed to Add Place");
      } else if (addPlace) {
        actionOutputLabel.setText(" Failed to Add Time");
      }
    } else {
      actionOutputLabel.setText("Select Group");
    }
  }

  /**
   * This method populates the createRoleOutputLabel. It calls the createRole method in CurrentGroup
   * class.
   */
  public void createRoleBtHandler() {
    String newRole = createRoleTF.getText().trim();
    if (!newRole.equals("")) {
      actionOutputLabel.setText(
          cGroup.createRole(updateGroupsLV.getSelectionModel().getSelectedItem(), newRole));
    }
  }

  /**
   * This method populates createGroupOutputLabel. It calls the createGroup method in the
   * CurrentGroup class.
   */
  public void createGroupBtHandler() {
    String newGroupName = newGroupNameTF.getText().trim();
    String newGroupTime1 = newGroupTime1TF.getText().trim();
    String newGroupPlace1 = newGroupPlace1TF.getText().trim();
    String newGroupTime2 = newGroupTime2TF.getText().trim();
    String newGroupPlace2 = newGroupPlace2TF.getText().trim();
    String newGroupTime3 = newGroupTime3TF.getText().trim();
    String newGroupPlace3 = newGroupPlace3TF.getText().trim();
    String newGroupRoles = newGroupRolesTF.getText().trim();
    if (!newGroupName.equals("")) {
      String newGroupTime = null;
      String newGroupPlace = null;
      // if top one time and place field filled out
      if ((!newGroupTime1.equals("")) && (!newGroupPlace1.equals(""))) {
        newGroupTime = newGroupTime1;
        newGroupPlace = newGroupPlace1;

        // if top two time and place field filled out
        if ((!newGroupTime1.equals("")) && (!newGroupPlace1.equals("")) && (!newGroupTime2
            .equals(""))
            && (!newGroupPlace2.equals(""))) {
          newGroupTime += ", " + newGroupTime2;
          newGroupPlace += ", " + newGroupPlace2;

          // if all time and place fields filled out
          if ((!newGroupTime1.equals("")) && (!newGroupPlace1.equals("")) && (!newGroupTime2
              .equals(""))
              && (!newGroupPlace2.equals("")) && (!newGroupTime3.equals("")) && (!newGroupPlace3
              .equals(""))) {
            newGroupTime += ", " + newGroupTime3;
            newGroupPlace += ", " + newGroupPlace3;
          }
        }
      }
      if (newGroupRoles.equals("")) {
        newGroupRoles = null;
      }
      actionOutputLabel
          .setText(cGroup.createGroup(newGroupName, newGroupTime, newGroupPlace, newGroupRoles));
    }
  }

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
