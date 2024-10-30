package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
    private static Connection dbConnection;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the user database
            initializeUserDatabase();

            // Check if an admin user exists in the database
            boolean adminExists = H2Database.checkForAdminUser();

            // Load the appropriate FXML based on whether an admin user exists
            Parent root;
            if (adminExists) {
                root = FXMLLoader.load(getClass().getResource("Login Page.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("Create1stUserAdmin.fxml"));
            }

            // Set up the primary stage
            primaryStage.setTitle("CampusHive");
            primaryStage.setScene(new Scene(root, 1080, 720));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to initialize the H2 user database
    private void initializeUserDatabase() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:h2:./data/users/userdb", "sa", "");
            Statement stmt = dbConnection.createStatement();

            // Create user table
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "username VARCHAR(255) PRIMARY KEY, "
                    + "password VARCHAR(255), "
                    + "firstName VARCHAR(255), "
                    + "middleName VARCHAR(255), "
                    + "lastName VARCHAR(255), "
                    + "email VARCHAR(255), "
                    + "isActive BOOLEAN, "
                    + "roles VARCHAR(255))";
            stmt.execute(createUserTableSQL);

            // Create invitations table
            String createInvitationTableSQL = "CREATE TABLE IF NOT EXISTS invitations ("
                    + "email VARCHAR(255), "
                    + "invitationCode VARCHAR(255) PRIMARY KEY, "
                    + "roles VARCHAR(255))";
            stmt.execute(createInvitationTableSQL);

            System.out.println("User database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize the user database.");
        }
    }

    // Close the database connection when the application stops
    @Override
    public void stop() {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
