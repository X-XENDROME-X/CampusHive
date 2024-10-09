package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Invite_User_PageController {

    @FXML
    private TextArea emailAddress;  // TextArea for email input

    @FXML
    private Button sendInvite;  // Button to send invite

    @FXML
    private Label Message;  // Label to display success message

    @FXML
    private CheckBox instructor;  // CheckBox for Instructor role

    @FXML
    private CheckBox admin;  // CheckBox for Admin role

    @FXML
    private CheckBox Student;  // CheckBox for Student role

    @FXML
    private Button home;  // Home button

    // Method to handle the Send Invite button action
    @FXML
    void handleSendInvite(ActionEvent event) {
        // Get the email address from the TextArea
        String email = emailAddress.getText();

        // Basic validation to check if email is provided
        if (email.isEmpty()) {
            Message.setText("Please provide an email address.");
            Message.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        // Display a success message
        Message.setText("Invite sent to: " + email);
        Message.setTextFill(javafx.scene.paint.Color.valueOf("#256f25")); // Success message color

        // Optionally, you could handle the roles here, e.g., store or process the selected roles
        StringBuilder roles = new StringBuilder("Roles: ");
        if (Student.isSelected()) {
            roles.append("Student ");
        }
        if (admin.isSelected()) {
            roles.append("Admin ");
        }
        if (instructor.isSelected()) {
            roles.append("Instructor ");
        }
        System.out.println(roles.toString()); // For debugging purposes
    }

    // Method to handle the Home button action
    @FXML
    void handleHomeButtonAction(ActionEvent event) {
        try {
            // Load the admin homepage FXML file
            Parent adminHomepage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));

            // Set up the new scene
            Scene adminHomepageScene = new Scene(adminHomepage);

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            currentStage.setScene(adminHomepageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // You might want to show an error message to the user
        }
    }
}
