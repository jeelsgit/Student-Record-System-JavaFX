package com.example.studentrecordsystem;

import com.example.studentrecordsystem.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Student Record System");

        // Initialize Database (create table if needed)
        DatabaseUtil.initializeDatabase();

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            // Adjust the path if your FXML is elsewhere or you use modules
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/com/example/studentrecordsystem/MainView.fxml")));
            BorderPane rootLayout = loader.load(); // Use the correct root layout type

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app if needed (optional)
            // MainViewController controller = loader.getController();
            // controller.setMainApp(this); // If you add such a method

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions loading FXML
            // Show an error alert
        } catch (NullPointerException e) {
            System.err.println("Error: Could not find FXML file. Check the path.");
            e.printStackTrace();
            // Show an error alert
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
