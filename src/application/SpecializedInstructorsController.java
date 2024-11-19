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

public class SpecializedInstructorsController {
    @FXML
    private Button homeButton;

    @FXML
    private GridPane instructorGrid;

    @FXML
    public void initialize() {
        try {
            loadInstructorData();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading instructor data: " + e.getMessage());
        }
    }

    private void loadInstructorData() {
        // Sample data - replace with actual database query
        String[][] instructorData = {
            {"INS001", "prof_john"},
            {"INS002", "prof_sarah"},
            {"INS003", "prof_mike"}
        };

        for (int i = 0; i < instructorData.length; i++) {
            for (int j = 0; j < instructorData[i].length; j++) {
                Label label = createInstructorLabel(instructorData[i][j]);
                instructorGrid.add(label, j, i + 1);
                GridPane.setHalignment(label, HPos.CENTER); // Center in grid cell
            }
        }
    }

    private Label createInstructorLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Dubai Light", FontWeight.NORMAL, 20));
        label.setPrefWidth(400);
        label.setAlignment(Pos.CENTER); // Center text within label
        label.setStyle("-fx-padding: 5 0 5 0;"); // Add some vertical padding
        return label;
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
            if (session == null) {
                showError("No active session found!");
                return;
            }

            String userRole = session.getRole();
            String destinationPage;

            // Determine destination page based on user role
            if (userRole.equalsIgnoreCase("Instructor")) {
                destinationPage = "Instructor_Homepage.fxml";
            } else if (userRole.equalsIgnoreCase("Admin")) {
                destinationPage = "Admin_Home_Page.fxml";
            } else {
                // Handle unexpected role
                showError("Invalid user role: " + userRole);
                return;
            }

            // Load the appropriate FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(destinationPage));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error navigating to home page: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error: " + e.getMessage());
        }
    }
}