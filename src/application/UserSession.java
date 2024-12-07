/**
 * <p> UserSession Class </p>
 *
 * <p> Description: This class handles the user session management within the application,
 * including functionalities for maintaining user information such as username, role, 
 * and email. It implements the Singleton pattern to ensure only one instance of 
 * the user session exists during the application lifecycle. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


package application;
import java.util.Stack;

// Cache Setup For User Control / Session
public class UserSession {
    private static UserSession instance;
    private Stack<String> navigationHistory = new Stack<>();
    private String username;
    private String role;
    private String email;

    private UserSession(String username, String role, String email) {
        this.username = username;
        this.role = role;
        this.email = email;
    }

    // Initialize the UserSession instance
    public static void initializeSession(String username, String role, String email) {
        if (instance == null) {
            instance = new UserSession(username, role, email);
        }
    }

    // Get the existing instance
    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Session has not been initialized. Call initializeSession first.");
        }
        return instance;
    }

    public void addPageToHistory(String page) {
        System.out.println("Navigating from: " + page);
        navigationHistory.push(page);
    }


    public String getPreviousPage() {
        return navigationHistory.isEmpty() ? null : navigationHistory.pop();
    }

    public boolean hasPreviousPage() {
        return !navigationHistory.isEmpty();
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void cleanUserSession() {
        instance = null;
        navigationHistory.clear();
    }
}
