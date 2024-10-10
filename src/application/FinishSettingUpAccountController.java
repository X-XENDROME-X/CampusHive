/*******
 * <p> FinishSettingUpAccountController Class </p>
 * 
 * <p> Description: This class manages the user interface and logic for finalizing user account setup in the 
 * Campus Hive application. It handles user input for completing account details, validates the information, 
 * and updates the user status in the system upon successful setup.</p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */

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
    private Label lasName;

    @FXML
    private TextField LastNameTextField;

    @FXML
    private TextField EmailAddressTestField;

    @FXML
    private Label emaiAddress;

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
	
	private String username;
	
	private String password;
	
    public void setUserData(String username, String password) {
        this.username = username;
        this.password = password;

    }

    @FXML
    void handleSaveButtonAction() {
        // Check for empty fields
        if (FirstNameTestField.getText().isEmpty() || EmailAddressTestField.getText().isEmpty() || LastNameTextField.getText().isEmpty()) {
            errorMessage.setText("First Name, Last Name, and Email Address cannot be empty.");
        } else {
            errorMessage.setText(""); // Clear error message

            // Collect data
            String firstName = FirstNameTestField.getText();
            String middleName = MiddleNameTextField.getText(); // Can be optional, handle accordingly
            String lastName = LastNameTextField.getText();
            String emailAddress = EmailAddressTestField.getText();

            // Write data to CSV using CSVDatabase
            try {
                boolean isActive = true; // Assuming a new user is active
                String roles = "user"; // Assign default role, adjust as needed
                CSVDatabase.addUser(username, password, firstName, middleName, lastName, emailAddress, isActive, roles);

                // Load the next scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SELECTROLE02.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) Save.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage.setText("Error saving data. Please try again.");
            }
        }
    }
}


