	package application;
	
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.fxml.Initializable;
	import javafx.scene.Node;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.*;
	import javafx.stage.Stage;
	
	import java.io.IOException;
	import java.net.URL;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.ResourceBundle;
	
	public class GroupArticlePage implements Initializable {
	
	    @FXML private MenuItem backupGroupMenuItem;
	    @FXML private MenuItem restoreMergeMenuItem;
	    @FXML private MenuItem restoreAllMenuItem;
	    @FXML private CheckBox sqlFiddleCheck;
	    @FXML private CheckBox h2Check;
	    @FXML private CheckBox javaCheck;
	    @FXML private CheckBox javaFXCheck;
	    @FXML private CheckBox eclipseCheck;
	    @FXML private CheckBox intelliJCheck;
	    @FXML private Button applyFiltersButton;
	    @FXML private TextField searchBox;
	    @FXML private Label selectedGroupsLabel;
	    @FXML private ListView<HelpArticle> articleListView;
	    @FXML private Button ShowSelectedButton;
	
	    @FXML private Button backButton;
	    @FXML private Label errorLabel;
	
	    private ObservableList<HelpArticle> articles;
	
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        articles = FXCollections.observableArrayList();
	        articleListView.setItems(articles);
	
	        // Set up event handlers
	        applyFiltersButton.setOnAction(this::handleApplyFilters);
	        ShowSelectedButton.setOnAction(this::handleShowArticleButtonAction);
	
	        // Load articles based on selected groups
	        loadArticles();
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
	    
	    @FXML
	    private void handleShowArticleButtonAction(ActionEvent event) {
	        try {
	            // Load the HelpArticlePage FXML file
	            Parent helpArticleView = FXMLLoader.load(getClass().getResource("HelpArticleView.fxml"));
	            // Set up the new scene
	            Scene helpArticleVScene = new Scene(helpArticleView);
	            // Get the current stage from the event source
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            // Set the new scene on the stage
	            currentStage.setScene(helpArticleVScene);
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    private void handleApplyFilters(ActionEvent event) {
	        // Get selected groups
	        List<String> selectedGroups = getSelectedGroups();
	
	        // Update selected groups label
	        selectedGroupsLabel.setText(String.join(", ", selectedGroups));
	
	        // Filter articles based on selected groups
	        filterArticles(selectedGroups);
	    }
	
	    
	
	
	    private List<String> getSelectedGroups() {
	        List<String> selectedGroups = new ArrayList<>();
	
	        if (sqlFiddleCheck.isSelected()) selectedGroups.add("SQLFiddle");
	        if (h2Check.isSelected()) selectedGroups.add("H2");
	        if (javaCheck.isSelected()) selectedGroups.add("Java");
	        if (javaFXCheck.isSelected()) selectedGroups.add("JavaFX");
	        if (eclipseCheck.isSelected()) selectedGroups.add("Eclipse");
	        if (intelliJCheck.isSelected()) selectedGroups.add("IntelliJ");
	
	        return selectedGroups;
	    }
	
	    private void filterArticles(List<String> selectedGroups) {
	        // Database connection setup
	        String url = "jdbc:h2:./data/help_articles";
	        String user = "sa";
	        String password = "";

	        // Create a base query
	        StringBuilder queryBuilder = new StringBuilder("SELECT id, title, description FROM articles WHERE ");
	        
	        // Dynamically build the WHERE clause using LIKE for each selected group
	        for (int i = 0; i < selectedGroups.size(); i++) {
	            if (i > 0) {
	                queryBuilder.append(" OR "); // Add OR between conditions
	            }
	            queryBuilder.append("groups LIKE ?");
	        }

	        String query = queryBuilder.toString();

	        try (Connection connection = DriverManager.getConnection(url, user, password);
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            // Set the parameters for the prepared statement
	            for (int i = 0; i < selectedGroups.size(); i++) {
	                statement.setString(i + 1, "%" + selectedGroups.get(i) + "%"); // Use wildcards for LIKE
	            }

	            ResultSet resultSet = statement.executeQuery();

	            // Clear the existing items in the ListView
	            articleListView.getItems().clear();
	            articles.clear(); // Clear the articles list to avoid duplicates

	            // Add the fetched articles to the list and update the ListView
	            while (resultSet.next()) {
	                long id = resultSet.getLong("id");
	                String title = resultSet.getString("title");
	                String description = resultSet.getString("description");
	                HelpArticle article = new HelpArticle(id, title, description);
	                //articles.add(article); // Add to articles list
	                articleListView.getItems().add(article); // Update the ListView
	            }
	            
	            // Update the success message
	            errorLabel.setStyle("-fx-text-fill: green;");
	            errorLabel.setText("Applied Filter Successfully.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	            errorLabel.setText("Failed to load articles. Please try again.");
	            articles.clear(); // Clear articles on error to avoid stale data
	        }
	    }

	
	    private void loadArticles() {
	        // TODO: Implement loading articles based on selected groups
	        // For now, just add some sample articles
	        articles.add(new HelpArticle(1, "Article 1", "Sample description 1"));
	        articles.add(new HelpArticle(2, "Article 2", "Sample description 2"));
	        articles.add(new HelpArticle(3, "Article 3", "Sample description 3"));
	    }
	
	    // Helper class to represent a Help Article
	    private class HelpArticle {
	    	private long id;
	        private String title;
	        private String description;
	
	        public HelpArticle(long id,String title, String description) {
	        	this.id = id;
	            this.title = title;
	            this.description = description;
	        }
	        
	        public long getId() {return id;}
	        public String getTitle() {
	            return title;
	        }
	
	        public String getDescription() {
	            return description;
	        }
	
	        @Override
	        public String toString() {
	            return String.format("ID: %d, Title: %s, Description: %s", id, title, description);
	        }
	    }
	}