package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ResetUserAccountPageController {

    @FXML
    private TextField UsernameField;  // TextField for entering username

    @FXML
    private Label usernameLabel;  // Label to display "OTP sent to user" message

    @FXML
    private Button Reset;  // Reset button

    @FXML
    private Button Home;  // Home button

    @FXML
    private Label otpMessageLabel; 
    
    @FXML
    void handleResetButton(ActionEvent event) {
        // Update the label when the Reset button is pressed
    	 if (UsernameField.getText().isEmpty()) {
             otpMessageLabel.setText("Username field can't be empty."); // Display error message
         } else {
             // Update the label when the Reset button is pressed
             otpMessageLabel.setText("OTP sent to the user.");
             // You can add additional logic here, such as sending an OTP
         }
    }

    @FXML
    void handleHomeButton(ActionEvent event) {
        // Switch to the adminhomepage.fxml when Home button is pressed
        try {
            // Load the admin homepage FXML
            Parent adminHomepage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));

            // Get the current stage from the Home button
            Stage stage = (Stage) Home.getScene().getWindow();

            // Set the new scene to the stage
            stage.setScene(new Scene(adminHomepage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // You could display an error message on the page here if needed
        }
    }
}
