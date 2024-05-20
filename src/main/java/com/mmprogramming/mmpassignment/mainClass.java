package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class mainClass extends Application {

    private final Set<ImageView> selectedImages = new HashSet<>();
    private boolean isSelectMode = false;

    @Override
    public void start(Stage primaryStage) {

        DBHelper.createDatabase();

//        Testing Database call
//        List squareFilePath = new ArrayList<>(DBHelper.getSquarePaths());
//        for(int i = 0; i < squareFilePath.size(); i++)
//        {
//            System.out.println(squareFilePath.get(i));
//        }

        // Set up the stage
        Scene scene = new Scene(layout(primaryStage));
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Photos");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private StackPane layout(Stage primaryStage){
        // Put a background image for the home page
        Image backgroundImage = new Image(getClass().getResource("/com/mmprogramming/mmpassignment/resource_image/bgMMP.jpg").toString());
//        System.out.println(backgroundImage.getHeight());
//        System.out.println(backgroundImage.getWidth());

        // Apply GaussianBlur effect to the background image
        GaussianBlur gaussianBlur = new GaussianBlur(10); // You can adjust the radius value
        ImageView bgView = new ImageView(backgroundImage);
        bgView.setEffect(gaussianBlur);

        // Bind the size of the background image to the size of the scene
        bgView.fitWidthProperty().bind(primaryStage.widthProperty());
        bgView.fitHeightProperty().bind(primaryStage.heightProperty());

        // Create a StackPane as the root node
        StackPane root = new StackPane();

        // Create a ScrollPane and VBox
        ScrollPane sp = new ScrollPane();
        VBox vBox = new VBox();

        // Set the property of VBox
        vBox.setPrefWidth(960);
        vBox.setPrefHeight(540);
        vBox.setPadding(new Insets(20, 20, 20, 20));

        // Set the property of ScrollPane with VBox as its content
        sp.setContent(vBox);
        sp.setPrefWidth(960);
        sp.setPrefHeight(540);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        // Add background image, and ScrollPane to the root node
        root.getChildren().addAll(bgView, sp);

        // Set the title of the page
        Text title = new Text("My Photos");
        title.setFont(new Font("Segoe UI Variable", 48));
        title.setFont(Font.font(null, FontWeight.BOLD, 80));
        title.setStyle("-fx-stroke: black; -fx-stroke-width: 1; -fx-fill: white");

        // Create a HBox to contain 2 side-by-side buttons
        HBox button_row = new HBox();
        button_row.setPadding(new Insets(0, 20, 10, 0));
        Button select_button = new Button("Select");
        Button upload_button = new Button("Upload");
        button_row.getChildren().addAll(select_button, upload_button);
        button_row.setAlignment(Pos.TOP_RIGHT);
        button_row.setSpacing(10);

        // Create a FlowPane to contain all the uploaded photos
        FlowPane photo_grid = new FlowPane();
        photo_grid.setHgap(20);
        photo_grid.setVgap(20);

        // Display all photos in the FlowPane
        displayImages(photo_grid);

        // Toggle mode when Select button is clicked
        select_button.setOnAction(event -> {
            if (isSelectMode) {
                // Exit Select mode
                isSelectMode = false;
                select_button.setText("Select");
                upload_button.setText("Upload");
                clearSelection(photo_grid);
            } else {
                // Enter Select mode to create a slideshow
                isSelectMode = true;
                select_button.setText("Cancel");
                upload_button.setText("Create a slideshow");
            }
        });

        // Upload button functionality in both toggle mode
        upload_button.setOnAction(event -> {
            if (isSelectMode) {
                // Create a slideshow
                List<Image> selectedImage = new ArrayList<>();
                for (ImageView imageView : selectedImages) {
                    Image image = imageView.getImage();
                    selectedImage.add(image);
                }
                createSlideshow(primaryStage, selectedImage);
            } else {
                // Upload an image
                uploadImage(primaryStage);
            }
        });

        // Add title, HBox, and FlowPane as child of the VBox.
        vBox.getChildren().addAll(title, button_row, photo_grid);

        return root;
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

    private void handleImageClick(String imagePath) {
        System.out.println("Image clicked: " + imagePath);
        // Implement navigation logic here
    }

    private void displayImages(FlowPane photo_grid){
        // Get image paths from database
        List<String> imagePaths = DBHelper.getSquarePaths();

        // Loop through all the image paths from the database
        for (String path : imagePaths) {
            String imagePath = path;
            String pathing = path;
            if (!path.startsWith("http") && !path.startsWith("file:/")) {
                imagePath = Paths.get(path).toUri().toString();
            }

            // Ensure the path starts from "src"
            int srcIndex = pathing.indexOf("src");
            if (srcIndex != -1) {
                pathing = pathing.substring(srcIndex);
            }

            boolean star = DBHelper.getStarValue(pathing);

            // Create StackPane
            StackPane imageStack = new StackPane();
            String heartPath = mainClass.class.getResource("/com/mmprogramming/mmpassignment/resource_image/heart.png").toString();

            Image heartIcon = new Image(heartPath);
            ImageView heartOverlay = new ImageView(heartIcon);
            heartOverlay.setFitHeight(40);
            heartOverlay.setFitWidth(40);
            heartOverlay.setTranslateX(70);
            heartOverlay.setTranslateY(-70);

            // Create Image and ImageView
            Image image = new Image(imagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(210);
            imageView.setFitHeight(210);
            imageView.setPreserveRatio(true);

            // Set image click event handler
            imageView.setOnMouseClicked(event -> {
                if (isSelectMode) {
                    // Events when Select button is active
                    if (selectedImages.contains(imageView)) {
                        selectedImages.remove(imageView);
                        imageView.setStyle(""); // Remove highlight from photo
                    } else {
                        selectedImages.add(imageView);
                        imageView.setStyle("-fx-effect: innershadow(three-pass-box, green, 20, 0.5, 0, 0);"); // Highlight the photo
                    }
                } else {
                    // Navigate to the photo's page when Select button is not active
                    handleImageClick(path);
                }
            });

            // Create ColorAdjust for image hover effect
            ColorAdjust hoverEffect = new ColorAdjust();
            hoverEffect.setBrightness(0.14);

            // Increase brightness of photo when mouse is hovering over it
            imageView.setOnMouseEntered(e -> {
                if (!isSelectMode) imageView.setEffect(hoverEffect);
            });

            // Return to normal when mouse is no longer hovering over it
            imageView.setOnMouseExited(e -> {
                if (!isSelectMode) imageView.setEffect(null);
            });

            // Set visibility based on the star value
            heartOverlay.setVisible(star);
            imageStack.getChildren().addAll(imageView, heartOverlay);
            // Add ImageView to FlowPane
            photo_grid.getChildren().add(imageStack);
        }
    }

    private void clearSelection(FlowPane photoGrid) {
        for (ImageView imageView : selectedImages) {
            imageView.setStyle(""); // Remove highlight
        }
        selectedImages.clear();
    }

    private void createSlideshow(Stage primaryStage, List<Image> selectedImage) {
        SlideShowMaker slideShowMaker = new SlideShowMaker(selectedImage);

        // Create a new stage for UploadPage
        Stage slideShowMakerStage = new Stage();

        // Call the start method of UploadPage, passing the new stage
        slideShowMaker.start(slideShowMakerStage);

        // Close the primaryStage (optional)
        primaryStage.close();
        // Implement slideshow creation logic here
    }

    public static void main(String[] args) {
        launch(args);
    }
}
