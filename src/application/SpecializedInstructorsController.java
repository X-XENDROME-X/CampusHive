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

public class SpecializedInstructorsController {
    @FXML
    private Button homeButton;

    @FXML
    private GridPane instructorGrid;

    @FXML
    public void initialize() {
        try {
            loadInstructorData();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading instructor data: " + e.getMessage());
        }
    }

    private void loadInstructorData() {
        // Get instructors with special view rights from the database
        List<Instructor> instructors = fetchInstructorsWithSpecialView();

        // Clear any existing data in the grid before adding new data
        instructorGrid.getChildren().clear();

        // Set up headers
        Label headerName = createInstructorLabel("Name");
        instructorGrid.add(headerName, 0, 0);
        GridPane.setHalignment(headerName, HPos.CENTER);

        Label headerUsername = createInstructorLabel("Username");
        instructorGrid.add(headerUsername, 1, 0);
        GridPane.setHalignment(headerUsername, HPos.CENTER);

        // Add each instructor to the grid
        if (instructors.isEmpty()) {
            showError("No instructors found with special view access.");
        } else {
            for (int i = 0; i < instructors.size(); i++) {
                Instructor instructor = instructors.get(i);

                Label instructorNameLabel = createInstructorLabel(instructor.getName());
                instructorGrid.add(instructorNameLabel, 0, i + 1);
                GridPane.setHalignment(instructorNameLabel, HPos.CENTER);

                Label usernameLabel = createInstructorLabel(instructor.getUsername());
                instructorGrid.add(usernameLabel, 1, i + 1);
                GridPane.setHalignment(usernameLabel, HPos.CENTER);
            }
        }
    }

    // Fetch instructors who have special view permission set to true from the database
    private List<Instructor> fetchInstructorsWithSpecialView() {
        List<Instructor> instructors = new ArrayList<>();

        // Replace with your actual database credentials and query
        final String DB_URL = "jdbc:h2:./data/users/userdb";
        final String DB_USER = "sa";
        final String DB_PASSWORD = "";

        // SQL query to get all instructors
        String query = "SELECT username, firstName FROM users WHERE roles = 'Instructor'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String name = rs.getString("firstName");

                // Check if the instructor has specialview permission using H2Database.checkSpecialView
                if (H2Database.checkSpecialView(username)) {
                    instructors.add(new Instructor(username, name));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error fetching data from database: " + e.getMessage());
        }

        return instructors;
    }

    private Label createInstructorLabel(String text) {
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

    // Instructor class to represent instructor data
    class Instructor {
        private String username;
        private String name;

        public Instructor(String username, String name) {
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
