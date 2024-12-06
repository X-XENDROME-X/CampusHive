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

public class SpecializedStudentsController {

    @FXML
    private Button homeButton;

    @FXML
    private GridPane studentGrid;

    @FXML
    public void initialize() {
        try {
            loadStudentData();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading student data: " + e.getMessage());
        }
    }

    // Load the student data from the database where specialview is true
    private void loadStudentData() {
        // Get students with special view rights from the database
        List<Student> students = fetchStudentsWithSpecialView();

        // Clear any existing data in the grid before adding new data
        studentGrid.getChildren().clear();

        // Set up headers
        Label headerName = createStudentLabel("Name");
        studentGrid.add(headerName, 0, 0);
        GridPane.setHalignment(headerName, HPos.CENTER);

        Label headerUsername = createStudentLabel("Username");
        studentGrid.add(headerUsername, 1, 0);
        GridPane.setHalignment(headerUsername, HPos.CENTER);

        // Add each student to the grid
        if (students.isEmpty()) {
            showError("No students found with special view access.");
        } else {
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);

                Label studentNameLabel = createStudentLabel(student.getName());
                studentGrid.add(studentNameLabel, 0, i + 1);
                GridPane.setHalignment(studentNameLabel, HPos.CENTER);

                Label usernameLabel = createStudentLabel(student.getUsername());
                studentGrid.add(usernameLabel, 1, i + 1);
                GridPane.setHalignment(usernameLabel, HPos.CENTER);
            }
        }
    }

    // Fetch students who have special view permission set to true from the database
    private List<Student> fetchStudentsWithSpecialView() {
        List<Student> students = new ArrayList<>();
       // UserSession session = UserSession.getInstance();
        // Replace with your actual database credentials and query
        final String DB_URL = "jdbc:h2:./data/users/userdb";
        final String DB_USER = "sa";
        final String DB_PASSWORD = "";

        // SQL query to get all users
        String query = "SELECT username, firstName FROM users WHERE LOWER(roles) LIKE '%student%'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String name = rs.getString("firstName");

                // Check if the user has specialview permission using H2Database.checkSpecialView
                if (H2Database.checkSpecialView(username)) {
                    students.add(new Student(username, name));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error fetching data from database: " + e.getMessage());
        }

        return students;
    }

    private Label createStudentLabel(String text) {
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

    // Student class to represent student data
    class Student {
        private String username;
        private String name;

        public Student(String username, String name) {
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
