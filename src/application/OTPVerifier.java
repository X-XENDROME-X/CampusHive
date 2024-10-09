package application;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class OTPVerifier {

    // Define your sender email and password
    private static final String SENDER_EMAIL = "campushiveattempe@gmail.com";
    private static final String SENDER_PASSWORD = "zfupefihnyumvpyg";

    // Placeholder for the recipient email
    private static final String recipientEmail = "shoryaraj2022@gmail.com"; // Replace this with actual user email

    public static void main(String[] args) {
        // Generate the OTP
        String otp = generateOTP(6); // OTP with 6 digits

        // Send the OTP email
        boolean result = sendOTP(recipientEmail, otp);
        if (result) {
            System.out.println("OTP sent successfully to " + recipientEmail);
        } else {
            System.out.println("Failed to send OTP to " + recipientEmail);
        }
    }

    // Generate a random OTP of 'length' digits
    public static String generateOTP(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return otp.toString();
    }

    // Send the OTP email using Gmail SMTP
    public static boolean sendOTP(String recipient, String otp) {
        // Setup mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create session with email credentials
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Your OTP Code");
            message.setText("Dear User,\n\nYour OTP code is: " + otp + "\n\nPlease use this to verify your account.");

            // Send the message
            Transport.send(message);
            return true; // Email sent successfully
        } catch (MessagingException e) {
            e.printStackTrace(); // Print error if any occurs
            return false; // Failed to send email
        }
    }
}
