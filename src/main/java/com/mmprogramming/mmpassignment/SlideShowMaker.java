package com.mmprogramming.mmpassignment;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SlideShowMaker extends Application {
    private int currentIndex = 0;
    private Label textLabel;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;
    private List<ImageView> graphicsList = new ArrayList<>(); // List to store graphic elements
    private List<Image> imageList;

    // List to store image texts
    private final List<String> imageTexts = new ArrayList<>();
    private final String captionFilePath = "captions.txt";


    public SlideShowMaker(List<Image> imageList) {
        this.imageList = imageList;
    }



    @Override
    public void start(Stage primaryStage) {

        // Load images from the folder
        // Folder path containing images
        String folderPath = "src/main/resources/com/mmprogramming/mmpassignment/square_image";
        loadCaptionsFromFile(captionFilePath);


        VBox root = new VBox();

        imageView = new ImageView();
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
//        imageView.fitHeightProperty().bind(primaryStage.heightProperty().subtract(100));

        VBox.setVgrow(imageView, Priority.ALWAYS);

        root.setAlignment(Pos.CENTER);

        imageView.setPreserveRatio(true);

        textLabel = new Label();
        textLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");
        textLabel.setPadding(new Insets(10));
        VBox.setVgrow(textLabel, Priority.NEVER);

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
            currentIndex = (currentIndex + 1) % imageList.size();
            updateImage();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

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
                mediaPlayer.pause();
            }
        });


        //add addText button
        addText.setOnAction(actionEvent -> showTextInputDialog());

        //add addGraphics
        addGraphics.setOnAction(actionEvent -> addGraphics());

        root.getChildren().addAll(imageView, textLabel, buttonBar);
        Scene scene = new Scene(root, 800, 600);

        // Set up the stage
        primaryStage.setTitle("Image Slideshow");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // Method to load captions from a text file
    private void loadCaptionsFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int i = 0;
            while (scanner.hasNextLine()) {
                if (i < imageTexts.size()) {
                    imageTexts.set(i, scanner.nextLine());
                } else {
                    imageTexts.add(scanner.nextLine());
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to save captions to a text file
    private void saveCaptionsToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String text : imageTexts) {
                writer.println(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update the image in the ImageView
    private void updateImage() {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), imageView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
            Image image = imageList.get(currentIndex);
            imageView.setImage(image);
            textLabel.setText(imageTexts.get(currentIndex)); // update the image text
    }

    private void addGraphics() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Select a Graphic");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        File graphicsFolder = new File("src/main/resources/com/mmprogramming/mmpassignment/graphics");
        File[] graphicFiles = graphicsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

        ToggleGroup toggleGroup = new ToggleGroup();

        if (graphicFiles != null) {
            for (int i = 0; i < graphicFiles.length; i++) {
                File graphicFile = graphicFiles[i];
                Image image = new Image(graphicFile.toURI().toString());
                ImageView imageView = new ImageView(image);

                // Resize the image to the desired size
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                HBox hBox = new HBox(imageView);
                hBox.setAlignment(Pos.CENTER);

                RadioButton radioButton = new RadioButton();
                radioButton.setGraphic(imageView);
                radioButton.setToggleGroup(toggleGroup);
                radioButton.setUserData(graphicFile.getAbsolutePath());

                grid.add(radioButton, 0, i);
            }

            if (graphicFiles.length > 0) {
                ((RadioButton) toggleGroup.getToggles().get(0)).setSelected(true);
            }
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return (String) toggleGroup.getSelectedToggle().getUserData();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            System.out.println("Selected graphic: " + result);
        });
    }


    private void showTextInputDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Slide Text");
        dialog.setHeaderText("Enter text for the current slide:");

        // Set the button types.
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create the text field and set it with the current slide text.
        TextField textField = new TextField();
        textField.setText(imageTexts.get(currentIndex));

        // Layout for the dialog.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Text:"), 0, 0);
        grid.add(textField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to the text input when the confirm button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return textField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            imageTexts.set(currentIndex, result);
            textLabel.setText(result); // Update label
            saveCaptionsToFile(captionFilePath); // Save the updated captions to the file
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

