package currentGroup;

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
  Connection conn;
  ResultSet rs;

  /**
   * This is the constructor for the CurrentGroup class and sets the schoolId String.
   *
   * @param schoolId is the Id of the user currently logged in
   */
  public CurrentGroup(String schoolId) {
    this.schoolId = schoolId;
    try {
      // Register JDBC driver
      Class.forName("org.h2.Driver");
      // Create a connection to database
      conn = DriverManager.getConnection("jdbc:h2:./res/data", "", "");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is a getter for the groups the user is currently in. It uses a precompiled
   * statement to get the string the groups, then parses the string at the commas.
   *
   * @return the List of groups the user is currently in
   */
  public List<String> getCurrentGroupsList() {
    List<String> currentGroupsL = new ArrayList<>();
    try {
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
    try {
      String sql = "SELECT NAME FROM GROUPS";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      rs = pstmt.executeQuery();
      while (rs.next()) {
        allGroupsL.add(rs.getString(1));
      }
      for (String s : allGroupsL) {
        if (!currentGroupsL.contains(s)) {
          availableGroupsL.add(s);
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
    String[] databaseOutputPlaceArr = {""};
    try {
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
    try {
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
   * @return a Sting that states weather or not the method was successful
   */
  public String addGroup(String newGroup) {
    try {
      String sql = "SELECT * FROM user WHERE SchoolId = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, schoolId);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentGroup = rs.getString("Groups");
        if (currentGroup == null) {
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
   * @param selectedGroup is the current group selected
   * @param role          is the requested role selected from the ListView
   * @param name          is the name of the user requesting the role
   * @return a Sting that states weather or not the method was successful
   */
  public String requestRole(String selectedGroup, String role, String name) {
    try {
      String sql = "SELECT REQUESTEDROLES FROM GROUPS WHERE NAME = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentRequestedRoles = rs.getString(1);
        if (currentRequestedRoles == null) {
          currentRequestedRoles = name + "/" + role;
        } else {
          currentRequestedRoles += ", " + name + "/" + role;
        }
        String sql1 = "UPDATE GROUPS SET REQUESTEDROLES = ? WHERE NAME = ?";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        // Execute SQL string
        pstmt1.setString(1, currentRequestedRoles);
        pstmt1.setString(2, selectedGroup);
        pstmt1.executeUpdate();
      }
      return "Role Requested";
    } catch (Exception e) {
      e.printStackTrace();
      return "Fail to Request Role";
    }
  }

  /**
   * This method adds the requested role selected to the TANKENROLES column and removes it from the
   * AVAILROLES and REQUESTEDREOLES columns.
   *
   * @param selectedGroup is the current group selected
   * @param role          is the role of the requested role
   * @param name          is the name that goes along with the requested role
   * @return a Sting that states weather or not the method was successful
   */
  public String acceptRoleRequest(String selectedGroup, String role, String name) {
    try {
      String sql = "SELECT TAKENROLES FROM GROUPS WHERE NAME = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentTakenRoles = rs.getString(1);
        if (currentTakenRoles == null) {
          currentTakenRoles = (name + "/" + role);
        } else {
          currentTakenRoles += ", " + (name + "/" + role);
        }
        String sql1 = "UPDATE GROUPS SET TAKENROLES = ? WHERE NAME = ?";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        // Execute SQL string
        pstmt1.setString(1, currentTakenRoles);
        pstmt1.setString(2, selectedGroup);
        pstmt1.executeUpdate();
        String sql2 = "SELECT AVAILROLES FROM GROUPS WHERE NAME = ?";
        PreparedStatement pstmt2 = conn.prepareStatement(sql2);
        // Execute SQL string
        pstmt2.setString(1, selectedGroup);
        rs = pstmt2.executeQuery();
        if (rs.next()) {
          String originalAvailableRoles = rs.getString(1);
          System.out.println("Original avail " + originalAvailableRoles);
          if (originalAvailableRoles != null) {
            String[] updatedAvailableRolesArr = originalAvailableRoles.split(", ");
            String updatedAvailableRoles = "";
            for (String s : updatedAvailableRolesArr) {
              if (!s.equals(role)
                  && s != null) {
                if (updatedAvailableRoles.equals("")) {
                  updatedAvailableRoles += s;
                  System.out.println(
                      "initial add " + s + " to avail, when role = "
                          + role);
                } else {
                  updatedAvailableRoles += ", " + s;
                  System.out.println("adding " + s + " to avail");
                }
              }
            }
            if (updatedAvailableRoles.equals("")) {
              updatedAvailableRoles = null;
              System.out.println(updatedAvailableRoles + " 3");
            }
            String sql3 = "UPDATE GROUPS SET AVAILROLES = ? WHERE NAME = ?";
            PreparedStatement pstmt3 = conn.prepareStatement(sql3);
            // Execute SQL string
            pstmt3.setString(1, updatedAvailableRoles);
            pstmt3.setString(2, selectedGroup);
            System.out.println("Avail updated to " + updatedAvailableRoles);
            pstmt3.executeUpdate();

            String sql4 = "SELECT REQUESTEDROLES FROM GROUPS WHERE NAME = ?";
            PreparedStatement pstmt4 = conn.prepareStatement(sql4);
            // Execute SQL string
            pstmt4.setString(1, selectedGroup);
            rs = pstmt4.executeQuery();
            if (rs.next()) {
              String originalRequestedRoles = rs.getString(1);
              System.out.println(originalRequestedRoles);
              if (originalRequestedRoles != null) {
                String[] updatedRequestedRolesArr = originalRequestedRoles.split(", ");
                String updatedRequestedRoles = "";
                for (int i = 0; i < updatedRequestedRolesArr.length; i++) {
                  if (!updatedRequestedRolesArr[i].equals(name + "/" + role)
                      && updatedRequestedRolesArr[i] != null) {
                    if (updatedRequestedRoles.equals("")) {
                      updatedRequestedRoles += updatedRequestedRolesArr[i];
                      System.out.println("initial add " + updatedAvailableRolesArr[i]
                          + " to requested, when request = " + name + "/" + role);
                    } else {
                      updatedRequestedRoles += ", " + updatedRequestedRolesArr[i];
                      System.out.println("adding " + updatedAvailableRolesArr[i] + " to requested");
                    }
                  }

                }
                if (updatedRequestedRoles.equals("")) {
                  updatedRequestedRoles = null;
                  System.out.println(updatedRequestedRoles + " 3");
                }
                String sql5 = "UPDATE GROUPS SET REQUESTEDROLES = ? WHERE NAME = ?";
                PreparedStatement pstmt5 = conn.prepareStatement(sql5);
                // Execute SQL string
                pstmt5.setString(1, updatedRequestedRoles);
                pstmt5.setString(2, selectedGroup);
                System.out.println("updated request " + updatedRequestedRoles);
                pstmt5.executeUpdate();
              }
            }
          }
        }
      }
      return "Role Accepted";
    } catch (Exception e) {
      e.printStackTrace();
      return "Fail to Accept Role";
    }
  }

  /**
   * This method searches through the REQUESTEDROLES column in the selectedGroup row and removes the
   * "name/role" from the String in the database.
   *
   * @param selectedGroup is the current group selected
   * @param role          is the role of the requested role
   * @param name          is the name that goes along with the requested role
   * @return a Sting that states weather or not the method was successful
   */
  public String declineRoleRequest(String selectedGroup, String role, String name) {
    String newRequestedRole = null;
    try {
      String sql = "SELECT REQUESTEDROLES FROM GROUPS WHERE NAME = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentRequestedRoles = rs.getString(1);
        if (currentRequestedRoles != null) {
          String[] updatedRoleRequested = currentRequestedRoles.split(", ");
          if (updatedRoleRequested.length > 1) {
            if (updatedRoleRequested[0].equals(name + "/" + role)) {
              newRequestedRole = updatedRoleRequested[1];
              for (int i = 2; i < updatedRoleRequested.length; i++) {
                newRequestedRole += ", " + updatedRoleRequested[i];
              }
              System.out.println("1 " + newRequestedRole);
            } else {
              newRequestedRole = updatedRoleRequested[0];
              for (int i = 1; i < updatedRoleRequested.length; i++) {
                if (!updatedRoleRequested[i].equals(name + "/" + role)) {
                  newRequestedRole += ", " + updatedRoleRequested[i];
                }
              }
              System.out.println("2 " + newRequestedRole);
            }
          }
        }
      }
      System.out.println(newRequestedRole);
      String sql1 = "UPDATE GROUPS SET REQUESTEDROLES = ? WHERE NAME = ?";
      PreparedStatement pstmt1 = conn.prepareStatement(sql1);
      // Execute SQL string
      pstmt1.setString(1, newRequestedRole);
      pstmt1.setString(2, selectedGroup);
      pstmt1.executeUpdate();
      return "Role Declined";
    } catch (Exception e) {
      e.printStackTrace();
      return "Fail to Decline Role";
    }
  }

  /**
   * This method resets meeting info(Place or Time) to the appropriate area.
   *
   * @param selectedGroup is the group that the info is being added to
   * @param meetingInfo   is the meetingInfo being added(Time or Place)
   * @param ifTime        is boolean to know if it adding th time or place of the group
   * @return a boolean that states if the method was successful
   */
  public boolean resetMeetingInfo(String selectedGroup, String meetingInfo, boolean ifTime) {
    String sql;
    try {
      if (ifTime) {
        sql = "UPDATE GROUPS SET TIME = ? WHERE NAME = ?";
      } else {
        sql = "UPDATE GROUPS SET PLACE = ? WHERE NAME = ?";
      }
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, meetingInfo);
      pstmt.setString(2, selectedGroup);
      pstmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * This method adds meeting info(Place or Time) to the appropriate area.
   *
   * @param selectedGroup is the group that the info is being added to
   * @param meetingInfo   is the meetingInfo being added(Time or Place)
   * @param ifTime        is boolean to know if it adding th time or place of the group
   * @return a boolean that states if the method was successful
   */
  public boolean addToMeetingInfo(String selectedGroup, String meetingInfo, boolean ifTime) {
    String sql;
    String sql1;
    try {
      if (ifTime) {
        sql = "SELECT TIME FROM GROUPS WHERE  NAME = ?";
      } else {
        sql = "SELECT PLACE FROM GROUPS WHERE  NAME = ?";
      }
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String addedInfo = rs.getString(1);
        if (addedInfo == null) {
          addedInfo = meetingInfo;
        } else {
          addedInfo += ", " + meetingInfo;
        }
        if (ifTime) {
          sql1 = "UPDATE GROUPS SET TIME = ? WHERE NAME = ?";
        } else {
          sql1 = "UPDATE GROUPS SET PLACE = ? WHERE NAME = ?";
        }
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        // Execute SQL string
        pstmt1.setString(1, addedInfo);
        pstmt1.setString(2, selectedGroup);
        System.out.println("added Info = " + addedInfo);
        System.out.println("group = " + selectedGroup);
        pstmt1.executeUpdate();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * This method adds role to the AVAILROLES column.
   *
   * @param selectedGroup is the group that the role is being added to
   * @param role          is the role being added
   * @return a String that states weather or not the method was successful
   */
  public String createRole(String selectedGroup, String role) {
    try {
      String sql = "SELECT AVAILROLES FROM GROUPS WHERE NAME = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, selectedGroup);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        String currentAvailableRoles = rs.getString(1);
        if (currentAvailableRoles == null) {
          currentAvailableRoles = role;
        } else {
          currentAvailableRoles += ", " + role;
        }
        String sql1 = "UPDATE GROUPS SET AVAILROLES = ? WHERE NAME = ?";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        // Execute SQL string
        pstmt1.setString(1, currentAvailableRoles);
        pstmt1.setString(2, selectedGroup);
        pstmt1.executeUpdate();
      }
      return "Role Created";
    } catch (Exception e) {
      e.printStackTrace();
      return "Failed to Create Role";
    }
  }

  /**
   * This method adds a new group the the GROUPS table if the name of the groupName doesn't already
   * exist.
   *
   * @param groupName         is the name of the new group being added
   * @param groupMeetingTimes is the meeting time of the new group being added
   * @param groupMeetingPlace is the meeting place of the new group being added
   * @param groupRoles        is the roles of the new group being added
   * @return a String that states weather or not the method was successful
   */
  public String createGroup(String groupName, String groupMeetingTimes, String groupMeetingPlace,
      String groupRoles) {
    try {
      String sql = "INSERT INTO GROUPS (NAME, TIME, PLACE, AVAILROLES) SELECT ?, ?, ?, ? WHERE NOT EXISTS( SELECT NAME FROM GROUPS WHERE NAME = ?);\n";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Execute SQL string
      pstmt.setString(1, groupName);
      pstmt.setString(2, groupMeetingTimes);
      pstmt.setString(3, groupMeetingPlace);
      pstmt.setString(4, groupRoles);
      pstmt.setString(5, groupName);
      int added = pstmt.executeUpdate();
      if (added == 0) {
        return "Name Already Exists";
      }
      String sql1 = "SELECT * FROM user WHERE SchoolId = ?";
      PreparedStatement pstmt1 = conn.prepareStatement(sql1);
      // Execute SQL string
      pstmt1.setString(1, schoolId);
      rs = pstmt1.executeQuery();
      if (rs.next()) {
        String currentGroup = rs.getString("Groups");
        if (currentGroup.equals("null")) {
          currentGroup = groupName;
        } else {
          currentGroup += ", " + groupName;
        }
        String sql2 = "UPDATE USER SET GROUPS = ? WHERE SCHOOLID = ?";
        PreparedStatement pstmt2 = conn.prepareStatement(sql2);
        // Execute SQL string
        pstmt2.setString(1, currentGroup);
        pstmt2.setString(2, schoolId);
        pstmt2.executeUpdate();
      }
      return "Group Created";
    } catch (Exception e) {
      e.printStackTrace();
      return "Fail to Create Group";
    }
  }
}