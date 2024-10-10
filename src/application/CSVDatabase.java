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
    public static List<String[]> getUsers() throws IOException {
        checkCSVFile();
        BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
        String line;
        List<String[]> users = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            users.add(data);
        }
        reader.close();
        return users;
    }

    // Method to update user account setup (e.g., after finishing account setup)
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

    // Method to check if an invitation code exists and is valid (one-time use)
    public static boolean checkInvitationCode(String email, String invitationCode) throws IOException {
        checkInvitationCSVFile();
        BufferedReader reader = new BufferedReader(new FileReader(INVITATIONS_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data[0].equals(email) && data[1].equals(invitationCode)) {
                return true; // Code is valid
            }
        }
        reader.close();
        return false; // Invalid code
    }

    // Method to remove used invitation code after validation
    public static void removeInvitationCode(String email, String invitationCode) throws IOException {
        List<String[]> invitations = new ArrayList<>();
        checkInvitationCSVFile();
        BufferedReader reader = new BufferedReader(new FileReader(INVITATIONS_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (!(data[0].equals(email) && data[1].equals(invitationCode))) {
                invitations.add(data); // Keep only the unused codes
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
