package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.File;
import java.net.URL;

public class MyPhotos extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(layout(), 960, 540);
        stage.setScene(scene);
        stage.setTitle("My Photos");
        stage.show();
    }

    private VBox layout(){
        VBox root = new VBox();
        root.setPadding(new Insets(20, 20, 20, 20));

        Label title = new Label("My Photos");
        title.setFont(new Font("Segoe UI Variable", 48));
        title.setFont(Font.font(null, FontWeight.BOLD, 80));

        HBox button_row = new HBox();
        Button select_button = new Button("Select");
        Button upload_button = new Button("Upload");
        button_row.getChildren().addAll(select_button, upload_button);
        button_row.setAlignment(Pos.TOP_RIGHT);
        button_row.setSpacing(10);

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

        root.getChildren().addAll(title, button_row, photo_grid);
        return root;
    }
}
