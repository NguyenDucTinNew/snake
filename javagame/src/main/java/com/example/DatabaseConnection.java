package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/snakegame";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void saveScore(String username, int score) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO players (name, scorest) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setInt(2, score);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
        }
    }
    public void saveMap(String datamap) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO maps (map_data) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, datamap);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getMapData(int dataId) {
        String mapData = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT map_data FROM maps WHERE map_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dataId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                mapData = resultSet.getString("map_data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapData;
    }
     

}