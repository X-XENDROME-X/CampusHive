/*******
 * <p> CSVDatabase Class </p>
 * 
 * <p> Description: This class provides methods for managing user data in the Campus Hive application using CSV files. It supports adding users, retrieving user data, updating account status, and verifying login credentials. This class acts as a simple database layer for the application, storing user details such as username, password, and roles in a CSV file. </p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */


package application;

import java.io.*;
import java.util.*;

public class CSVDatabase {

    private static final String FILE_PATH = "users.csv";
    private static final String INVITATIONS_FILE_PATH = "invitations.csv";

    // Method to check if the users CSV file exists
    private static void checkCSVFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    // Method to check if the invitations CSV file exists
    private static void checkInvitationCSVFile() throws IOException {
        File file = new File(INVITATIONS_FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    // Method to add a new user to the users CSV, including roles
    public static void addUser(String username, String password, String firstName, String middleName, String lastName, String email, boolean isActive, String roles) throws IOException {
        checkCSVFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(username + "," + password + "," + firstName + "," + middleName + "," + lastName + "," + email + "," + isActive + "," + roles);
            writer.newLine();
        }
    }

    // Method to retrieve user data (returns list of all users)
    public static List<String[]> getUsers() {
        List<String[]> users = new ArrayList<>();
        try {
            checkCSVFile();
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                users.add(data);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

 // Method to update a user's roles in the users CSV
    public static void updateUserRoles(String username, String newRoles) throws IOException {
        List<String[]> users = getUsers(); // Retrieve the list of users
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

        for (String[] user : users) {
            if (user[0].equals(username)) {
                user[7] = newRoles; // Assuming roles are at index 7
            }
            writer.write(String.join(",", user) + "\n");
        }
        writer.close();
    }


    // Method to update user account setup
    public static void updateUserAccountSetup(String username) throws IOException {
        List<String[]> users = getUsers();
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

        for (String[] user : users) {
            if (user[0].equals(username)) {
                user[6] = "true"; // Set account setup status to true
            }
            writer.write(String.join(",", user) + "\n");
        }
        writer.close();
    }

    // Method to add an invitation code along with roles to the invitation CSV
    public static void addInvitationCode(String email, String invitationCode, String roles) throws IOException {
        checkInvitationCSVFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVITATIONS_FILE_PATH, true))) {
            writer.write(email + "," + invitationCode + "," + roles); // Saving email, code, and roles
            writer.newLine();
        }
    }
    
 // Method to update a user's password by email
    public static boolean updatePasswordByEmail(String email, String newPassword) throws IOException {
        List<String[]> users = getUsers(); // Retrieve the list of users
        boolean updated = false;

        // Create a new BufferedWriter to write back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] user : users) {
                if (user[5].equals(email)) { // Assuming email is at index 5
                    user[1] = newPassword; // Assuming password is at index 1
                    updated = true; // Mark that the password was updated
                }
                writer.write(String.join(",", user)); // Write back the user data
                writer.newLine();
            }
        }

        return updated; // Return true if the password was updated
    }

    public static boolean deleteUserByUsername(String username) {
        try {
            List<String[]> users = getUsers(); // Retrieve the list of users
            List<String[]> updatedUsers = new ArrayList<>();
            boolean found = false;

            for (String[] user : users) {
                if (user[0].equals(username)) { // Check if the username matches
                    found = true; // Mark as found
                    continue; // Skip this user to delete them
                }
                updatedUsers.add(user); // Keep other users
            }

            // Write back the updated list to the CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String[] user : updatedUsers) {
                    writer.write(String.join(",", user));
                    writer.newLine();
                }
            }

            return found; // Return true if a user was found and deleted
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return false on error
        }
    }

    // Method to check if an invitation code exists (without email)
    public static String getRoleByInvitationCode(String invitationCode) throws IOException {
        checkInvitationCSVFile();
        BufferedReader reader = new BufferedReader(new FileReader(INVITATIONS_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data[1].equals(invitationCode)) { // Checking only invitation code
                return data[2]; // Returning associated roles
            }
        }
        reader.close();
        return null; // Invitation code not found
    }

    // Method to remove used invitation code after validation
    public static void removeInvitationCode(String invitationCode) throws IOException {
        List<String[]> invitations = new ArrayList<>();
        checkInvitationCSVFile();
        BufferedReader reader = new BufferedReader(new FileReader(INVITATIONS_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (!data[1].equals(invitationCode)) { // Keep only unused codes
                invitations.add(data);
            }
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(INVITATIONS_FILE_PATH));
        for (String[] invitation : invitations) {
            writer.write(String.join(",", invitation));
            writer.newLine();
        }
        writer.close();
    }

    // Method to check login credentials
    public static String[] checkLogin(String username, String password) throws IOException {
        List<String[]> users = getUsers();
        for (String[] user : users) {
            if (user[0].equals(username) && user[1].equals(password)) {
                return user;
            }
        }
        return null; // User not found or invalid credentials
    }
}
