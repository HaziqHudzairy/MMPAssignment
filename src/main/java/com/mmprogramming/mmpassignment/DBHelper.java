package com.mmprogramming.mmpassignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:ImageEditor.db";

    public static void createDatabase() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                createTable(conn);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found exception:");
            e.printStackTrace();
        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                handleSQLException(ex);
            }
        }
    }

    private static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS ImageData (\n"
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "image_file_path TEXT,\n"
                + "star BOOLEAN,\n"
                + "text TEXT,\n"
                + "square_file_path TEXT\n"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void addData(String imageFilePath, boolean star, String text, String squareFilePath) {
        String sql = "INSERT INTO ImageData (image_file_path, star, text, square_file_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imageFilePath);
            pstmt.setBoolean(2, star);
            pstmt.setString(3, text);
            pstmt.setString(4, squareFilePath);
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

    public static void editData(int id, String imageFilePath, boolean star, String text, String squareFilePath) {
        String sql = "UPDATE ImageData SET image_file_path = ?, star = ?, text = ?, square_file_path = ? WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imageFilePath);
            pstmt.setBoolean(2, star);
            pstmt.setString(3, text);
            pstmt.setString(4, squareFilePath);
            pstmt.setInt(5, id);
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
