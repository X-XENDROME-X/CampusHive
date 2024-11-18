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
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

package application;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
}