package dao;

import model.Paper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaperDAO {

    // Method to search papers by partial match in title, abstract, or content
    public List<Paper> searchPapers(String query) throws SQLException {
        List<Paper> papers = new ArrayList<>();
        String sql = "SELECT * FROM papers WHERE title ILIKE ? OR abstract ILIKE ? OR content ILIKE ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String likeQuery = "%" + query + "%";
            statement.setString(1, likeQuery);
            statement.setString(2, likeQuery);
            statement.setString(3, likeQuery);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Paper paper = new Paper(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("abstract"),
                        resultSet.getString("content"),
                        resultSet.getInt("year"),
                        resultSet.getInt("author_id"),
                        null // Placeholder for author name
                );
                papers.add(paper);
            }
        }
        
        return papers;
    }

    // Method to add a new paper to the database
    public void addPaper(Paper paper) throws SQLException {
        String sql = "INSERT INTO papers (title, abstract, content, year, author_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, paper.getTitle());
            pstmt.setString(2, paper.getAbstract());
            pstmt.setString(3, paper.getContent());
            pstmt.setInt(4, paper.getYear());
            pstmt.setInt(5, paper.getAuthorId());
            pstmt.executeUpdate();
        }
    }

    // Method to get papers by author name
    public List<Paper> getPapersByAuthorName(String authorName) throws SQLException {
        List<Paper> papers = new ArrayList<>();
        String sql = "SELECT p.id, p.title, p.abstract, p.content, p.year, p.author_id, a.name AS author_name " +
                     "FROM papers p JOIN authors a ON p.author_id = a.id WHERE a.name ILIKE ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String likeQuery = "%" + authorName + "%";
            statement.setString(1, likeQuery);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Paper paper = new Paper(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("abstract"),
                        resultSet.getString("content"),
                        resultSet.getInt("year"),
                        resultSet.getInt("author_id"),
                        resultSet.getString("author_name") // Populate author name
                );
                papers.add(paper);
            }
        }
        
        return papers;
    }
}