/**
 * <p> DeleteArticleTest Class </p>
 *
 * <p> Description: This class contains unit tests for verifying the deletion functionality 
 * of articles from the HelpArticlePage. It includes setup, teardown, and test methods to 
 * ensure the delete operation works as expected and data is correctly managed. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


package application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

class DeleteArticleTest {

    private HelpArticlePage page;

    // The ID for the article we'll be testing
    private final long TEST_ARTICLE_ID = 999L;
    private static final String DB_URL = "jdbc:h2:./data/articles/help_articles"; // Path to H2 database
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize the HelpArticlePage before each test
        page = new HelpArticlePage();
        
        // Create the table and set up the test data in the in-memory database
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS articles (" +
                    "id BIGINT PRIMARY KEY, " +
                    "level VARCHAR(20), " +
                    "title VARCHAR(255), " +
                    "description TEXT, " +
                    "keywords VARCHAR(255), " +
                    "body TEXT, " +
                    "isSensitive BOOLEAN, " +
                    "groups VARCHAR(255), " +
                    "referenceLinks VARCHAR(255));";
            statement.execute(createTableSQL);

            // Insert a mock article to test deletion
            String insertArticleSQL = "INSERT INTO articles (id, level, title, description, keywords, body, isSensitive, groups, referenceLinks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertArticleSQL)) {
                pstmt.setLong(1, TEST_ARTICLE_ID);
                pstmt.setString(2, "Beginner");
                pstmt.setString(3, "Test Article");
                pstmt.setString(4, "Test Description");
                pstmt.setString(5, "keyword1,keyword2");
                pstmt.setString(6, "Test Body");
                pstmt.setBoolean(7, false);
                pstmt.setString(8, "Java");
                pstmt.setString(9, "link1,link2");
                pstmt.executeUpdate();
            }
        }
    }

    @Test
    public void testDeleteArticle() throws Exception {
        // Arrange: Retrieve the article
        HelpArticle articleToDelete = getArticleById(TEST_ARTICLE_ID);
        assertNotNull(articleToDelete, "Article should be present before deletion");

        // Act: Perform the deletion 
        page.deleteArticle(articleToDelete);

        //Verify that the article no longer exists in the database
        HelpArticle deletedArticle = getArticleById(TEST_ARTICLE_ID);
        assertNull(deletedArticle, "Article should be deleted from the database after deletion");
    }

    private HelpArticle getArticleById(long articleId) throws SQLException {
        String query = "SELECT * FROM articles WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new HelpArticle(
                            rs.getLong("id"),
                            rs.getString("level"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("keywords"),
                            rs.getString("body"),
                            rs.getString("referenceLinks"),
                            rs.getBoolean("isSensitive"),
                            rs.getString("groups")
                    );
                }
                return null;
            }
        }
    }

//     Optionally clean up after each test if needed
     @AfterEach
     public void tearDown() throws SQLException {
         try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
              Statement statement = connection.createStatement()) {
             String deleteSQL = "DELETE FROM articles WHERE id = ?";
             try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
                 pstmt.setLong(1, TEST_ARTICLE_ID);
                 pstmt.executeUpdate();
             }
         }
     }
}
