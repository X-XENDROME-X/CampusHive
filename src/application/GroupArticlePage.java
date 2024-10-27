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
        // TODO: Implement filtering logic based on selected groups
        // For now, just print the selected groups
        System.out.println("Filter articles by groups: " + selectedGroups);
    }

    private void loadArticles() {
        // TODO: Implement loading articles based on selected groups
        // For now, just add some sample articles
        articles.add(new HelpArticle("Article 1", "Sample description 1"));
        articles.add(new HelpArticle("Article 2", "Sample description 2"));
        articles.add(new HelpArticle("Article 3", "Sample description 3"));
    }

    // Helper class to represent a Help Article
    private class HelpArticle {
        private String title;
        private String description;

        public HelpArticle(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}