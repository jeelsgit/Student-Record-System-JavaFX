module com.example.studentrecordsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.studentrecordsystem to javafx.fxml;
    opens com.example.studentrecordsystem.view to javafx.fxml;
    opens com.example.studentrecordsystem.model to javafx.base;

    exports com.example.studentrecordsystem;
}