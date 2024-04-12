package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CatManagementUI extends Application {
    private static final String DB_URL = "jdbc:sqlite:./cats.db";
    private Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Установка соединения с базой данных
        connection = DriverManager.getConnection(DB_URL);

        primaryStage.setTitle("Cat Management System");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Labels
        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");
        Label breedIdLabel = new Label("Breed ID:");

        // Text fields
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField breedIdField = new TextField();

        // Buttons
        Button addButton = new Button("Add Cat");
        Button displayButton = new Button("Display Cats");

        // Add Cat Button Click Event
        addButton.setOnAction(event -> {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            int breedId = Integer.parseInt(breedIdField.getText());
            try {
                CatManagementApp app = new CatManagementApp(connection);
                app.addCat(name, age, breedId);
                clearFields(nameField, ageField, breedIdField);
                displayAlert("Cat added successfully!", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                displayAlert("Error adding cat: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Display Cats Button Click Event
        displayButton.setOnAction(event -> {
            try {
                CatManagementApp app = new CatManagementApp(connection);
                app.displayAllCats();
            } catch (SQLException e) {
                displayAlert("Error displaying cats: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Add components to grid
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(ageLabel, 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(breedIdLabel, 0, 2);
        grid.add(breedIdField, 1, 2);
        grid.add(addButton, 0, 3);
        grid.add(displayButton, 1, 3);

        // Set scene
        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Closing the connection when the application is exited
        primaryStage.setOnCloseRequest(event -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void displayAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
