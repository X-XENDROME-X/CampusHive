package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;

public class OTPController {

    @FXML
    private PasswordField otpField;

    @FXML
    private Button verifyButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleVerifyButtonAction(ActionEvent event) {
        String otpInput = otpField.getText();

        // Basic validation for OTP (e.g., checking if it's 6 digits)
        if (otpInput.isEmpty()) {
            showErrorMessage("OTP cannot be empty.");
        } else if (!otpInput.matches("\\d{6}")) {
            showErrorMessage("OTP must be a 6-digit number.");
        } else {
            // Placeholder for verifying OTP from the database or backend
            boolean isOTPValid = checkOTPInDatabase(otpInput);

            if (isOTPValid) {
                // Proceed to the next page or action on successful OTP verification
                proceedToNextStep();
            } else {
                showErrorMessage("Invalid OTP. Please try again.");
            }
        }
    }

    // Method to display error messages
    private void showErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    // Placeholder for the actual OTP check logic
    private boolean checkOTPInDatabase(String otp) {
        // Implement actual OTP validation with the backend or database here
        return otp.equals("123456"); // Placeholder for demonstration
    }

    // Method to proceed to the next step (e.g., dashboard) after OTP validation
    private void proceedToNextStep() {
        // Logic for proceeding to the next page or action
        // For example, load a dashboard page or display a success message
        System.out.println("OTP verified successfully. Proceeding to the next step.");
    }
}
