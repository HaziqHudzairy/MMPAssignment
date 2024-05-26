package com.mmprogramming.mmpassignment;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class SlideShowMaker extends Application {
    private int currentIndex = 0;
    private ImageView imageView;
    private StackPane stackPane;
    private MediaPlayer mediaPlayer;
    private List<Image> imageList;

    public SlideShowMaker(List<Image> imageList) {
        this.imageList = imageList;
    }

    @Override
    public void start(Stage primaryStage) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Load images from the folder
        // Folder path containing images
        String folderPath = "src/main/resources/com/mmprogramming/mmpassignment/square_image";

        VBox root = new VBox();

        stackPane = new StackPane();
        stackPane.setMaxHeight(400);
        stackPane.setMaxWidth(400);

        imageView = new ImageView();
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);

        root.setAlignment(Pos.CENTER);
        imageView.setPreserveRatio(true);
        stackPane.getChildren().add(imageView);

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

        root.getChildren().addAll(stackPane, buttonBar);
        Scene scene = new Scene(root, 800, 600);

        // Set up the stage
        primaryStage.setTitle("Image Slideshow");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to update the image in the ImageView
    private void updateImage() {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), imageView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        Image image = imageList.get(currentIndex);
        imageView.setImage(image);
    }

    private void addGraphics() {
        Dialog<Image> dialog = new Dialog<>();
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
                radioButton.setUserData(image);

                grid.add(radioButton, 0, i);
            }

            if (graphicFiles.length > 0) {
                ((RadioButton) toggleGroup.getToggles().get(0)).setSelected(true);
            }
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                RadioButton selectedImage = (RadioButton) toggleGroup.getSelectedToggle();
                return (Image) selectedImage.getUserData();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            Pos position = showPositionDialog();
            if (position != null) {
                ImageView imageView = new ImageView(result);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                stackPane.getChildren().add(imageView);
                StackPane.setAlignment(imageView, position);
            }
        });
    }

    private Pos showPositionDialog() {
        Dialog<Pos> positionDialog = new Dialog<>();
        positionDialog.setTitle("Select Position");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        positionDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ToggleGroup positionGroup = new ToggleGroup();

        RadioButton topLeft = new RadioButton("Top Left");
        topLeft.setToggleGroup(positionGroup);
        topLeft.setUserData(Pos.TOP_LEFT);

        RadioButton topRight = new RadioButton("Top Right");
        topRight.setToggleGroup(positionGroup);
        topRight.setUserData(Pos.TOP_RIGHT);

        RadioButton bottomLeft = new RadioButton("Bottom Left");
        bottomLeft.setToggleGroup(positionGroup);
        bottomLeft.setUserData(Pos.BOTTOM_LEFT);

        RadioButton bottomRight = new RadioButton("Bottom Right");
        bottomRight.setToggleGroup(positionGroup);
        bottomRight.setUserData(Pos.BOTTOM_RIGHT);

        grid.add(topLeft, 0, 0);
        grid.add(topRight, 1, 0);
        grid.add(bottomLeft, 0, 1);
        grid.add(bottomRight, 1, 1);

        positionDialog.getDialogPane().setContent(grid);

        positionDialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                RadioButton selectedPosition = (RadioButton) positionGroup.getSelectedToggle();
                return (Pos) selectedPosition.getUserData();
            }
            return null;
        });

        return positionDialog.showAndWait().orElse(null);
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
            Mat currentImage = image2Mat(imageList.get(currentIndex));
            Imgproc.putText (
                    currentImage,                          // Matrix obj of the image
                    result,          // Text to be added
                    new Point(10, 50),               // point
                    Imgproc.FONT_HERSHEY_PLAIN ,      // front face
                    1,                               // front scale
                    new Scalar(0, 0, 0),             // Scalar object for color
                    4                                // Thickness
            );
            imageList.set(currentIndex, Mat2Image(currentImage));
            updateImage();
        });
    }

    public static Mat image2Mat(Image image) {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        return bufferedImage2Mat(bImage);
    }

    public static Mat bufferedImage2Mat(BufferedImage in) {
        Mat out; // The output matrix that will be returned
        byte[] data; // Array to hold the image data
        int r, g, b; // Variables to hold the red, green, and blue components of the image
        int height = in.getHeight(); // Height of the input BufferedImage
        int width = in.getWidth(); // Width of the input BufferedImage

        // Check if the image type is RGB or ARGB
        if (in.getType() == BufferedImage.TYPE_INT_RGB || in.getType() == BufferedImage.TYPE_INT_ARGB) {
            // Create a 3-channel Mat (CV_8UC3) for RGB images
            out = new Mat(height, width, CvType.CV_8UC3);
            // Initialize the data array to hold the image data
            data = new byte[height * width * (int) out.elemSize()];
            // Get the image's RGB data
            int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
            // Loop through each pixel and extract RGB values
            for (int i = 0; i < dataBuff.length; i++) {
                // Blue component
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                // Green component
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                // Red component
                data[i * 3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
            }
        } else {
            // Create a 1-channel Mat (CV_8UC1) for grayscale images
            out = new Mat(height, width, CvType.CV_8UC1);
            // Initialize the data array to hold the image data
            data = new byte[height * width * (int) out.elemSize()];
            // Get the image's RGB data
            int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
            // Loop through each pixel and convert to grayscale
            for (int i = 0; i < dataBuff.length; i++) {
                // Extract the red, green, and blue components
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                // Calculate the grayscale value using the luminosity method
                data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b));
            }
        }
        // Put the image data into the Mat object
        out.put(0, 0, data);
        // Return the Mat object
        return out;
    }

    public static Image Mat2Image(Mat frame) {
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer, according to the PNG format
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

}

