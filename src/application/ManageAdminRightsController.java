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
            if (!"admin".equals(currentRole)) {
                showError("Access denied! Only admins can grant access.");
                return;
            }

            // Fetch the target user's role
            String targetRole = getRoleForUser(targetUsername);

            // Validate that the target user is not a student
            if ("student".equals(targetRole)) {
                showError("Cannot grant admin rights to a student!");
                return;
            }

            // Proceed to grant admin access
            grantAdminRights(targetUsername);
            showSuccess("Admin access granted successfully!");

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
            if (!"admin".equals(currentRole)) {
                showError("Access denied! Only admins can revoke access.");
                return;
            }

            // Fetch the target user's role
            String targetRole = getRoleForUser(targetUsername);

            // Proceed to revoke admin access
            revokeAdminRights(targetUsername);
            showSuccess("Admin access revoked successfully!");

        } catch (Exception e) {
            showError("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Grant admin rights to the specified user.
     * 
     * @param username The username of the user to grant admin rights to.
     */
    private void grantAdminRights(String username) {
        // TODO: Implement database logic to update the user's role to 'admin'
        System.out.println("Granting admin rights to: " + username);
    }

    /**
     * Revoke admin rights from the specified user.
     * 
     * @param username The username of the user to revoke admin rights from.
     */
    private void revokeAdminRights(String username) {
        // TODO: Implement database logic to update the user's role (e.g., revert to 'student')
        System.out.println("Revoking admin rights from: " + username);
    }

    /**
     * Fetch the role of the specified user from the database.
     * 
     * @param username The username of the user.
     * @return The role of the user (e.g., "admin" or "student").
     */
    private String getRoleForUser(String username) {
        // TODO: Replace this mock implementation with actual database query logic
        // Example: Query the database to get the user's role
        if ("pshriv".equals(username)) {
            return "student"; // Mocked role for testing
        } else if ("adminUser".equals(username)) {
            return "admin"; // Mocked admin role
        }
        return "unknown"; // Default for unrecognized users
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

