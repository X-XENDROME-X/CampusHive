/**
 * <p> Main Class </p>
 *
 * <p> Description: This class serves as the entry point for the JavaFX application.
 * It initializes the user database and manages the primary application window, 
 * including loading different FXML views based on the presence of an admin user. 
 * It also coordinates the start of the application and ensures smooth navigation 
 * throughout the user interface. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project made (Phase 4)
 */


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

            initializeUserDatabase();
            H2Database.printUsers();
            boolean adminExists = H2Database.checkForAdminUser();

            Parent root;
            if (adminExists) {
                root = FXMLLoader.load(getClass().getResource("Login Page.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("Create1stUserAdmin.fxml"));
            }

            primaryStage.setTitle("CampusHive");
            primaryStage.setScene(new Scene(root, 1080, 720));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUserDatabase() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:h2:./data/users/userdb", "sa", "");
            Statement stmt = dbConnection.createStatement();

            
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
            
            String alterSpecialviewColumnSQL = "ALTER TABLE users ADD COLUMN IF NOT EXISTS specialview BOOLEAN";
            stmt.execute(alterSpecialviewColumnSQL);
            
            String alterSpecialadminColumnSQL = "ALTER TABLE users ADD COLUMN IF NOT EXISTS specialadmin BOOLEAN";
            stmt.execute(alterSpecialadminColumnSQL);
            
            

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
