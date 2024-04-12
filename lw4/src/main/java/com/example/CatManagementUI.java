package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private ComboBox<String> breedComboBox, breedComboBox2;
    private ComboBox<Integer> catIdComboBox;
    private ListView<String> catListView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Установка соединения с базой данных
        connection = DriverManager.getConnection(DB_URL);
        catManagementApp = new CatManagementApp(connection);

        primaryStage.setTitle("Cat Management System");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        catListView = new ListView<>();

        // Load breeds from database and populate ComboBox
        breedComboBox = new ComboBox<>();
        breedComboBox2 = new ComboBox<>();
        loadBreedsIntoComboBox();
        
        // Load cat IDs from database and populate ComboBox
        catIdComboBox = new ComboBox<>();
        loadCatIdsIntoComboBox();

        // Labels and TextFields for adding new cat
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();

        // Add Breed section
        Label addBreedLabel = new Label("Add New Breed");
        Label breedNameLabel = new Label("Breed Name:");
        TextField breedNameField = new TextField();
        Label breedDescriptionLabel = new Label("Description:");
        TextField breedDescriptionField = new TextField();

        Button addBreedButton = new Button("Add Breed");
        addBreedButton.setOnAction(event -> {
            String breedName = breedNameField.getText();
            String description = breedDescriptionField.getText();
            try {
                catManagementApp.addBreed(breedName, description);
                displayAlert("Breed added successfully!", Alert.AlertType.INFORMATION);
                loadBreedsIntoComboBox(); // Refresh ComboBox after adding a breed
            } catch (SQLException e) {
                displayAlert("Error adding breed: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Add Cat section
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
                refreshCatIdComboBox(); // Refresh ComboBox after adding a cat
            } catch (SQLException e) {
                displayAlert("Error adding cat: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Delete Cat section
        Button deleteCatButton = new Button("Delete Cat");
        deleteCatButton.setOnAction(event -> {
            int catId = catIdComboBox.getValue();
            try {
                catManagementApp.deleteCat(catId);
                displayAlert("Cat deleted successfully!", Alert.AlertType.INFORMATION);
                refreshCatIdComboBox(); // Refresh ComboBox after deleting a cat
            } catch (SQLException e) {
                displayAlert("Error deleting cat: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Delete Breed section
        Button deleteBreedButton = new Button("Delete Breed");
        deleteBreedButton.setOnAction(event -> {
            String selectedBreed = breedComboBox2.getValue();
            try {
                int breedId = catManagementApp.getBreedIdByName(selectedBreed);
                catManagementApp.deleteBreed(breedId);
                displayAlert("Breed deleted successfully!", Alert.AlertType.INFORMATION);
                loadBreedsIntoComboBox(); // Refresh ComboBox after deleting a breed
                refreshCatIdComboBox(); // Refresh ComboBox after deleting a breed (to update cat list)
            } catch (SQLException e) {
                displayAlert("Error deleting breed: " + e.getMessage(), Alert.AlertType.ERROR);
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
                addButton, displayButton,
                new Separator(),
                addBreedLabel,
                breedNameLabel, breedNameField,
                breedDescriptionLabel, breedDescriptionField,
                addBreedButton,
                new Separator(),
                new Label("Select Cat ID to Delete:"),
                catIdComboBox,
                deleteCatButton,
                new Separator(),
                new Label("Select Breed to Delete (and all related cats):"),
                breedComboBox2,
                deleteBreedButton
        );

        // Set scene
        Scene scene = new Scene(vbox, 500, 600);
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
            breedComboBox2.getItems().clear();
            while (resultSet.next()) {
                String breedName = resultSet.getString("breed_name");
                breedComboBox.getItems().add(breedName);
                breedComboBox2.getItems().add(breedName);
            }
            // breedComboBox2.getItems().addAll(catManagementApp.getAllBreedNames());
        } catch (SQLException e) {
            displayAlert("Error loading breeds: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadCatIdsIntoComboBox() {
        try {
            catIdComboBox.getItems().clear();
            catIdComboBox.getItems().addAll(catManagementApp.getAllCatIds());
        } catch (SQLException e) {
            displayAlert("Error loading cat IDs: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void refreshCatIdComboBox() {
        loadCatIdsIntoComboBox();
        refreshCatListView(catListView);
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

    private void displayAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
