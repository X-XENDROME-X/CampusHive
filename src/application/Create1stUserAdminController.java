/**
 * <p> Create1stUserAdminController Class </p>
 *
 * <p> Description: This class manages the creation of the first admin account in the application.
 * It includes functions to validate inputs, create the admin account, and handle navigation to
 * the login page after successful account creation. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 *  
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

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
import javafx.application.Platform;

public class Create1stUserAdminController {

    @FXML
    private PasswordField password;

    @FXML
    private Button CreateAdminAccount;

    @FXML
    private ImageView logo;

    @FXML
    private Label error;

    @FXML
    private TextField userName;

    @FXML
    void handleCreateAdminAccount(ActionEvent event) {

        error.setText("");

        String username = userName.getText();

        String passwordInput = password.getText();

        if (username.isEmpty() || passwordInput.isEmpty()) {
            error.setText("Fields can't be empty.");
            return;
        }

        String passwordValidationResult = PasswordEvaluator.evaluatePassword(passwordInput);

        if (!passwordValidationResult.isEmpty()) {
            error.setText(passwordValidationResult);
        } else {

            try {
                if (H2Database.userExists(username)) {

                    error.setText("Admin account already exists. Please use a different username.");
                } else {

                    createAdminAccount(username, passwordInput);

                    switchToLoginPage();
                }
            } catch (SQLException e) {

                e.printStackTrace();
                error.setText("An error occurred while checking the user. Please try again.");
            }
        }
    }

    @FXML
    private void initialize() {

        Platform.runLater(() -> userName.requestFocus());

        userName.setOnAction(event -> password.requestFocus());

        password.setOnAction(event -> handleCreateAdminAccount(new ActionEvent()));
    }

    private void createAdminAccount(String username, String password) {
        try {

            H2Database.addUser(username, password, "", "", "", "", true, "admin",true,true);
            System.out.println("Admin account created for username: " + username);
        } catch (SQLException e) {

            e.printStackTrace();
            error.setText("Failed to create admin account. Please try again.");
        }
    }

    private void switchToLoginPage() {
        try {

            Parent loginPage = FXMLLoader.load(getClass().getResource("Login Page.fxml"));

            Stage stage = (Stage) CreateAdminAccount.getScene().getWindow();

            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
            error.setText("Failed to load the login page.");
        }
    }
}