/*******
 * <p> ListofUsersController Class </p>
 * 
 * <p> Description: This class manages the user interface for displaying and managing the list of 
 * users within the Campus Hive application. It handles functionalities such as retrieving user 
 * data from the database, displaying it in the UI, and providing options for user management, 
 * including deletion and role assignment.</p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
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
import java.util.Arrays;
import java.util.List;

public class ListofUsersController {
	
	public String stripCharacter(String str, char charToRemove) {
	    return str.replace(String.valueOf(charToRemove), "");
	}

    @FXML
    private Button homeButton;

    @FXML
    private GridPane userGrid; // GridPane to hold user data

    // Initialize method to populate the GridPane when the scene loads
    @FXML
    public void initialize() {
        List<String[]> users = CSVDatabase.getUsers();

        // Calculate the font size based on the number of users
        double baseFontSize = calculateFontSize(users.size());

        int rowIndex = 1;  // Start from row 1, since row 0 is for headers

        for (String[] user : users) {
            if (user.length < 8) { // Ensure the user array has enough fields
                System.err.println("Invalid user data: " + Arrays.toString(user));
                continue; // Skip invalid entries
            }

            String username = stripCharacter(user[0], '"');
            String fullName = user[2] + " " + user[4]; // First name and last name
            String roles = stripCharacter(user[7], '"'); // Roles field

            // Create text elements for user data
            Label usernameLabel = new Label(username);
            Label nameLabel = new Label(fullName);
            Label roleLabel = new Label(roles);

            // Set dynamic font size
            usernameLabel.setFont(Font.font("System", baseFontSize));
            nameLabel.setFont(Font.font("System", baseFontSize));
            roleLabel.setFont(Font.font("System", baseFontSize));

            // Add the data labels to the GridPane (1 row per user)
            userGrid.add(usernameLabel, 0, rowIndex);
            userGrid.add(nameLabel, 1, rowIndex);
            userGrid.add(roleLabel, 2, rowIndex);

            rowIndex++;
        }
    }


    // Calculate dynamic font size based on the number of users
    private double calculateFontSize(int userCount) {
        if (userCount < 5) {
            return 18; // Larger font size for fewer users
        } else if (userCount < 10) {
            return 16; // Medium font size for a moderate number of users
        } else {
            return 14; // Smaller font size for more users
        }
    }

    // Method to handle the home button action
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            // Load the admin homepage FXML file
            Parent adminHomepage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
            
            // Set up the new scene
            Scene adminHomepageScene = new Scene(adminHomepage);
            
            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set the new scene on the stage
            currentStage.setScene(adminHomepageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
