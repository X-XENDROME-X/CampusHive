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

    @FXML
    private void handleSendOtpAction() {
        String email = emailField.getText();

        // Validate email
        if (!isValidEmail(email)) {
            showError("Invalid email format.");
            return;
        }

        // Generate OTP
        generatedOtp = generateOtp();
        sendOtpEmail(email, generatedOtp);
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

    
    private void sendOtpEmail(String email, String otp) {
        // Set up the SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Change to your SMTP host
        props.put("mail.smtp.port", "587"); // Change to your SMTP port

        // Create a session with an authenticator
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("campushiveattempe@gmail.com", "zfupefihnyumvpyg"); // Use your email and app password
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("campushiveattempe@gmail.com")); // Change to your email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp);

            // Send the message
            Transport.send(message);
            showError("OTP sent successfully to " + email);
        } catch (MessagingException e) {
            showError("Error sending OTP: " + e.getMessage());
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

        // Placeholder logic for changing password
        if (newPassword.equals(retypePassword)) {
            // Logic to update password in database goes here
            showError("Password changed successfully!");
        } else {
            showError("Passwords do not match. Please try again.");
        }
    }

 
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }


    private boolean isValidEmail(String email) {
        // Regular expression for validating email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*(?:\\.[a-z]{2,})$";
        
        // Return true if the email matches the regex pattern, false otherwise
        return email != null && email.matches(emailRegex);
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

