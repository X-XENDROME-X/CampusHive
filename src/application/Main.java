/*******
 * <p> Main Class </p>
 * 
 * <p> Description: The entry point of the Campus Hive JavaFX application. 
 * This class initializes the application and loads the primary user interface, 
 * setting up the application environment and managing the application's lifecycle.</p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */

	package application;

	import javafx.application.Application;
	import javax.mail.*;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.stage.Stage;
	import java.sql.Connection;
	import java.sql.SQLException;
	import javax.activation.*;


	public class Main extends Application {


	    @Override
	    public void start(Stage primaryStage) {
	        try {
	            Parent root = FXMLLoader.load(getClass().getResource("Create1stUserAdmin.fxml"));
	            primaryStage.setTitle("CampusHive");
	            primaryStage.setScene(new Scene(root, 1080, 720)); 
	            primaryStage.setResizable(false);
	            primaryStage.show();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }




	    public static void main(String[] args) {
	        launch(args);
	    }
	}
