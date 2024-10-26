package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;
    
    @FXML
    private Button forgotPasswordButton;
    
    @FXML
    private Button CreateButton;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required.");
            errorLabel.setVisible(true);
        } else {
            try {
                // Check credentials in the database
                String[] userData = H2Database.checkLogin(username, password);
                if (userData != null) {
                    errorLabel.setVisible(false);

                    // Extract user information
                    String email = userData[5]; // Assuming email is at index 5
                    String role = userData[7];  // Assuming roles are at index 7
                    boolean isActive = Boolean.parseBoolean(userData[6]); // Account active status

                    // Create a user session
                    UserSession.getInstance(username, role, email);

                    // Check account setup and load appropriate page
                    if (email.isEmpty()) {
                        // Load the Finish Setting Up My Account page
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Finish_Setting_Up_Page 2.fxml"));
                        Parent root = loader.load();

                        // Get current stage and set the new scene
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } else if (isActive) {
                        // Load the SELECTROLE02 page if the account is active
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("SELECTROLE02.fxml"));
                        Parent root = loader.load();
                        
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        errorLabel.setText("Your account is inactive.");
                        errorLabel.setVisible(true);
                    }
                } else {
                    errorLabel.setText("Invalid login credentials.");
                    errorLabel.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("An error occurred during login.");
                errorLabel.setVisible(true);
            }
        }
    }


    @FXML
    private void handleForgotPasswordButtonAction(ActionEvent event) {
        try {
            // Load the Forgot Password page without validation
            Parent root = FXMLLoader.load(getClass().getResource("ResetPassOTP_user.fxml"));
            Stage stage = (Stage) forgotPasswordButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load Forgot Password page.");
            errorLabel.setVisible(true);
        }
    }
    
    // Method to switch to the login page
    @FXML
    private void switchToCreateAccountPage(ActionEvent event) {
        try {
            // Load the Login Page FXML
            Parent loginPage = FXMLLoader.load(getClass().getResource("Create Account Page.fxml"));

            // Get the current stage from the LoginButton
            Stage stage = (Stage) CreateButton.getScene().getWindow();

            // Set the new scene to the stage
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load the login page.");
        }
    }
}
