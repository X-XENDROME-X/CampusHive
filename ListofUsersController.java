package application;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ListofUsersController {

    @FXML
    private Button homeButton;

    @FXML
    private ListView<?> myListView;

    // Method to handle the home button action
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
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
        }
    }
}
