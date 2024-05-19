package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
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
        primaryStage.setTitle("JavaFX Background Image with Gaussian Blur and Button Panel");
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
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20, 20, 20, 20));

        // Bind the size of the ImageView to the size of the scene
        bgView.fitWidthProperty().bind(primaryStage.widthProperty());
        bgView.fitHeightProperty().bind(primaryStage.heightProperty());

        // Add the ImageView to the root node
        root.getChildren().addAll(bgView, vBox);

        Label title = new Label("My Photos");
        title.setFont(new Font("Segoe UI Variable", 48));
        title.setFont(Font.font(null, FontWeight.BOLD, 80));

        HBox button_row = new HBox();
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

        try{
            String imagePath = "/resources/Images/bgMMP.jpg";
            Image image = new Image(getClass().getResource(imagePath).toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setOnMouseClicked(e -> {
                System.out.println("Image clicked");
            });
            photo_grid.getChildren().add(imageView);
        }
        catch (NullPointerException e){
            System.out.println("Image is null");
        }

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

    public static void main(String[] args) {
        launch(args);
    }
}
