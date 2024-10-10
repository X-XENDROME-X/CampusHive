/*******
 * <p> SELECTROLEcontroller Class </p>
 * 
 * <p> Description: Manages the user interface for role selection within the Campus Hive 
 * JavaFX application. This class handles user input for selecting the appropriate role 
 * (e.g., student, instructor, admin) during the account setup process, ensuring that 
 * users are assigned the correct permissions and functionalities based on their chosen role. </p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
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
    private Button studentButton; // Assuming this is the "STUDENT" button
    @FXML
    private Button adminButton;   // Assuming this is the "ADMIN" button
    @FXML
    private Button instructorButton; // Assuming this is the "INSTRUCTOR" button

    @FXML
    private Label title;

    // This method is triggered when the "STUDENT" button is pressed
    @FXML
    public void handleStudentButtonAction() {
        try {
            // Load the student homepage FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("STUDENTHOMEPAGE.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) studentButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     //You can implement similar methods for the ADMIN and INSTRUCTOR buttons
    @FXML
    public void handleAdminButtonAction() {
        try {
            // Load the admin homepage FXML (you'll need to create adminhomepage.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Home_Page.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) adminButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleInstructorButtonAction() {
        try {
            // Load the instructor homepage FXML (you'll need to create instructorhomepage.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Instructor_Homepage.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) instructorButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
