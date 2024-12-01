module CampusHive {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.mail;
	requires java.sql;
	requires java.activation;
	requires javafx.graphics;
	requires org.junit.jupiter.api;
	
	opens application to javafx.graphics, javafx.fxml;
}
