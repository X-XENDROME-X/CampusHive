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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageAdminRightsController {
    @FXML
    private ImageView Logo;
    
    @FXML
    private Label Heading;
    
    @FXML
    private TextField adminUsernameField;
    
    @FXML
    private ComboBox<String> accessLevelComboBox;
    
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
        // Initialize access level combo box
        ObservableList<String> accessLevels = FXCollections.observableArrayList(
            "Full Admin Access",
            "Article Group A Admin",
            "Article Group B Admin",
            "Article Group C Admin",
            "Limited Admin Access"
        );
        accessLevelComboBox.setItems(accessLevels);
        
        // Add listener for username field
        adminUsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText("");
        });
        
        // Add listener for combo box
        accessLevelComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText("");
        });
    }

    @FXML
    private void handleGrantAccess(ActionEvent event) {
        if (validateInputs()) {
            try {
                String username = adminUsernameField.getText().trim();
                String accessLevel = accessLevelComboBox.getValue();
                
                if (userExists(username)) {
                    if (!hasAdminAccess(username)) {
                        grantAdminAccess(username, accessLevel);
                        showSuccess("Admin access granted successfully!");
                        clearFields();
                    } else {
                        showError("User already has admin access!");
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
        if (validateUsername()) {
            try {
                String username = adminUsernameField.getText().trim();
                
                if (userExists(username)) {
                    if (hasAdminAccess(username)) {
                        revokeAdminAccess(username);
                        showSuccess("Admin access revoked successfully!");
                        clearFields();
                    } else {
                        showError("User does not have admin access!");
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
        if (!validateUsername()) {
            return false;
        }
        
        if (accessLevelComboBox.getValue() == null) {
            showError("Please select an access level!");
            return false;
        }
        
        return true;
    }

    private boolean validateUsername() {
        String username = adminUsernameField.getText().trim();
        
        if (username.isEmpty()) {
            showError("Please enter a username!");
            return false;
        }
        
        if (username.length() < 3) {
            showError("Username must be at least 3 characters long!");
            return false;
        }
        
        return true;
    }

    private boolean userExists(String username) throws SQLException {
        // TODO: Implement database check for user existence
        return true; // Placeholder
    }

    private boolean hasAdminAccess(String username) throws SQLException {
        // TODO: Implement database check for admin access
        return false; // Placeholder
    }

    private void grantAdminAccess(String username, String accessLevel) throws SQLException {
        // TODO: Implement database operation to grant admin access
    }

    private void revokeAdminAccess(String username) throws SQLException {
        // TODO: Implement database operation to revoke admin access
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
        adminUsernameField.clear();
        accessLevelComboBox.getSelectionModel().clearSelection();
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