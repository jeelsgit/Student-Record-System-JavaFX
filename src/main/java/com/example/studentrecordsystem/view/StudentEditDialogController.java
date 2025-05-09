package com.example.studentrecordsystem.view;

import com.example.studentrecordsystem.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentEditDialogController {

    @FXML
    private TextField studentIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField majorField;

    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;
    private boolean isEditMode = false; // Flag to know if we are editing

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization if needed (e.g., add listeners for validation)
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the student to be edited in the dialog.
     *
     * @param student
     */
    public void setStudent(Student student) {
        this.student = student;

        if (student.getStudentId() != null && !student.getStudentId().isEmpty()) {
            // If student has an ID, we are likely in Edit mode
            isEditMode = true;
            studentIdField.setText(student.getStudentId());
            studentIdField.setEditable(false); // Don't allow editing the primary key
            firstNameField.setText(student.getFirstName());
            lastNameField.setText(student.getLastName());
            emailField.setText(student.getEmail());
            majorField.setText(student.getMajor());
        } else {
            // Add mode
            isEditMode = false;
            studentIdField.setEditable(true);
        }
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleSave() {
        if (isInputValid()) {
            // Update the student object with data from the fields
            student.setStudentId(studentIdField.getText());
            student.setFirstName(firstNameField.getText());
            student.setLastName(lastNameField.getText());
            student.setEmail(emailField.getText());
            student.setMajor(majorField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields using specific patterns.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        // Define validation patterns
        String namePattern = "^[a-zA-Z\\s'-]+$"; // Allows letters (upper/lower), spaces, apostrophe, hyphen
        // Basic email pattern - allows most common formats but not fully RFC compliant
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        // Simple ID pattern - allows letters and numbers, at least one character
        String idPattern = "^[a-zA-Z0-9]+$";
        // Simple major pattern - avoid only numbers/dots, allow most other things (adjust if needed)
        String majorAvoidOnlyNumbersPattern = "^(?!^\\d*\\.?\\d*$).*$"; // Asserts string is not just digits/dot


        // 1. Student ID Validation (Not Empty + Pattern if adding)
        String studentId = studentIdField.getText();
        if (studentId == null || studentId.trim().isEmpty()) {
            errorMessage += "Student ID cannot be empty!\n";
        } else if (studentIdField.isEditable() && !studentId.trim().matches(idPattern)) {
            // Only check pattern in Add mode (when field is editable)
            errorMessage += "Student ID must contain only letters and numbers!\n";
        }

        // 2. First Name Validation (Not Empty + Pattern)
        String firstName = firstNameField.getText();
        if (firstName == null || firstName.trim().isEmpty()) {
            errorMessage += "First name cannot be empty!\n";
        } else if (!firstName.trim().matches(namePattern)) {
            errorMessage += "First name contains invalid characters (use only letters, spaces, ', -).\n";
        }

        // 3. Last Name Validation (Not Empty + Pattern)
        String lastName = lastNameField.getText();
        if (lastName == null || lastName.trim().isEmpty()) {
            errorMessage += "Last name cannot be empty!\n";
        } else if (!lastName.trim().matches(namePattern)) {
            errorMessage += "Last name contains invalid characters (use only letters, spaces, ', -).\n";
        }

        // 4. Email Validation (Optional Field + Pattern Check if not empty)
        String email = emailField.getText();
        if (email != null && !email.trim().isEmpty() && !email.trim().matches(emailPattern)) {
            errorMessage += "Invalid email address format!\n";
        }

        // 5. Major Validation (Optional Field + Pattern Check if not empty)
        String major = majorField.getText();
        if (major != null && !major.trim().isEmpty() && !major.trim().matches(majorAvoidOnlyNumbersPattern)) {
            errorMessage += "Major cannot consist only of numbers or be empty if typed.\n";
            // Or add a more specific pattern if required, e.g., like namePattern:
            // if (major != null && !major.trim().isEmpty() && !major.trim().matches(namePattern)) {
            //     errorMessage += "Major contains invalid characters!\n";
            // }
        }


        // --- Check if any errors were found ---
        if (errorMessage.isEmpty()) {
            return true; // All checks passed
        } else {
            // Show the accumulated error messages.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage); // Ensure alert is on top of the dialog
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct the following errors:");
            // Use a TextArea for potentially long error messages
            TextArea textArea = new TextArea(errorMessage);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setContent(textArea);
            alert.setResizable(true); // Allow resizing if message is long

            alert.showAndWait();

            return false; // Validation failed
        }
    }
}
