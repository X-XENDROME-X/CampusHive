module CampusHive {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.mail;
	requires java.sql;
	requires java.activation;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}
