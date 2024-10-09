package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;


public class FinishSettingUpAccountController {

    @FXML
    private Label lastName;

    @FXML
    private TextField LastNameTextField;

    @FXML
    private TextField EmailAddressTestField;

    @FXML
    private Label emailAddress;

    @FXML
    private TextField FirstNameTestField;

    @FXML
    private Label firsName;

    @FXML
    private Label errorMessage;

    @FXML
    private Label middleName;

    @FXML
    private Button Save;

    @FXML
    private Label HeadingLabel;

    @FXML
    private TextField MiddleNameTextField;

    @FXML
    private ImageView Logo;

    @FXML
    void handleSaveButtonAction() {
        if (FirstNameTestField.getText().isEmpty() || EmailAddressTestField.getText().isEmpty()) {
            errorMessage.setText("First Name and Email Address cannot be empty.");
        } else {
        	  errorMessage.setText(""); // Clear error message
              //errorMessage.setVisible(false); // Hide error message

              // Load the next scene
              try {
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("SELECTROLE02.fxml"));
                  Parent root = loader.load();
                  Stage stage = (Stage) Save.getScene().getWindow();
                  Scene scene = new Scene(root);
                  stage.setScene(scene);
                  stage.show();
              } catch (IOException e) {
                  e.printStackTrace();
                  errorMessage.setText("Error loading the next scene. Please try again.");
                  //errorMessage.setVisible(true); // Show error message if there's an issue loading the scene
              }
        }
    }
}


