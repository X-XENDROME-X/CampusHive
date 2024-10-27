package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;

public class FinishSettingUpAccountController {

    @FXML
    private TextField LastNameTextField;

    @FXML
    private TextField EmailAddressTestField;

    @FXML
    private TextField FirstNameTestField;

    @FXML
    private Label errorMessage;

    @FXML
    private Button Save;

    @FXML
    private TextField MiddleNameTextField;

    // Handle save button click event
    @FXML
    void handleSaveButtonAction() {
        // Check for empty fields
        if (FirstNameTestField.getText().isEmpty() || EmailAddressTestField.getText().isEmpty() || LastNameTextField.getText().isEmpty()) {
            errorMessage.setText("First Name, Last Name, and Email Address cannot be empty.");
        } else {
            errorMessage.setText(""); // Clear error message

            // Collect data
            String firstName = FirstNameTestField.getText();
            String middleName = MiddleNameTextField.getText(); // Can be optional, handle accordingly
            String lastName = LastNameTextField.getText();
            String emailAddress = EmailAddressTestField.getText();

            // Update the existing user in the H2 database
            try {
                boolean isActive = true; // Assuming a user being updated is active
                
                UserSession session = UserSession.getInstance();


                // Use the updateUser method to update existing user information
                H2Database.updateUser(session.getUsername(), firstName, middleName, lastName, emailAddress, isActive, session.getRole());

                // Load the next scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SELECTROLE02.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) Save.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (SQLException e) {
                e.printStackTrace();
                errorMessage.setText("Error updating user information. Please try again.");
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage.setText("Error loading the next scene. Please try again.");
            }
        }
    }
}
