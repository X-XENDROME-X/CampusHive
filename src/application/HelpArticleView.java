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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    public static class HelpArticle {
        private long id;
        private String title;
        private String description;
        private String body;
        private String keywords;
        private String links;
        private String level;

        public HelpArticle(long id, String title, String description, 
                           String body, String keywords, 
                           String links, String level) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.body = body;
            this.keywords = keywords;
            this.links = links;
            this.level = level;
        }

        // Add getters for the new fields
        public long getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getBody() { return body; }
        public String getKeywords() { return keywords; }
        public String getLinks() { return links; }
        public String getLevel() { return level; }
    }

    public void loadArticleById(long articleId) {
        HelpArticle article = fetchFullArticleDetails(articleId);  // Fetch full details from DB
        if (article != null) {
            displayArticleDetails(article);  // Display the details on UI components
        }
    }
    
    
    private HelpArticle fetchFullArticleDetails(long articleId) {
        String query = "SELECT * FROM articles WHERE id = ?";
        
        String url = "jdbc:h2:./data/articles/help_articles";
        String user = "sa";
        String password = "";
        
        try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(query)) {
               statement.setLong(1, articleId);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                   // Log the fetched values for debugging
                   String title = resultSet.getString("title");
                   String description = resultSet.getString("description");
                   String body = resultSet.getString("body");
                   String keywords = resultSet.getString("keywords");
                   String level = resultSet.getString("level");

                   System.out.println("Fetched article: " + title);  // Debug log
                   return new HelpArticle(articleId, title, description, body, keywords, "", level);
               } else {
                   System.out.println("No article found with ID: " + articleId);  // Debug log
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return null;
       }
    
    private void displayArticleDetails(HelpArticle article) {
        titleLabel.setText(article.getTitle());
        levelLabel.setText("Level: " + article.getLevel()); // Ensure levelLabel is set
        descriptionLabel.setText(article.getDescription());
        keywordsLabel.setText("Keywords: " + article.getKeywords());
        bodyTextArea.setText(article.getBody());
        // If links are supposed to be a list, you might want to split and set them in the ListView
        if (!article.getLinks().isEmpty()) {
            linksListView.getItems().setAll(article.getLinks().split(",")); // Assuming links are comma-separated
        }
    }

    
    
    public void setArticle(HelpArticle article) {
        selectedArticle = article;
        titleLabel.setText(article.getTitle());
        levelLabel.setText("Level: " + article.getLevel());
        //groupLabel.setText("Group: " + article.getGroups());
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