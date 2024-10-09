package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class AddRemoveUserRoleController {

	@FXML
    private Label ErrorMessageLabel;

    @FXML
    private Button HomeButton;

    @FXML
    private Label Heading;

    @FXML
    private ComboBox<String> RoleSelectionBox;

    @FXML
    private Button AddRoleButton;

    @FXML
    private ImageView HomeButtonImage;

    @FXML
    private TextField UsernameTextField;

    @FXML
    private ImageView Logo;

    @FXML
    private Button RemoveRoleButton;

    
    @FXML
    void initialize() {
        RoleSelectionBox.getItems().addAll("Admin", "Student", "Instructor");
        RoleSelectionBox.setPromptText("Select Role");
    }
    
    @FXML
    void AddRole() {
        if (UsernameTextField.getText().isEmpty() || RoleSelectionBox.getValue() == null) {
            ErrorMessageLabel.setText("Username and Role Selection cannot be empty.");
        } else {
            // Add role logic here
            ErrorMessageLabel.setText(""); 
        }
    }

    @FXML
    void RemoveRole() {
        if (UsernameTextField.getText().isEmpty() || RoleSelectionBox.getValue() == null) {
            ErrorMessageLabel.setText("Username and Role Selection cannot be empty.");
        } else {
            // Remove role logic here
            ErrorMessageLabel.setText(""); 
        }
    }

    
 // Method to handle the Home button action
    @FXML
    void handleHomeButtonAction(ActionEvent event) {
        try {
            Parent adminHomepage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
            Scene adminHomepageScene = new Scene(adminHomepage);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(adminHomepageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}




