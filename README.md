# Steps to Execute Project
## Prerequisites
- Java SDK 21
- JavaFX SDK 21
- JavaFX Media Package (installed from Maven repository)
- [OpenCV 4.9.0](https://opencv.org/releases/)
- [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc)

## How to run the project
1. Open the project folder in any IDE of your choice.
2. Install all the prerequisites into the project.
3. Create a new Run Configuration with mainClass as the Main Class.
4. Build and run the mainClass.

# Project Description
Memoria is your personal photo album where you can safely express yourself. With Memoria, you can upload personal
photos, put on filters, and write captions so that the memories will forever live on. Memoria also allows you to create
a slideshow from selected images. Be creative with your personal slideshow as you can play your favourite songs, add
visual effects, and write meaningful texts.

To keep your privacy safe, Memoria creates a database in your own machine, and stores your personal photos locally.
As a user, you will not have to worry about security as no personal data is shared online or with any third-party app.

# Members and Tasks Assignment

## MUHAMMAD DZAKY ABDURRASYID
  - Developed the main layout of the slideshow, ensuring a clean and user-friendly interface.
  - Implemented the VBox and HBox layout structures to organize the components neatly.
  - Configured ImageView and Label properties, such as size, alignment, and style, for optimal display.

## MUHAMAD FIRDAUS BIN YAMAT
  - Develop the graphic effects adding into the slideshow.
  - Create functions to allow caption adding during the slideshow. 

## MUHAMMAD HAZIQ FARHAN BIN ARDHI
  - Develop an imageviewer page
  - View the uploaded image with a larger scale
  - Add caption in the photos
  - Create save button to save caption into database and display the caption
  
## MUHAMMAD HAZIQ HUDZAIRY BIN HUSSIN
  - Develop database class and functions.
  - Develop the uploading image functions.
  - Create filters and manipulate images to be uploaded.

## UMAR MUZAKKI BIN ZULKIFLI
  - Design and develop the Home page that displays all the uploaded photos.
  - Develop the method to send selected photos to the slideshow creation.
  - Build the logic to dynamically navigate to each image.

# Description of Functions and Outputs

## DBHelper Class Functions
| Function | Description |
|----------|-------------|
| `createDatabase` | Creates the SQLite database and initializes the `ImageData` table if it does not exist. |
| `createTable` | Creates the `ImageData` table if it does not exist. Takes a `Connection` object as a parameter. |
| `addData` | Adds a new record to the `ImageData` table. Parameters include the image file path, star flag, text, text color, and square file path. |
| `removeData` | Removes a record from the `ImageData` table by ID. Takes an integer `id` as a parameter. |
| `getSquarePaths` | Retrieves a list of all square file paths from the `ImageData` table. Returns a list of strings. |
| `getStarValue` | Retrieves the star value of the image record by square file path. Takes a string `squareFilePath` as a parameter and returns a boolean. |
| `getOriginalImage` | Retrieves the original image file path by square file path. Takes a string `squareFilePath` as a parameter and returns a string. |
| `handleSQLException` | Handles and prints details of SQL exceptions. Takes a `SQLException` object as a parameter. |

## mainClass Functions
| Function | Description |
|----------|-------------|
| `start` | Initializes the primary stage and sets up the main application layout. Calls `DBHelper.createDatabase()` to initialize the database. |
| `layout` | Sets up the user interface layout, including background image, buttons, and photo grid. Handles button actions for selecting and uploading images. Returns a `StackPane` as the root node. |
| `uploadImage` | Opens a file chooser dialog to upload an image. If an image is selected, it launches the `EditNewImage` stage. Takes `Stage` as a parameter. |
| `handleImageClick` | Handles the event when an image is clicked. Closes the primary stage and opens the `ImageViewer` stage with the clicked image. Takes `Stage` and `String` imagePath as parameters. |
| `displayImages` | Retrieves image paths from the database and displays them in a `FlowPane`. Handles image click events for selecting and viewing images. Takes `Stage` and `FlowPane` as parameters. |
| `clearSelection` | Clears the selection of images, removing any highlights. Takes `FlowPane` as a parameter. |
| `createSlideshow` | Creates a slideshow from the selected images and launches the `SlideShowMaker` stage. Takes `Stage` and a list of `Image` objects as parameters. |

## EditNewImage Functions
| Method | Description |
| ----------- | ----------- |
| `EditNewImage(File selectedFile)`   | Constructor method that initializes an instance of the `EditNewImage` class with a selected file. |
| `start(Stage primaryStage)`         | Method that initializes and displays the GUI components for editing images.                   |
| `getColorString(Color color)`       | Method that converts a `Color` object to a string representation.                              |
| `cropToSquare(Mat matImage)`        | Method that crops the input image to a square shape.                                           |
| `updateImage(VBox middleVB, Mat matImage)` | Method that updates the displayed image in the middle VBox.                          |
| `updateBrightness(VBox middleVB, Mat currentImage)` | Method that updates the brightness of the displayed image.                           |
| `adjustBrightness(Mat image, double factor)` | Method that adjusts the brightness of the input image.                                      |
| `convertToGrayscale(Mat inputImage)` | Method that converts the input image to grayscale.                                             |
| `applyGaussianBlur(Mat image, double radius)` | Method that applies Gaussian blur to the input image.                                       |
| `SlideShowMaker(List<Image> imageList)`                  | Constructor method that initializes an instance of the `SlideShowMaker` class with a list of images. |
| `start(Stage primaryStage)`                              | Method that initializes and displays the GUI components for creating a slideshow.                |
| `updateImage()`                                          | Method that updates the image in the `ImageView`.                                                |
| `showTextInputDialog()`                                  | Method that displays a dialog for editing the text of the current slide.                          |
| `addGraphics()`                                          | Method that displays a dialog for selecting a graphic to add to the slide.                        |
