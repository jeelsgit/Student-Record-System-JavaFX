package com.example.studentrecordsystem.view; // Use your correct package

import com.example.studentrecordsystem.dao.StudentDAO;
import com.example.studentrecordsystem.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // Import FilteredList
import javafx.collections.transformation.SortedList;   // Import SortedList
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class MainViewController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> studentIdColumn;
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, String> majorColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    // --- Add Search Field ---
    @FXML
    private TextField searchField;
    // ------------------------

    private StudentDAO studentDAO;
    // --- Modify Data Lists for Filtering ---
    private ObservableList<Student> studentData; // Master list
    private FilteredList<Student> filteredData;  // Filtered view
    private SortedList<Student> sortedData;      // Sorted view (bound to table)
    // ----------------------------------------


    public MainViewController() {
        studentDAO = new StudentDAO();
    }

    @FXML
    private void initialize() {
        // --- Column Setup ---
        // Standard setup for most columns
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));

        // --- Custom Setup for Student ID Column with Natural Sorting ---
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        // Add a custom comparator for natural sorting attempt
        studentIdColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1 == null && s2 == null) return 0;
                if (s1 == null) return -1;
                if (s2 == null) return 1;

                // Attempt to extract numeric and non-numeric parts
                String numStr1 = s1.replaceAll("[^0-9]", ""); // Get only digits
                String numStr2 = s2.replaceAll("[^0-9]", "");
                String alphaStr1 = s1.replaceAll("[0-9]", ""); // Get only non-digits
                String alphaStr2 = s2.replaceAll("[0-9]", "");

                // Compare non-numeric parts first (e.g., 'S' in 'S100')
                int alphaCompare = alphaStr1.compareToIgnoreCase(alphaStr2);
                if (alphaCompare != 0) {
                    return alphaCompare;
                }

                // If non-numeric parts are the same, compare numeric parts
                if (!numStr1.isEmpty() && !numStr2.isEmpty()) {
                    try {
                        long num1 = Long.parseLong(numStr1);
                        long num2 = Long.parseLong(numStr2);
                        if (num1 != num2) {
                            return Long.compare(num1, num2); // Numerical comparison
                        }
                        // If numbers are also the same, fall through to full string compare
                    } catch (NumberFormatException e) {
                        // One or both numeric parts couldn't be parsed as long, fall back
                    }
                } else if (!numStr1.isEmpty()) { // s1 has number, s2 doesn't
                    return 1; // Typically number comes after letter part if prefixes match
                } else if (!numStr2.isEmpty()) { // s2 has number, s1 doesn't
                    return -1;
                }
                // If all else fails (e.g., both non-numeric, or parsing failed), compare original strings
                return s1.compareToIgnoreCase(s2);
            }
        });
        // -------------------------------------------------------------

        // --- Initialize Lists and Filtering Logic ---
        studentData = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(studentData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Perform null checks before accessing fields
                return (student.getStudentId() != null && student.getStudentId().toLowerCase().contains(lowerCaseFilter)) ||
                        (student.getFirstName() != null && student.getFirstName().toLowerCase().contains(lowerCaseFilter)) ||
                        (student.getLastName() != null && student.getLastName().toLowerCase().contains(lowerCaseFilter)) ||
                        (student.getEmail() != null && student.getEmail().toLowerCase().contains(lowerCaseFilter)) ||
                        (student.getMajor() != null && student.getMajor().toLowerCase().contains(lowerCaseFilter));
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(studentTable.comparatorProperty());
        studentTable.setItems(sortedData);
        // ----------------------------------------

        // --- Load initial data ---
        loadStudentData();
        // ----------------------------------------

        // --- Selection Listener and Button States ---
        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleSelectionChange(newValue));
        handleSelectionChange(null); // Disable edit/delete initially
        // ----------------------------------------

        // --- Tooltips ---
        addButton.setTooltip(new Tooltip("Add a new student record"));
        editButton.setTooltip(new Tooltip("Edit the selected student record"));
        deleteButton.setTooltip(new Tooltip("Delete the selected student record"));
        searchField.setTooltip(new Tooltip("Filter students by ID, Name, Email, or Major"));
        // ----------------------------------------
    }


    /**
     * Loads student data from the DAO into the master studentData list.
     * The table updates automatically because it's bound to the SortedList
     * which wraps the FilteredList which wraps studentData.
     */
    private void loadStudentData() {
        // Fetch fresh data
        ObservableList<Student> freshData = studentDAO.getAllStudents();
        // Update the source list - this triggers updates in Filtered/Sorted lists
        studentData.setAll(freshData);
    }


    // --- handleSelectionChange (Same as before) ---
    private void handleSelectionChange(Student selectedStudent) {
        boolean studentIsSelected = (selectedStudent != null);
        editButton.setDisable(!studentIsSelected);
        deleteButton.setDisable(!studentIsSelected);
    }

    // --- handleAddStudent ---
    // Now just needs to call loadStudentData() after successful add
    @FXML
    private void handleAddStudent() {
        Student tempStudent = new Student();
        boolean okClicked = showStudentEditDialog(tempStudent, "Add Student");
        if (okClicked) {
            if (studentDAO.addStudent(tempStudent)) {
                loadStudentData(); // Reload all data to update the master list
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add student. Student ID might already exist.");
            }
        }
    }

    // --- handleEditStudent ---
    // Now just needs to call loadStudentData() after successful update
    @FXML
    private void handleEditStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean okClicked = showStudentEditDialog(selectedStudent, "Edit Student");
            if (okClicked) {
                if(studentDAO.updateStudent(selectedStudent)) {
                    loadStudentData(); // Reload all data to update the master list
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update student.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student in the table to edit.");
        }
    }

    // --- handleDeleteStudent ---
    // Now just needs to call loadStudentData() after successful delete
    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Delete Student Record");
            confirmationAlert.setContentText("Are you sure you want to delete the student: " +
                    selectedStudent.getFirstName() + " " + selectedStudent.getLastName() + "?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (studentDAO.deleteStudent(selectedStudent.getStudentId())) {
                    loadStudentData(); // Reload all data to update the master list
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete student.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student in the table to delete.");
        }
    }

    // --- showStudentEditDialog (Same as before) ---
    public boolean showStudentEditDialog(Student student, String title) {
        try {
            FXMLLoader loader = new FXMLLoader();
            // Ensure this path is correct for your structure
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/com/example/studentrecordsystem/StudentEditDialog.fxml")));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            // dialogStage.initOwner(searchField.getScene().getWindow()); // Set owner if desired
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            StudentEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the edit dialog: " + e.getMessage());
            return false;
        } catch (NullPointerException e) {
            System.err.println("Error: Could not find StudentEditDialog.fxml file. Check the path.");
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not find the edit dialog layout file.");
            return false;
        }
    }

    // --- showAlert (Same as before) ---
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- refreshTable method is no longer needed as updates happen via list binding ---
    // private void refreshTable() { ... } // REMOVE this method
}