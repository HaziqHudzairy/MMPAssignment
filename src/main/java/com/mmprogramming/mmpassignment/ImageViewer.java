package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;

public class ImageViewer extends Application {

    private ImageView imageView;
    private BorderPane root;

    private int imageID;
    private VBox captionDisplayBox;

    private BorderPane borderPane = new BorderPane();

//    private static final String DATABASE_URL = "jdbc:sqlite:captions.db";
    private static final String DATABASE_URL = "jdbc:sqlite:ImageEditor.db";

    public ImageViewer(int ID) {
        this.imageID = ID;
    }

    @Override
    public void start(Stage primaryStage) {

        String imagePath = DBHelper.getImageFilePath(imageID);

        // Create ImageView for displaying the image
        imageView = new ImageView();
        Image originalImage = new Image(new File(imagePath).toURI().toString());
        imageView.setImage(originalImage);

        // Set preserveRatio property to true
        imageView.setPreserveRatio(true);

        // Set the size of the ImageView to match the scene
        imageView.setFitWidth(960);
        imageView.setFitHeight(540);

        // Create a StackPane to hold the ImageView as the background
        StackPane backgroundPane = new StackPane(imageView);

        // Button to add caption
        Button captionButton = new Button("Add Caption");
        styleButton(captionButton);
        captionButton.setOnAction(this::addCaption);

        // Button to exit
        Button backButton = new Button("Back");
        styleButton(backButton);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.close();
                mainClass main = new mainClass();
                main.start(new Stage());
            }
        });

        // Create HBox for captionButton
        HBox buttonBox = new HBox(captionButton);
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setPadding(new Insets(10));

        // Create HBox for exitButton
        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        backButtonBox.setPadding(new Insets(10));

        // Create a StackPane to center the image
        //StackPane imagePane = new StackPane(vBox);

        // Create VBox to display captions
        VBox captionDisplayBox2 = new VBox(10);
        captionDisplayBox2.setAlignment(Pos.BOTTOM_CENTER);
        captionDisplayBox2.setPadding(new Insets(10));
        Text text = new Text(DBHelper.getText(imageID));
        text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        text.setFill(Color.WHITE);
        String TextFill = DBHelper.getTextColor(imageID);
        text.setWrappingWidth(300);
        captionDisplayBox2.getChildren().addAll(text);

        //root.setCenter(imagePane);
        borderPane.setRight(buttonBox);
        borderPane.setLeft(backButtonBox);
        borderPane.setCenter(captionDisplayBox2);

        backgroundPane.getChildren().addAll(borderPane);

        // Fetch and display captions from the database
        displayCaptionsFromDatabase();

        // Create scene and set it on the stage
        Scene scene = new Scene(backgroundPane, 960, 540);

        primaryStage.setResizable(true);
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
            DBHelper.getText(imageID);// Update displayed captions after saving
        });

        // Create an HBox to hold the TextField and save button
        HBox captionBox = new HBox(10, captionField, saveButton);
        captionBox.setAlignment(Pos.CENTER);
        captionBox.setPadding(new Insets(10));

        // Update the BorderPane with the VBox
        borderPane.setBottom(captionBox);
    }

    private void saveCaptionToDatabase(String caption) {
        String insertSQL = "INSERT INTO ImageData (text) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, caption);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayCaptionsFromDatabase() {
        //captionDisplayBox.getChildren().clear(); // Clear existing captions

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