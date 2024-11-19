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
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

package application;

// Cache Setup For User Control / Session

public class UserSession {

    private static UserSession instance;
    private String username;
    private String role;
    private String email;

    private UserSession(String username, String role, String email) {
        this.username = username;
        this.role = role;
        this.email = email;
    }

    public static UserSession getInstance(String username, String role, String email) {
        if (instance == null) {
            instance = new UserSession(username, role, email);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
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
        instance = null; // Clear session when the user logs out
    }
}
