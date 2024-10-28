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
import java.sql.SQLException;

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
            // If the password is valid, check if the username already exists
            try {
                if (H2Database.userExists(username)) {
                    error.setText("Admin account already exists. Please use a different username.");
                } else {
                    // Create the admin account
                    createAdminAccount(username, passwordInput);

                    // Switch to the login page after successful account creation
                    switchToLoginPage();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                error.setText("An error occurred while checking the user. Please try again.");
            }
        }
    }

    // Method to create admin account and add it to the users table in the H2 database
    private void createAdminAccount(String username, String password) {
        try {
            // Add user with the role "admin" to the database
            H2Database.addUser(username, password, "", "", "", "", true, "admin");
            System.out.println("Admin account created for username: " + username);
        } catch (SQLException e) {
            e.printStackTrace();
            error.setText("Failed to create admin account. Please try again.");
        }
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
