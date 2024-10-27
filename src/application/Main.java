package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
    private static Connection dbConnection;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the H2 database connection
            initializeDatabase();

            // Check if an admin user exists in the database
            boolean adminExists = H2Database.checkForAdminUser();

            // Load the appropriate FXML based on whether an admin user exists
            Parent root;
            if (adminExists) {
                // If admin exists, load the Login Page
                root = FXMLLoader.load(getClass().getResource("Login Page.fxml"));
            } else {
                // If no admin exists, load the Create 1st Admin User page
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


    // Method to initialize the H2 database
    private void initializeDatabase() {
        try {
            // Connect to the H2 database (stored locally in a file called userdb)
            dbConnection = DriverManager.getConnection("jdbc:h2:./userdb", "sa", "");

            // Create a Statement to execute SQL queries
            Statement stmt = dbConnection.createStatement();

            // Create the users table if it doesn't exist
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

            // Create the invitations table if it doesn't exist
            String createInvitationTableSQL = "CREATE TABLE IF NOT EXISTS invitations ("
                    + "email VARCHAR(255), "
                    + "invitationCode VARCHAR(255) PRIMARY KEY, "
                    + "roles VARCHAR(255))";
            stmt.execute(createInvitationTableSQL);

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize the database.");
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
