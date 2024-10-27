package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteUserAccountController {

    @FXML
    private TextField usernameField; // For entering the username
    @FXML
    private Button deleteButton;      // Confirm deletion button
    @FXML
    private CheckBox confirmationCheckBox; // Checkbox for confirmation
    @FXML
    private Label statusLabel;        // For displaying status messages

    @FXML
    private Button cancelButton;
    
    @FXML
    private Button homeButton;

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        
        // Ensure user input is provided and confirmed via the checkbox
        if (username.isEmpty()) {
            statusLabel.setText("Username cannot be empty.");
        } else if (!confirmationCheckBox.isSelected()) {
            statusLabel.setText("Please confirm that you want to delete the account.");
        } else {
            // Perform account deletion logic here
            boolean success = deleteUserAccount(username);
            if (success) {
                statusLabel.setText("User account deleted successfully.");
            } else {
                statusLabel.setText("Failed to delete user account. User may not exist.");
            }
        }
    }
    private boolean deleteUserAccount(String username) {
        try {
            // Call the method from H2Database to delete the user
            boolean isDeleted = H2Database.deleteUserByUsername(username);

            if (isDeleted) {
                statusLabel.setText("User account deleted successfully.");
            } else {
                statusLabel.setText("User not found. Account deletion failed.");
            }

            return isDeleted; // Return true if the user was deleted, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("An error occurred while trying to delete the account.");
            return false;
        }
    }


    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        // Handle cancellation logic (e.g., redirect to another page)
        statusLabel.setText("Account deletion canceled.");
    }
    
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            // Load the Admin Home Page FXML file
            Parent adminHomePage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
            
            // Set up the new scene
            Scene adminHomeScene = new Scene(adminHomePage);
            
            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(adminHomeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
