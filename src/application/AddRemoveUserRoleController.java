/**
 * <p> AddRemoveUserRoleController Class </p>
 *
 * <p> Description: This class handles the functionality for adding and removing user roles within
 * an application. It includes methods for role management, validation, and error handling related
 * to user role assignments. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth phase of this JavaFX project was completed
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
import java.sql.SQLException;
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

        RoleSelectionBox.getItems().addAll("admin", "student", "instructor");

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

            List<String[]> users = H2Database.getUsers();
            boolean roleExists = false;
            String existingRoles = null;

            for (String[] user : users) {

                if (user[0].equals(username)) {

                    existingRoles = user[7];

                    if (existingRoles != null && existingRoles.contains(selectedRole)) {
                        roleExists = true;
                    }
                    break;
                }
            }

            if (roleExists) {
                ErrorMessageLabel.setText("Role already exists.");
            } else {

                String updatedRoles = existingRoles != null ? existingRoles + "," + selectedRole : selectedRole;

                H2Database.updateUserRoles(username, updatedRoles);
                ErrorMessageLabel.setText("Role added successfully.");
            }
        } catch (SQLException e) {

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

            List<String[]> users = H2Database.getUsers();
            boolean roleExists = false;
            String existingRoles = null;

            for (String[] user : users) {

                if (user[0].equals(username)) {

                    existingRoles = user[7];

                    if (existingRoles != null && existingRoles.contains(selectedRole)) {
                        roleExists = true;
                    }
                    break;
                }
            }

            if (!roleExists) {
                ErrorMessageLabel.setText("Role does not exist.");
            } else {

                String updatedRoles = existingRoles.replace("," + selectedRole, "").replace(selectedRole + ",", "").replace(selectedRole, "");

                H2Database.updateUserRoles(username, updatedRoles);
                ErrorMessageLabel.setText("Role removed successfully.");
            }
        } catch (SQLException e) {

            ErrorMessageLabel.setText("An error occurred while accessing the user database.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            UserSession session = UserSession.getInstance();
            Parent homePage;
            

        	 if (session.hasPreviousPage()) {
                 String previousPage = session.getPreviousPage();
                 homePage = FXMLLoader.load(getClass().getResource(previousPage));

                 System.out.println("Redirecting to: " + previousPage);
                 
                 Scene homeScene = new Scene(homePage);
                 Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 currentStage.setScene(homeScene);
                 currentStage.show();
             } else {
                 System.out.println("No previous page found.");
                 String role = session.getRole(); // Retrieve role from session if available
                 if (role == null) {
                     System.out.println("Role not set in session. Defaulting to SELECTROLE02.fxml.");
                     homePage = FXMLLoader.load(getClass().getResource("SELECTROLE02.fxml"));
                 } else if ("admin".equals(role)) {
                     homePage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
                 } else if ("instructor".equals(role)) {
                     homePage = FXMLLoader.load(getClass().getResource("Instructor_Homepage.fxml"));
                 } else if ("student".equals(role)) {
                	 homePage = FXMLLoader.load(getClass().getResource("STUDENTHOMEPAGE.fxml"));
                 } else {
                     throw new IOException("User role not recognized");
                 }

                 Scene homeScene = new Scene(homePage);
                 Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 currentStage.setScene(homeScene);
                 currentStage.show();
                 
             }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
