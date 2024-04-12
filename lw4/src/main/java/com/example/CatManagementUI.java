package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CatManagementUI extends Application {
    private static final String DB_URL = "jdbc:sqlite:./cats.db";
    private Connection connection;
    private CatManagementApp catManagementApp;
    private ComboBox<String> breedComboBox;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Установка соединения с базой данных
        connection = DriverManager.getConnection(DB_URL);
        catManagementApp = new CatManagementApp(connection);

        primaryStage.setTitle("Cat Management System");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        ListView<String> catListView = new ListView<>();

        // Load breeds from database and populate ComboBox
        breedComboBox = new ComboBox<>();
        loadBreedsIntoComboBox();

        // Labels and TextFields for adding new cat
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();

        // Add Cat Button
        Button addButton = new Button("Add Cat");
        addButton.setOnAction(event -> {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String selectedBreed = breedComboBox.getValue();
            try {
                int breedId = catManagementApp.getBreedIdByName(selectedBreed);
                catManagementApp.addCat(name, age, breedId);
                displayAlert("Cat added successfully!", Alert.AlertType.INFORMATION);
                refreshCatListView(catListView); // Refresh ListView after adding a cat
            } catch (SQLException e) {
                displayAlert("Error adding cat: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Display Cats Button
        Button displayButton = new Button("Display Cats");
        displayButton.setOnAction(event -> {
            refreshCatListView(catListView);
        });

        // Add components to VBox
        vbox.getChildren().addAll(
                catListView,
                new Separator(),
                nameLabel, nameField,
                ageLabel, ageField,
                new Label("Breed:"), breedComboBox,
                addButton, displayButton
        );

        // Set scene
        Scene scene = new Scene(vbox, 500, 400);
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

    private void loadBreedsIntoComboBox() {
        try {
            ResultSet resultSet = catManagementApp.getAllBreeds();
            breedComboBox.getItems().clear();
            while (resultSet.next()) {
                String breedName = resultSet.getString("breed_name");
                breedComboBox.getItems().add(breedName);
            }
        } catch (SQLException e) {
            displayAlert("Error loading breeds: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void displayAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshCatListView(ListView<String> catListView) {
        try {
            ResultSet resultSet = catManagementApp.getAllCatsWithBreeds();
            catListView.getItems().clear();
            while (resultSet.next()) {
                int catId = resultSet.getInt("cat_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String breedName = resultSet.getString("breed_name");
                String description = resultSet.getString("description");
                String catInfo = String.format("Cat ID: %d, Name: %s, Age: %d, Breed: %s (%s)",
                        catId, name, age, breedName, description);
                catListView.getItems().add(catInfo);
            }
        } catch (SQLException e) {
            displayAlert("Error displaying cats: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
