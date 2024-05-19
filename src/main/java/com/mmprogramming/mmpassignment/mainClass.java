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
import java.util.List;

public class mainClass extends Application {

    @Override
    public void start(Stage primaryStage) {

//        Testing Database call
//        List squareFilePath = new ArrayList<>(DBHelper.getSquarePaths());
//        for(int i = 0; i < squareFilePath.size(); i++)
//        {
//            System.out.println(squareFilePath.get(i));
//        }

        Scene scene = new Scene(layout(primaryStage));
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Photos");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private StackPane layout(Stage primaryStage){
        Image backgroundImage = new Image(getClass().getResource("/com/mmprogramming/mmpassignment/resource_image/bgMMP.jpg").toString());
        System.out.println(backgroundImage.getHeight());
        System.out.println(backgroundImage.getWidth());

        // Apply GaussianBlur effect to the background image
        GaussianBlur gaussianBlur = new GaussianBlur(10); // You can adjust the radius value
        ImageView bgView = new ImageView(backgroundImage);
        bgView.setEffect(gaussianBlur);

        // Create a StackPane as the root node
        StackPane root = new StackPane();
        ScrollPane sp = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setPrefWidth(960);
        vBox.setPrefHeight(540);
        vBox.setPadding(new Insets(20, 20, 20, 20));

        sp.setContent(vBox);
        sp.setPrefWidth(960);
        sp.setPrefHeight(540);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        // Bind the size of the ImageView to the size of the scene
        bgView.fitWidthProperty().bind(primaryStage.widthProperty());
        bgView.fitHeightProperty().bind(primaryStage.heightProperty());

        // Add the ImageView to the root node
        root.getChildren().addAll(bgView, sp);

        Text title = new Text("My Photos");
        title.setFont(new Font("Segoe UI Variable", 48));
        title.setFont(Font.font(null, FontWeight.BOLD, 80));
        title.setStyle("-fx-stroke: black; -fx-stroke-width: 1; -fx-fill: white");

        HBox button_row = new HBox();
        button_row.setPadding(new Insets(0, 20, 10, 0));
        Button select_button = new Button("Select");
        Button upload_button = new Button("Upload");
        button_row.getChildren().addAll(select_button, upload_button);
        button_row.setAlignment(Pos.TOP_RIGHT);
        button_row.setSpacing(10);

        upload_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                uploadImage(primaryStage); // Pass primaryStage object
            }
        });

        FlowPane photo_grid = new FlowPane();
        photo_grid.setHgap(20);
        photo_grid.setVgap(20);

        displayImages(photo_grid);

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

        for (String path : imagePaths) {
            String imagePath = path;
            if (!path.startsWith("http") && !path.startsWith("file:/")) {
                imagePath = Paths.get(path).toUri().toString();
            }
            // Create Image and ImageView
            Image image = new Image(imagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(210); // Adjust as needed
            imageView.setFitHeight(210); // Adjust as needed
            imageView.setPreserveRatio(true);

            // Set click event handler
            imageView.setOnMouseClicked(event -> {
                // Navigate to another class or scene
                handleImageClick(path);
            });

            // Add ImageView to FlowPane
            photo_grid.getChildren().add(imageView);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
