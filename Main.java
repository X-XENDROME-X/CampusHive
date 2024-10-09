package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;	



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Parent root = FXMLLoader.load(getClass().getResource("SELECTROLE02.fxml"));
			//Parent root = FXMLLoader.load(getClass().getResource("STUDENTHOMEPAGE.fxml"));
			//Parent root = FXMLLoader.load(getClass().getResource("Login Page OTP.fxml"));
			Parent root = FXMLLoader.load(getClass().getResource("Create1stUserAdmin.fxml"));
			//Parent root = FXMLLoader.load(getClass().getResource("Create1stUserAdmin.fxml"));
			//Parent root = FXMLLoader.load(getClass().getResource("Finish_Setting_Up_Page 2.fxml"));

			Scene scene = new Scene(root,1080,720);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


//package application;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//
//public class Main extends Application {
//	@Override
//	public void start(Stage primaryStage) {
//		try {
//			Parent root = FXMLLoader.load(getClass().getResource("Finish_Setting_Up_page-2.fxml"));
//	        primaryStage.setTitle("Finish Setting Up Account");
//	        primaryStage.setScene(new Scene(root, 1080, 720)); 
//	        primaryStage.setResizable(false);
//	        primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void main(String[] args) {
//		launch(args);
//	}
//}
