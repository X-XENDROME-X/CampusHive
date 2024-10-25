/*******
 * <p> CreateAccountPageController Class </p>
 * 
 * <p> Description: This class manages the logic for user account creation in the Campus Hive application. It handles user input, validates form data, and creates new user accounts with relevant details such as username, password, and role. This controller is a part of the user onboarding process within the application. </p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */

package application;

import java.io.IOException;
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

    // Invitation code validation method
    private String isValidInvitationCode(String invitationCode) {
        try {
            // Check if the invitation code exists and get associated roles
            String role = CSVDatabase.getRoleByInvitationCode(invitationCode);
            if (role != null) {
                // Remove the invitation code once used
                CSVDatabase.removeInvitationCode(invitationCode);
                return role; // Return the role associated with the invitation
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Invitation code not found
    }

    // Logic to create account and save to CSV
    private boolean createAccount(String username, String password, String role) {
        try {
            // Assuming email is in usernameField for simplicity. Adjust if needed.
            CSVDatabase.addUser(username, password, "", "", "", "", false, role);
            return true; // Successful account creation
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed account creation
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
