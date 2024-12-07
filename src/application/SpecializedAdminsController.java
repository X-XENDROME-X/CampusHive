/**
 * <p> SpecializedAdminsController Class </p>
 *
 * <p> Description: This class manages the functionality for displaying admins with special
 * admin permissions within an application. It fetches data from a database, loads it into a 
 * GridPane for display, and includes error handling. The class also handles navigation actions 
 * through a home button based on the user's session. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import java.io.IOException;
import java.sql.*; // Import JDBC
import java.util.List;
import java.util.ArrayList;

public class SpecializedAdminsController {
    @FXML
    private Button homeButton;

    @FXML
    private GridPane adminGrid;

    @FXML
    public void initialize() {
        try {
            loadAdminData();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading admin data: " + e.getMessage());
        }
    }

    private void loadAdminData() {
        // Get admins with specialadmin permission set to true from the database
        List<Admin> admins = fetchAdminsWithSpecialAdmin();

        // Clear any existing data in the grid before adding new data
        adminGrid.getChildren().clear();

        // Set up headers
        Label headerName = createAdminLabel("Name");
        adminGrid.add(headerName, 0, 0);
        GridPane.setHalignment(headerName, HPos.CENTER);

        Label headerUsername = createAdminLabel("Username");
        adminGrid.add(headerUsername, 1, 0);
        GridPane.setHalignment(headerUsername, HPos.CENTER);

        // Add each admin to the grid
        if (admins.isEmpty()) {
            showError("No admins found with specialadmin access.");
        } else {
            for (int i = 0; i < admins.size(); i++) {
                Admin admin = admins.get(i);

                Label adminNameLabel = createAdminLabel(admin.getName());
                adminGrid.add(adminNameLabel, 0, i + 1);
                GridPane.setHalignment(adminNameLabel, HPos.CENTER);

                Label usernameLabel = createAdminLabel(admin.getUsername());
                adminGrid.add(usernameLabel, 1, i + 1);
                GridPane.setHalignment(usernameLabel, HPos.CENTER);
            }
        }
    }

    // Fetch admins who have specialadmin permission set to true from the database
    private List<Admin> fetchAdminsWithSpecialAdmin() {
        List<Admin> admins = new ArrayList<>();

        // Replace with your actual database credentials and query
        final String DB_URL = "jdbc:h2:./data/users/userdb";
        final String DB_USER = "sa";
        final String DB_PASSWORD = "";

        // SQL query to get all admins
        String query = "SELECT username, firstName FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String name = rs.getString("firstName");

                // Check if the admin has specialadmin permission
                if (H2Database.checkSpecialAdmin(username)) {
                    admins.add(new Admin(username, name));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error fetching data from database: " + e.getMessage());
        }

        return admins;
    }

    private Label createAdminLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Dubai Light", FontWeight.NORMAL, 20));
        label.setPrefWidth(400);
        label.setAlignment(Pos.CENTER); // Center text within label
        label.setStyle("-fx-padding: 5 0 5 0;"); // Add some vertical padding
        return label;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            UserSession session = UserSession.getInstance();
            Parent homePage;

            // Home redirection logic
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
                String role = session.getRole();
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

    // Admin class to represent admin data
    class Admin {
        private String username;
        private String name;

        public Admin(String username, String name) {
            this.username = username;
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }
    }
}
