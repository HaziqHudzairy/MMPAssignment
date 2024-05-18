package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SlideShowMaker extends Application {
    private int currentIndex = 0;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;

    // List of image URLs or paths
    private final String[] imagePaths = {
            "C:\\Users\\serve\\Downloads\\ImagesForTesting\\1.jpg",
            "C:\\Users\\serve\\Downloads\\ImagesForTesting\\2.png",
            "C:\\Users\\serve\\Downloads\\ImagesForTesting\\3.jpg"
    };

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        imageView = new ImageView();
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty().subtract(80));

        VBox.setVgrow(imageView, Priority.ALWAYS);

        root.setAlignment(Pos.CENTER);

        imageView.setPreserveRatio(true);

        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(20));

        Button playButton = new Button("Play Video");
        Button pauseButton = new Button("Pause");
        Button chooseSoundFile = new Button("Choose Sound File");
        Button addGraphics = new Button("Add Graphics");
        Button addText = new Button("Add Text");

        buttonBar.getChildren().addAll(playButton, pauseButton, chooseSoundFile, addText, addGraphics);

        updateImage();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            currentIndex = (currentIndex + 1) % imagePaths.length;
            updateImage();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        addGraphics.setOnAction(actionEvent -> {

        });

        chooseSoundFile.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            if(file != null) {
                String selected = file.toURI().toString();
                Media media = new Media(selected);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }
            currentIndex = 0;
            updateImage();
            timeline.pause();
        });

        playButton.setOnAction(actionEvent -> {
            timeline.play();
            if(mediaPlayer != null) {
                mediaPlayer.play();
            }
        });

        pauseButton.setOnAction(actionEvent -> {
            timeline.pause();
            if(mediaPlayer != null) {
                mediaPlayer.play();
            }
        });

        root.getChildren().addAll(imageView, buttonBar);
        Scene scene = new Scene(root, 800, 600);

        // Set up the stage
        primaryStage.setTitle("Image Slideshow");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to update the image in the ImageView
    private void updateImage()  {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), imageView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        try {
            Image image = new Image(new FileInputStream(imagePaths[currentIndex]));
            imageView.setImage(image);
        } catch (FileNotFoundException e) {}
    }

    // Method to show image selection dialog
    private void showImageSelectionDialog() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Select an Image");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 0; i < imagePaths.length; i++) {
            RadioButton radioButton = new RadioButton("Image " + (i + 1));
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setUserData(i);
            grid.add(radioButton, 0, i);
        }

        if (imagePaths.length > 0) {
            ((RadioButton) toggleGroup.getToggles().get(0)).setSelected(true);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return (Integer) toggleGroup.getSelectedToggle().getUserData();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            currentIndex = result;
            updateImage();
        });
    }

//    WORK IN PROGRESS
//    private void addGraphics() {
//
//    }
//
//    private void addText() {
//
//    }

    public static void main(String[] args) {
        launch(args);
    }
}

