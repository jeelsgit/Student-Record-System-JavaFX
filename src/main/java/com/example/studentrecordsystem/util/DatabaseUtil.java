package com.example.studentrecordsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    // Database file name (will be created in the project root directory)
    private static final String DB_NAME = "student_records.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    // Initialize database - create table if it doesn't exist
    public static void initializeDatabase() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS students (" +
                " student_id TEXT PRIMARY KEY NOT NULL," +
                " first_name TEXT NOT NULL," +
                " last_name TEXT NOT NULL," +
                " email TEXT," +
                " major TEXT" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Create table
            stmt.execute(createTableSql);
            System.out.println("Database initialized successfully (Table 'students' checked/created).");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            // Handle exception appropriately - maybe exit app if DB is crucial
        }
    }

    // Get a database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Ensure the JDBC driver is loaded. Usually not needed with modern JDBC drivers,
            // but good practice for older systems or specific configurations.
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(CONNECTION_STRING);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            throw new SQLException("SQLite JDBC driver not found.", e);
        }
    }

    // Optional: Method to close resources quietly
    public static void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Log or ignore
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
}
