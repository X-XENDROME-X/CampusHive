package application;

import java.security.SecureRandom;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Invite_User_PageController {

    @FXML
    private TextArea emailAddress;  // TextArea for email input

    @FXML
    private Button sendInvite;  // Button to send invite

    @FXML
    private Label Message;  // Label to display success message

    @FXML
    private CheckBox instructor;  // CheckBox for Instructor role

    @FXML
    private CheckBox admin;  // CheckBox for Admin role

    @FXML
    private CheckBox Student;  // CheckBox for Student role

    @FXML
    private Button home;  // Home button

    // Method to handle the Send Invite button action
    @FXML
    void handleSendInvite(ActionEvent event) {
        String email = emailAddress.getText();
        
        if (email.isEmpty()) {
            Message.setText("Please provide an email address.");
            Message.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        // Determine roles based on the checkboxes
        StringBuilder roles = new StringBuilder();
        if (instructor.isSelected()) roles.append("Instructor ");
        if (admin.isSelected()) roles.append("Admin ");
        if (Student.isSelected()) roles.append("Student ");
        
        if (roles.length() == 0) {
            Message.setText("Please select at least one role.");
            Message.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        // Generate one-time invitation code
        String invitationCode = generateInvitationCode(8); // Length 8

        try {
            // Store the invitation code and roles in the CSV
            CSVDatabase.addInvitationCode(email, invitationCode, roles.toString().trim());

            // Store the user and roles in the user CSV
            CSVDatabase.addUser(email, "", "", "", "", email, false, roles.toString().trim());

            // Send the email
            boolean emailSent = sendInviteEmail(email, invitationCode);

            if (emailSent) {
                Message.setText("Invite sent to: " + email);
                Message.setTextFill(javafx.scene.paint.Color.valueOf("#256f25"));
            } else {
                Message.setText("Failed to send invite.");
                Message.setTextFill(javafx.scene.paint.Color.RED);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Message.setText("Error sending invite.");
            Message.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    
    private boolean sendInviteEmail(String email, String invitationCode) {
        final String SENDER_EMAIL = "campushiveattempe@gmail.com";
        final String SENDER_PASSWORD = "zfupefihnyumvpyg"; // Use app password for security
        final String LOGO_URL = "https://i.ibb.co/VB2s22K/unnamed.png";
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "Campus Hive"));
            message.setRecipients(RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your Campus Hive Invitation Code");

            // Updated HTML content
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<div style='text-align: center;'>" +
                    "<img src='" + LOGO_URL
                    + "' alt='Company Logo' style='width: 100px; height: 100px; border-radius: 50%; margin-bottom: 20px;'>"
                    + "</div>" +
                    "<div style='padding: 20px; border: 1px solid #ccc; border-radius: 10px; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #333;'>Welcome to Campus Hive!</h2>" +
                    "<p style='color: #555;'>You have been invited to join Campus Hive. Please use the invitation code below to set up your account.</p>" +
                    "<p style='font-size: 24px; font-weight: bold; color: #256f25;'>Invitation Code: <strong>" + invitationCode + "</strong></p>" +
                    "<p>This code is for one-time use only.</p>" +
                    "<br>" +
                    "<p style='color: #555;'>If you did not request this invitation, please contact our support team.</p>" +
                    "<p style='color: #999; font-size: 12px;'>This is an automated message. Please do not reply.</p>" +
                    "</div>" +
                    "<div style='text-align: center; margin-top: 20px;'>" +
                    "<p style='color: #aaa; font-size: 10px;'>© 2024 Campus Hive. All rights reserved.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method to handle the Home button action
    @FXML
    void handleHomeButtonAction(ActionEvent event) {
        try {
            // Load the admin homepage FXML file
            Parent adminHomepage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));

            // Set up the new scene
            Scene adminHomepageScene = new Scene(adminHomepage);

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            currentStage.setScene(adminHomepageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // You might want to show an error message to the user
        }
    }
    
    public static String generateInvitationCode(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
