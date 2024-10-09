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
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required.");
            errorLabel.setVisible(true);
        } else {
            // Placeholder for database login verification logic
            // boolean isValidUser = checkCredentialsInDatabase(username, password, invitationCode);

            // For now, assume successful login
            boolean isValidUser = true; // Replace with actual database logic

            if (isValidUser) {
                // Hide any error message
                errorLabel.setVisible(false);
                try {
                    // Load the OTP login page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Finish_Setting_Up_Page 2.fxml"));
                    Parent root = loader.load();
                    // Get current stage and set the new scene
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    errorLabel.setText("Failed to load OTP page.");
                    errorLabel.setVisible(true);
                }
                // Load the next page (LoginPageOTP)
            } else {
                errorLabel.setText("Invalid login credentials.");
                errorLabel.setVisible(true);
            }
        }
    }

    @FXML
    private void handleForgotPasswordButtonAction(ActionEvent event) {
        try {
            // Load the Forgot Password page without validation
            Parent root = FXMLLoader.load(getClass().getResource("ResetPassOTP.fxml"));
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
}
