/**
 * <p> AddRemoveUserRoleControllerTest Class </p>
 *
 * <p> Description: This class provides unit tests for the AddRemoveUserRoleController, ensuring 
 * that functionalities like adding roles, removing roles, error handling, and UI component 
 * interactions work as expected. </p>
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class AddRemoveUserRoleControllerTest {
    private AddRemoveUserRoleController controller;
    private Label errorMessageLabel;
    private Button homeButton;
    private Label heading;
    private ComboBox roleSelectionBox;
    private TextField usernameTextField;
    private Button addRoleButton;
    private Button removeRoleButton;

    @BeforeAll
    public static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void cleanupDatabase() throws SQLException {
        try {
            H2Database.deleteUserByUsername("testUser");
            H2Database.deleteUserByUsername("testUser2");
            H2Database.deleteUserByUsername("testUser3");
            H2Database.deleteUserByUsername("testUser4");
            H2Database.deleteUserByUsername("testUser5");
        } catch (SQLException e) {
            // Ignore if users don't exist
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new AddRemoveUserRoleController();
        
        errorMessageLabel = new Label();
        homeButton = new Button();
        heading = new Label();
        roleSelectionBox = new ComboBox<>();
        usernameTextField = new TextField();
        addRoleButton = new Button();
        removeRoleButton = new Button();

        Field errorLabel = AddRemoveUserRoleController.class.getDeclaredField("ErrorMessageLabel");
        Field homeBtn = AddRemoveUserRoleController.class.getDeclaredField("HomeButton");
        Field headingLabel = AddRemoveUserRoleController.class.getDeclaredField("Heading");
        Field comboBox = AddRemoveUserRoleController.class.getDeclaredField("RoleSelectionBox");
        Field usernameField = AddRemoveUserRoleController.class.getDeclaredField("UsernameTextField");
        Field addButton = AddRemoveUserRoleController.class.getDeclaredField("AddRoleButton");
        Field removeButton = AddRemoveUserRoleController.class.getDeclaredField("RemoveRoleButton");

        errorLabel.setAccessible(true);
        homeBtn.setAccessible(true);
        headingLabel.setAccessible(true);
        comboBox.setAccessible(true);
        usernameField.setAccessible(true);
        addButton.setAccessible(true);
        removeButton.setAccessible(true);

        errorLabel.set(controller, errorMessageLabel);
        homeBtn.set(controller, homeButton);
        headingLabel.set(controller, heading);
        comboBox.set(controller, roleSelectionBox);
        usernameField.set(controller, usernameTextField);
        addButton.set(controller, addRoleButton);
        removeButton.set(controller, removeRoleButton);

        Method initMethod = AddRemoveUserRoleController.class.getDeclaredMethod("initialize");
        initMethod.setAccessible(true);
        initMethod.invoke(controller);
    }

    @Test
    void testHomeButtonNavigation() {
        UserSession.initializeSession("testUser", "ADMIN", "testPage");
        assertDoesNotThrow(() -> {
            Method homeButtonMethod = AddRemoveUserRoleController.class.getDeclaredMethod("handleHomeButtonAction", ActionEvent.class);
            homeButtonMethod.setAccessible(true);
            homeButtonMethod.invoke(controller, new ActionEvent());
        });
    }

    @Test
    void testAddRoleWithEmptyFields() throws Exception {
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertEquals("Username and Role Selection cannot be empty.", errorMessageLabel.getText());
    }

    @Test
    void testRemoveRoleWithEmptyFields() throws Exception {
        Method removeRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("RemoveRole");
        removeRoleMethod.setAccessible(true);
        removeRoleMethod.invoke(controller);
        assertEquals("Username and Role Selection cannot be empty.", errorMessageLabel.getText());
    }

    @Test
    void testRoleSelectionBoxInitialization() {
        assertTrue(roleSelectionBox.getItems().contains("admin"));
        assertTrue(roleSelectionBox.getItems().contains("student"));
        assertTrue(roleSelectionBox.getItems().contains("instructor"));
        assertEquals("Select Role", roleSelectionBox.getPromptText());
    }

    @Test
    void testAddRoleSuccess() throws Exception {
        H2Database.addUser("testUser", "password", "email@test.com",
            "Test", "User", "STUDENT", false, "", false, false);
        usernameTextField.setText("testUser");
        roleSelectionBox.setValue("admin");
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertEquals("Role added successfully.", errorMessageLabel.getText());
    }

    @Test
    void testAddDuplicateRole() throws Exception {
        H2Database.addUser("testUser2", "password", "email@test.com",
            "Test", "User", "admin", false, "admin", false, false);
        usernameTextField.setText("testUser2");
        roleSelectionBox.setValue("admin");
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertEquals("Role already exists.", errorMessageLabel.getText());
    }

    @Test
    void testRemoveRoleSuccess() throws Exception {
        H2Database.addUser("testUser3", "password", "email@test.com",
            "Test", "User", "admin", false, "admin", false, false);
        usernameTextField.setText("testUser3");
        roleSelectionBox.setValue("admin");
        Method removeRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("RemoveRole");
        removeRoleMethod.setAccessible(true);
        removeRoleMethod.invoke(controller);
        assertEquals("Role removed successfully.", errorMessageLabel.getText());
    }

    @Test
    void testRemoveNonexistentRole() throws Exception {
        H2Database.addUser("testUser", "password", "email@test.com",
            "Test", "User", "student", false, "", false, false);
        usernameTextField.setText("testUser");
        roleSelectionBox.setValue("admin");
        Method removeRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("RemoveRole");
        removeRoleMethod.setAccessible(true);
        removeRoleMethod.invoke(controller);
        assertEquals("Role does not exist.", errorMessageLabel.getText());
    }

    @Test
    void testDatabaseError() throws Exception {
        usernameTextField.setText("");
        roleSelectionBox.setValue("admin");
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertTrue(errorMessageLabel.getText().contains("cannot be empty"));
    }

    @Test
    void testSpecialCharactersInUsername() throws Exception {
        String specialUsername = "test@user#123";
        H2Database.addUser(specialUsername, "password", "email@test.com",
            "Test", "User", "", false, "", false, false);
        usernameTextField.setText(specialUsername);
        roleSelectionBox.setValue("admin");
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertEquals("Role added successfully.", errorMessageLabel.getText());
    }

    @Test
    void testMultipleRoleAssignment() throws Exception {
        H2Database.addUser("testUser4", "password", "email@test.com",
            "Test", "User", "student", false, "student", false, false);
        usernameTextField.setText("testUser4");
        roleSelectionBox.setValue("admin");
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertEquals("Role added successfully.", errorMessageLabel.getText());
    }

    @Test
    void testEmptyRoleString() throws Exception {
        H2Database.addUser("testUser5", "password", "email@test.com",
            "Test", "User", "", false, "", false, false);
        usernameTextField.setText("testUser5");
        roleSelectionBox.setValue("admin");
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertEquals("Role added successfully.", errorMessageLabel.getText());
    }

    @Test
    void testSQLExceptionHandling() throws Exception {
        String nonExistentUser = "nonexistentuser123";
        try {
            H2Database.deleteUserByUsername(nonExistentUser);
        } catch (SQLException e) {
            // Ignore if user doesn't exist
        }
        usernameTextField.setText(nonExistentUser);
        roleSelectionBox.setValue("admin");
        Method addRoleMethod = AddRemoveUserRoleController.class.getDeclaredMethod("AddRole");
        addRoleMethod.setAccessible(true);
        addRoleMethod.invoke(controller);
        assertTrue(errorMessageLabel.getText().contains("error"));
    }

    @Test
    void testUsernameFieldInput() {
        usernameTextField.setText("testUser");
        assertEquals("testUser", usernameTextField.getText().trim());
    }

    @Test
    void testRoleSelectionBoxSelection() {
        roleSelectionBox.setValue("admin");
        assertEquals("Admin", roleSelectionBox.getValue());
    }

    @Test
    void testErrorMessageDisplay() {
        errorMessageLabel.setText("Test error message");
        assertEquals("Test error message", errorMessageLabel.getText());
    }

    @Test
    void testHeadingLabel() {
        heading.setText("Role Management");
        assertEquals("Role Management", heading.getText());
    }
}
