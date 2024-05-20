package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewImage extends Application {

    private double cumulativeBrightnessFactor = 1.0;
    private Stage primaryStage;
    private Mat currentImage;
    private Mat finalImage;
    private boolean star;
    public double image_width = 0;
    public  int currentColorIndex = 0;
    public Color textColorContainer = Color.WHITE;

    private boolean grayscaleEnabled = false;
    private File selectedFile;

    String imagePath;

    public ViewImage(String imageUrl) {
        System.out.println(imageUrl);
        this.imagePath = imageUrl;
    }

    @Override
    public void start(Stage primaryStage) {
        // Create the database if it doesn't exist
        DBHelper.createDatabase();

        // Load background image and apply Gaussian blur effect
        Image backgroundImage = new Image(getClass().getResource("/com/mmprogramming/mmpassignment/resource_image/bgMMP.jpg").toString());
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setEffect(gaussianBlur);
        StackPane root = new StackPane();
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());
        root.getChildren().add(imageView);

        // Initialize layout components
        BorderPane buttonPanel = new BorderPane();
        BorderPane imagePanel = new BorderPane();

        // Set up middle section of the GUI
        VBox middleVB = new VBox();
        middleVB.setStyle("-fx-background-color: rgba(100, 100, 100, 0.5);");
        middleVB.setAlignment(Pos.CENTER);
        middleVB.setMinHeight(imageView.getFitHeight());
        imagePanel.setCenter(middleVB);
        Image image = new Image(new File(imagePath).toURI().toString());
        ImageView imageViewFromPath = new ImageView(image);
        middleVB.getChildren().addAll(imageViewFromPath);

        // Set up bottom section of the GUI
        HBox bottomHboxLayer1 = new HBox();
        bottomHboxLayer1.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        bottomHboxLayer1.setMinHeight(50);
        bottomHboxLayer1.setAlignment(Pos.CENTER);
        bottomHboxLayer1.setPadding(new Insets(0, 20, 0, 0));
        bottomHboxLayer1.setSpacing(10);
        imagePanel.setBottom(bottomHboxLayer1);

        // Set up right panel for cancel button
        VBox rightVbox = new VBox();
        rightVbox.setAlignment(Pos.TOP_RIGHT);
        rightVbox.setPadding(new Insets(20, 20, 0, 0));
        Button cancelBtn = new Button("Back");
        // Event handler for cancel button
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Close the current stage and open a new instance of the main class
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();
                mainClass main = new mainClass();
                main.start(new Stage());
            }
        });
        rightVbox.getChildren().add(cancelBtn);
        buttonPanel.setRight(rightVbox);

        // Set up additional UI elements for text overlay
        VBox middleVBLayer2 = new VBox();
        middleVBLayer2.setMinWidth(130);
        middleVBLayer2.setAlignment(Pos.BOTTOM_CENTER);
        middleVBLayer2.setPadding(new Insets(0, 0, 50, 0));
        HBox bottomHbox = new HBox();
        bottomHbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        bottomHbox.setMinHeight(50);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setPadding(new Insets(0, 20, 0, 0));
        bottomHbox.setSpacing(10);

        // Input field for adding text overlay
        TextField insertText = new TextField();
        insertText.setMinWidth(500);
        Text text = new Text();

        // Buttons for adding/removing text overlay and confirmation
        Button addTxtBtn = new Button("Add Text");
        Button changeColor = new Button("Change color");
        Button removeTextBtn = new Button("Remove text");
        Button confirmBtn = new Button("Confirm");

        // Define an array of colors to rotate through
        Color[] textColors = {Color.WHITE, Color.BLACK, Color.RED, Color.BLUE, Color.GREEN};


        // Event handler for adding text overlay
//        addTxtBtn.setOnAction(e -> {
//            String inputText = insertText.getText();
//            text.setText(inputText);
//            text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//            text.setFill(textColors[0]);
//            text.setWrappingWidth(300);
//            middleVBLayer2.getChildren().add(text);
//            buttonPanel.setCenter(middleVBLayer2);
//            insertText.clear();
//        });

        // Event handler for text color
//        changeColor.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                Color nextColor = textColors[currentColorIndex];
//                textColorContainer = textColors[currentColorIndex];
//                text.setFill(nextColor);
//                currentColorIndex = (currentColorIndex + 1) % textColors.length;
//            }
//        });
//
//        // Event handler for removing text overlay
//        removeTextBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                middleVBLayer2.getChildren().clear();
//            }
//        });

        // Add UI elements to bottom panel
        bottomHbox.getChildren().addAll(insertText, addTxtBtn, changeColor, removeTextBtn, confirmBtn);
        buttonPanel.setBottom(bottomHbox);

        // Add GUI components to root stack pane
        StackPane layers = new StackPane();
        layers.getChildren().addAll(imagePanel);
        root.getChildren().add(layers);
        StackPane layer2 = new StackPane();
        layer2.getChildren().addAll(buttonPanel);
        root.getChildren().add(layer2);

        // Set up scene and display the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Edit image");
        primaryStage.show();


        // Load OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Event handler for confirm button
        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Close current stage and open a new instance of the main class
                Stage stage = (Stage) confirmBtn.getScene().getWindow();
                stage.close();
                mainClass main = new mainClass();
                main.start(new Stage());
            }
        });
    }

    // Method to convert a Color object to a string representation in the form of Color.COLOR_NAME
    private static String getColorString(Color color) {
        // Check predefined colors
        if (color.equals(Color.WHITE)) {
            return "Color.WHITE";
        } else if (color.equals(Color.BLACK)) {
            return "Color.BLACK";
        } else if (color.equals(Color.RED)) {
            return "Color.RED";
        } else if (color.equals(Color.BLUE)) {
            return "Color.BLUE";
        } else if (color.equals(Color.GREEN)) {
            return "Color.GREEN";
        }

        // For custom colors, return hexadecimal representation
        return color.toString();
    }
}

