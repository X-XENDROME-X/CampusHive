/**
 * <p> ManageViewingRightsController Class </p>
 *
 * <p> Description: This class handles the functionality for managing the viewing rights of users within
 * an application. It includes methods for granting and revoking access to special article groups, along with
 * necessary error handling and user interface updates. The class interacts with the database to update 
 * user access permissions based on the student's ID and selected group. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project (Phase 4)
 */


package application;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class ManageViewingRightsController {

    @FXML
    private ImageView Logo;

    @FXML
    private Label Heading;
    
    @FXML
    private ComboBox<String> articleGroupComboBox;

    @FXML
    private TextField studentIdField;

    @FXML
    private Button homeButton;

    @FXML
    private Button grantAccessButton;

    @FXML
    private Button revokeAccessButton;

    @FXML
    private Label errorMessageLabel;

    private static final String DB_URL = "jdbc:h2:./data/users/userdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    @FXML
    public void initialize() {
        // Initialize the dropdown with "Special Access Group"
        articleGroupComboBox.getItems().clear();
        articleGroupComboBox.getItems().add("Special Access Group");
        articleGroupComboBox.getSelectionModel().selectFirst(); // Select it by default
    }

    @FXML
    public void handleGrantAccess() {
        String studentId = studentIdField.getText();
        String selectedGroup = articleGroupComboBox.getValue();

        if (studentId.isEmpty() || selectedGroup == null) {
            errorMessageLabel.setText("Please provide a valid Student ID and select an article group.");
            return;
        }

        // Grant access logic
        boolean success = grantAccessToStudent(studentId, selectedGroup);
        if (success) {
            errorMessageLabel.setText("Access granted successfully.");
        } else {
            errorMessageLabel.setText("Failed to grant access. Please try again.");
        }
    }

    @FXML
    public void handleRevokeAccess() {
        String studentId = studentIdField.getText();
        String selectedGroup = articleGroupComboBox.getValue();

        if (studentId.isEmpty() || selectedGroup == null) {
            errorMessageLabel.setText("Please provide a valid Student ID and select an article group.");
            return;
        }

        // Revoke access logic
        boolean success = revokeAccessFromStudent(studentId, selectedGroup);
        if (success) {
            errorMessageLabel.setText("Access revoked successfully.");
        } else {
            errorMessageLabel.setText("Failed to revoke access. Please try again.");
        }
    }

    private boolean grantAccessToStudent(String studentId, String group) {
        String updateQuery = "UPDATE users SET specialView = TRUE, specialAdmin = FALSE WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, studentId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Granted special view access to " + studentId + " for group " + group);
                return true;
            } else {
                System.out.println("Failed to grant special view access: User not found or no changes made.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean revokeAccessFromStudent(String studentId, String group) {
        String updateQuery = "UPDATE users SET specialView = FALSE WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, studentId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Revoked special view access from " + studentId + " for group " + group);
                return true;
            } else {
                System.out.println("Failed to revoke special view access: User not found or no changes made.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    private void showError(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setStyle("-fx-text-fill: #ff0606;");
    }

    private void showSuccess(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setStyle("-fx-text-fill: #47ae6a;");
    }



    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            UserSession session = UserSession.getInstance();
            Parent homePage;

            if (session.hasPreviousPage()) {
                String previousPage = session.getPreviousPage();
                homePage = FXMLLoader.load(getClass().getResource(previousPage));
                Scene homeScene = new Scene(homePage);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(homeScene);
                currentStage.show();
            } else {
                String role = session.getRole();
                if (role == null) {
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
