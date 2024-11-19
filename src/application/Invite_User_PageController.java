/**
 * <p> Invite_User_PageController Class </p>
 *
 * <p> Description: This class handles the user interface and functionality for inviting users to the
 * application. It includes methods for gathering email addresses, generating invitation codes, 
 * sending emails, and navigating to the home page. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 2.00 2024-10-29 Updated documentation and comments for clarity
 */


package application;

import java.security.SecureRandom;
import java.sql.SQLException;
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
    private TextArea emailAddress;

    @FXML
    private Button sendInvite;

    @FXML
    private Label Message;

    @FXML
    private CheckBox instructor;

    @FXML
    private CheckBox admin;

    @FXML
    private CheckBox Student;

    @FXML
    private Button home;

    @FXML
    void handleSendInvite(ActionEvent event) {

        String email = emailAddress.getText();

        if (email.isEmpty()) {

            Message.setText("Please provide an email address.");

            Message.setTextFill(javafx.scene.paint.Color.RED);
            return; 
        }

        StringBuilder roles = new StringBuilder();

        if (instructor.isSelected()) roles.append("Instructor ");

        if (admin.isSelected()) roles.append("Admin ");

        if (Student.isSelected()) roles.append("Student ");

        if (roles.length() == 0) {

            Message.setText("Please select at least one role.");

            Message.setTextFill(javafx.scene.paint.Color.RED);
            return; 
        }

        String invitationCode = generateInvitationCode(8); 

        try {

            H2Database.addInvitationCode(email, invitationCode, roles.toString().trim());

            boolean emailSent = sendInviteEmail(email, invitationCode);

            if (emailSent) {

                Message.setText("Invite sent to: " + email);

                Message.setTextFill(javafx.scene.paint.Color.valueOf("#256f25"));
            } else {

                Message.setText("Failed to send invite.");

                Message.setTextFill(javafx.scene.paint.Color.RED);
            }
        } catch (SQLException e) {

            e.printStackTrace();

            Message.setText("Error storing invitation in the database.");

            Message.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    private boolean sendInviteEmail(String email, String invitationCode) {

        final String SENDER_EMAIL = "campushiveattempe@gmail.com";

        final String SENDER_PASSWORD = "zfupefihnyumvpyg"; 

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