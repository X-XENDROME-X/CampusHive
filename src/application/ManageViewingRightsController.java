package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

/**
 * ManageViewingRightsController Class
 * 
 * Description: This class handles the functionality for managing student viewing rights
 * for special access articles within the application.
 * 
 * Copyright: Campus Hive © 2024
 * 
 * @author Th01
 * @version 1.00 2024-11-15
 */
public class ManageViewingRightsController {
    @FXML
    private ImageView Logo;
    
    @FXML
    private Label Heading;
    
    @FXML
    private TextField studentIdField;
    
    @FXML
    private ComboBox<String> articleGroupComboBox;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Button grantAccessButton;
    
    @FXML
    private Button revokeAccessButton;
    
    @FXML
    private Label errorMessageLabel;

    @FXML
    public void initialize() {
        setupArticleGroups();
        setupListeners();
    }

    private void setupArticleGroups() {
        ObservableList<String> articleGroups = FXCollections.observableArrayList(
            "Special Access Group"
    
        );
        articleGroupComboBox.setItems(articleGroups);
    }

    private void setupListeners() {
        // Clear error message when student ID is modified
        studentIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText("");
            
            // Only allow numbers and letters
            if (!newValue.matches("[a-zA-Z0-9]*")) {
                studentIdField.setText(newValue.replaceAll("[^a-zA-Z0-9]", ""));
            }
        });

        // Clear error message when article group is selected
        articleGroupComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText("");
        });
    }

    @FXML
    private void handleGrantAccess(ActionEvent event) {
        if (validateInputs()) {
            try {
                String username = studentIdField.getText().trim();
                String articleGroup = articleGroupComboBox.getValue();
                
                if (studentExists(username)) {
                    if (!hasAccess(username, articleGroup)) {
                        grantStudentAccess(username, articleGroup);
                        showSuccess("Access granted successfully!");
                        clearFields();
                    } else {
                        showError("User already has access to this article group!");
                    }
                } else {
                    showError("Username does not exist!");
                }
            } catch (SQLException e) {
                showError("Database error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRevokeAccess(ActionEvent event) {
        if (validateInputs()) {
            try {
                String username = studentIdField.getText().trim();
                String articleGroup = articleGroupComboBox.getValue();
                
                if (studentExists(username)) {
                    if (hasAccess(username, articleGroup)) {
                        revokeStudentAccess(username, articleGroup);
                        showSuccess("Access revoked successfully!");
                        clearFields();
                    } else {
                        showError("User does not have access to this article group!");
                    }
                } else {
                    showError("Username does not exist!");
                }
            } catch (SQLException e) {
                showError("Database error: " + e.getMessage());
            }
        }
    }

    private boolean validateInputs() {
        String username = studentIdField.getText().trim();
        
        if (username.isEmpty()) {
            showError("Please enter a Username!");
            return false;
        }

        
        if (articleGroupComboBox.getValue() == null) {
            showError("Please select an Article Group!");
            return false;
        }
        
        return true;
    }

    private boolean studentExists(String studentId) throws SQLException {
        // TODO: Implement database check for student existence
        return true; // Placeholder
    }

    private boolean hasAccess(String studentId, String articleGroup) throws SQLException {
        // TODO: Implement database check for existing access
        return false; // Placeholder
    }

    private void grantStudentAccess(String studentId, String articleGroup) throws SQLException {
        // TODO: Implement database operation to grant access
    }

    private void revokeStudentAccess(String studentId, String articleGroup) throws SQLException {
        // TODO: Implement database operation to revoke access
    }

    private void showError(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setStyle("-fx-text-fill: #ff0606;");
    }

    private void showSuccess(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setStyle("-fx-text-fill: #47ae6a;");
    }

    private void clearFields() {
        studentIdField.clear();
        articleGroupComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            UserSession session = UserSession.getInstance();
            Parent homePage;
            

             if (session.hasPreviousPage()) {
                 String previousPage = session.getPreviousPage();
                 homePage = FXMLLoader.load(getClass().getResource(previousPage));

                 System.out.println("Redirecting to: " + previousPage);
                 Scene homeScene = new Scene(homePage);
                 Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 currentStage.setScene(homeScene);
                 currentStage.show();
             } else {
                 System.out.println("No previous page found.");
                 String role = session.getRole(); 
                 if (role == null) {
                     System.out.println("Role not set in session. Defaulting to SELECTROLE02.fxml.");
                     homePage = FXMLLoader.load(getClass().getResource("SELECTROLE02.fxml"));
                 } else if ("admin".equals(role)) {
                     homePage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
                 } else if ("instructor".equals(role)) {
                     homePage = FXMLLoader.load(getClass().getResource("Instructor_Homepage.fxml"));
                 } else if ("student".equals(role)) {
                     homePage = FXMLLoader.load(getClass().getResource("STUDENTHOMEPAGE.fxml"));
                 } else {
                     throw new IOException("User role not recognized");
                 }
                 Scene homeScene = new Scene(homePage);
                 Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 currentStage.setScene(homeScene);
                 currentStage.show();
             }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}