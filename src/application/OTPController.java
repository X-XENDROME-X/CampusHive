package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

public class OTPController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField otpField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField retypePasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button sendOtpButton;
    @FXML
    private Button verifyButton;

    private String generatedOtp;
    
    private long otpGeneratedTime; // To store OTP generation timestamp


    @FXML
    public void initialize() {
        emailField.requestFocus(); // Set focus to the email field
    }
    
    private void disableOtpButtonFor30Seconds() {
        sendOtpButton.setDisable(true); // Disable the button
        sendOtpButton.setStyle("-fx-background-color: lightgray; -fx-border-radius: 10; fx-background-radius: 10;"); // Change button color to light gray

        // Create a Timeline to re-enable the button after 30 seconds
        new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(30),
                        e -> {
                            sendOtpButton.setDisable(false); // Re-enable the button
                            sendOtpButton.setStyle(""); // Reset the button style
                        }))
                .play();
    }
    
    @FXML
    private void handleSendOtpAction() {
        String email = emailField.getText();

        if (email == null || email.isEmpty()) {
            showError("Please enter a valid email address.");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address.");
            return;
        }

        disableOtpButtonFor30Seconds();

        // Generate and send OTP
        generatedOtp = generateOtp();
        if (sendOtpEmail(email, generatedOtp)) {
            showError("OTP sent successfully to " + email);
        } else {
            showError("Failed to send OTP. Please try again.");
        }
    }
    @FXML
    private void handleVerifyButtonAction() {
        String enteredOtp = otpField.getText();
        
        // Check if the OTP entered matches the generated OTP
        if (enteredOtp.equals(generatedOtp)) {
            // If OTP is valid, proceed to change password
            changePassword();
        } else {
            showError("Invalid OTP. Please try again.");
        }
    }
    
    private boolean isValidEmail(String email) {
        // Regular expression for validating email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*(?:\\.[a-z]{2,})$";
        
        // Return true if the email matches the regex pattern, false otherwise
        return email != null && email.matches(emailRegex);
    }

    
    private boolean sendOtpEmail(String email, String otp) {
    	
        final String SENDER_EMAIL = "campushiveattempe@gmail.com";
        final String SENDER_PASSWORD = "zfupefihnyumvpyg"; // Use app password for security
        final String LOGO_URL = "https://i.ibb.co/VB2s22K/unnamed.png";
        // Set up the SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Change to your SMTP host
        props.put("mail.smtp.port", "587"); // Change to your SMTP port

        // Create a session with an authenticator
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD); // Use your email and app password
            }
        });
        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "Campus Hive"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your OTP Code for CampusHive");

            // HTML content
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<div style='text-align: center;'>" +
                    "<img src='" + LOGO_URL
                    + "' alt='Company Logo' style='width: 100px; height: 100px; border-radius: 50%; margin-bottom: 20px;'>"
                    +
                    "</div>" +
                    "<div style='padding: 20px; border: 1px solid #ccc; border-radius: 10px; max-width: 600px; margin: 0 auto;'>"
                    +
                    "<h2 style='color: #333;'>Dear User,</h2>" +
                    "<p style='color: #555;'>Your OTP code is: <strong>" + otp + "</strong></p>" +
                    "<p>Please use this code to verify your account.</p>" +
                    "<br>" +
                    "<p style='color: #555;'>If you did not request this OTP, please update your password immediately by logging into your account.</p>"
                    +
                    "<p style='color: #999; font-size: 12px;'>This is an automated message. Please do not reply.</p>" +
                    "</div>" +
                    "<div style='text-align: center; margin-top: 20px;'>" +
                    "<p style='color: #aaa; font-size: 10px;'>© 2024 Campus Hive. All rights reserved.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            // Set the email content as HTML
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Send the message
            Transport.send(message);

            return true; // Email sent successfully
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace(); // Print error if any occurs
            return false; // Failed to send email
        }
    }

    @FXML
    private String generateOtp() {
        Random random = new Random();
        // Generate a 6-digit OTP
        return String.format("%06d", random.nextInt(1000000));
    }
    
    @FXML
    private void changePassword() {
        String newPassword = newPasswordField.getText();
        String retypePassword = retypePasswordField.getText();

        if (newPassword == null || newPassword.isEmpty() || retypePassword == null || retypePassword.isEmpty()) {
            showError("Please fill in all password fields.");
            return;
        }

        if (!newPassword.equals(retypePassword)) {
            showError("Passwords do not match. Please try again.");
            return;
        }

        if (newPassword.length() < 6) { // Example length check
            showError("Password should be at least 6 characters long.");
            return;
        }

        // Logic to update the password in the database goes here
        showError("Password changed successfully!");
    }

 
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }


    @FXML
    void handleHomeButtonAction(ActionEvent event) {
        try {
            Parent adminHomepage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
            Scene adminHomepageScene = new Scene(adminHomepage);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(adminHomepageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

