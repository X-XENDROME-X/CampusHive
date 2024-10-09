package application;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Admin_Home_PageController {

    @FXML
    private Button deleteUser;

    @FXML
    private Button editRoles;

    @FXML
    private Button inviteUser;

    @FXML
    private Button listUsers;

    @FXML
    private Button logOut;

    @FXML
    private Button resetUser;

    // Add this method to handle the logout button action
    @FXML
    private void handleLogoutAction(ActionEvent event) {
        try {
            // Load the Create Account FXML file
            Parent createAccountPage = FXMLLoader.load(getClass().getResource("Create Account Page.fxml"));
            
            // Set up the new scene
            Scene createAccountScene = new Scene(createAccountPage);
            
            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(createAccountScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
