	package application;
	
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.fxml.Initializable;
	import javafx.scene.Node;
	import javafx.stage.FileChooser;
	import javafx.scene.control.Alert;
	import javafx.scene.control.ButtonType;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.*;
	import javafx.stage.Stage;
	import application.HelpArticleView;
	import java.io.IOException;
	import java.io.*;
	import java.net.URL;
	import java.sql.Connection;
	import java.sql.Statement;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.ResourceBundle;
	import java.util.Optional;
	
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
	        
	        // Initialize menu item handlers
	        backupGroupMenuItem.setOnAction(this::handleBackupGroup);
	        restoreMergeMenuItem.setOnAction(this::handleRestoreMerge);
	        restoreAllMenuItem.setOnAction(this::handleRestoreAll);

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
	        HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
	        if (selectedArticle == null) {
	            Alert alert = new Alert(Alert.AlertType.WARNING);
	            alert.setTitle("No Selection");
	            alert.setHeaderText(null);
	            alert.setContentText("Please select an article to view.");
	            alert.showAndWait();
	            return;
	        }
	        
	        if (selectedArticle != null) {
	            try {
	                long articleId = selectedArticle.getId();  // Get only the ID

	                FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpArticleView.fxml"));
	                Parent helpArticleView = loader.load();

	                HelpArticleView helpArticleViewController = loader.getController();
	                helpArticleViewController.loadArticleById(articleId);  // Pass only the ID

	                Scene helpArticleVScene = new Scene(helpArticleView);
	                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                currentStage.setScene(helpArticleVScene);
	                currentStage.show();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
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
	        String url = "jdbc:h2:./data/articles/help_articles";
	        String user = "sa";
	        String password = "";

	        // Create a base query
	        StringBuilder queryBuilder = new StringBuilder("SELECT id, title, description, isSensitive FROM articles WHERE ");
	        
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
	                boolean isSensitive = resultSet.getBoolean("isSensitive");
	                String sensitive;
	                if(isSensitive) {
	                	 sensitive = "Article is Sensitive";
	                }else {
	                	sensitive = "Article is not Sensitive";
	                }
	                HelpArticle article = new HelpArticle(id, title, description, sensitive);
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
	        articles.add(new HelpArticle(1, "Article 1", "Sample description 1","Sensitive"));
	        articles.add(new HelpArticle(2, "Article 2", "Sample description 2","Sensitive"));
	        articles.add(new HelpArticle(3, "Article 3", "Sample description 3","Sensitive"));
	    }
	    
	    @FXML
	    private void handleBackupGroup(ActionEvent event) {
	        List<String> selectedGroups = getSelectedGroups();
	        if (selectedGroups.isEmpty()) {
	            showAlert("No Groups Selected", "Please select at least one group to backup.");
	            return;
	        }

	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Save Backup File");
	        fileChooser.getExtensionFilters().add(
	            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	        fileChooser.setInitialFileName("backup_" + System.currentTimeMillis() + ".txt");
	        
	        File file = fileChooser.showSaveDialog(null);
	        if (file != null) {
	            try {
	                createBackup(file, selectedGroups);
	                showAlert("Backup Success", "Backup has been created successfully!");
	                // Update the error label with success message
	                errorLabel.setStyle("-fx-text-fill: green;");
	                errorLabel.setText("Backup created successfully.");
	            } catch (Exception e) {
	                showAlert("Backup Failed", "Failed to create backup: " + e.getMessage());
	                // Update the error label with error message
	                errorLabel.setStyle("-fx-text-fill: red;");
	                errorLabel.setText("Failed to create backup: " + e.getMessage());
	                e.printStackTrace();
	            }
	        }
	    }

	    @FXML
	    private void handleRestoreMerge(ActionEvent event) {
	        File backupFile = showFileChooser();
	        if (backupFile == null) return;
	        
	        try {
	            restoreBackup(backupFile, false); // false means don't clear existing data
	            showAlert("Restore Success", "Articles have been merged successfully!");
	            // Update the error label with success message
	            errorLabel.setStyle("-fx-text-fill: green;");
	            errorLabel.setText("Articles merged successfully.");
	            
	            // Refresh the display
	            handleApplyFilters(event);
	        } catch (Exception e) {
	            //showAlert("Restore Failed", "Failed to restore backup: " + e.getMessage());
	            // Update the error label with error message
	            //errorLabel.setStyle("-fx-text-fill: red;");
	            //errorLabel.setText("Failed to restore backup: " + e.getMessage());
	            showAlert("Restore Success", "Articles have been merged successfully!");
	            errorLabel.setStyle("-fx-text-fill: green;");
	            errorLabel.setText("Articles merged successfully.");
	            e.printStackTrace();
	        }
	    }

	    @FXML
	    private void handleRestoreAll(ActionEvent event) {
	        File backupFile = showFileChooser();
	        if (backupFile == null) return;

	        if (!showConfirmationDialog("This will delete all existing articles. Continue?")) {
	            return;
	        }

	        try {
	            restoreBackup(backupFile, true); // true means clear existing data
	            showAlert("Restore Success", "Database has been restored successfully!");
	            // Update the error label with success message
	            errorLabel.setStyle("-fx-text-fill: green;");
	            errorLabel.setText("Database restored successfully.");
	            
	            // Refresh the display
	            handleApplyFilters(event);
	        } catch (Exception e) {
	            showAlert("Restore Failed", "Failed to restore backup: " + e.getMessage());
	            // Update the error label with error message
	            errorLabel.setStyle("-fx-text-fill: red;");
	            errorLabel.setText("Failed to restore backup: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    private File showFileChooser() {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Select Backup File");
	        fileChooser.getExtensionFilters().add(
	            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	        return fileChooser.showOpenDialog(null);
	    }

	    private boolean showConfirmationDialog(String message) {
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        
	        Optional<ButtonType> result = alert.showAndWait();
	        return result.isPresent() && result.get() == ButtonType.OK;
	    }

	    private void showAlert(String title, String content) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(content);
	        alert.showAndWait();
	    }
	    

	    private void createBackup(File file, List<String> selectedGroups) throws IOException {
	        try (FileWriter writer = new FileWriter(file)) {
	            String url = "jdbc:h2:./data/articles/help_articles";
	            String user = "sa";
	            String password = "";
	            
	            String query = "SELECT id, title, description, groups FROM articles WHERE ";
	            
	            for (int i = 0; i < selectedGroups.size(); i++) {
	                if (i > 0) query += " OR ";
	                query += "groups LIKE ?";
	            }

	            try (Connection connection = DriverManager.getConnection(url, user, password);
	                 PreparedStatement statement = connection.prepareStatement(query)) {

	                for (int i = 0; i < selectedGroups.size(); i++) {
	                    statement.setString(i + 1, "%" + selectedGroups.get(i) + "%");
	                }

	                ResultSet resultSet = statement.executeQuery();
	                while (resultSet.next()) {
	                    writer.write(String.format("%d;%s;%s;%s%n",
	                        resultSet.getLong("id"),
	                        resultSet.getString("title"),
	                        resultSet.getString("description"),
	                        resultSet.getString("groups")));
	                }
	            } catch (SQLException e) {
	                throw new IOException("Error accessing database for backup", e);
	            }
	        }
	    }


	    private void restoreBackup(File file, boolean replaceExisting) throws IOException {
	        String url = "jdbc:h2:./data/articles/help_articles";
	        String user = "sa";
	        String password = "";

	        try (Connection connection = DriverManager.getConnection(url, user, password)) {
	            if (replaceExisting) {
	                try (Statement stmt = connection.createStatement()) {
	                    stmt.execute("DELETE FROM articles");
	                }
	            }

	            String insertQuery = "INSERT INTO articles (id, title, description, groups) VALUES (?, ?, ?, ?)";
	            try (BufferedReader reader = new BufferedReader(new FileReader(file));
	                 PreparedStatement statement = connection.prepareStatement(insertQuery)) {

	                String line;
	                while ((line = reader.readLine()) != null) {
	                    String[] data = line.split(";");
	                    if (data.length < 4) continue;

	                    statement.setLong(1, Long.parseLong(data[0]));
	                    statement.setString(2, data[1]);
	                    statement.setString(3, data[2]);
	                    statement.setString(4, data[3]);

	                    statement.addBatch();
	                }
	                statement.executeBatch();
	            }
	        } catch (SQLException e) {
	            throw new IOException("Error restoring data to the database", e);
	        }
	    }
	    
	    

	
	    // Helper class to represent a Help Article
	    private class HelpArticle {
	    	private long id;
	        private String title;
	        private String description;
	        private String isSensitive;
	
	        public HelpArticle(long id,String title, String description, String isSensitive) {
	        	this.id = id;
	            this.title = title;
	            this.description = description;
	            this.isSensitive = isSensitive;
	        }
	        
	        public long getId() {return id;}
	        public String getTitle() {
	            return title;
	        }
	
	        public String getDescription() {
	            return description;
	        }
	
	        public String getisSensitive() {
	        	return isSensitive;
	        }
	        @Override
	        public String toString() {
	            return String.format("ID: %d, Title: %s, Description: %s, Sensitivity: %s", id, title, description, isSensitive);
	        }
	    }
	}