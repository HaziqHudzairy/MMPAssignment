package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MyPhotos extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setPadding(new Insets(20, 20, 20, 20));

        Label title = new Label("My Photos");
        title.setFont(new Font("Segoe UI Variable", 48));
        title.setFont(Font.font(null, FontWeight.BOLD, 80));
        HBox button_row = new HBox();
        FlowPane photo_grid = new FlowPane();

        Button select_button = new Button("Select");
        Button upload_button = new Button("Upload");
        button_row.getChildren().addAll(select_button, upload_button);
        button_row.setAlignment(Pos.TOP_RIGHT);
        button_row.setSpacing(10);

        root.getChildren().addAll(title, button_row, photo_grid);

        Scene scene = new Scene(root, 960, 540);
        stage.setScene(scene);
        stage.setTitle("My Photos");
        stage.show();
    }
}
