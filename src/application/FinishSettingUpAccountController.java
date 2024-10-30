/**
 * <p> FinishSettingUpAccountController Class </p>
 *
 * <p> Description: This class handles the setup process for a user account, guiding the user through
 * filling in required fields like first name, last name, and email. It also validates required fields
 * and saves the data in the database. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

package application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Platform;

public class FinishSettingUpAccountController {

    @FXML
    private TextField LastNameTextField;

    @FXML
    private TextField EmailAddressTestField;

    @FXML
    private TextField FirstNameTestField;

    @FXML
    private Label errorMessage;

    @FXML
    private Button Save;

    @FXML
    private TextField MiddleNameTextField;

    @FXML
    private void initialize() {

        Platform.runLater(() -> FirstNameTestField.requestFocus());

        FirstNameTestField.setOnAction(event -> MiddleNameTextField.requestFocus());

        MiddleNameTextField.setOnAction(event -> LastNameTextField.requestFocus());

        LastNameTextField.setOnAction(event -> EmailAddressTestField.requestFocus());

        EmailAddressTestField.setOnAction(event -> handleSaveButtonAction());
    }

    @FXML
    void handleSaveButtonAction() {

        if (FirstNameTestField.getText().isEmpty() || EmailAddressTestField.getText().isEmpty() || LastNameTextField.getText().isEmpty()) {

            errorMessage.setText("First Name, Last Name, and Email Address cannot be empty.");
        } else {

            errorMessage.setText("");

            String firstName = FirstNameTestField.getText();

            String middleName = MiddleNameTextField.getText();

            String lastName = LastNameTextField.getText();

            String emailAddress = EmailAddressTestField.getText();

            try {

                boolean isActive = true;

                UserSession session = UserSession.getInstance();

                H2Database.updateUser(session.getUsername(), firstName, middleName, lastName, emailAddress, isActive, session.getRole());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("SELECTROLE02.fxml"));

                Parent root = loader.load();

                Stage stage = (Stage) Save.getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            } catch (SQLException e) {

                e.printStackTrace();

                errorMessage.setText("Error updating user information. Please try again.");
            } catch (IOException e) {

                e.printStackTrace();

                errorMessage.setText("Error loading the next scene. Please try again.");
            }
        }
    }
}