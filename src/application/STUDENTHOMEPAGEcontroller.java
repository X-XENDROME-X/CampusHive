package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class STUDENTHOMEPAGEcontroller {

    @FXML
    private Button logout;

    @FXML
    private Label title;
    
    @FXML Button Back;
    

    @FXML
    private void handleLogoutAction() {
        try {
            UserSession.getInstance().cleanUserSession(); // Clear the session
            // Load the Create Account FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Create Account Page.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) logout.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user
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
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(adminHomeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
