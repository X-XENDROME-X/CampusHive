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
            if (session == null) {
                showError("No active session found!");
                return;
            }

            String userRole = session.getRole();
            String destinationPage;

            // Determine destination page based on user role
            if (userRole.equalsIgnoreCase("Instructor")) {
                destinationPage = "Instructor_Homepage.fxml";
            } else if (userRole.equalsIgnoreCase("Admin")) {
                destinationPage = "Admin_Home_Page.fxml";
            } else {
                // Handle unexpected role
                showError("Invalid user role: " + userRole);
                return;
            }

            // Load the appropriate FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(destinationPage));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error navigating to home page: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error: " + e.getMessage());
        }
    }
}