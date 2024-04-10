package com.example;

import java.sql.*;
import java.util.Scanner;

public class CatManagementApp {
    private static final String DB_URL = "jdbc:sqlite:./cats.db";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
                Statement statement = connection.createStatement()) {

            // Создание таблицы cats
            statement.execute("CREATE TABLE IF NOT EXISTS cats (" +
                    "cat_id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "age INTEGER," +
                    "breed_id INTEGER," +
                    "FOREIGN KEY (breed_id) REFERENCES breeds(breed_id))");

            // Создание таблицы breeds
            statement.execute("CREATE TABLE IF NOT EXISTS breeds (" +
                    "breed_id INTEGER PRIMARY KEY," +
                    "breed_name TEXT NOT NULL," +
                    "description TEXT)");

            // Создаем объект приложения и запускаем его
            CatManagementApp app = new CatManagementApp(connection);
            app.start();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private final Connection connection;

    public CatManagementApp(Connection connection) {
        this.connection = connection;
    }

    private int getUserInput(String message) {
        System.out.print(message);
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }

    public void start() throws SQLException {
        boolean running = true;
        while (running) {
            System.out.println("Выберите действие:");
            System.out.println("1. Вывести всех котов с породами");
            System.out.println("2. Добавить новую породу");
            System.out.println("3. Добавить нового кота");
            System.out.println("4. Удалить породу");
            System.out.println("5. Удалить кота");
            System.out.println("0. Выйти из программы");

            int choice = getUserInput("Введите номер действия: ");

            switch (choice) {
                case 1:
                    displayAllCats();
                    break;
                case 2:
                    addBreed(null, null);
                    break;
                case 3:
                    addCat(null, choice, choice);
                    break;
                case 4:
                    deleteBreed(choice);
                    break;
                case 5:
                    deleteCat(choice);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
                    break;
            }
        }
    }

    // Метод для вывода всех котов с их породами
    public void displayAllCats() throws SQLException {
        String sql = "SELECT cats.cat_id, cats.name, cats.age, breeds.breed_name, breeds.description " +
                "FROM cats JOIN breeds ON cats.breed_id = breeds.breed_id";
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int catId = resultSet.getInt("cat_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String breedName = resultSet.getString("breed_name");
                String description = resultSet.getString("description");
                System.out.printf("Cat ID: %d, Name: %s, Age: %d, Breed: %s (%s)\n",
                        catId, name, age, breedName, description);
            }
        }
    }

    // Метод для добавления новой породы
    public void addBreed(String breedName, String description) throws SQLException {
        String sql = "INSERT INTO breeds (breed_name, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, breedName);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
            System.out.println("New breed added successfully.");
        }
    }

    // Метод для добавления нового кота
    public void addCat(String name, int age, int breedId) throws SQLException {
        String sql = "INSERT INTO cats (name, age, breed_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, breedId);
            preparedStatement.executeUpdate();
            System.out.println("New cat added successfully.");
        }
    }

    // Метод для удаления породы по идентификатору
    public void deleteBreed(int breedId) throws SQLException {
        // Проверяем, есть ли коты этой породы
        String checkSql = "SELECT * FROM cats WHERE breed_id = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setInt(1, breedId);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Cannot delete breed with existing cats.");
                return;
            }
        }

        // Удаляем породу
        String deleteSql = "DELETE FROM breeds WHERE breed_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.setInt(1, breedId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Breed deleted successfully.");
            } else {
                System.out.println("Breed not found.");
            }
        }
    }

    // Метод для удаления кота по идентификатору
    public void deleteCat(int catId) throws SQLException {
        String sql = "DELETE FROM cats WHERE cat_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, catId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cat deleted successfully.");
            } else {
                System.out.println("Cat not found.");
            }
        }
    }
}
