/**
 * <p> DeleteUserAccountControllerTest Class </p>
 *
 * <p> Description: This class contains unit tests for the DeleteUserAccountController. 
 * It ensures proper functionality, including edge cases, error handling, 
 * and interaction with UI components. </p>
 *
 * <p> Key Features Tested:
 * - Validation for empty usernames
 * - Confirmation checkbox validation
 * - Successful and unsuccessful user deletions
 * - Navigation and session handling
 * - Interaction with database for user management
 * - Handling special cases such as special characters, multiple roles, and active users
 * </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


package application;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class DeleteUserAccountControllerTest {
    private DeleteUserAccountController controller;
    private TextField usernameField;
    private Button deleteButton;
    private CheckBox confirmationCheckBox;
    private Label statusLabel;
    private Button cancelButton;
    private Button homeButton;

    @BeforeAll
    public static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void cleanupDatabase() throws SQLException {
        try {
            H2Database.deleteUserByUsername("testUser");
            H2Database.deleteUserByUsername("testUser2");
        } catch (SQLException e) {
            // Ignore if users don't exist
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new DeleteUserAccountController();
        usernameField = new TextField();
        deleteButton = new Button();
        confirmationCheckBox = new CheckBox();
        statusLabel = new Label();
        cancelButton = new Button();
        homeButton = new Button();

        // Use reflection to access private fields
        Field username = DeleteUserAccountController.class.getDeclaredField("usernameField");
        Field delete = DeleteUserAccountController.class.getDeclaredField("deleteButton");
        Field confirm = DeleteUserAccountController.class.getDeclaredField("confirmationCheckBox");
        Field status = DeleteUserAccountController.class.getDeclaredField("statusLabel");
        Field cancel = DeleteUserAccountController.class.getDeclaredField("cancelButton");
        Field home = DeleteUserAccountController.class.getDeclaredField("homeButton");

        // Make fields accessible
        username.setAccessible(true);
        delete.setAccessible(true);
        confirm.setAccessible(true);
        status.setAccessible(true);
        cancel.setAccessible(true);
        home.setAccessible(true);

        // Set the fields
        username.set(controller, usernameField);
        delete.set(controller, deleteButton);
        confirm.set(controller, confirmationCheckBox);
        status.set(controller, statusLabel);
        cancel.set(controller, cancelButton);
        home.set(controller, homeButton);
    }

    @Test
    void testDeleteWithEmptyUsername() throws Exception {
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
        assertEquals("Username cannot be empty.", statusLabel.getText());
    }

    @Test
    void testDeleteWithoutConfirmation() throws Exception {
        usernameField.setText("testUser");
        confirmationCheckBox.setSelected(false);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
        assertEquals("Please confirm that you want to delete the account.", statusLabel.getText());
    }

    @Test
    void testSuccessfulDeletion() throws Exception {
        H2Database.addUser("testUser", "password", "email@test.com",
            "Test", "User", "STUDENT", false, "", false, false);
        usernameField.setText("testUser");
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
        assertEquals("User account deleted successfully.", statusLabel.getText());
    }

    @Test
    void testDeleteNonExistentUser() throws Exception {
        usernameField.setText("nonexistentUser");
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
        assertEquals("Failed to delete user account. User may not exist.", statusLabel.getText());
    }

    @Test
    void testCancelButton() throws Exception {
        Method cancelMethod = DeleteUserAccountController.class.getDeclaredMethod("handleCancelButtonAction", ActionEvent.class);
        cancelMethod.setAccessible(true);
        cancelMethod.invoke(controller, new ActionEvent());
        assertEquals("Account deletion canceled.", statusLabel.getText());
    }

    @Test
    void testHomeNavigation() {
        UserSession.initializeSession("testUser", "ADMIN", "testPage");
        assertDoesNotThrow(() -> {
            Method homeMethod = DeleteUserAccountController.class.getDeclaredMethod("handleHomeButtonAction", ActionEvent.class);
            homeMethod.setAccessible(true);
            homeMethod.invoke(controller, new ActionEvent());
        });
    }

    @Test
    void testUserNotFoundMessage() throws Exception {
        usernameField.setText("nonexistentUser");
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("deleteUserAccount", String.class);
        deleteMethod.setAccessible(true);
        boolean result = (boolean) deleteMethod.invoke(controller, "nonexistentUser");
        assertFalse(result);
        assertEquals("User not found. Account deletion failed.", statusLabel.getText());
    }

    @Test
    void testDatabaseErrorHandling() throws Exception {
        usernameField.setText("invalidUser@#$");
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("deleteUserAccount", String.class);
        deleteMethod.setAccessible(true);
        boolean result = (boolean) deleteMethod.invoke(controller, "invalidUser@#$");
        assertFalse(result);
    }

    @Test
    void testSuccessfulDeletionWithMultipleRoles() throws Exception {
        H2Database.addUser("testUserMultiRoles", "password", "email@test.com",
            "Test", "User", "STUDENT,ADMIN", false, "", false, false);
        usernameField.setText("testUserMultiRoles");
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
        assertEquals("User account deleted successfully.", statusLabel.getText());
    }

    @Test
    void testNavigationWithDifferentRoles() {
        String[] roles = {"admin", "instructor", "student"};
        for (String role : roles) {
            UserSession.initializeSession("testUser", role, "testPage");
            assertDoesNotThrow(() -> {
                Method homeMethod = DeleteUserAccountController.class.getDeclaredMethod("handleHomeButtonAction", ActionEvent.class);
                homeMethod.setAccessible(true);
                homeMethod.invoke(controller, new ActionEvent());
            });
        }
    }

    @Test
    void testUsernameFieldValidation() throws Exception {
        usernameField.setText(" ");
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
    }

    @Test
    void testDeleteUserWithSpecialCharacters() throws Exception {
        String specialUsername = "test@user123";
        H2Database.addUser(specialUsername, "password", "email@test.com",
            "Test", "User", "STUDENT", false, "", false, false);
        usernameField.setText(specialUsername);
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
        assertEquals("User account deleted successfully.", statusLabel.getText());
    }

    @Test
    void testNavigationWithNoSession() {
        assertDoesNotThrow(() -> {
            Method homeMethod = DeleteUserAccountController.class.getDeclaredMethod("handleHomeButtonAction", ActionEvent.class);
            homeMethod.setAccessible(true);
            homeMethod.invoke(controller, new ActionEvent());
        });
    }

    @Test
    void testDeleteActiveUser() throws Exception {
        H2Database.addUser("activeUser", "password", "email@test.com",
            "Test", "User", "STUDENT", true, "", false, false);
        usernameField.setText("activeUser");
        confirmationCheckBox.setSelected(true);
        Method deleteMethod = DeleteUserAccountController.class.getDeclaredMethod("handleDeleteButtonAction", ActionEvent.class);
        deleteMethod.setAccessible(true);
        deleteMethod.invoke(controller, new ActionEvent());
        assertEquals("User account deleted successfully.", statusLabel.getText());
    }

    @Test
    void testDeleteButtonDisabledState() throws Exception {
        confirmationCheckBox.setSelected(false);
        assertFalse(deleteButton.isDisabled());
    }

    @Test
    void testStatusLabelInitialState() {
        assertTrue(statusLabel.getText().isEmpty());
    }

    @Test
    void testNavigationWithPreviousPage() {
        UserSession.initializeSession("testUser", "ADMIN", "previousPage.fxml");
        assertDoesNotThrow(() -> {
            Method homeMethod = DeleteUserAccountController.class.getDeclaredMethod("handleHomeButtonAction", ActionEvent.class);
            homeMethod.setAccessible(true);
            homeMethod.invoke(controller, new ActionEvent());
        });
    }
}
