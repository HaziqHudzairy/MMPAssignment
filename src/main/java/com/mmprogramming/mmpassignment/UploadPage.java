package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class UploadPage extends Application {

    private File selectedFile;

    public UploadPage(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    @Override
    public void start(Stage primaryStage) {

        // Load the image from the resources directory
        Image backgroundImage = new Image(getClass().getResource("/Images/bgMMP.jpg").toString());

        // Apply GaussianBlur effect to the background image
        GaussianBlur gaussianBlur = new GaussianBlur(10); // You can adjust the radius value
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setEffect(gaussianBlur);

        // Create a StackPane as the root node
        StackPane root = new StackPane();


        // Bind the size of the ImageView to the size of the scene
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());

        // Add the ImageView to the root node
        root.getChildren().add(imageView);
        BorderPane buttonPanel = new BorderPane();
        //Middle Bar
        VBox middleVB = new VBox();
        middleVB.setStyle("-fx-background-color: rgba(100, 100, 100, 0.5);");
        middleVB.setAlignment(Pos.CENTER);
        //showImage(middleVB,selectedFile);
        buttonPanel.setCenter(middleVB);

        //Left Bar
        VBox leftVbox = new VBox();
        leftVbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        leftVbox.setSpacing(10);
        leftVbox.setMinWidth(130);
        leftVbox.setAlignment(Pos.TOP_CENTER);
        leftVbox.setPadding(new Insets(20, 0, 0, 0));

        VBox textContainer = new VBox();
        textContainer.setMinHeight(50);
        textContainer.setAlignment(Pos.CENTER);

        Text menuText = new Text("Filter");
        menuText.setFill(Color.WHITE);
        menuText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        textContainer.getChildren().addAll(menuText);

        Button toggleGrayscaleButton = new Button("Grayscale");
        Button increaseBrightnessButton = new Button("+ Brightness");
        Button decreaseBrightnessButton = new Button("- Brightness");
        Button toggleGaussianBlur = new Button("Blur");
        Button toggleSepiaButton = new Button("Sepia");
        Button toggleSmoothingButton = new Button("Smoothing");
        Button resetBtn =  new Button("Reset");



        toggleGrayscaleButton.setPrefSize(100, 40);
        increaseBrightnessButton.setPrefSize(100, 40);
        decreaseBrightnessButton.setPrefSize(100, 40);
        toggleGaussianBlur.setPrefSize(100, 40);
        toggleSepiaButton.setPrefSize(100, 40);
        toggleSmoothingButton.setPrefSize(100, 40);
        resetBtn.setPrefSize(100, 40);

//        // Toggle Grayscale Button
//        toggleGrayscaleButton.setOnAction(event -> {
//            applyGrayscale(middleVB, selectedFile);
//        });
//
//        // Increase Brightness Button
//        increaseBrightnessButton.setOnAction(event -> {
//            increaseBrightness(middleVB, selectedFile);
//        });
//
//        // Decrease Brightness Button
//        decreaseBrightnessButton.setOnAction(event -> {
//            decreaseBrightness(middleVB, selectedFile);
//        });




        leftVbox.getChildren().addAll(textContainer, toggleGrayscaleButton, increaseBrightnessButton,decreaseBrightnessButton,toggleGaussianBlur,toggleSepiaButton,toggleSmoothingButton,resetBtn);
        buttonPanel.setLeft(leftVbox);

        //Bottom bar
        HBox bottomHbox = new HBox();
        bottomHbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        bottomHbox.setMinHeight(50);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setPadding(new Insets(0, 20, 0, 0));
        bottomHbox.setSpacing(10);

        TextField insertText = new TextField();
        insertText.setMinWidth(500);

        Button addTxtBtn = new Button("Add Text");
        addTxtBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addText();
            }
        });

        Button confirmBtn = new Button("Confirm");

        bottomHbox.getChildren().addAll(insertText, addTxtBtn, confirmBtn);
        buttonPanel.setBottom(bottomHbox);



        // Stack the black background behind the VBox
        StackPane layers = new StackPane();
        layers.getChildren().addAll(buttonPanel);
        // Add the layered layout to the root node
        root.getChildren().add(layers);

        StackPane topLayers = new StackPane();
        BorderPane backBP = new BorderPane();

        VBox backBtnContainer = new VBox();
        backBtnContainer.setAlignment(Pos.TOP_RIGHT);
        backBtnContainer.setPadding(new Insets(20, 20, 0, 0));
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();

                // Create an instance of mainClass and start it
                mainClass main = new mainClass();
                main.start(new Stage());
            }
        });

        backBtnContainer.getChildren().add(cancelBtn);
        backBP.setRight(backBtnContainer);
        topLayers.getChildren().add(backBP);
        root.getChildren().add(topLayers);


        // Create the scene with the root node
        Scene scene = new Scene(root);

        // Set the scene to the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Background Image with Gaussian Blur and Button Panel");
        primaryStage.show();

        // After the layout pass, call showImage
        Platform.runLater(() -> showImage(middleVB, selectedFile));
    }


    public static void showImage(VBox middleVB, File selectedFile) {
        // Load the selected image into an ImageView
        ImageView imageView = new ImageView(new Image(selectedFile.toURI().toString()));

        // Calculate aspect ratio of the image
        double aspectRatio = imageView.getImage().getWidth() / imageView.getImage().getHeight();

        // Determine whether width or height should be adjusted
        if (aspectRatio > 1) { // width > height
            imageView.setFitWidth(middleVB.getWidth());
            imageView.setFitHeight(middleVB.getWidth() / aspectRatio);
        } else { // height > width
            imageView.setFitHeight(middleVB.getHeight());
            imageView.setFitWidth(middleVB.getHeight() * aspectRatio);
        }

        middleVB.getChildren().add(imageView);
    }
    private Image applyGrayscale(Image image) {
        // Convert the image to grayscale
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage grayImage = new WritableImage(width, height);
        PixelWriter pixelWriter = grayImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                pixelWriter.setColor(x, y, Color.gray(grayValue));
            }
        }
        return grayImage;
    }

    private Image increaseBrightness(Image image) {
        // Increase the brightness of the image
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage brighterImage = new WritableImage(width, height);
        PixelWriter pixelWriter = brighterImage.getPixelWriter();

        double brightnessFactor = 0.1; // Adjust this factor as needed

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double red = color.getRed() + brightnessFactor;
                double green = color.getGreen() + brightnessFactor;
                double blue = color.getBlue() + brightnessFactor;
                pixelWriter.setColor(x, y, Color.color(clamp(red), clamp(green), clamp(blue)));
            }
        }
        return brighterImage;
    }

    private Image decreaseBrightness(Image image) {
        // Decrease the brightness of the image
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage darkerImage = new WritableImage(width, height);
        PixelWriter pixelWriter = darkerImage.getPixelWriter();

        double brightnessFactor = 0.1; // Adjust this factor as needed

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double red = color.getRed() - brightnessFactor;
                double green = color.getGreen() - brightnessFactor;
                double blue = color.getBlue() - brightnessFactor;
                pixelWriter.setColor(x, y, Color.color(clamp(red), clamp(green), clamp(blue)));
            }
        }
        return darkerImage;
    }

    private double clamp(double value) {
        // Clamp the value between 0 and 1
        return Math.min(Math.max(value, 0), 1);
    }



    public static void addText() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}