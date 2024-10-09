package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class STUDENTHOMEPAGEcontroller {

    @FXML
    private Button logout;

    @FXML
    private Label title;

    @FXML
    private void handleLogoutAction() {
        try {
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
}
