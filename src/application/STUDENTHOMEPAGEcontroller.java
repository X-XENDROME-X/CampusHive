package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class STUDENTHOMEPAGEcontroller {
    @FXML
    private Button logout;
    
    @FXML
    private Label title;
    
    @FXML
    private Button back;
    
    @FXML
    private Label StudentName;
    
    @FXML
    private Button getHelpButton;

    @FXML
    private void handleLogoutAction() {
        try {
            UserSession.getInstance().cleanUserSession(); // Clear the session
            // Load the Create Account FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login Page.fxml"));
            Parent root = loader.load();
            // Get the current stage and set the new scene
            Stage stage = (Stage) logout.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user
        }
    }

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        if (session != null) {
            StudentName.setText(session.getUsername() + " (" + session.getRole() + ")!");
        } else {
            StudentName.setText("No active session.");
        }
    }

    @FXML
    private void handleViewingArticles(ActionEvent event) {
        try {
        	UserSession.getInstance().addPageToHistory("STUDENTHOMEPAGE.fxml");
            Parent HelpArticlePage = FXMLLoader.load(getClass().getResource("GroupArticlePage.fxml"));
            Scene HelpArticleScene = new Scene(HelpArticlePage);
            Stage currentStage = (Stage) back.getScene().getWindow();
            currentStage.setScene(HelpArticleScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGetHelp(ActionEvent event) {
        try {
        	UserSession.getInstance().addPageToHistory("STUDENTHOMEPAGE.fxml");
            Parent sendMessagePage = FXMLLoader.load(getClass().getResource("SendMessageView.fxml"));
            Scene sendMessageScene = new Scene(sendMessagePage);
            Stage currentStage = (Stage) getHelpButton.getScene().getWindow();
            currentStage.setScene(sendMessageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user
            System.err.println("Error loading Send Message page: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            Parent adminHomePage = FXMLLoader.load(getClass().getResource("SELECTROLE02.fxml"));
            Scene adminHomeScene = new Scene(adminHomePage);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(adminHomeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}