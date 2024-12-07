/*******
 * <p> Admin_Home_PageController Class </p>
 * 
 * <p> Description: This class controls the Admin Home Page in the Campus Hive application. 
 * It handles user interactions and provides functionalities such as managing user accounts, 
 * viewing the list of users, and navigating to other administrative tasks like inviting users, 
 * resetting accounts, and modifying roles. </p>
 * 
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Admin_Home_PageController {

    @FXML
    private Button deleteUser;

    @FXML
    private Button editRoles;

    @FXML
    private Button inviteUser;

    @FXML
    private Button listUsers;

    @FXML
    private Button logOut;

    @FXML
    private Button resetUser;

    @FXML 
    private Button back;
    
    @FXML
    private Label errorMessageLabel;
    
    @FXML private Button manageViewingRights;
    @FXML private Button manageAdminRights;
    @FXML private Button viewSpecializedStudents;
    @FXML private Button viewSpecializedInstructors;
    @FXML private Button viewSpecializedAdmins;
    @FXML private Button viewGenericMessages;
    @FXML private Button viewSpecificMessages;

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        try {

            UserSession.getInstance().cleanUserSession();

            Parent createAccountPage = FXMLLoader.load(getClass().getResource("Login Page.fxml"));

            Scene createAccountScene = new Scene(createAccountPage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(createAccountScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelpArticleManagement(ActionEvent event) {
        try {
            UserSession.getInstance().addPageToHistory("Admin_Home_Page.fxml");

            Parent HelpArticlePage = FXMLLoader.load(getClass().getResource("HelpArticlePage.fxml"));

            Scene HelpArticleScene = new Scene(HelpArticlePage);

            Stage currentStage = (Stage) back.getScene().getWindow();

            currentStage.setScene(HelpArticleScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResetUserAction(ActionEvent event) {
        try {
            UserSession.getInstance().addPageToHistory("Admin_Home_Page.fxml");

            Parent resetUserAccountPage = FXMLLoader.load(getClass().getResource("ResetPassOTP.fxml"));

            Scene resetUserAccountScene = new Scene(resetUserAccountPage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(resetUserAccountScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInviteUserAction(ActionEvent event) {
        try {
            UserSession.getInstance().addPageToHistory("Admin_Home_Page.fxml");

            Parent inviteUserPage = FXMLLoader.load(getClass().getResource("Invite_User_Page.fxml"));

            Scene inviteUserScene = new Scene(inviteUserPage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(inviteUserScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListUsersAction(ActionEvent event) {
        try {
            UserSession.getInstance().addPageToHistory("Admin_Home_Page.fxml");

            Parent userListPage = FXMLLoader.load(getClass().getResource("ListofUsers.fxml"));

            Scene userListScene = new Scene(userListPage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(userListScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteUserAction(ActionEvent event) {
        try {
            UserSession.getInstance().addPageToHistory("Admin_Home_Page.fxml");

            Parent deleteUserPage = FXMLLoader.load(getClass().getResource("Delete User Account.fxml"));

            Scene deleteUserScene = new Scene(deleteUserPage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(deleteUserScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditRoleAction(ActionEvent event) {
        try {
            UserSession.getInstance().addPageToHistory("Admin_Home_Page.fxml");

            Parent editRolePage = FXMLLoader.load(getClass().getResource("Add_Remove_UserRole.fxml"));

            Scene editRoleScene = new Scene(editRolePage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(editRoleScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {

            Parent adminHomePage = FXMLLoader.load(getClass().getResource("SELECTROLE02.fxml"));

            Scene adminHomeScene = new Scene(adminHomePage);

            Stage currentStage = (Stage) back.getScene().getWindow();

            currentStage.setScene(adminHomeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // New handlers for the additional buttons
    @FXML
    private void handleManageViewingRights(ActionEvent event) {
        try {
            // Check if the user has special viewing rights
            String username = UserSession.getInstance().getUsername();
            if (!H2Database.checkSpecialView(username)) {
                showError("Access Denied: You do not have permission to manage viewing rights.");
                return;
            }

            navigateToPage("ManageViewingRightsView.fxml", event);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: Unable to verify viewing rights.");
        } catch (IOException e) {
            e.printStackTrace();
            showError("An error occurred while navigating to the page.");
        }
    }

    @FXML
    private void handleManageAdminRights(ActionEvent event) {
        try {
            // Check if the user has special admin rights
            String username = UserSession.getInstance().getUsername();
            if (!H2Database.checkSpecialAdmin(username)) {
                showError("Access Denied: You do not have permission to manage admin rights.");
                return;
            }

            navigateToPage("ManageAdminRightsView.fxml", event);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: Unable to verify admin rights.");
        } catch (IOException e) {
            e.printStackTrace();
            showError("An error occurred while navigating to the page.");
        }
    }


    private void showError(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setStyle("-fx-text-fill: #ff0606;");
    }


    @FXML
    private void handleViewSpecializedStudents(ActionEvent event) {
        try {
            navigateToPage("SpecializedStudentsView.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewSpecializedInstructors(ActionEvent event) {
        try {
            navigateToPage("SpecializedInstructorsView.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewSpecializedAdmins(ActionEvent event) {
        try {
            navigateToPage("SpecializedAdminsView.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleViewGenericMessages(ActionEvent event) {
        try {
            navigateToPage("GenericMessagesView.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewSpecificMessages(ActionEvent event) {
        try {
            navigateToPage("SpecificMessagesView.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Helper method to reduce code duplication
    private void navigateToPage(String fxmlFile, ActionEvent event) throws IOException {
        UserSession.getInstance().addPageToHistory("Admin_Home_Page.fxml");
        Parent page = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(page);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(scene);
        currentStage.show();
    }
}
