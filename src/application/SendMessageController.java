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
import java.util.Optional;

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
        setupComboBoxes();
        setupListeners();
        setupPromptText();
    }

    private void setupComboBoxes() {
        ObservableList<String> messageTypes = FXCollections.observableArrayList(
            "Select Message Type",
            "Generic Message",
            "Specific Query",
            "Technical Support",
            "Feedback"
        );
        messageTypeComboBox.setItems(messageTypes);
        messageTypeComboBox.setValue("Select Message Type");

        ObservableList<String> recipients = FXCollections.observableArrayList(
            "Select Recipient",
            "All Instructors",
            "All Admins",
            "Specific Instructor",
            "Specific Admin"
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
        // TODO: Implement actual message sending logic here
        try {
            Thread.sleep(1000); // Simulate sending delay
        } catch (InterruptedException e) {
            throw new RuntimeException("Message sending was interrupted");
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
        if (isAnyFieldFilled() && !isMessageSent) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Navigation");
            alert.setHeaderText("You have unsaved changes");
            alert.setContentText("Do you want to leave without sending your message?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                return;
            }
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("STUDENTHOMEPAGE.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Error navigating to student home page: " + e.getMessage());
        }
    }
}