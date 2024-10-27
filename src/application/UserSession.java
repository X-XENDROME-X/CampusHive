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
