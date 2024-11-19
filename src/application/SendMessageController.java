package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SendMessageController Class
 * 
 * Description: This class handles the functionality for sending messages
 * from students to instructors or admins.
 * 
 * Copyright: Campus Hive © 2024
 * 
 * @author Th01
 * @version 1.00 2024-11-15
 */
public class SendMessageController {
    @FXML
    private Button homeButton;
    
    @FXML
    private ComboBox<String> messageTypeComboBox;
    
    @FXML
    private ComboBox<String> recipientComboBox;
    
    @FXML
    private TextField subjectField;
    
    @FXML
    private TextArea messageArea;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private Button sendButton;

    private boolean isMessageSent = false;

    @FXML
    public void initialize() {
    	initializeMessageDatabase();
        setupComboBoxes();
        setupListeners();
        setupPromptText();
    }
    
    private static Connection dbConnection;

    private void initializeMessageDatabase() {
        try {
            // Establish connection to H2 database
            dbConnection = DriverManager.getConnection("jdbc:h2:./data/messages/messagedb", "sa", "");
            Statement stmt = dbConnection.createStatement();

            // Create the messages table if it doesn't exist
            String createMessagesTableSQL = "CREATE TABLE IF NOT EXISTS messages ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "studentID VARCHAR(50) NOT NULL, "
                    + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "subject VARCHAR(255), "
                    + "message TEXT NOT NULL, "
                    + "messageType VARCHAR(50) NOT NULL, "
                    + "recipientType VARCHAR(50) NOT NULL)";
            stmt.execute(createMessagesTableSQL);

            System.out.println("Message database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize the message database.");
        }
    }
    
    public static Connection getDbConnection() {
        return dbConnection;
    }
    
    private void setupComboBoxes() {
        ObservableList<String> messageTypes = FXCollections.observableArrayList(
            "Select Message Type",
            "Generic Message",
            "Specific Query"
            //"Technical Support",
            //"Feedback"
        );
        messageTypeComboBox.setItems(messageTypes);
        messageTypeComboBox.setValue("Select Message Type");

        ObservableList<String> recipients = FXCollections.observableArrayList(
            "Select Recipient",
            "All Instructors",
            "All Admins"
            //"Specific Instructor",
            //"Specific Admin"
        );
        recipientComboBox.setItems(recipients);
        recipientComboBox.setValue("Select Recipient");
    }

    private void setupListeners() {
        messageTypeComboBox.setOnAction(event -> handleMessageTypeChange());
        recipientComboBox.setOnAction(event -> handleRecipientChange());
        
        subjectField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) {
                subjectField.setText(oldValue);
                showError("Subject cannot exceed 100 characters");
            }
        });

        messageArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1000) {
                messageArea.setText(oldValue);
                showError("Message cannot exceed 1000 characters");
            }
        });
    }

    private void setupPromptText() {
        subjectField.setPromptText("Enter a brief subject (max 100 characters)");
        messageArea.setPromptText("Type your message here (max 1000 characters)");
    }

    private void handleMessageTypeChange() {
        String selectedType = messageTypeComboBox.getValue();
        updatePromptText(selectedType);
    }

    private void updatePromptText(String messageType) {
        switch (messageType) {
            case "Generic Message":
                messageArea.setPromptText("Enter your general message or inquiry...");
                break;
            case "Specific Query":
                messageArea.setPromptText("Please provide specific details about your query...");
                break;
            case "Technical Support":
                messageArea.setPromptText("Describe the technical issue you're experiencing...");
                break;
            case "Feedback":
                messageArea.setPromptText("Share your feedback or suggestions...");
                break;
            default:
                messageArea.setPromptText("Type your message here...");
        }
    }

    private void handleRecipientChange() {
        if (recipientComboBox.getValue().startsWith("Specific")) {
            messageArea.setPromptText(messageArea.getPromptText() + 
                "\nPlease mention the specific recipient's name in your message.");
        }
    }

    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        if (isAnyFieldFilled()) {
            Alert confirmClear = new Alert(Alert.AlertType.CONFIRMATION);
            confirmClear.setTitle("Clear Form");
            confirmClear.setHeaderText("Are you sure you want to clear all fields?");
            confirmClear.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmClear.showAndWait();
            if (result.get() == ButtonType.OK) {
                clearFields();
                showInfo("All fields have been cleared");
            }
        }
    }

    private boolean isAnyFieldFilled() {
        return !messageTypeComboBox.getValue().equals("Select Message Type") ||
               !recipientComboBox.getValue().equals("Select Recipient") ||
               !subjectField.getText().trim().isEmpty() ||
               !messageArea.getText().trim().isEmpty();
    }

    @FXML
    private void handleSendButtonAction(ActionEvent event) {
        if (validateFields()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Send Message");
            alert.setHeaderText("Confirm Message Send");
            alert.setContentText("Are you sure you want to send this message?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    sendMessage();
                    isMessageSent = true;
                    showSuccess("Message sent successfully!");
                    clearFields();
                } catch (Exception e) {
                    showError("Failed to send message: " + e.getMessage());
                }
            }
        }
    }

    private void sendMessage() {
        // Retrieve input values from the JavaFX fields
    	UserSession session = UserSession.getInstance();
        //String userRole = session.getRole();
        String studentID = session.getUsername(); // Replace this with dynamic student ID if available
        String messageType = messageTypeComboBox.getValue();
        String recipientType = recipientComboBox.getValue();
        String subject = subjectField.getText().trim();
        String message = messageArea.getText().trim();
        
        LocalDateTime now = LocalDateTime.now();
        String formattedTimestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // SQL query to insert message into the database
        String insertSQL = "INSERT INTO messages (studentID, date,  subject, message, messageType, recipientType) "
                         + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            // Prepare the SQL statement
            java.sql.PreparedStatement preparedStatement = dbConnection.prepareStatement(insertSQL);

            // Bind parameters to the SQL query
            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, formattedTimestamp); 
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, message);
            preparedStatement.setString(5, messageType);
            preparedStatement.setString(6, recipientType);

            viewDatabaseContents();
            
            // Execute the statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Message successfully saved to the database.");
            } else {
                throw new RuntimeException("No rows affected. Message not saved.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving message to database: " + e.getMessage());
        }
    }
    
    private void viewDatabaseContents() {
        String query = "SELECT * FROM messages";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Student ID: " + rs.getString("studentID"));
                System.out.println("Date: " + rs.getTimestamp("date"));
                System.out.println("Subject: " + rs.getString("subject"));
                System.out.println("Message: " + rs.getString("message"));
                System.out.println("Message Type: " + rs.getString("messageType"));
                System.out.println("Recipient Type: " + rs.getString("recipientType"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (messageTypeComboBox.getValue().equals("Select Message Type")) {
            errorMessage.append("- Please select a message type\n");
        }
        if (recipientComboBox.getValue().equals("Select Recipient")) {
            errorMessage.append("- Please select a recipient\n");
        }
        if (subjectField.getText().trim().isEmpty()) {
            errorMessage.append("- Subject is required\n");
        }
        if (messageArea.getText().trim().isEmpty()) {
            errorMessage.append("- Message content is required\n");
        }
        if (messageArea.getText().trim().length() < 10) {
            errorMessage.append("- Message must be at least 10 characters long\n");
        }

        if (errorMessage.length() > 0) {
            showError("Please correct the following errors:\n" + errorMessage.toString());
            return false;
        }
        return true;
    }

    private void clearFields() {
        messageTypeComboBox.setValue("Select Message Type");
        recipientComboBox.setValue("Select Recipient");
        subjectField.clear();
        messageArea.clear();
        isMessageSent = false;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
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
    }
}