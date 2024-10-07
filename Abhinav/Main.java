package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
    private static Stage primaryStage; // Store the primary stage for navigation

    @Override
    public void start(Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage; // Set primary stage
            Parent root = FXMLLoader.load(getClass().getResource("Login Page.fxml"));
            Scene scene = new Scene(root, 800, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
