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
  -

## MUHAMAD FIRDAUS BIN YAMAT
  -

## MUHAMMAD HAZIQ FARHAN BIN ARDHI
- Develop an imageviewer page
- View the uploaded image with a larger scale
- Add caption in the photos
- Create save button to save caption into database and display the caption

### <u> *Class: ImageViewer* </u>

### Fields:

*imageView*: An ImageView object for displaying images.

*root*: A BorderPane object to hold the layout components.

*captionDisplayBox*: A VBox for displaying captions.

*DATABASE_URL*: A constant string representing the URL for the database.

### Methods:

*start(Stage primaryStage)*: Initializes the GUI components and displays the image viewer interface.

*addCaption(ActionEvent event)*: Event handler method to add a caption to the image.

*saveCaptionToDatabase(String caption)*: Saves the caption to the database.

*displayCaptionsFromDatabase()*: Fetches and displays captions from the database.

*styleButton(Button button)*: Sets the preferred width and height for buttons.

*main(String[] args)*: Main method to launch the application.

### Other Components:

*`Button captionButton`*: Button to add a caption.

*Button backButton*: Button to exit.

*HBox buttonBox*: Holds the captionButton.

*HBox backButtonBox*: Holds the backButton.

*StackPane imagePane*: Centers the image.

*Scene scene*: The scene to be displayed on the stage.

### Database Handling:

The application interacts with a SQLite database to store and retrieve captions associated with images.

### User Interface:

The interface consists of an image displayed in the center, with options to add captions and exit.

Captions are displayed at the bottom of the interface.

### Functionality:

Users can add captions to the displayed image, which are then saved to the database.

Captions stored in the database are fetched and displayed in the UI.

  

---
## MUHAMMAD HAZIQ HUDZAIRY BIN HUSSIN

### <u> *Develop database class and functions:* </u>

### 1. Database Connection

### Method Name: `createDatabase()`

- **Description:** Establishes a connection to the SQLite database.
- **Features:**
  - Uses JDBC to connect to the SQLite database.
  - Creates the database if it doesn't exist.
  - Invokes the `createTable()` method to create the necessary table if the connection is successful.

### 2. Table Creation

### Method Name: `createTable(Connection conn)`

- **Description:** Creates the `ImageData` table if it doesn't exist.
- **Features:**
  - Defines the table schema with columns for ID, image file path, star rating, text, text color, and square file path.
  - Executes a SQL statement to create the table.

### 3. Data Manipulation

### Methods:

- `addData(String imageFilePath, boolean star, String text, String text_color, String squareFilePath)`
  - **Description:** Inserts new data into the `ImageData` table.

- `removeData(int id)`
  - **Description:** Deletes a record from the `ImageData` table based on the given ID.

- `editData(int id, String imageFilePath, boolean star, String text, String text_color, String squareFilePath)`
  - **Description:** Updates an existing record in the `ImageData` table.

- **Features:**
  - Utilize prepared statements to prevent SQL injection.
  - Handles various CRUD (Create, Read, Update, Delete) operations on database records.

### 4. Data Retrieval

### Methods:

- `getSquarePaths()`
  - **Description:** Retrieves a list of square file paths from the `ImageData` table.

- `getImageID(String squareFilePath)`
  - **Description:** Retrieves the image ID associated with a given square file path.

- `getStarValue(String squareFilePath)`
  - **Description:** Retrieves the boolean value associated with a given square file path to differentiate between captioned image on non captioned image.

- `getImagePathClicked(String squareFilePath)`
  - **Description:** Retrieves the image file path associated with a given square file path.

- **Features:**
  - Executes SQL queries to fetch data from the database.
  - Provides methods to retrieve specific data based on criteria.

### 5. Exception Handling

### Method: `handleSQLException(SQLException e)`

- **Description:** Handles SQL exceptions by printing error messages and stack traces.
- **Features:**
  - Logs detailed information about SQL exceptions to aid in debugging.

### <u> *Develop methods needed for the EditNewImage class* </u>
### Features and Functionality

1. **Image Editing Controls:**
  - Users can toggle grayscale conversion of the image.
  - Brightness adjustment buttons allow users to increase or decrease the brightness of the image.
  - Blur button applies a Gaussian blur effect to the image.

2. **Text Overlay:**
  - Users can input text in a text field to add text overlays to the image.
  - A color change button allows users to change the color of the text overlay.
  - Users can remove text overlays with a button.

3. **Image Saving:**
  - Users can confirm their edits and save the resulting image.
  - The saved image includes applied filters, text overlays, and adjustments.
  - A separate square image is generated and saved for use in the application.

4. **Database Integration:**
  - The class integrates with the `DBHelper` class to perform database operations.
  - After confirming edits, relevant data such as file paths, text, and star ratings are stored in the database.

5. **Exception Handling:**
  - The class includes exception handling for SQL exceptions, providing error messages and stack traces for debugging.

## Methods

- `start(Stage primaryStage)`: Entry point of the application. Initializes the user interface components and sets up event handlers.
- `getColorString(Color color)`: Converts a Color object to a string representation.
- `cropToSquare(Mat matImage)`: Crops the input image to a square shape.
- `updateImage(VBox middleVB, Mat matImage)`: Updates the displayed image in the middle VBox.
- `updateBrightness(VBox middleVB, Mat currentImage)`: Updates the brightness of the displayed image.
- `adjustBrightness(Mat image, double factor)`: Adjusts the brightness of the input image.
- `convertToGrayscale(Mat inputImage)`: Converts the input image to grayscale.

---

## UMAR MUZAKKI BIN ZULKIFLI
  - Design and develop the Home page that displays all the uploaded photos.
  - Develop the method to send selected photos to the slideshow creation.
  - Build the logic to dynamically navigate to each image.

# Description of Functions and Outputs
| Syntax | Description |
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
| `addGraphics()`                                          | Method that displays a dialog for selecting a graphic to add to the slide.  
| `layout(Stage primaryStage)`                              | Method that creates and configures the layout for the application.                               |
| `uploadImage(Stage primaryStage)`                         | Method that handles the functionality to upload an image.                                         |
| `handleImageClick(String imagePath)`                      | Method that handles the click event for an image.                                                 |
| `displayImages(FlowPane photo_grid)`                      | Method that displays images fetched from the database in the photo grid.                           |
| `clearSelection(FlowPane photoGrid)`                      | Method that clears the selection of images in the photo grid.                                      |
| `createSlideshow(Stage primaryStage, List<Image> selectedImage)` | Method that creates a slideshow with selected images.                                         |
| `main(String[] args)`| Main method that launches the application.     
