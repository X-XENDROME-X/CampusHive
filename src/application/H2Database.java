/**
 * <p> H2Database Class </p>
 *
 * <p> Description: This class manages the interaction with an H2 database, handling operations
 * related to users and invitations, including adding, updating, deleting users, and managing 
 * invitation codes. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 2.00 2024-10-29 Second version of this JavaFX project was made
 */

package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2Database {

    private static final String DB_URL = "jdbc:h2:./data/users/userdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(255) PRIMARY KEY, " +
                    "password VARCHAR(255), " +
                    "firstName VARCHAR(255), " +
                    "middleName VARCHAR(255), " +
                    "lastName VARCHAR(255), " +
                    "email VARCHAR(255), " +
                    "isActive BOOLEAN, " +
                    "roles VARCHAR(255))";
            statement.execute(createUserTable);

            String createInvitationTable = "CREATE TABLE IF NOT EXISTS invitations (" +
                    "email VARCHAR(255), " +
                    "invitationCode VARCHAR(255) PRIMARY KEY, " +
                    "roles VARCHAR(255))";
            statement.execute(createInvitationTable);
        }
    }

    public static void addUser(String username, String password, String firstName, String middleName, String lastName, String email, boolean isActive, String roles) throws SQLException {
        String insertUserQuery = "INSERT INTO users (username, password, firstName, middleName, lastName, email, isActive, roles) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, middleName);
            preparedStatement.setString(5, lastName);
            preparedStatement.setString(6, email);
            preparedStatement.setBoolean(7, isActive);
            preparedStatement.setString(8, roles);
            preparedStatement.executeUpdate();
        }
    }

    public static List<String[]> getUsers() throws SQLException {
        List<String[]> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM users";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                String[] user = {
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("firstName"),
                        resultSet.getString("middleName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        String.valueOf(resultSet.getBoolean("isActive")),
                        resultSet.getString("roles")
                };
                users.add(user);
            }
        }

        return users;
    }

    public static boolean checkForAdminUser() {
        String query = "SELECT COUNT(*) FROM users WHERE roles = 'admin'";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;  
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  
    }

    public static void updateUser(String username, String firstName, String middleName, String lastName, String email, boolean isActive, String roles) throws SQLException {

        String sql = "UPDATE users SET firstName = ?, middleName = ?, lastName = ?, email = ?, isActive = ?, roles = ? WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, firstName);

            if (middleName == null || middleName.isEmpty()) {
                pstmt.setNull(2, java.sql.Types.VARCHAR); 
            } else {
                pstmt.setString(2, middleName); 
            }

            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setBoolean(5, isActive);
            pstmt.setString(6, roles);
            pstmt.setString(7, username);  

            pstmt.executeUpdate();
        }
    }

    public static void updateUserRoles(String username, String newRoles) throws SQLException {
        String updateUserRolesQuery = "UPDATE users SET roles = ? WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateUserRolesQuery)) {

            preparedStatement.setString(1, newRoles);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean userExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  
            }
        }
        return false;
    }

    public static void updateUserAccountSetup(String username) throws SQLException {
        String updateUserQuery = "UPDATE users SET isActive = true WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateUserQuery)) {

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
    }

    public static void addInvitationCode(String email, String invitationCode, String roles) throws SQLException {
        String insertInvitationQuery = "INSERT INTO invitations (email, invitationCode, roles) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertInvitationQuery)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, invitationCode);
            preparedStatement.setString(3, roles);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean updatePasswordByEmail(String email, String newPassword) throws SQLException {
        String updatePasswordQuery = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updatePasswordQuery)) {

            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; 
        }
    }

    public static boolean deleteUserByUsername(String username) throws SQLException {
        String deleteUserQuery = "DELETE FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteUserQuery)) {

            preparedStatement.setString(1, username);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0; 
        }
    }

    public static String getRoleByInvitationCode(String invitationCode) throws SQLException {
        String selectQuery = "SELECT roles FROM invitations WHERE invitationCode = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, invitationCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("roles");
            } else {
                return null; 
            }
        }
    }

    public static void removeInvitationCode(String invitationCode) throws SQLException {
        String deleteInvitationQuery = "DELETE FROM invitations WHERE invitationCode = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteInvitationQuery)) {

            preparedStatement.setString(1, invitationCode);
            preparedStatement.executeUpdate();
        }
    }

    public static String[] checkLogin(String username, String password) throws SQLException {
        String selectQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String[] user = {
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("firstName"),
                        resultSet.getString("middleName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        String.valueOf(resultSet.getBoolean("isActive")),
                        resultSet.getString("roles")
                };
                return user;
            } else {
                return null; 
            }
        }
    }
}
