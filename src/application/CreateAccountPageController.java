package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;
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
import javafx.application.Platform;

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

    // Method to initialize and add "Enter" key functionality Coded this cus I am Physically repulsed by he mouse :>
    @FXML
    private void initialize() {
        // Delay the focus request until the scene is fully loaded
        Platform.runLater(() -> usernameField.requestFocus());

        // Move focus to passwordField when Enter is pressed in usernameField
        usernameField.setOnAction(event -> passwordField.requestFocus());

        // Move focus to confirmPasswordField when Enter is pressed in passwordField
        passwordField.setOnAction(event -> confirmPasswordField.requestFocus());

        // Move focus to invitationCodeField when Enter is pressed in confirmPasswordField
        confirmPasswordField.setOnAction(event -> invitationCodeField.requestFocus());

        // Trigger account creation when Enter is pressed in invitationCodeField
        invitationCodeField.setOnAction(event -> handleCreateAccountButtonAction(new ActionEvent()));
    }

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
        } else if (!isValidPassword(password)) {
            statusLabel.setText("Password must be at least 7 characters, include upper/lowercase, 1 special character, and no spaces, '-', ':', ';'.");
        } else {
            String role = isValidInvitationCode(invitationCode);
            if (role != null) {
                boolean success = createAccount(username, password, role);
                if (success) {
                    statusLabel.setText("Account created successfully.");
                    switchToLoginPage(event); // After account creation, go to login page
                } else {
                    statusLabel.setText("Failed to create account.");
                }
            } else {
                statusLabel.setText("Invalid invitation code.");
            }
        }
    }

    // Password validation method
    private boolean isValidPassword(String password) {
        // Regex to check password constraints
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)[^\\s\\-:;]{7,}$";
        return Pattern.matches(passwordPattern, password);
    }

 // Invitation code validation method using H2 database
    private String isValidInvitationCode(String invitationCode) {
        try {
            // Check if the invitation code exists and get associated roles from the database
            String role = H2Database.getRoleByInvitationCode(invitationCode);
            if (role != null) {
                // Remove the invitation code from the database once it is used
                H2Database.removeInvitationCode(invitationCode);
                return role; // Return the role associated with the invitation
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Invitation code not found or error occurred
    }


 // Logic to create an account and save it to the H2 database
    private boolean createAccount(String username, String password, String role) {
        try {
            // Assuming other user details are optional or set to default (empty strings)
            H2Database.addUser(username, password, "", "", "", "", false, role);
            return true; // Account creation successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failed account creation due to an SQL exception
        }
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
