package com.mmprogramming.mmpassignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditNewImage extends Application {

    private double cumulativeBrightnessFactor = 1.0;
    private Stage primaryStage;
    private Mat currentImage;
    private Mat finalImage;
    private boolean star;
    public double image_width = 0;
    public  int currentColorIndex = 0;
    public Color textColorContainer = Color.WHITE;

    private boolean grayscaleEnabled = false;
    private File selectedFile;

    public EditNewImage(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    @Override
    public void start(Stage primaryStage) {

        // Create the database if it doesn't exist
        DBHelper.createDatabase();

        // Load background image and apply Gaussian blur effect
        Image backgroundImage = new Image(getClass().getResource("/com/mmprogramming/mmpassignment/resource_image/bgMMP.jpg").toString());
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setEffect(gaussianBlur);
        StackPane root = new StackPane();
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());
        root.getChildren().add(imageView);

        // Initialize layout components
        BorderPane buttonPanel = new BorderPane();
        BorderPane imagePanel = new BorderPane();

        // Set up middle section of the GUI
        VBox middleVB = new VBox();
        middleVB.setStyle("-fx-background-color: rgba(100, 100, 100, 0.5);");
        middleVB.setAlignment(Pos.CENTER);
        middleVB.setMinHeight(imageView.getFitHeight());
        imagePanel.setCenter(middleVB);

        // Set up left section of the GUI
        VBox leftVboxLayer1 = new VBox();
        // Styling for left panel
        leftVboxLayer1.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        leftVboxLayer1.setSpacing(10);
        leftVboxLayer1.setMinWidth(130);
        leftVboxLayer1.setAlignment(Pos.TOP_CENTER);
        leftVboxLayer1.setPadding(new Insets(20, 0, 0, 0));
        imagePanel.setLeft(leftVboxLayer1);

        // Set up bottom section of the GUI
        HBox bottomHboxLayer1 = new HBox();
        bottomHboxLayer1.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        bottomHboxLayer1.setMinHeight(50);
        bottomHboxLayer1.setAlignment(Pos.CENTER);
        bottomHboxLayer1.setPadding(new Insets(0, 20, 0, 0));
        bottomHboxLayer1.setSpacing(10);
        imagePanel.setBottom(bottomHboxLayer1);

        // Set up left panel for buttons
        VBox leftVbox = new VBox();
        leftVbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        leftVbox.setSpacing(10);
        leftVbox.setMinWidth(130);
        leftVbox.setAlignment(Pos.TOP_CENTER);
        leftVbox.setPadding(new Insets(20, 0, 0, 0));

        // Set up text] container for filter indicator
        VBox textContainer = new VBox();
        textContainer.setMinHeight(50);
        textContainer.setAlignment(Pos.CENTER);
        Text menuText = new Text("Filter");
        menuText.setFill(Color.WHITE);
        menuText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textContainer.getChildren().addAll(menuText);

        // Buttons for image filters and actions
        Button toggleGrayscaleButton = new Button("Grayscale");
        Button increaseBrightnessButton = new Button("+ Brightness");
        Button decreaseBrightnessButton = new Button("- Brightness");
        Button toggleGaussianBlur = new Button("Blur");
        Button resetBtn =  new Button("Reset");

        // Set preferred sizes for buttons
        toggleGrayscaleButton.setPrefSize(100, 40);
        increaseBrightnessButton.setPrefSize(100, 40);
        decreaseBrightnessButton.setPrefSize(100, 40);
        toggleGaussianBlur.setPrefSize(100, 40);
        resetBtn.setPrefSize(100, 40);

        // Add buttons to the left panel
        leftVbox.getChildren().addAll(textContainer, toggleGrayscaleButton, increaseBrightnessButton, decreaseBrightnessButton, toggleGaussianBlur, resetBtn);
        buttonPanel.setLeft(leftVbox);

        // Set up right panel for cancel button
        VBox rightVbox = new VBox();
        rightVbox.setAlignment(Pos.TOP_RIGHT);
        rightVbox.setPadding(new Insets(20, 20, 0, 0));
        Button cancelBtn = new Button("Cancel");
        // Event handler for cancel button
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Close the current stage and open a new instance of the main class
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();
                mainClass main = new mainClass();
                main.start(new Stage());
            }
        });
        rightVbox.getChildren().add(cancelBtn);
        buttonPanel.setRight(rightVbox);

        // Set up additional UI elements for text overlay
        VBox middleVBLayer2 = new VBox();
        middleVBLayer2.setMinWidth(130);
        middleVBLayer2.setAlignment(Pos.BOTTOM_CENTER);
        middleVBLayer2.setPadding(new Insets(0, 0, 50, 0));
        HBox bottomHbox = new HBox();
        bottomHbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        bottomHbox.setMinHeight(50);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setPadding(new Insets(0, 20, 0, 0));
        bottomHbox.setSpacing(10);

        // Input field for adding text overlay
        TextField insertText = new TextField();
        insertText.setMinWidth(500);
        Text text = new Text();

        // Buttons for adding/removing text overlay and confirmation
        Button addTxtBtn = new Button("Add Text");
        Button changeColor = new Button("Change color");
        Button removeTextBtn = new Button("Remove text");
        Button confirmBtn = new Button("Confirm");

        // Define an array of colors to rotate through
        Color[] textColors = {Color.WHITE, Color.BLACK, Color.RED, Color.BLUE, Color.GREEN};


        // Event handler for adding text overlay
        addTxtBtn.setOnAction(e -> {
            String inputText = insertText.getText();
            text.setText(inputText);
            text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            text.setFill(textColors[0]);
            text.setWrappingWidth(300);
            middleVBLayer2.getChildren().add(text);
            buttonPanel.setCenter(middleVBLayer2);
            insertText.clear();
        });

        // Event handler for text color
        changeColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Color nextColor = textColors[currentColorIndex];
                textColorContainer = textColors[currentColorIndex];
                text.setFill(nextColor);
                currentColorIndex = (currentColorIndex + 1) % textColors.length;
            }
        });

        // Event handler for removing text overlay
        removeTextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                middleVBLayer2.getChildren().clear();
            }
        });

        // Add UI elements to bottom panel
        bottomHbox.getChildren().addAll(insertText, addTxtBtn, changeColor, removeTextBtn, confirmBtn);
        buttonPanel.setBottom(bottomHbox);

        // Add GUI components to root stack pane
        StackPane layers = new StackPane();
        layers.getChildren().addAll(imagePanel);
        root.getChildren().add(layers);
        StackPane layer2 = new StackPane();
        layer2.getChildren().addAll(buttonPanel);
        root.getChildren().add(layer2);

        // Set up scene and display the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Edit image");
        primaryStage.show();

        // Load OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Read selected image file
        Mat matImage = Imgcodecs.imread(selectedFile.getAbsolutePath());
        String imageName = selectedFile.getName();

        // Update displayed image
        updateImage(middleVB, matImage);

        // Store current image and create a copy for adjustments
        currentImage = matImage.clone();
        Mat brightnessImage = matImage.clone();
        finalImage = currentImage;

        // Event handlers for image filter buttons
        toggleGrayscaleButton.setOnAction(e -> {
            grayscaleEnabled = !grayscaleEnabled;
            finalImage = updateImage(middleVB, currentImage);
            if (toggleGrayscaleButton.getText().equals("Grayscale")) {
                toggleGrayscaleButton.setText("Remove Grayscale");
            } else {
                toggleGrayscaleButton.setText("Grayscale");
            }
        });

        increaseBrightnessButton.setOnAction(e -> {
            cumulativeBrightnessFactor += 0.2;
            if (cumulativeBrightnessFactor > 2.4) {
                cumulativeBrightnessFactor = 2.4;
            }
            currentImage = updateBrightness(middleVB, brightnessImage);
            finalImage = currentImage;
        });

        decreaseBrightnessButton.setOnAction(e -> {
            cumulativeBrightnessFactor -= 0.2;
            if (cumulativeBrightnessFactor < 0.2) {
                cumulativeBrightnessFactor = 0.2;
            }
            currentImage = updateBrightness(middleVB, brightnessImage);
            finalImage = currentImage;
        });

        toggleGaussianBlur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (toggleGaussianBlur.getText().equals("Blur")) {
                    Mat gaussianBlurMultiplier = applyGaussianBlur((applyGaussianBlur(currentImage, 9)), 9);
                    finalImage = updateImage(middleVB, applyGaussianBlur(gaussianBlurMultiplier, 9));
                    toggleGaussianBlur.setText("Remove Blur");
                } else {
                    finalImage = updateImage(middleVB, currentImage);
                    toggleGaussianBlur.setText("Blur");
                }
            }
        });

