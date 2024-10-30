/**
 * <p> ListofUsersController Class </p>
 *
 * <p> Description: This class handles the functionality for displaying a list of users within
 * the application. It retrieves user data from the H2 database, dynamically sets font sizes 
 * based on the number of users, and manages the action for the home button. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

package application;

import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader; 
import javafx.scene.Node; 
import javafx.scene.Parent; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.control.Label; 
import javafx.stage.Stage; 
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 
import javafx.event.ActionEvent; 
import javafx.scene.layout.GridPane; 
import javafx.scene.text.Font; 
import javafx.scene.text.FontWeight; 

import java.io.IOException; 
import java.sql.SQLException; 
import java.util.Arrays; 
import java.util.List; 

public class ListofUsersController {

    public String stripCharacter(String str, char charToRemove) {

        return str.replace(String.valueOf(charToRemove), "");
    }

    @FXML 
    private Button homeButton;

    @FXML 
    private GridPane userGrid; 

    @FXML
    public void initialize() {
        try {

            List<String[]> users = H2Database.getUsers(); 

            double baseFontSize = calculateFontSize(users.size());

            int rowIndex = 1;  

            for (String[] user : users) {

                if (user.length < 8) { 

                    System.err.println("Invalid user data: " + Arrays.toString(user));
                    continue; 
                }

                String username = stripCharacter(user[0], '"');

                String fullName = user[2] + " " + user[4]; 

                String roles = stripCharacter(user[7], '"'); 

                Label usernameLabel = new Label(username);

                Label nameLabel = new Label(fullName);

                Label roleLabel = new Label(roles);

                usernameLabel.setFont(Font.font("System", baseFontSize));

                nameLabel.setFont(Font.font("System", baseFontSize));

                roleLabel.setFont(Font.font("System", baseFontSize));

                userGrid.add(usernameLabel, 0, rowIndex);

                userGrid.add(nameLabel, 1, rowIndex);

                userGrid.add(roleLabel, 2, rowIndex);

                rowIndex++;
            }
        } catch (SQLException e) {

            e.printStackTrace();

            System.err.println("Error fetching users from the database: " + e.getMessage());
        }
    }

    private double calculateFontSize(int userCount) {

        if (userCount < 5) {

            return 18; 

        } else if (userCount < 10) {

            return 16; 
        } else {

            return 14; 
        }
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {

            Parent adminHomepage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));

            Scene adminHomepageScene = new Scene(adminHomepage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(adminHomepageScene);

            currentStage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}