/**
 * <p> LoginController Class </p>
 *
 * <p> Description: This class manages the login process for users in the application,
 * handling user input validation, session management, and navigation to various pages 
 * based on user credentials. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

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
import javafx.application.Platform; 

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
    private void initialize() {

        Platform.runLater(() -> usernameField.requestFocus()); 
        usernameField.setOnAction(event -> passwordField.requestFocus()); 
        passwordField.setOnAction(event -> handleLoginButtonAction(new ActionEvent())); 
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {

        String username = usernameField.getText(); 
        String password = passwordField.getText(); 

        if (username.isEmpty() || password.isEmpty()) { 
            errorLabel.setText("All fields are required."); 
            errorLabel.setVisible(true); 
        } else {
            try {

                String[] userData = H2Database.checkLogin(username, password); 
                if (userData != null) { 
                    errorLabel.setVisible(false); 

                    String email = userData[5]; 
                    String role = userData[7]; 
                    boolean isActive = Boolean.parseBoolean(userData[6]); 

                    UserSession.getInstance(username, role, email); 

                    if (email.isEmpty()) { 

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Finish_Setting_Up_Page 2.fxml")); 
                        Parent root = loader.load(); 

                        Stage stage = (Stage) loginButton.getScene().getWindow(); 
                        Scene scene = new Scene(root); 
                        stage.setScene(scene); 
                        stage.show(); 
                    } else if (isActive) { 

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

    @FXML
    private void switchToCreateAccountPage(ActionEvent event) {

        try {

            Parent loginPage = FXMLLoader.load(getClass().getResource("Create Account Page.fxml")); 

            Stage stage = (Stage) CreateButton.getScene().getWindow(); 

            stage.setScene(new Scene(loginPage)); 
            stage.show(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
            errorLabel.setText("Failed to load the login page."); 
        }
    }
}