//        toggleSepiaButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                if (toggleSepiaButton.getText().equals("Sepia")) {
//                    ColorAdjust sepiaEffect = new ColorAdjust();
//                    sepiaEffect.setHue(0.05);
//                    sepiaEffect.setSaturation(0.8);
//                    sepiaEffect.setBrightness(-0.05);
//                    sepiaEffect.setContrast(1.2);
//                    middleVB.getChildren().get(0).setEffect(sepiaEffect);
//                    toggleSepiaButton.setText("Remove Sepia");
//                } else {
//                    middleVB.getChildren().get(0).setEffect(null);
//                    toggleSepiaButton.setText("Sepia");
//                }
//            }
//        });

        // Event handler for reset button
        resetBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cumulativeBrightnessFactor = 1.0;
                grayscaleEnabled = false;
                toggleGrayscaleButton.setText("Grayscale");
                currentImage = Imgcodecs.imread(selectedFile.getAbsolutePath());
                finalImage = updateImage(middleVB, currentImage);
            }
        });

        // Event handler for confirm button
        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Generate timestamp for saving filtered image
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String outputPath = "src/main/resources/com/mmprogramming/mmpassignment/Images/" + imageName + "_Filtered_" + timestamp + ".jpg";
                // Save filtered image
                Imgcodecs.imwrite(outputPath, finalImage);
                System.out.println("Filtered image saved to: " + outputPath);

                // Generate path for square image
                String outputPathSquare = "src/main/resources/com/mmprogramming/mmpassignment/square_image/" + imageName + "_Filtered_square" + timestamp + ".jpg";

                // Store text color
                String text_color = getColorString(textColorContainer);
                System.out.println(text_color);

                // Process text overlay if present
                String finalText = text.getText();
                if (!finalText.isEmpty()) {
                    star = true;
                    System.out.println(finalText + "+");
                    // Load heart icon
//                    Mat heartIcon = Imgcodecs.imread("src/main/resources/com/mmprogramming/mmpassignment/resource_image/heart.png", Imgcodecs.IMREAD_UNCHANGED);
                    URL resourceUrl = EditNewImage.class.getResource("/com/mmprogramming/mmpassignment/resource_image/heart.png");
                    if (resourceUrl == null) {
                        throw new IllegalArgumentException("Resource not found: /com/mmprogramming/mmpassignment/resource_image/heart.png");
                    }

                    // Convert URL to absolute path
                    String absolutePath = null;
                    try {
                        absolutePath = Paths.get(resourceUrl.toURI()).toFile().getAbsolutePath();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    // Read the image using the absolute path
                    Mat heartIcon = Imgcodecs.imread(absolutePath, Imgcodecs.IMREAD_UNCHANGED);

                    if (heartIcon.empty()) {
                        System.err.println("Cannot load heart icon!");
                        return;
                    }

                    // Add heart icon to square image
                    int gap = 10;
                    Mat squareImage = addHeartIcon(cropToSquare(finalImage), heartIcon, gap);
                    Imgcodecs.imwrite(outputPathSquare, squareImage);
                } else {
                    star = false;
                    System.out.println(finalText + "-");
                    // Crop image to square
                    Mat squareImage = cropToSquare(finalImage);
                    Imgcodecs.imwrite(outputPathSquare, squareImage);
                }

                // Add data to database
                DBHelper.addData(outputPath, star, finalText, text_color, outputPathSquare);

                // Close current stage and open a new instance of the main class
                Stage stage = (Stage) confirmBtn.getScene().getWindow();
                stage.close();
                mainClass main = new mainClass();
                main.start(new Stage());
            }
        });
    }

    // Method to convert a Color object to a string representation in the form of Color.COLOR_NAME
    private static String getColorString(Color color) {
        // Check predefined colors
        if (color.equals(Color.WHITE)) {
            return "Color.WHITE";
        } else if (color.equals(Color.BLACK)) {
            return "Color.BLACK";
        } else if (color.equals(Color.RED)) {
            return "Color.RED";
        } else if (color.equals(Color.BLUE)) {
            return "Color.BLUE";
        } else if (color.equals(Color.GREEN)) {
            return "Color.GREEN";
        }

        // For custom colors, return hexadecimal representation
        return color.toString();
    }


    /**
     * Crops the input image to a square shape.
     *
     * @param matImage The input image as a matrix.
     * @return The cropped square image.
     */
    public static Mat cropToSquare(Mat matImage) {
        // Calculate crop dimensions
        int cropX, cropY, cropWidth, cropHeight;
        if (matImage.width() > matImage.height()) {
            // Landscape orientation: crop horizontally
            cropHeight = matImage.height();
            cropWidth = matImage.height(); // Make it a square
            cropX = (matImage.width() - cropWidth) / 2; // Center the crop horizontally
            cropY = 0;
        } else {
            // Portrait orientation or square: crop vertically
            cropWidth = matImage.width();
            cropHeight = matImage.width(); // Make it a square
            cropX = 0;
            cropY = (matImage.height() - cropHeight) / 2; // Center the crop vertically
        }

        // Crop the image
        Rect cropRect = new Rect(cropX, cropY, cropWidth, cropHeight);
        return new Mat(matImage, cropRect);
    }

    /**
     * Adds a heart icon to the input image.
     *
     * @param matImage  The input image as a matrix.
     * @param heartIcon The heart icon image as a matrix.
     * @param gap       The gap between the border and the heart icon.
     * @return The image with the heart icon added.
     */
    public static Mat addHeartIcon(Mat matImage, Mat heartIcon, int gap) {
        // Ensure heart icon has an alpha channel (transparency)
        if (heartIcon.channels() < 4) {
            Imgproc.cvtColor(heartIcon, heartIcon, Imgproc.COLOR_BGR2BGRA);
        }

        // Resize the heart icon to fit the desired size
        int heartWidth = heartIcon.width();
        int heartHeight = heartIcon.height();
        int maxWidth = matImage.width() / 5; // Adjust the size of the heart icon (20% of the image width)
        int maxHeight = matImage.height() / 5; // Adjust the size of the heart icon (20% of the image height)

        if (heartWidth > maxWidth || heartHeight > maxHeight) {
            double aspectRatio = (double) heartWidth / heartHeight;
            if (heartWidth > heartHeight) {
                heartWidth = maxWidth;
                heartHeight = (int) (maxWidth / aspectRatio);
            } else {
                heartHeight = maxHeight;
                heartWidth = (int) (maxHeight * aspectRatio);
            }
            Size newSize = new Size(heartWidth, heartHeight);
            Imgproc.resize(heartIcon, heartIcon, newSize);
        }

        // Position to place the heart icon
        int xPos = matImage.width() - heartIcon.width() - gap;
        int yPos = gap;

        // Overlay the heart icon
        for (int y = 0; y < heartIcon.height(); y++) {
            for (int x = 0; x < heartIcon.width(); x++) {
                double[] heartPixel = heartIcon.get(y, x);
                double[] imagePixel = matImage.get(yPos + y, xPos + x);

                if (heartPixel.length == 4 && heartPixel[3] != 0) { // Check if the pixel is not fully transparent
                    double alpha = heartPixel[3] / 255.0;
                    double inverseAlpha = 1.0 - alpha;

                    // Blend the heart icon pixel with the image pixel
                    double[] blendedPixel = new double[3];
                    for (int i = 0; i < 3; i++) {
                        blendedPixel[i] = heartPixel[i] * alpha + imagePixel[i] * inverseAlpha;
                    }
                    matImage.put(yPos + y, xPos + x, blendedPixel);
                }
            }
        }

        return matImage;
    }


    /**
     * Updates the displayed image in the middle VBox.
     *
     * @param middleVB The middle VBox where the image is displayed.
     * @param matImage The input image as a matrix.
     * @return The resized image as a matrix.
     */
    private Mat updateImage(VBox middleVB, Mat matImage) {
        middleVB.getChildren().clear();

        if (grayscaleEnabled) {
            matImage = convertToGrayscale(matImage);
        }

        // Calculate the aspect ratio
        double aspectRatio = (double) matImage.width() / matImage.height();

        // Define the desired width and height based on VBox size
        double desiredWidth, desiredHeight;
        if (matImage.width() > matImage.height()) {
            desiredWidth = middleVB.getWidth();
            desiredHeight = (middleVB.getWidth() / aspectRatio);
        } else {
            desiredHeight = middleVB.getHeight();
            desiredWidth = (middleVB.getHeight() * aspectRatio);
        }

        image_width = desiredWidth;

        // Resize the image using OpenCV
        Mat resizedMat = new Mat();
        Size newSize = new Size(desiredWidth, desiredHeight);
        Imgproc.resize(matImage, resizedMat, newSize);

        // Convert the resized Mat to a JavaFX Image
        Imgcodecs.imwrite("temp_resized_image.jpg", resizedMat);
        Image resizedImage = new Image(new File("temp_resized_image.jpg").toURI().toString());

        // Create ImageView and set the resized image
        ImageView imageViewResized = new ImageView(resizedImage);

        // Add ImageView to middleVB
        middleVB.getChildren().addAll(imageViewResized);
        return resizedMat;
    }

    /**
     * Updates the brightness of the displayed image.
     *
     * @param middleVB     The middle VBox where the image is displayed.
     * @param currentImage The current image as a matrix.
     * @return The adjusted image as a matrix.
     */
    private Mat updateBrightness(VBox middleVB, Mat currentImage) {
        // Adjust brightness relative to the original image
        Mat adjustedImage = adjustBrightness(currentImage.clone(), cumulativeBrightnessFactor);
        updateImage(middleVB, adjustedImage); // Update the displayed image
        return adjustedImage;
    }

    /**
     * Adjusts the brightness of the input image.
     *
     * @param image  The input image as a matrix.
     * @param factor The brightness adjustment factor.
     * @return The adjusted image as a matrix.
     */
    private Mat adjustBrightness(Mat image, double factor) {
        double alpha = cumulativeBrightnessFactor * factor;
        // Ensure alpha stays within the range of 0.1 to 2
        alpha = Math.max(0.1, Math.min(2.0, alpha));
        Mat adjustedImage = new Mat();
        image.convertTo(adjustedImage, -1, alpha, 0);
        return adjustedImage;
    }

    /**
     * Converts the input image to grayscale.
     *
     * @param inputImage The input image as a matrix.
     * @return The grayscale image as a matrix.
     */
    private Mat convertToGrayscale(Mat inputImage) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(inputImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        return grayImage;
    }

    /**
     * Applies Gaussian blur to the input image.
     *
     * @param image  The input image as a matrix.
     * @param radius The radius of the Gaussian blur kernel.
     * @return The blurred image as a matrix.
     */
    private Mat applyGaussianBlur(Mat image, double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Invalid radius for Gaussian blur. Radius must be greater than 0.");
        }
        Mat updatedImage = new Mat();
        Imgproc.GaussianBlur(image, updatedImage, new Size(radius, radius), 0);

        return updatedImage;
    }
}

