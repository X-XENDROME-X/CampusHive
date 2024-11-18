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
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
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
            UserSession.getInstance().cleanUserSession(); 

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login Page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) LogOutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    public void initialize() {

        UserSession session = UserSession.getInstance();
        if (session != null) {

            InstructorName.setText(session.getUsername() + " (" + session.getRole() + ")!");
        } else {

            InstructorName.setText("No active session.");
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {

            Parent adminHomePage = FXMLLoader.load(getClass().getResource("SELECTROLE02.fxml"));

            Scene adminHomeScene = new Scene(adminHomePage);

            Stage currentStage = (Stage) back.getScene().getWindow();

            currentStage.setScene(adminHomeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelpArticleManagement(ActionEvent event) {
        try {
            UserSession.getInstance().addPageToHistory("Instructor_Homepage.fxml");

            Parent HelpArticlePage = FXMLLoader.load(getClass().getResource("HelpArticlePage.fxml"));

            Scene HelpArticleScene = new Scene(HelpArticlePage);

            Stage currentStage = (Stage) back.getScene().getWindow();

            currentStage.setScene(HelpArticleScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}