package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class Invite_User_PageController {

    @FXML
    private TextField emailAddress;

    @FXML
    private ChoiceBox<String> roles;

    @FXML
    private Button sendInvite;

    @FXML
    private Label successMsg;
    
    @FXML
    public void initialize() {
        // Initialize the roles choice box with some example roles
        roles.getItems().addAll("Admin", "Editor", "Viewer");
        successMsg.setVisible(false); // Hide the success message initially
    }
    
    @FXML
    void onSendInviteClick(ActionEvent event) {
        String email = emailAddress.getText();
        String role = (String) roles.getValue();

        // Simple validation
        if (email.isEmpty() || role == null) {
            successMsg.setText("Please enter an email and select a role.");
            successMsg.setStyle("-fx-text-fill: red;");
            successMsg.setVisible(true);
            return;
        }

        // Simulate sending an invite (you can replace this with real logic later)
        sendInvite(email, role);
        
        // Display success message
        successMsg.setText("Invite sent successfully!");
        successMsg.setStyle("-fx-text-fill: green;");
        successMsg.setVisible(true);
        
        // Optionally clear the fields
        emailAddress.clear();
        roles.setValue(null);
    }

    // Simulate sending the invite (you can replace this with real backend logic)
    private void sendInvite(String email, String role) {
        System.out.println("Invite sent to " + email + " with role: " + role);
    }

}