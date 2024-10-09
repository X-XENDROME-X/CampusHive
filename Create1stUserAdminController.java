package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class Create1stUserAdminController { 

    @FXML
    private PasswordField password;  // Password input field

    @FXML
    private Button CreateAdminAccount;  // Button to create the admin account

    @FXML
    private ImageView logo;  // Logo image

    @FXML
    private Label error;  // Label to display error messages

    @FXML
    private TextField userName;  // Username input field

    @FXML
    void handleCreateAdminAccount(ActionEvent event) {
        // Clear previous error message
        error.setText("");

        // Get the entered username and password
        String username = userName.getText();
        String passwordInput = password.getText();

        // Check if username or password is empty and display error
        if (username.isEmpty() || passwordInput.isEmpty()) {
            error.setText("Fields can't be empty.");
            return;
        }

        // Validate the password using the PasswordEvaluator class
        String passwordValidationResult = PasswordEvaluator.evaluatePassword(passwordInput);

        if (!passwordValidationResult.isEmpty()) {
            // If password fails validation, display the error message
            error.setText(passwordValidationResult);
        } else {
            // If the password is valid, create the admin account
            createAdminAccount(username, passwordInput);

            // Switch to the login page after successful account creation
            switchToLoginPage();
        }
    }

    // Method to create admin account (stub for now)
    private void createAdminAccount(String username, String password) {
        // Logic to handle admin account creation
        System.out.println("Admin account created for username: " + username);
    }

    // Method to switch to the login page
    private void switchToLoginPage() {
        try {
            // Load the Login Page FXML
            Parent loginPage = FXMLLoader.load(getClass().getResource("Login Page.fxml"));

            // Get the current stage from the CreateAdminAccount button
            Stage stage = (Stage) CreateAdminAccount.getScene().getWindow();

            // Set the new scene to the stage
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            error.setText("Failed to load the login page.");
        }
    }
}
