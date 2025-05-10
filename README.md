# Student Record Management System (JavaFX)

A desktop application for managing basic student records, built with JavaFX, Java, Apache Maven, and SQLite. This project demonstrates CRUD (Create, Read, Update, Delete) operations, dynamic search functionality, input validation, and a clean user interface.

## Screenshots

![Main Window]([URL_TO_YOUR_MAIN_WINDOW_SCREENSHOT.png](https://github.com/jeelsgit/Student-Record-System-JavaFX/blob/master/screenshots/Add_Edit_Dialog.png))
*Main application window displaying student records and search functionality.*

![Add/Edit Dialog]([URL_TO_YOUR_ADD_EDIT_DIALOG_SCREENSHOT.png](https://github.com/jeelsgit/Student-Record-System-JavaFX/blob/master/screenshots/Main_Window.png))
*Dialog for adding or editing student details with input validation.*

## Features

*   **CRUD Operations:**
    *   **Create:** Add new student records with ID, name, email, and major.
    *   **Read:** View all student records in a sortable and filterable table.
    *   **Update:** Modify existing student information.
    *   **Delete:** Remove student records from the system with confirmation.
*   **Dynamic Search:** Filter students in real-time by ID, first name, last name, email, or major.
*   **Input Validation:** Ensures data integrity for required fields and correct formats (e.g., for names and email).
*   **User-Friendly Interface:** Clean and intuitive GUI built with JavaFX.
*   **Data Persistence:** Student data is stored locally in an SQLite database file (`student_records.db`).
*   **Accessibility:** Tooltips for interactive elements.
*   **Natural Sorting:** Student ID column supports natural sorting for alphanumeric IDs.

## Technologies Used

*   **Language:** Java (JDK 21)
*   **GUI Framework:** JavaFX (Version 21, via OpenJFX libraries)
*   **UI Definition:** FXML
*   **Database:** SQLite
*   **Database Connectivity:** JDBC (via SQLite JDBC Driver - Version 3.49.0.0)
*   **Build Tool:** Apache Maven
*   **IDE:** IntelliJ IDEA (Version 2024.3.5)
*   **Optional Tools:** SceneBuilder (for FXML design), DB Browser for SQLite (for database management)

## Setup & Running

### Prerequisites

*   Java Development Kit (JDK) 21 or later.
*   Apache Maven (if building from source).

### Option 1: Running from IDE (Recommended for Development)

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/jeelsgit/Student-Record-System-JavaFX
    ```
2.  **Open in IntelliJ IDEA:**
    *   Open IntelliJ IDEA.
    *   Select "Open" or "Import Project" and navigate to the cloned project directory.
    *   Allow IntelliJ to import the project as a Maven project and resolve dependencies.
3.  **Run the Application:**
    *   Locate the `MainApp.java` file (usually in `src/main/java/com/example/studentrecordsystem/`).
    *   Right-click on `MainApp.java` and select "Run 'MainApp.main()'".
    *   The SQLite database file (`student_records.db`) will be created in the project's root directory on first launch if it doesn't exist.

## Project Structure (Overview)
