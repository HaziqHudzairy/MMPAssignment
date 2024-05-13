package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

import java.io.ByteArrayInputStream;
import java.io.File;

public class EditNewImage extends Application {

    private double cumulativeBrightnessFactor = 1.0;
    private Stage primaryStage; // set primary stage as class level
    private Mat currentImage; // set the current image to be clone for effect usage
    private boolean grayscaleEnabled = false; // set the initial grayscale status to false
    private File selectedFile;

    public EditNewImage(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    @Override
    public void start(Stage primaryStage) {
        //BACKGROUND OF THE LAYOUT
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
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        BorderPane buttonPanel = new BorderPane();
        BorderPane imagePanel = new BorderPane();
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //MIDDLE BAR
        VBox middleVB = new VBox();
        middleVB.setStyle("-fx-background-color: rgba(100, 100, 100, 0.5);");
        middleVB.setAlignment(Pos.CENTER);
        middleVB.setMinHeight(imageView.getFitHeight());

        imagePanel.setCenter(middleVB);

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        VBox leftVboxLayer1 = new VBox();
        leftVboxLayer1.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        leftVboxLayer1.setSpacing(10);
        leftVboxLayer1.setMinWidth(130);
        leftVboxLayer1.setAlignment(Pos.TOP_CENTER);
        leftVboxLayer1.setPadding(new Insets(20, 0, 0, 0));

        imagePanel.setLeft(leftVboxLayer1);

        HBox bottomHboxLayer1 = new HBox();
        bottomHboxLayer1.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        bottomHboxLayer1.setMinHeight(50);
        bottomHboxLayer1.setAlignment(Pos.CENTER);
        bottomHboxLayer1.setPadding(new Insets(0, 20, 0, 0));
        bottomHboxLayer1.setSpacing(10);

        imagePanel.setBottom(bottomHboxLayer1);
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //LEFT BAR
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

        leftVbox.getChildren().addAll(textContainer, toggleGrayscaleButton, increaseBrightnessButton,decreaseBrightnessButton,toggleGaussianBlur,toggleSepiaButton,toggleSmoothingButton,resetBtn);
        buttonPanel.setLeft(leftVbox);
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //RIGHT PANEL
        VBox rightVbox = new VBox();
        rightVbox.setAlignment(Pos.TOP_RIGHT);
        rightVbox.setPadding(new Insets(20, 20, 0, 0));
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

        rightVbox.getChildren().add(cancelBtn);
        buttonPanel.setRight(rightVbox);

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //BOTTOM BAR
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

            }
        });

        Button confirmBtn = new Button("Confirm");

        bottomHbox.getChildren().addAll(insertText, addTxtBtn, confirmBtn);
        buttonPanel.setBottom(bottomHbox);

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Stack the black background behind the VBox
        StackPane layers = new StackPane();
        layers.getChildren().addAll(imagePanel);
        // Add the layered layout to the root node
        root.getChildren().add(layers);
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        StackPane layer2 = new StackPane();
        layer2.getChildren().addAll(buttonPanel);
        // Add the layered layout to the root node
        root.getChildren().add(layer2);
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Create the scene with the root node
        Scene scene = new Scene(root);
        // Set the scene to the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Background Image with Gaussian Blur and Button Panel");
        primaryStage.show();

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Read the image using OpenCV
        Mat matImage = Imgcodecs.imread(selectedFile.getAbsolutePath());

        updateImage(middleVB,matImage);

        currentImage = matImage.clone();

        toggleGrayscaleButton.setOnAction(e -> {
            grayscaleEnabled = !grayscaleEnabled; // Toggle grayscale mode
            updateImage(middleVB, currentImage); // Update the displayed image
            if (toggleGrayscaleButton.getText().equals("Grayscale")) {
                toggleGrayscaleButton.setText("Remove Grayscale");
            } else {
                toggleGrayscaleButton.setText("Grayscale");
            }

        });

        // Add event handlers to adjust brightness
        increaseBrightnessButton.setOnAction(e -> {
            cumulativeBrightnessFactor *= 1.2; // Increase brightness by 20%
            updateImage(middleVB, adjustBrightness(currentImage, 1.0));
        });

        decreaseBrightnessButton.setOnAction(e -> {
            cumulativeBrightnessFactor *= 0.8; // Decrease brightness by 20%
            updateImage(middleVB, adjustBrightness(currentImage, 1.0));
        });

        toggleGaussianBlur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (toggleGaussianBlur.getText().equals("Blur")) {
                     // Apply Gaussian blur
                    Mat gaussianBlurMultiplier= applyGaussianBlur((applyGaussianBlur(currentImage, 9)),9);
                    updateImage(middleVB,applyGaussianBlur(gaussianBlurMultiplier,9)); // Update the displayed image
                    toggleGaussianBlur.setText("Remove Blur");
                } else {
                    updateImage(middleVB, currentImage); // Update the displayed image
                    toggleGaussianBlur.setText("Blur");
                }
            }
        });
        toggleSepiaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (toggleSepiaButton.getText().equals("Sepia")) {
                    // Apply sepia filter
                    Mat sepiaImage = applySepia(currentImage);
                    updateImage(middleVB, sepiaImage); // Update the displayed image
                    toggleSepiaButton.setText("Remove Sepia");
                } else {
                    // Remove sepia filter (display original image)
                    updateImage(middleVB, currentImage); // Update the displayed image with the original image
                    toggleSepiaButton.setText("Sepia");
                }
            }
        });

    }
    private void updateImage(VBox middleVB, Mat matImage)
    {
        middleVB.getChildren().clear();

        if (grayscaleEnabled) {
            matImage = convertToGrayscale(matImage);
        }
        // Calculate the aspect ratio
        double aspectRatio = (double) matImage.width() / matImage.height();
        System.out.println(aspectRatio);

        // Define the desired width and height based on VBox size
        double desiredWidth, desiredHeight;
        if (matImage.width() > matImage.height()) {
            desiredWidth = middleVB.getWidth();
            desiredHeight = (middleVB.getWidth() / aspectRatio);
        } else {
            desiredHeight = middleVB.getHeight();
            desiredWidth = (middleVB.getHeight() * aspectRatio);
        }

        // Resize the image using OpenCV
        Mat resizedMat = new Mat();
        Size newSize = new Size(desiredWidth, desiredHeight);
        Imgproc.resize(matImage, resizedMat, newSize);
        System.out.println("Desired Width: " + desiredWidth);
        System.out.println("Desired Height: " + desiredHeight);
        System.out.println("New Size: " + newSize);


        // Convert the resized Mat to a JavaFX Image
        Imgcodecs.imwrite("temp_resized_image.jpg", resizedMat);
        Image resizedImage = new Image(new File("temp_resized_image.jpg").toURI().toString());

        // Create ImageView and set the resized image
        ImageView imageViewResized = new ImageView(resizedImage);

        // Add ImageView to middleVB
        middleVB.getChildren().addAll(imageViewResized);
    }

    private Mat adjustBrightness(Mat image, double factor) {
        double alpha = cumulativeBrightnessFactor * factor;
        // Ensure alpha stays within the range of 0.1 to 2
        alpha = Math.max(0.1, Math.min(2.0, alpha));
        Mat adjustedImage = new Mat();
        image.convertTo(adjustedImage, -1, alpha, 0);
        return adjustedImage;
    }

    private Mat convertToGrayscale(Mat inputImage) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(inputImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        return grayImage;
    }

    private Mat applyGaussianBlur(Mat image, double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Invalid radius for Gaussian blur. Radius must be greater than 0.");
        }
        Mat updatedImage = new Mat();
        Imgproc.GaussianBlur(image, updatedImage, new Size(radius, radius), 0);

        return updatedImage;
    }

    public Mat applySepia(Mat inputImage) {
        Mat outputImage = new Mat(inputImage.size(), inputImage.type());

        // Convert inputImage to BGR if it's not already in that format
        if (inputImage.channels() == 1) {
            Imgproc.cvtColor(inputImage, inputImage, Imgproc.COLOR_GRAY2BGR);
        }

        for (int y = 0; y < inputImage.rows(); y++) {
            for (int x = 0; x < inputImage.cols(); x++) {
                double[] pixel = inputImage.get(y, x);
                double[] sepiaPixel = applySepiaTransformation(pixel);
                outputImage.put(y, x, sepiaPixel);
            }
        }

        return outputImage;
    }

    // Debugging: Print pixel values before and after applying sepia transformation
    private double[] applySepiaTransformation(double[] pixel) {
        double[] sepiaPixel = new double[3];

        sepiaPixel[0] = Math.min(255, (pixel[0] * 0.393) + (pixel[1] * 0.769) + (pixel[2] * 0.189)); // Red
        sepiaPixel[1] = Math.min(255, (pixel[0] * 0.349) + (pixel[1] * 0.686) + (pixel[2] * 0.168)); // Green
        sepiaPixel[2] = Math.min(255, (pixel[0] * 0.272) + (pixel[1] * 0.534) + (pixel[2] * 0.131)); // Blue

        return sepiaPixel;
    }



}
