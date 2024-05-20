package com.mmprogramming.mmpassignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DBHelper class for managing SQLite database operations for ImageEditor.
 */
public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:ImageEditor.db";

    /**
     * Creates the database and initializes the table if it does not exist.
     */
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

    /**
     * Creates the ImageData table if it does not exist.
     * @param conn the database connection
     */
    private static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS ImageData (\n"
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "image_file_path TEXT,\n"
                + "star BOOLEAN,\n"
                + "text TEXT,\n"
                + "text_color TEXT,\n"
                + "square_file_path TEXT\n"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * Adds a new record to the ImageData table.
     * @param imageFilePath the path to the image file
     * @param star the star flag for the image
     * @param text the text associated with the image
     * @param text_color the color of the text
     * @param squareFilePath the path to the square image file
     */
    public static void addData(String imageFilePath, boolean star, String text, String text_color, String squareFilePath) {
        String sql = "INSERT INTO ImageData (image_file_path, star, text, text_color, square_file_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imageFilePath);
            pstmt.setBoolean(2, star);
            pstmt.setString(3, text);
            pstmt.setString(4, text_color);
            pstmt.setString(5, squareFilePath);
            pstmt.executeUpdate();
            System.out.println("Recorded in database");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

/**
 * Removes a record from the ImageData table by ID.
 * @param id the ID
 *           of the record to be removed
 */
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

    /**
     * Retrieves a list of all square file paths from the ImageData table.
     * @return a list of square file paths
     */
    public static List<String> getSquarePaths() {
        List<String> squareFilePaths = new ArrayList<>();
        String sql = "SELECT square_file_path FROM ImageData ORDER BY ID DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                squareFilePaths.add(rs.getString("square_file_path"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return squareFilePaths;
    }


    /**
     * Retrieves the star value of the image record by square file path.
     * @param squareFilePath the square file path
     * @return the star value of the image record
     */
    public static boolean getStarValue(String squareFilePath) {
        boolean starValue = false;
        String sql = "SELECT star FROM ImageData WHERE square_file_path = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, squareFilePath);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    starValue = rs.getBoolean("star");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return starValue;
    }

    /**
     * Handles and prints details of SQL exceptions.
     * @param e the SQL exception to be handled
     */
    private static void handleSQLException(SQLException e) {
        System.err.println("SQL Exception:");
        e.printStackTrace();
    }

    /**
     * Edits the text and text color of an existing record in the ImageData table.
     * @param id the ID of the record to be updated
     * @param text the new text associated with the image
     * @param text_color the new color of the text
     */
    public static void editTextAndColor(int id, String text, String text_color) {
        String sql = "UPDATE ImageData SET text = ?, text_color = ? WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text);
            pstmt.setString(2, text_color);
            pstmt.setInt(3, id);
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

    /**
     * Retrieves the text and text color of the image record by ID.
     * @param ID the ID
     * @return the ID of the image record
     */
    public static String getText(int ID) {
        String text = null;
        String sql = "SELECT text FROM ImageData WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID); // Set the ID parameter as an int
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the values from the result set
                    text = rs.getString("text");
                } else {
                    System.out.println("No record found for the given ID: " + ID);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        // Return the retrieved text
        return text;
    }

    public static String getTextColor(int ID) {
        String textColor = null;
        String sql = "SELECT text_color FROM ImageData WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID); // Set the ID parameter as an int
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the values from the result set
                    textColor = rs.getString("text_color");
                } else {
                    System.out.println("No record found for the given ID: " + ID);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        // Return the retrieved text
        return textColor;
    }


    /**
     * Retrieves the ID of the image record by square file path.
     * @param squareFilePath the square file path
     * @return the ID of the image record
     */
    public static int getImageID(String squareFilePath){
        int imageID = 0;
        String sql = "SELECT ID FROM ImageData WHERE square_file_path = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, squareFilePath);
            try (ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    imageID = rs.getInt(1);
                } else {
                    System.out.println("No ID found for the given path: " + squareFilePath);
                }
            }
        } catch (SQLException e){
            handleSQLException(e);
        }
        return imageID;
    }

    /**
     * Retrieves the image file path of the image record by ID.
     * @param id the ID of the image record
     * @return the image file path
     */
    public static String getImageFilePath(int id) {
        String imageFilePath = null;
        String sql = "SELECT image_file_path FROM ImageData WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    imageFilePath = rs.getString("image_file_path");
                } else {
                    System.out.println("No image file path found for the given ID: " + id);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return imageFilePath;
    }
}

