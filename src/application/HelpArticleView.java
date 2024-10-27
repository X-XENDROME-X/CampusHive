package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpArticleView {

    @FXML private Label titleLabel;
    @FXML private Label levelLabel;
    @FXML private Label groupLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label keywordsLabel;
    @FXML private TextArea bodyTextArea;
    @FXML private ListView<String> linksListView;
    @FXML private Button backButton;

    private HelpArticle selectedArticle;

    public void setArticle(HelpArticle article) {
        selectedArticle = article;
        titleLabel.setText(article.getTitle());
        levelLabel.setText("Level: " + article.getLevel());
        groupLabel.setText("Group: " + article.getGroups());
        descriptionLabel.setText(article.getDescription());
        keywordsLabel.setText("Keywords: " + article.getKeywords());
        bodyTextArea.setText(article.getBody());
        linksListView.getItems().setAll(article.getLinks());
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
    	try {
            // Load the HelpArticlePage FXML file
            Parent helpArticlePage = FXMLLoader.load(getClass().getResource("HelpArticlePage.fxml"));

            // Set up the new scene
            Scene helpArticleScene = new Scene(helpArticlePage);

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            currentStage.setScene(helpArticleScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}