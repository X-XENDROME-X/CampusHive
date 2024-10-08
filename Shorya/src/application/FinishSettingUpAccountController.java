package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
            // Save logic here
        }
    }
}


