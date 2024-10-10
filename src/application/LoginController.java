/*******
 * <p> LoginController Class </p>
 * 
 * <p> Description: This class handles user authentication within the Campus Hive application. 
 * It manages the login process by validating user credentials against the stored data and 
 * directing users to the appropriate interface based on their account setup status. </p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */

package application;

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
                String[] userData = CSVDatabase.checkLogin(username, password);
                if (userData != null) {
                    // Hide any error message
                    errorLabel.setVisible(false);

                    // Check if the email field exists and if the account is active
                    String email = userData[5]; // Assuming email is at index 5
                    boolean isActive = Boolean.parseBoolean(userData[6]); // Assuming active status is at index 6

                    System.out.println("User Data: " + Arrays.toString(userData));
                    System.out.println("Email: " + email);
                    System.out.println("Is Active: " + isActive);
                    if (email.isEmpty()) {
                        // Load the Finish Setting Up My Account page
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Finish_Setting_Up_Page 2.fxml"));
                        Parent root = loader.load();
                        
                        // Pass only username and password to the next controller
                        FinishSettingUpAccountController controller = loader.getController();
                        controller.setUserData(username, password);
                        
                        // Get current stage and set the new scene
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } else if (isActive) {
                        // Load the SELECTROLE02 page if the email exists and is active
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("SELECTROLE02.fxml"));
                        Parent root = loader.load();
                        
                        // Get current stage and set the new scene
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
