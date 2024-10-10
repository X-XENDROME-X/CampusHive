/*******
 * <p> DatabaseInitializer Class </p>
 * 
 * <p> Description: This class is responsible for initializing the CSV-based database used in the Campus Hive application. It ensures that the necessary CSV files are created and ready for storing user-related data, handling the setup of the file structure and initial records if needed. This serves as a foundational component for ensuring smooth database operations within the application. </p>
 * 
 * <p> Copyright: Campus Hive Â© 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 1.002024-10-09 First version of this JavaFX project was made
 * 
 */

package application;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                email TEXT,
                first_name TEXT,
                middle_name TEXT,
                last_name TEXT,
                preferred_first_name TEXT,
                roles TEXT
            )
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
