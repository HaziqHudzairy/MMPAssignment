package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ImageViewer extends Application {

    private ImageView imageView;
    private BorderPane root;
    private VBox captionDisplayBox;

    private static final String DATABASE_URL = "jdbc:sqlite:captions.db";

    @Override
    public void start(Stage primaryStage) {

        // Create ImageView for displaying the image
        imageView = new ImageView();
        Image originalImage = new Image("file:original_image.jpg");
        imageView.setImage(originalImage);

        // Button to add caption
        Button captionButton = new Button("Add Caption");
        styleButton(captionButton);
        captionButton.setOnAction(this::addCaption);

        // Button to exit
        Button backButton = new Button("Back");
        styleButton(backButton);
        backButton.setOnAction(e -> primaryStage.close());

        // Create HBox for captionButton
        HBox buttonBox = new HBox(captionButton);
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setPadding(new Insets(10));

        // Create HBox for exitButton
        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        backButtonBox.setPadding(new Insets(10));

        // Create a StackPane to center the image
        StackPane imagePane = new StackPane(imageView);

        // Create VBox to display captions
        captionDisplayBox = new VBox(10);
        captionDisplayBox.setAlignment(Pos.BOTTOM_CENTER);
        captionDisplayBox.setPadding(new Insets(10));

        // Create BorderPane to hold the image, buttons, and captions
        root = new BorderPane();
        root.setCenter(imagePane);
        root.setRight(buttonBox);
        root.setLeft(backButtonBox);
        root.setBottom(captionDisplayBox);

        // Fetch and display captions from the database
        displayCaptionsFromDatabase();

        // Create scene and set it on the stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Image Viewer");
        primaryStage.show();
    }

    private void addCaption(ActionEvent event) {
        // Create a TextField for entering caption
        TextField captionField = new TextField();
        captionField.setPrefWidth(200); // Adjust the preferred width as needed

        // Create a save button for saving the caption
        Button saveButton = new Button("Save");
        styleButton(saveButton);
        saveButton.setOnAction(e -> {
            saveCaptionToDatabase(captionField.getText());
            displayCaptionsFromDatabase(); // Update displayed captions after saving
        });

        // Create an HBox to hold the TextField and save button
        HBox captionBox = new HBox(10, captionField, saveButton);
        captionBox.setAlignment(Pos.CENTER);
        captionBox.setPadding(new Insets(10));

        // Update the BorderPane with the VBox
        root.setBottom(captionBox);
    }

    private void saveCaptionToDatabase(String caption) {
        String insertSQL = "INSERT INTO captions (caption) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, caption);
            pstmt.executeUpdate();
            System.out.println("Caption saved to database.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayCaptionsFromDatabase() {
        captionDisplayBox.getChildren().clear(); // Clear existing captions

        String selectSQL = "SELECT caption FROM captions";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String caption = rs.getString("caption");
                Label captionLabel = new Label(caption);
                captionDisplayBox.getChildren().add(captionLabel);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void styleButton(Button button) {
        button.setPrefWidth(100);
        button.setPrefHeight(20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}