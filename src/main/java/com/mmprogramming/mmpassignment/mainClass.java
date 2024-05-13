package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class mainClass extends Application {

    @Override
    public void start(Stage primaryStage) {
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
        Button addImage = new Button("Add Image");
        addImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                uploadImage(primaryStage); // Pass primaryStage object
            }
        });
        root.getChildren().add(addImage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Background Image with Gaussian Blur and Button Panel");
        primaryStage.show();
    }

    public static void uploadImage(Stage primaryStage) { // Accept primaryStage object as argument
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        // Set initial directory (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Set file extension filter (optional)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpg, *.png)", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Create an instance of UploadPage and pass selectedFile to its constructor
            EditNewImage editNewImage = new EditNewImage(selectedFile);

            // Create a new stage for UploadPage
            Stage editNewImageStage = new Stage();

            // Call the start method of UploadPage, passing the new stage
            editNewImage.start(editNewImageStage);

            // Close the primaryStage (optional)
            primaryStage.close();
        } else {
            System.out.println("No image file selected");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
