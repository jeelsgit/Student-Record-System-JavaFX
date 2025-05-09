package com.example.studentrecordsystem.dao;

import com.example.studentrecordsystem.model.Student;
import com.example.studentrecordsystem.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDAO {

    // --- READ ---
    /**
     * Retrieves all students from the database.
     * @return An ObservableList of Student objects.
     */
    public ObservableList<Student> getAllStudents() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        // SQL query to select all columns from the students table, ordered for consistency
        String sql = "SELECT student_id, first_name, last_name, email, major FROM students ORDER BY last_name, first_name";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection(); // Get connection from utility class
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            // Loop through the result set
            while (rs.next()) {
                // Create a new Student object for each row
                Student student = new Student(
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("major")
                );
                studentList.add(student); // Add the student to the list
            }
        } catch (SQLException e) {
            System.err.println("Error getting students from database: " + e.getMessage());
            // In a real app, you might show an error Alert here
            // e.printStackTrace(); // Print stack trace for debugging
        } finally {
            // Ensure resources are closed even if an error occurs
            DatabaseUtil.closeQuietly(rs);
            DatabaseUtil.closeQuietly(stmt);
            DatabaseUtil.closeQuietly(conn);
        }
        return studentList; // Return the list (might be empty if error or no data)
    }

    // --- CREATE ---
    /**
     * Adds a new student record to the database.
     * Handles potential SQLException (like duplicate primary key).
     * @param student The Student object containing the data to add.
     * @return true if the student was added successfully, false otherwise.
     */
    public boolean addStudent(Student student) {
        // SQL query with placeholders (?) for safe parameter insertion
        String sql = "INSERT INTO students(student_id, first_name, last_name, email, major) VALUES(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // Set the values for the placeholders
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getMajor());

            int affectedRows = pstmt.executeUpdate(); // Execute the insert statement
            return affectedRows > 0; // Return true if one row was affected (successful insertion)

        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            // This error often occurs if the student_id already exists (PRIMARY KEY constraint violation)
            // Consider adding more specific error feedback to the user based on the SQLException type/code
            return false; // Return false indicating failure
        } finally {
            DatabaseUtil.closeQuietly(pstmt);
            DatabaseUtil.closeQuietly(conn);
        }
    }

    // --- UPDATE ---
    /**
     * Updates an existing student record in the database.
     * Uses the student_id to identify the record to update.
     * @param student The Student object containing the updated data (including the original student_id).
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateStudent(Student student) {
        // SQL query to update fields based on the student_id
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, major = ? WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // Set the values for the placeholders
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            pstmt.setString(5, student.getStudentId()); // Use student_id in the WHERE clause

            int affectedRows = pstmt.executeUpdate(); // Execute the update statement
            return affectedRows > 0; // Return true if one row was affected

        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false; // Return false indicating failure
        } finally {
            DatabaseUtil.closeQuietly(pstmt);
            DatabaseUtil.closeQuietly(conn);
        }
    }

    // --- DELETE ---
    /**
     * Deletes a student record from the database based on the student ID.
     * @param studentId The ID of the student to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteStudent(String studentId) {
        // SQL query to delete a row based on student_id
        String sql = "DELETE FROM students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // Set the value for the placeholder in the WHERE clause
            pstmt.setString(1, studentId);

            int affectedRows = pstmt.executeUpdate(); // Execute the delete statement
            return affectedRows > 0; // Return true if one row was affected

        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false; // Return false indicating failure
        } finally {
            DatabaseUtil.closeQuietly(pstmt);
            DatabaseUtil.closeQuietly(conn);
        }
    }
}
