package CurrentGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is for accessing the database and returning the result.
 *
 * @author Benjamin Deleuze
 */
public class CurrentGroup {

  String schoolId;

  /**
   * This is the constructor for the CurrentGroup class and sets the schoolId String.
   *
   * @param schoolId is the Id of the user currently logged in
   */
  public CurrentGroup(String schoolId) {
    this.schoolId = schoolId;
  }

  /**
   * This method is a getter for the groups the user is currently in. It uses a precompiled
   * statement to get the string the groups, then parses the string at the commas.
   *
   * @return the List of groups the user is currently in
   */
  public List<String> getCurrentGroupsList() {
    List<String> currentGroupsL = new ArrayList<>();
    Connection conn;
    ResultSet rs;
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
      String sql = "SELECT * FROM user WHERE SchoolId = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, schoolId);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentGroup = rs.getString("Groups");
        if (currentGroup != null) {
          String[] currentGroupsArr = currentGroup.split(", ");
          currentGroupsL = Arrays.asList(currentGroupsArr);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return currentGroupsL;
  }

  /**
   * This method populates the ListView of available groups the member can join.
   *
   * @param currentGroupsL is a List of the groups the user is currently in
   * @return a List of the groups that the member is not in
   */
  public List<String> getAvailableGroupsList(List<String> currentGroupsL) {
    List<String> availableGroupsL = new ArrayList<>();
    List<String> allGroupsL = new ArrayList<>();
    Connection conn;
    ResultSet rs;
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
      String sql = "SELECT NAME FROM GROUPS";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      rs = pstmt.executeQuery();
      while (rs.next()) {
        allGroupsL.add(rs.getString(1));
      }
      for (int i = 0; i < allGroupsL.size(); i++) {
        if (currentGroupsL.contains(allGroupsL.get(i))) {
        } else {
          availableGroupsL.add(allGroupsL.get(i));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return availableGroupsL;
  }

  /**
   * This method gets the output of the database of the params inputted.
   *
   * @param selectedGroup  is the group that rs will be looking at
   * @param outputArea     is the column that databaseOutput String will be set to
   * @param ifMeetingLabel is a boolean that if true gets the column "Place" and sets it to
   *                       databaseOutputPlace String
   * @return formatted String that will be set to a Label area in GUI
   */
  public String listViewClick(String selectedGroup, String outputArea, Boolean ifMeetingLabel) {
    String finalOutPutString = "";
    Connection conn;
    ResultSet rs;
    String[] databaseOutputPlaceArr = {""};
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
      String sql = "SELECT * FROM GROUPS WHERE NAME = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String databaseOutput = rs.getString(outputArea);
        if (databaseOutput != null) {
          String[] databaseOutputArr = databaseOutput.split(", ");
          if (ifMeetingLabel) {
            String databaseOutputPlace = rs.getString("Place");
            databaseOutputPlaceArr = databaseOutputPlace.split(", ");
          }
          for (int i = 0; i < databaseOutputArr.length; i++) {
            if (ifMeetingLabel) {
              finalOutPutString += databaseOutputPlaceArr[i] + "\n";
            }
            finalOutPutString += databaseOutputArr[i] + "\n\n";
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return finalOutPutString;
  }

  /**
   * This method is specifically to populate the request role ListView.
   *
   * @param selectedGroup is the group selected
   * @param outputArea    is the column the rs is looking at
   * @return List of the rs
   */
  public List listViewClick(String selectedGroup, String outputArea) {
    List<String> finalOutPutList = new ArrayList<>();
    Connection conn;
    ResultSet rs;
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
      String sql = "SELECT * FROM GROUPS WHERE NAME = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String databaseOutput = rs.getString(outputArea);
        if (databaseOutput != null) {
          String[] databaseOutputArr = databaseOutput.split(", ");
          finalOutPutList = Arrays.asList(databaseOutputArr);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return finalOutPutList;
  }

  /**
   * This method adds the current group selected in the ListView and adds it to the groups column in
   * the user table.
   *
   * @param newGroup is the groups currently selected in the ListView
   */
  public String addGroup(String newGroup) {
    Connection conn;
    ResultSet rs;
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
      String sql = "SELECT * FROM user WHERE SchoolId = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, schoolId);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentGroup = rs.getString("Groups");
        if (currentGroup.equals("null")) {
          currentGroup = newGroup;
        } else {
          currentGroup += ", " + newGroup;
        }
        String sql1 = "UPDATE USER SET GROUPS = ? WHERE SCHOOLID = ?";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        // Execute SQL string
        pstmt1.setString(1, currentGroup);
        pstmt1.setString(2, schoolId);
        pstmt1.executeUpdate();
      }
      return "Group Joined";
    } catch (Exception e) {
      e.printStackTrace();
      return "Failed to Join";
    }
  }

  /**
   * This method request a role for the member.
   *
   * @param role is the requested role selected from the ListView
   * @param name is the name of the user requesting the role
   */
  public String requestRole(String selectedGroup, String role, String name) {
    Connection conn;
    ResultSet rs;
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
      String sql = "SELECT REQUESTEDROLES FROM GROUPS WHERE NAME = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentAvailiableRoles = rs.getString(1);
        if (currentAvailiableRoles == null) {
          currentAvailiableRoles = name + "/" + role;
        } else {
          currentAvailiableRoles += ", " + name + "/" + role;
        }
        String sql1 = "UPDATE GROUPS SET REQUESTEDROLES = ? WHERE NAME = ?";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        // Execute SQL string
        pstmt1.setString(1, currentAvailiableRoles);
        pstmt1.setString(2, selectedGroup);
        pstmt1.executeUpdate();
      }
      return "Role Requested";
    } catch (Exception e) {
      e.printStackTrace();
      return "Fail to Request Role";
    }
  }

  public String acceptRoleRequest() {

    return "ok";
  }
}
