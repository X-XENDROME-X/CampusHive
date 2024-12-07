/**
 * <p> ManageAdminRightsController Class </p>
 *
 * <p> Description: This class handles the management of admin rights, including granting and revoking
 * admin privileges for users. It includes methods for validating user inputs, interacting with the database 
 * to update user roles, and handling UI events related to admin access. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
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

public class ManageAdminRightsController {

    @FXML
    private ImageView Logo;

    @FXML
    private Label Heading;

    @FXML
    private TextField adminUsernameField;

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
        adminUsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText("");
        });
    }

    @FXML
    private void handleGrantAccess() {
        String targetUsername = adminUsernameField.getText().trim();

        if (targetUsername.isEmpty()) {
            showError("Please enter a username.");
            return;
        }

        try {
            UserSession session = UserSession.getInstance();
            String currentRole = session.getRole();

            // Ensure the current user has admin rights
            if (!"admin".equalsIgnoreCase(currentRole)) {
                showError("Access denied! Only admins can grant access.");
                return;
            }

            // Fetch the target user's role
            String targetRole = getRoleForUser(targetUsername);

            // Validate that the target user is not already an admin
            if ("admin".equals(targetRole)) {
                showError("The user is already an admin.");
                return;
            }
            if("Student".equals(targetRole)) {
            	showError("Students cannot be granted admin access.");
            	return;
            }

            // Proceed to grant admin access
            boolean success = grantAdminRights(targetUsername);
            if (success) {
                showSuccess("Admin access granted successfully!");
            } else {
                showError("Failed to grant admin rights. Please try again.");
            }

        } catch (Exception e) {
            showError("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Handle the "Revoke Access" button click.
     */
    @FXML
    private void handleRevokeAccess() {
        String targetUsername = adminUsernameField.getText().trim();

        if (targetUsername.isEmpty()) {
            showError("Please enter a username.");
            return;
        }

        try {
            UserSession session = UserSession.getInstance();
            String currentRole = session.getRole();

            // Ensure the current user has admin rights
            if (!"admin".equalsIgnoreCase(currentRole)) {
                showError("Access denied! Only admins can revoke access.");
                return;
            }

            // Fetch the target user's role
            String targetRole = getRoleForUser(targetUsername);

            // Validate that the user is an admin and has the rights to be revoked
            if ("student".equals(targetRole)) {
                showError("The user is not an admin.");
                return;
            }

            // Proceed to revoke admin access
            boolean success = revokeAdminRights(targetUsername);
            if (success) {
                showSuccess("Admin access revoked successfully!");
            } else {
                showError("Failed to revoke admin rights. Please try again.");
            }

        } catch (Exception e) {
            showError("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Grant admin rights to the specified user.
     * 
     * @param username The username of the user to grant admin rights to.
     */
    private boolean grantAdminRights(String username) {
        String updateQuery = "UPDATE users SET specialAdmin = TRUE WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Revoke admin rights from the specified user.
     * 
     * @param username The username of the user to revoke admin rights from.
     */
    private boolean revokeAdminRights(String username) {
        String updateQuery = "UPDATE users SET specialAdmin = FALSE WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetch the role of the specified user from the database.
     * 
     * @param username The username of the user.
     * @return The role of the user (e.g., "admin" or "student").
     */
    private String getRoleForUser(String username) {
        String selectQuery = "SELECT role FROM users WHERE username = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("role");
            }
            return "unknown";  // Default for unrecognized users
        } catch (SQLException e) {
            e.printStackTrace();
            return "unknown";
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
