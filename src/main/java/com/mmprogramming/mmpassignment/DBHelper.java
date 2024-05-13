package com.mmprogramming.mmpassignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.*;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:ImageEditor.db";

    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS ImageEditor");
            conn.setCatalog("ImageEditor"); // Switch to ImageEditor database
            createTable(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS ImageData (\n"
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "file_path TEXT,\n"
                + "star BOOLEAN,\n"
                + "text TEXT,\n"
                + "filter_type TEXT\n"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addData(String filePath, boolean star, String text, String filterType) {
        String sql = "INSERT INTO ImageData (file_path, star, text, filter_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, filePath);
            pstmt.setBoolean(2, star);
            pstmt.setString(3, text);
            pstmt.setString(4, filterType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeData(int id) {
        String sql = "DELETE FROM ImageData WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editData(int id, String filePath, boolean star, String text, String filterType) {
        String sql = "UPDATE ImageData SET file_path = ?, star = ?, text = ?, filter_type = ? WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, filePath);
            pstmt.setBoolean(2, star);
            pstmt.setString(3, text);
            pstmt.setString(4, filterType);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}


