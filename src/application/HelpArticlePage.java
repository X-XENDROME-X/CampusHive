package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

public class HelpArticlePage implements Initializable {


    @FXML private MenuItem newArticleMenuItem;
    @FXML private MenuItem backupMenuItem;
    @FXML private MenuItem restoreMenuItem;
    @FXML private MenuItem restoreAllMenuItem;
    @FXML private Button BackButton;
    
    @FXML private ComboBox<String> levelFilter;
    @FXML private TextField searchBox;
    @FXML private ListView<HelpArticle> articleList;
    @FXML private Button newArticleButton;
    
    @FXML private TextField idField;
    @FXML private ComboBox<String> articleLevel;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField keywordsField;
    @FXML private TextArea bodyArea;
    @FXML private ListView<String> linksList;
    @FXML private TextField newLinkField;
    @FXML private Button addLinkButton;
    @FXML private CheckBox sensitiveCheckbox;
    
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;
    
    @FXML private Label errorLabel;

    // Observable lists for UI components
    private ObservableList<HelpArticle> articles = FXCollections.observableArrayList();
    private ObservableList<String> links = FXCollections.observableArrayList();
    private final String[] levels = {"Beginner", "Intermediate", "Advanced", "Expert"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBoxes
        levelFilter.setItems(FXCollections.observableArrayList(levels));
        articleLevel.setItems(FXCollections.observableArrayList(levels));
        
        // Initialize ListViews
        articleList.setItems(articles);
        linksList.setItems(links);
        
        // Setup search functionality
        setupSearch();
        
        // Setup event handlers
        setupEventHandlers();
        
        // Load existing articles (you'll need to implement this)
        loadArticles();
    }

    private void setupSearch() {
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filterArticles();
        });

        levelFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterArticles();
        });
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the Admin Home Page FXML file
            Parent adminHomePage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
            
            // Set up the new scene
            Scene adminHomeScene = new Scene(adminHomePage);
            
            // Get the current stage from the event source
            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(adminHomeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterArticles() {
        String searchText = searchBox.getText().toLowerCase();
        String selectedLevel = levelFilter.getValue();
        
        ObservableList<HelpArticle> filteredList = FXCollections.observableArrayList();
        
        for (HelpArticle article : articles) {
            boolean levelMatch = selectedLevel == null || article.getLevel().equals(selectedLevel);
            boolean searchMatch = searchText.isEmpty() || 
                                article.getTitle().toLowerCase().contains(searchText) ||
                                article.getDescription().toLowerCase().contains(searchText) ||
                                article.getKeywords().toLowerCase().contains(searchText);
            
            if (levelMatch && searchMatch) {
                filteredList.add(article);
            }
        }
        
        articleList.setItems(filteredList);
    }

    private void setupEventHandlers() {
        // Menu items
        newArticleMenuItem.setOnAction(e -> handleNewArticle());
        backupMenuItem.setOnAction(e -> handleBackup());
        restoreMenuItem.setOnAction(e -> handleRestore(false));
        restoreAllMenuItem.setOnAction(e -> handleRestore(true));
        
        // Buttons
        newArticleButton.setOnAction(e -> handleNewArticle());
        addLinkButton.setOnAction(e -> handleAddLink());
        saveButton.setOnAction(e -> handleSave());
        deleteButton.setOnAction(e -> handleDelete());
        cancelButton.setOnAction(e -> handleCancel());
        
        // Article selection
        articleList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayArticle(newValue);
                }
            }
        );
    }

    private void handleNewArticle() {
        clearForm();
        idField.setText(generateNewId());
    }

    private void handleAddLink() {
        String newLink = newLinkField.getText().trim();
        if (!newLink.isEmpty()) {
            links.add(newLink);
            newLinkField.clear();
        }
    }

    private void handleSave() {
        try {
            HelpArticle article = createArticleFromForm();
            if (validateArticle(article)) {
                saveArticle(article);
                updateArticleList();
                showMessage("Article saved successfully", false);
            }
        } catch (Exception e) {
            showMessage("Error saving article: " + e.getMessage(), true);
        }
    }

    private HelpArticle createArticleFromForm() {
		// TODO Auto-generated method stub
		return null;
	}

	private void handleDelete() {
        HelpArticle selectedArticle = articleList.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            if (confirmDelete()) {
                deleteArticle(selectedArticle);
                updateArticleList();
                clearForm();
                showMessage("Article deleted successfully", false);
            }
        }
    }

    private void handleBackup() {
        try {
            // Implement backup logic
            showMessage("Backup completed successfully", false);
        } catch (Exception e) {
            showMessage("Backup failed: " + e.getMessage(), true);
        }
    }

    private void handleRestore(boolean removeAll) {
        try {
            // Implement restore logic
            if (removeAll) {
                // Remove all existing articles before restore
            }
            // Restore from backup
            loadArticles(); // Reload articles after restore
            showMessage("Restore completed successfully", false);
        } catch (Exception e) {
            showMessage("Restore failed: " + e.getMessage(), true);
        }
    }

    private void handleCancel() {
        clearForm();
        articleList.getSelectionModel().clearSelection();
    }

    private void displayArticle(HelpArticle article) {
        idField.setText(String.valueOf(article.getId()));
        articleLevel.setValue(article.getLevel());
        titleField.setText(article.getTitle());
        descriptionArea.setText(article.getDescription());
        keywordsField.setText(article.getKeywords());
        bodyArea.setText(article.getBody());
        links.setAll(article.getLinks());
        sensitiveCheckbox.setSelected(article.isSensitive());
    }

    private void clearForm() {
        idField.clear();
        articleLevel.setValue(null);
        titleField.clear();
        descriptionArea.clear();
        keywordsField.clear();
        bodyArea.clear();
        links.clear();
        sensitiveCheckbox.setSelected(false);
        errorLabel.setText("");
    }

    private boolean validateArticle(HelpArticle article) {
        if (article.getTitle().isEmpty()) {
            showMessage("Title is required", true);
            return false;
        }
        if (article.getLevel() == null) {
            showMessage("Level is required", true);
            return false;
        }
        // Add more validation as needed
        return true;
    }

    private void showMessage(String message, boolean isError) {
        errorLabel.setText(message);
        errorLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Article");
        alert.setContentText("Are you sure you want to delete this article?");
        return alert.showAndWait().get() == ButtonType.OK;
    }

    // Helper methods that you'll need to implement based on your data storage solution
    private void loadArticles() {
        // Implement loading articles from your data source
    }

    private void saveArticle(HelpArticle article) {
        // Implement saving article to your data source
    }

    private void deleteArticle(HelpArticle article) {
        // Implement deleting article from your data source
    }

    private void updateArticleList() {
        // Implement updating the articles list from your data source
    }

    private String generateNewId() {
        // Implement generating a new unique ID
        return String.valueOf(System.currentTimeMillis());
    }
}

// Helper class to represent a Help Article
class HelpArticle {
    private long id;
    private String level;
    private String title;
    private String description;
    private String keywords;
    private String body;
    private List<String> links;
    private boolean sensitive;

    public HelpArticle() {
        this.links = new ArrayList<>();
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public List<String> getLinks() { return links; }
    public void setLinks(List<String> links) { this.links = links; }
    
    public boolean isSensitive() { return sensitive; }
    public void setSensitive(boolean sensitive) { this.sensitive = sensitive; }

    @Override
    public String toString() {
        return title; // This will be displayed in the ListView
    }
}