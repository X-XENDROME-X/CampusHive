package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class SELECTROLEcontroller {

    @FXML
    private Button studentButton; // Assuming this is the "STUDENT" button
    @FXML
    private Button adminButton;   // Assuming this is the "ADMIN" button
    @FXML
    private Button instructorButton; // Assuming this is the "INSTRUCTOR" button

    @FXML
    private Label title;
    
    @FXML
    private Label errorMessage;  // This will be used to show errors

    // This method is triggered when the "STUDENT" button is pressed
    @FXML
    public void handleStudentButtonAction() {
        errorMessage.setVisible(false);  // Reset error message
        UserSession session = UserSession.getInstance();

        // Check if the user has the "Student" role
        if (session != null && (session.getRole().equalsIgnoreCase("student") || session.getRole().equalsIgnoreCase("admin"))) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("STUDENTHOMEPAGE.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) studentButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMessage.setText("Access denied. You do not have the Student role.");
            errorMessage.setVisible(true);  // Show the error message
        }
    }

     //You can implement similar methods for the ADMIN and INSTRUCTOR buttons
    @FXML
    public void handleAdminButtonAction() {
        errorMessage.setVisible(false);  // Reset error message
        UserSession session = UserSession.getInstance();
        if (session != null && session.getRole().equalsIgnoreCase("admin")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Home_Page.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) adminButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMessage.setText("Access denied. You do not have the Admin role.");
            errorMessage.setVisible(true);
        }
    }

    // Handle Instructor Button Action
    @FXML
    public void handleInstructorButtonAction() {
        errorMessage.setVisible(false);  // Reset error message
        UserSession session = UserSession.getInstance();
        if (session != null && (session.getRole().equalsIgnoreCase("instructor")|| session.getRole().equalsIgnoreCase("admin"))) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Instructor_Homepage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) instructorButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMessage.setText("Access denied. You do not have the Instructor role.");
            errorMessage.setVisible(true);
        }
    }
}
