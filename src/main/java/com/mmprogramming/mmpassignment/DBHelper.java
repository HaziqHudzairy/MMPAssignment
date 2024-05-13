package com.mmprogramming.mmpassignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:ImageEditor.db";

    public static void createDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                createTable(conn);
                conn.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            handleSQLException((SQLException) e);
        }
    }

    private static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS ImageData (\n"
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "file_path TEXT,\n"
                + "star BOOLEAN,\n"
                + "text TEXT\n"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void addData(String filePath, boolean star, String text) {
        String sql = "INSERT INTO ImageData (file_path, star, text) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, filePath);
            pstmt.setBoolean(2, star);
            pstmt.setString(3, text);
            pstmt.executeUpdate();
            System.out.println("Recorded in database");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void removeData(int id) {
        String sql = "DELETE FROM ImageData WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Record deleted successfully");
            } else {
                System.out.println("No records found with the given ID");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void editData(int id, String filePath, boolean star, String text) {
        String sql = "UPDATE ImageData SET file_path = ?, star = ?, text = ? WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, filePath);
            pstmt.setBoolean(2, star);
            pstmt.setString(3, text);
            pstmt.setInt(4, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Record updated successfully");
            } else {
                System.out.println("No records found with the given ID");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void handleSQLException(SQLException e) {
        System.err.println("SQL Exception:");
        e.printStackTrace();
    }
}


