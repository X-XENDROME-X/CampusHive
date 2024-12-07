/**
 * <p> CreateAccountPageController Class </p>
 *
 * <p> Description: This class handles the creation of new user accounts, including username, password validation, invitation code checks, and switching to the login page post-account creation. It uses the H2 database for user data management. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


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

    @FXML
    private void initialize() {

        Platform.runLater(() -> usernameField.requestFocus());

        usernameField.setOnAction(event -> passwordField.requestFocus());

        passwordField.setOnAction(event -> confirmPasswordField.requestFocus());

        confirmPasswordField.setOnAction(event -> invitationCodeField.requestFocus());

        invitationCodeField.setOnAction(event -> handleCreateAccountButtonAction(new ActionEvent()));
    }

    @FXML
    private void handleCreateAccountButtonAction(ActionEvent event) {

        String username = usernameField.getText();

        String password = passwordField.getText();

        String confirmPassword = confirmPasswordField.getText();

        String invitationCode = invitationCodeField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || invitationCode.isEmpty()) {
            statusLabel.setText("All fields must be filled.");
        } 

        else if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
        } 

        else if (!isValidPassword(password)) {
            statusLabel.setText("Password must be at least 7 characters, include upper/lowercase, 1 special character, and no spaces, '-', ':', ';'.");
        } 

        else {
            String role = isValidInvitationCode(invitationCode);
            if (role != null) {
                boolean success = createAccount(username, password, role);

                if (success) {
                    statusLabel.setText("Account created successfully.");
                    switchToLoginPage(event); 
                } else {
                    statusLabel.setText("Failed to create account.");
                }
            } else {
                statusLabel.setText("Invalid invitation code.");
            }
        }
    }

    private boolean isValidPassword(String password) {

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)[^\\s\\-:;]{7,}$";

        return Pattern.matches(passwordPattern, password);
    }

    private String isValidInvitationCode(String invitationCode) {
        try {

            String role = H2Database.getRoleByInvitationCode(invitationCode);
            if (role != null) {

                H2Database.removeInvitationCode(invitationCode);
                return role; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    private boolean createAccount(String username, String password, String role) {
        try {
            // Check if the 'instructor' role exists
            if (!H2Database.checkForInstructorRole() && "instructor".equals(role)) {
                // If 'instructor' role doesn't exist, create the account with the 'Instructor' role
                H2Database.addUser(username, password, "", "", "", "", false, "Instructor", true, true);
            } else {
                // Otherwise, create the user with the provided role
                H2Database.addUser(username, password, "", "", "", "", false, role, false, false);
            }
            return true; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    @FXML
    private void switchToLoginPage(ActionEvent event) {
        try {

            Parent loginPage = FXMLLoader.load(getClass().getResource("Login Page.fxml"));

            Stage stage = (Stage) LoginButton.getScene().getWindow();

            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load the login page.");
        }
    }
}
