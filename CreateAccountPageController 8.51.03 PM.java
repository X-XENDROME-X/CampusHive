package application;

import java.io.IOException;
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

public class CreateAccountPageController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button LoginButton;

    @FXML
    private Label statusLabel;
    
    @FXML
    private TextField invitationCodeField;

    // Method to handle account creation
    @FXML
    private void handleCreateAccountButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String invitationCode = invitationCodeField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || invitationCode.isEmpty()) {
            statusLabel.setText("All fields must be filled.");
        } else if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
        } else {
            boolean success = createAccount(username, password);
            if (success) {
                statusLabel.setText("Account created successfully.");
                switchToLoginPage(event); // After account creation, go to login page
            } else {
                statusLabel.setText("Failed to create account.");
            }
        }
    }

    // Logic to simulate account creation (return true for successful creation)
    private boolean createAccount(String username, String password) {
        return true; // Simulating successful account creation for now
    }

    // Method to switch to the login page
    @FXML
    private void switchToLoginPage(ActionEvent event) {
        try {
            // Load the Login Page FXML
            Parent loginPage = FXMLLoader.load(getClass().getResource("Login Page.fxml"));

            // Get the current stage from the LoginButton
            Stage stage = (Stage) LoginButton.getScene().getWindow();

            // Set the new scene to the stage
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load the login page.");
        }
    }
}
