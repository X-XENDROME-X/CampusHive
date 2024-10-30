/*******
 * <p> InstructorHomepageController Class </p>
 * 
 * <p> Description: This class handles the user interface and logic for the instructor's homepage 
 * within the Campus Hive application. It manages functionalities such as viewing and managing student 
 * interactions, accessing course materials, and facilitating discussions related to the curriculum. </p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */

package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class InstructorHomepageController {

    @FXML
    private Label Heading;

    @FXML
    private Button LogOutButton;

    @FXML
    private ImageView Logo;

    @FXML
    private Label InstructorName;
    
    @FXML
    private Button back;
    
    @FXML
    private void handleLogOutAction() {
        try {
            UserSession.getInstance().cleanUserSession(); // Clear the session

            // Load the Create Account FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login Page.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) LogOutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user
        }
    }

    @FXML
    public void initialize() {
        // Get the current user session
        UserSession session = UserSession.getInstance();
        if (session != null) {
            // Use the session data (username, role, etc.)
            InstructorName.setText(session.getUsername() + " (" + session.getRole() + ")!");
        } else {
            // Handle the case where no user session is active
            InstructorName.setText("No active session.");
        }
    }
    
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the Admin Home Page FXML file
            Parent adminHomePage = FXMLLoader.load(getClass().getResource("SELECTROLE02.fxml"));
            
            // Set up the new scene
            Scene adminHomeScene = new Scene(adminHomePage);
            
            // Get the current stage from the event source
            Stage currentStage = (Stage) back.getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(adminHomeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleHelpArticleManagement(ActionEvent event) {
        try {
            // Load the Admin Home Page FXML file
            Parent HelpArticlePage = FXMLLoader.load(getClass().getResource("HelpArticlePage.fxml"));
            
            // Set up the new scene
            Scene HelpArticleScene = new Scene(HelpArticlePage);
            
            // Get the current stage from the event source
            Stage currentStage = (Stage) back.getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(HelpArticleScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
