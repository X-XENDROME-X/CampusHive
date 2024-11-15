/**
 * <p> SELECTROLEcontroller Class </p>
 *
 * <p> Description: This class handles the selection of user roles within the application.
 * It includes methods for managing user interactions related to selecting student, admin, 
 * and instructor roles, along with visibility handling for error messages. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */


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
    private Button studentButton; 
    @FXML
    private Button adminButton;   
    @FXML
    private Button instructorButton; 

    @FXML
    private Label title;

    @FXML
    private Label errorMessage;  

    @FXML
    public void handleStudentButtonAction() {
        errorMessage.setVisible(false);  
        UserSession session = UserSession.getInstance();

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
            errorMessage.setVisible(true);  
        }
    }

    @FXML
    public void handleAdminButtonAction() {
        errorMessage.setVisible(false);  
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

    @FXML
    public void handleInstructorButtonAction() {
        errorMessage.setVisible(false);  
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