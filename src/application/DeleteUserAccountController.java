/**
 * <p> DeleteUserAccountController Class </p>
 *
 * <p> Description: This class provides the functionality to delete user accounts within the
 * application. It handles user input, validates deletion requests, and interacts with the database
 * to remove the specified user account. The class also includes error handling and feedback
 * mechanisms for successful or failed deletion attempts. </p>
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
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteUserAccountController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button deleteButton;

    @FXML
    private CheckBox confirmationCheckBox;

    @FXML
    private Label statusLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button homeButton;

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {

        String username = usernameField.getText();

        if (username.isEmpty()) {

            statusLabel.setText("Username cannot be empty.");
        } else if (!confirmationCheckBox.isSelected()) {

            statusLabel.setText("Please confirm that you want to delete the account.");
        } else {

            boolean success = deleteUserAccount(username);

            if (success) {
                statusLabel.setText("User account deleted successfully.");
            } else {
                statusLabel.setText("Failed to delete user account. User may not exist.");
            }
        }
    }

    private boolean deleteUserAccount(String username) {
        try {

            boolean isDeleted = H2Database.deleteUserByUsername(username);

            if (isDeleted) {
                statusLabel.setText("User account deleted successfully.");
            } else {
                statusLabel.setText("User not found. Account deletion failed.");
            }

            return isDeleted;
        } catch (SQLException e) {

            e.printStackTrace();

            statusLabel.setText("An error occurred while trying to delete the account.");

            return false;
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {

        statusLabel.setText("Account deletion canceled.");
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