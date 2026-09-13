package cs250;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
    // declare scene, imageview, and borderpane so they are accessible
    BorderPane pane = new BorderPane();      
    Scene scene = new Scene(pane);
    ImageView imageView = new ImageView();
    private Stage primaryStage;
    // track if the image has been saved or not
    private boolean isSaved = true;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        this.primaryStage = primaryStage;
        // set scene/stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("MS Paint");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        // have a menu bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem closeItem = new MenuItem("Close");

        menuBar.getMenus().add(fileMenu);
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, closeItem);
        
        pane.setTop(menuBar);
        
        // open an image and show it
        openItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image");

            File file = fileChooser.showOpenDialog(primaryStage);
            pane.setCenter(imageView);
            isSaved = false;
            // if file is not null, load the image and set it to the ImageView
            // in case they hit cancel in the file chooser
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setFitWidth(400);
                imageView.setPreserveRatio(true);
            }
        });
        // save controls
        // links to saveImage and saveImageAs methods
        saveItem.setOnAction(event -> saveImage());
        saveAsItem.setOnAction(event-> saveImageAs());
    
        // close controls
        // links to close method
        closeItem.setOnAction(event -> close());
        // handles window close button
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            close(); 
        });

        primaryStage.show();
    }
    // makes program remember the file that was opened
    private File currentFile;
    
    // save image method
    private void saveImage() {
        // if the current file is null, save it as a new file
        if (currentFile == null) {
            saveImageAs();
            return;
        } 
        
        // if a file is already open, override that file with the new image
        Image image = imageView.getImage();
        try {
            ImageIO.write(
                SwingFXUtils.fromFXImage(image, null), 
                "png", 
                currentFile
            );
            isSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // save as method
    // saves image as a new file
    private void saveImageAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );
        File file = fileChooser.showSaveDialog(this.primaryStage);

        if (file != null) {
            currentFile = file;
            saveImage();
        }
    }

    // close method
    private void close() {
        // if there are unsaved changes, show confirmation pop-up
        if (!isSaved) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes.");
            alert.setContentText("Are you sure you want to close?");

            ButtonType pick = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (pick == ButtonType.OK) {
                primaryStage.close();
            } 
        } else {
            primaryStage.close();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
