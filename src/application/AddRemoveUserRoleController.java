/*******
 * <p> AddRemoveUserRoleController Class </p>
 * 
 * <p> Description: The AddRemoveUserRoleController class manages the process of adding and removing roles for users in the Campus Hive application. It allows administrators to assign, revoke, or modify user roles by interacting with the backend data (CSV file or database), ensuring that role changes are updated in real-time within the system.</p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */


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
import java.util.List;

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
        String username = UsernameTextField.getText().trim();
        String selectedRole = RoleSelectionBox.getValue();
        
        if (username.isEmpty() || selectedRole == null) {
            ErrorMessageLabel.setText("Username and Role Selection cannot be empty.");
            return;
        }
        
        try {
            List<String[]> users = CSVDatabase.getUsers();
            boolean roleExists = false;
            String existingRoles = null;

            // Check if the user exists and get their existing roles
            for (String[] user : users) {
                if (user[0].equals(username)) { // Check username
                    existingRoles = user[7]; // Assuming roles are at index 7
                    if (existingRoles.contains(selectedRole)) {
                        roleExists = true; // Role already exists
                    }
                    break;
                }
            }

            if (roleExists) {
                ErrorMessageLabel.setText("Role already exists.");
            } else {
                // If user exists and role does not, add the role
                String updatedRoles = existingRoles != null ? existingRoles + "," + selectedRole : selectedRole;
                CSVDatabase.updateUserRoles(username, updatedRoles); // Method to update roles in CSV
                ErrorMessageLabel.setText(""); 
            }
        } catch (IOException e) {
            ErrorMessageLabel.setText("An error occurred while accessing the user database.");
            e.printStackTrace();
        }
    }

    @FXML
    void RemoveRole() {
        String username = UsernameTextField.getText().trim();
        String selectedRole = RoleSelectionBox.getValue();
        
        if (username.isEmpty() || selectedRole == null) {
            ErrorMessageLabel.setText("Username and Role Selection cannot be empty.");
            return;
        }

        try {
            List<String[]> users = CSVDatabase.getUsers();
            boolean roleExists = false;
            String existingRoles = null;

            // Check if the user exists and get their existing roles
            for (String[] user : users) {
                if (user[0].equals(username)) { // Check username
                    existingRoles = user[7]; // Assuming roles are at index 7
                    if (existingRoles.contains(selectedRole)) {
                        roleExists = true; // Role exists
                    }
                    break;
                }
            }

            if (!roleExists) {
                ErrorMessageLabel.setText("Role does not exist.");
            } else {
                // If user exists and role exists, remove the role
                String updatedRoles = existingRoles.replace("," + selectedRole, "").replace(selectedRole + ",", "").replace(selectedRole, "");
                CSVDatabase.updateUserRoles(username, updatedRoles); // Method to update roles in CSV
                ErrorMessageLabel.setText(""); 
            }
        } catch (IOException e) {
            ErrorMessageLabel.setText("An error occurred while accessing the user database.");
            e.printStackTrace();
        }
    }

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
