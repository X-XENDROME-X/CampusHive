/**
 * <p> HelpArticleView Class </p>
 *
 * <p> Description: This class manages the display and functionality for help articles within 
 * the application. It handles loading articles from the database, displaying their details, 
 * and managing user interactions, such as navigating back to the previous screen. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

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
    @FXML private TextArea linksListView;
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
                   String referenceLinks = resultSet.getString("referenceLinks");

                   System.out.println("Fetched article: " + title);  // Debug log
                   return new HelpArticle(articleId, title, description, body, keywords, referenceLinks, level);
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
        linksListView.setText(article.getLinks());
    }

    
    
    public void setArticle(HelpArticle article) {
        selectedArticle = article;
        titleLabel.setText(article.getTitle());
        levelLabel.setText("Level: " + article.getLevel());
        descriptionLabel.setText(article.getDescription());
        keywordsLabel.setText("Keywords: " + article.getKeywords());
        bodyTextArea.setText(article.getBody());
        linksListView.setText(article.getLinks());
        
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
    	try {
	            // Load the appropriate FXML
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("GroupArticlePage.fxml"));
	            Parent root = loader.load();
	            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}