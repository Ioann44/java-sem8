package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatManagementApp {
    private final Connection connection;

    public CatManagementApp(Connection connection) throws SQLException {
        this.connection = connection;

        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS cats (" +
                "cat_id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "age INTEGER," +
                "breed_id INTEGER," +
                "FOREIGN KEY (breed_id) REFERENCES breeds(breed_id))");

        statement.execute("CREATE TABLE IF NOT EXISTS breeds (" +
                "breed_id INTEGER PRIMARY KEY," +
                "breed_name TEXT NOT NULL," +
                "description TEXT)");

    }

    public void start() throws SQLException {
    }

    // MARK: Get joined cats
    public ResultSet getAllCatsWithBreeds() throws SQLException {
        String sql = "SELECT cats.cat_id, cats.name, cats.age, breeds.breed_name, breeds.description " +
                "FROM cats JOIN breeds ON cats.breed_id = breeds.breed_id";
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    // MARK: Get cat ids
    public List<Integer> getAllCatIds() throws SQLException {
        List<Integer> catIds = new ArrayList<>();
        String sql = "SELECT cat_id FROM cats";
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int catId = resultSet.getInt("cat_id");
                catIds.add(catId);
            }
        }
        return catIds;
    }

    // MARK: Get breeds
    public ResultSet getAllBreeds() throws SQLException {
        String sql = "SELECT * FROM breeds";
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    // MARK: Get breed by name
    public int getBreedIdByName(String breedName) throws SQLException {
        String sql = "SELECT breed_id FROM breeds WHERE breed_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, breedName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("breed_id");
            } else {
                throw new SQLException("Breed not found: " + breedName);
            }
        }
    }

    // MARK: Add breed
    public void addBreed(String breedName, String description) throws SQLException {
        String sql = "INSERT INTO breeds (breed_name, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, breedName);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
            System.out.println("New breed added successfully.");
        }
    }

    // MARK: Get breed names
    public String[] getAllBreedNames() throws SQLException {
        String sql = "SELECT breed_name FROM breeds";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }

        String[] breedNames = new String[rowCount];
        resultSet.beforeFirst();
        int index = 0;
        while (resultSet.next()) {
            breedNames[index] = resultSet.getString("breed_name");
            index++;
        }

        return breedNames;
    }

    // MARK: Add cat
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

    // MARK: Delete breed
    public void deleteBreed(int breedId) throws SQLException {
        // Delete associated cats
        String deleteCatsSql = "DELETE FROM cats WHERE breed_id = ?";
        try (PreparedStatement deleteCatsStatement = connection.prepareStatement(deleteCatsSql)) {
            deleteCatsStatement.setInt(1, breedId);
            int catsDeleted = deleteCatsStatement.executeUpdate();
            System.out.println(catsDeleted + " cats deleted for breed ID: " + breedId);
        } catch (SQLException e) {
            System.out.println("Error deleting cats: " + e.getMessage());
            return;
        }

        // Delete breed
        String deleteBreedSql = "DELETE FROM breeds WHERE breed_id = ?";
        try (PreparedStatement deleteBreedStatement = connection.prepareStatement(deleteBreedSql)) {
            deleteBreedStatement.setInt(1, breedId);
            int rowsAffected = deleteBreedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Breed deleted successfully.");
            } else {
                System.out.println("Breed not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting breed: " + e.getMessage());
        }
    }

    // MARK: Delete cat
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
