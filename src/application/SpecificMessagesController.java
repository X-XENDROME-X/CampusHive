/**
 * <p> SpecificMessagesController Class </p>
 *
 * <p> Description: This class handles the functionality of displaying specific messages related
 * to student queries. It loads the messages from a database and displays them in a grid. The class
 * also provides error handling for database connectivity issues, as well as functionality for 
 * redirecting users to their home page based on their session role.</p>
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class SpecificMessagesController {
    @FXML
    private Button homeButton;

    @FXML
    private GridPane messageGrid;

    private static final int MAX_SUBJECT_LENGTH = 50;
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        try {
            // Ensure database connection is established
            if (dbConnection == null || dbConnection.isClosed()) {
                initializeDatabaseConnection();
            }

            // Load messages into the grid
            loadMessages();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error while loading messages: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error while initializing: " + e.getMessage());
        }
    }

    private static Connection dbConnection;

    private void initializeDatabaseConnection() {
        try {
            // Initialize the database connection only once
            if (dbConnection == null || dbConnection.isClosed()) {
                dbConnection = DriverManager.getConnection("jdbc:h2:./data/messages/messagedb", "sa", "");
                System.out.println("Database connection established.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error initializing database connection: " + e.getMessage());
        }
    }
    
    private void loadMessages() {
        String query = "SELECT studentID, date, subject, message FROM messages WHERE messageType = 'Specific Query'";

        // Ensure the database connection is initialized
        if (dbConnection == null) {
            initializeDatabaseConnection();
        }

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int rowIndex = 1; // Start from the second row (row 0 is for headers)
            while (rs.next()) {
                String studentID = rs.getString("studentID");
                String date = rs.getTimestamp("date").toLocalDateTime().format(DATE_FORMATTER);
                String subject = rs.getString("subject");
                String message = rs.getString("message");

                // Add each piece of data as a label in the respective column
                messageGrid.add(createMessageLabel(studentID, 0), 0, rowIndex);
                messageGrid.add(createMessageLabel(date, 1), 1, rowIndex);
                messageGrid.add(createMessageLabel(subject, 2), 2, rowIndex);
                messageGrid.add(createMessageLabel(message, 3), 3, rowIndex);

                rowIndex++;
            }
        } catch (SQLException e) {
            showError("Error loading messages: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Label createMessageLabel(String text, int columnIndex) {
        Label label = new Label(formatText(text, columnIndex));
        label.setFont(Font.font("Dubai Light", FontWeight.NORMAL, 20));
        label.setAlignment(Pos.CENTER); // Center text within label
        
        // Set width based on column
        switch (columnIndex) {
            case 0: // Student ID
                label.setPrefWidth(200);
                break;
            case 1: // Date
                label.setPrefWidth(200);
                break;
            case 2: // Subject
                label.setPrefWidth(250);
                break;
            case 3: // Message
                label.setPrefWidth(320);
                break;
        }
        
        // Center alignment and wrapping
        label.setWrapText(true);
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setStyle("-fx-padding: 5 0 5 0; -fx-alignment: CENTER;");
        
        return label;
    }

    private String formatText(String text, int columnIndex) {
        if ((columnIndex == 2 || columnIndex == 3) && text.length() > MAX_SUBJECT_LENGTH) {
            return text.substring(0, MAX_SUBJECT_LENGTH - 3) + "...";
        }
        return text;
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
    }}
