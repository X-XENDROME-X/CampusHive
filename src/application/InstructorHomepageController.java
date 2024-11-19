package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * InstructorHomepageController Class
 * 
 * Description: This class handles the user interface and logic for the instructor's homepage
 * within the Campus Hive application. It manages functionalities such as viewing and managing
 * student interactions, accessing course materials, and facilitating discussions.
 * 
 * Copyright: Campus Hive © 2024
 * 
 * @author Th01
 * @version 2.00 2024-11-15
 */
public class InstructorHomepageController {
    @FXML
    private Label Heading;
    
    @FXML
    private Button LogOutButton;
    
    @FXML
    private ImageView Logo;
    
    @FXML
    private Label InstructorName;
    
    @FXML
    private Button back;
    
    @FXML private Button viewGenericMessagesButton;
    @FXML private Button viewSpecificMessagesButton;

    @FXML
    private void handleLogOutAction() {
        try {
            UserSession.getInstance().cleanUserSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login Page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) LogOutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        if (session != null) {
            InstructorName.setText(session.getUsername() + " (" + session.getRole() + ")!");
        } else {
            InstructorName.setText("No active session.");
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
    
    @FXML
    private void handleViewGenericMessages(ActionEvent event) {
        navigateToPage("GenericMessagesView.fxml", event);
    }

    @FXML
    private void handleViewSpecificMessages(ActionEvent event) {
        navigateToPage("SpecificMessagesView.fxml", event);
    }

    @FXML
    private void handleHelpArticleManagement(ActionEvent event) {
        navigateToPage("HelpArticlePage.fxml", event);
    }

    @FXML
    private void handleManageViewingRights(ActionEvent event) {
        navigateToPage("ManageViewingRightsView.fxml", event);
    }

    @FXML
    private void handleManageAdminRights(ActionEvent event) {
        navigateToPage("ManageAdminRightsView.fxml", event);
    }

    @FXML
    private void handleViewStudents(ActionEvent event) {
        navigateToPage("SpecializedStudentsView.fxml", event);
    }

    @FXML
    private void handleViewInstructors(ActionEvent event) {
        navigateToPage("SpecializedInstructorsView.fxml", event);
    }

    @FXML
    private void handleViewAdmins(ActionEvent event) {
        navigateToPage("SpecializedAdminsView.fxml", event);
    }

    private void navigateToPage(String fxmlFile, ActionEvent event) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(page);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user
            System.err.println("Error navigating to " + fxmlFile + ": " + e.getMessage());
        }
    }
}