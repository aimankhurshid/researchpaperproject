package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/projectdatabase";
    private static final String USER = "postgres";  // replace with your PostgreSQL username
    private static final String PASSWORD = "Aimankh.7";  // replace with your PostgreSQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}