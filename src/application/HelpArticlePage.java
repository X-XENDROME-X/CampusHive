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
import javafx.scene.layout.HBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;

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
    @FXML private Button viewByGroupButton;
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
    @FXML private HBox articleGroup;
    @FXML private CheckBox sqlFiddleCheckBox;
    @FXML private CheckBox h2CheckBox;
    @FXML private CheckBox javaCheckBox;
    @FXML private CheckBox javaFXCheckBox;
    @FXML private CheckBox eclipseCheckBox;
    @FXML private CheckBox intelliJCheckBox;
    
    
    
    
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
        
        initializeArticleGroups();
        
        // Load existing articles (you'll need to implement this)
        loadArticles();
    }
    

    
    @FXML
    private void handleGroupArticlePageAction(ActionEvent event) {
        try {

            Parent GroupPage = FXMLLoader.load(getClass().getResource("GroupArticlePage.fxml"));
            

            Scene GroupScene = new Scene(GroupPage);
            
            Stage currentStage = (Stage) viewByGroupButton.getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(GroupScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void initializeArticleGroups() {
        sqlFiddleCheckBox = new CheckBox("SQLFiddle");
        h2CheckBox = new CheckBox("H2");
        javaCheckBox = new CheckBox("Java");
        javaFXCheckBox = new CheckBox("JavaFX");
        eclipseCheckBox = new CheckBox("Eclipse");
        intelliJCheckBox = new CheckBox("IntelliJ");

        articleGroup.getChildren().addAll(
            sqlFiddleCheckBox, h2CheckBox, javaCheckBox,
            javaFXCheckBox, eclipseCheckBox, intelliJCheckBox
        );
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
            // Get the current user session
            UserSession session = UserSession.getInstance();

            Parent homePage;

            // Check the user's role and load the appropriate homepage
            if (session != null && session.getRole().equalsIgnoreCase("admin")) {
                // Load the Admin Home Page FXML file
                homePage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
            } else if (session != null && session.getRole().equalsIgnoreCase("instructor")) {
                // Load the Instructor Home Page FXML file
                homePage = FXMLLoader.load(getClass().getResource("Instructor_Homepage.fxml"));
            } else {
                // Handle the case where the user session is not found or role is not recognized
                // You could load a default page or show an error
                throw new IOException("User role not recognized");
            }

            // Set up the new scene
            Scene homeScene = new Scene(homePage);

            // Get the current stage from the event source
            Stage currentStage = (Stage) BackButton.getScene().getWindow();

            // Set the new scene on the stage
            currentStage.setScene(homeScene);
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
    	HelpArticle article = new HelpArticle();
    	article.setLevel(articleLevel.getValue());
        article.setGroups(getSelectedGroups());
        return article;
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
	
	private List<String> getSelectedGroups() {
        List<String> selectedGroups = new ArrayList<>();
        if (sqlFiddleCheckBox.isSelected()) selectedGroups.add("SQLFiddle");
        if (h2CheckBox.isSelected()) selectedGroups.add("H2");
        if (javaCheckBox.isSelected()) selectedGroups.add("Java");
        if (javaFXCheckBox.isSelected()) selectedGroups.add("JavaFX");
        if (eclipseCheckBox.isSelected()) selectedGroups.add("Eclipse");
        if (intelliJCheckBox.isSelected()) selectedGroups.add("IntelliJ");
        return selectedGroups;
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
        articleLevel.setValue(article.getLevel());
        updateArticleGroupCheckboxes(article.getGroups());
    }
    
    private void updateArticleGroupCheckboxes(List<String> groups) {
        sqlFiddleCheckBox.setSelected(groups.contains("SQLFiddle"));
        h2CheckBox.setSelected(groups.contains("H2"));
        javaCheckBox.setSelected(groups.contains("Java"));
        javaFXCheckBox.setSelected(groups.contains("JavaFX"));
        eclipseCheckBox.setSelected(groups.contains("Eclipse"));
        intelliJCheckBox.setSelected(groups.contains("IntelliJ"));
    }
    
    private void clearArticleGroupCheckboxes() {
        sqlFiddleCheckBox.setSelected(false);
        h2CheckBox.setSelected(false);
        javaCheckBox.setSelected(false);
        javaFXCheckBox.setSelected(false);
        eclipseCheckBox.setSelected(false);
        intelliJCheckBox.setSelected(false);
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
        articleLevel.setValue(null);
        clearArticleGroupCheckboxes();
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
    private List<String> groups;

    public HelpArticle() {
        this.links = new ArrayList<>();
        this.groups = new ArrayList<>();
    }
    
    

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) { this.groups = groups; }
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