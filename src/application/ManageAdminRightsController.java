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
    private Button homeButton;
    
    @FXML
    private Button grantAccessButton;
    
    @FXML
    private Button revokeAccessButton;
    
    @FXML
    private Label errorMessageLabel;

    @FXML
    public void initialize() {


        
        // Add listener for username field
        adminUsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText("");
        });
        

    }

    @FXML
    private void handleGrantAccess(ActionEvent event) {
        if (validateInputs()) {
            try {
                String username = adminUsernameField.getText().trim();
//                String accessLevel = accessLevelComboBox.getValue();
                
                if (userExists(username)) {
                    if (!hasAdminAccess(username)) {
                        grantAdminAccess(username);
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

    private void grantAdminAccess(String username) throws SQLException {
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