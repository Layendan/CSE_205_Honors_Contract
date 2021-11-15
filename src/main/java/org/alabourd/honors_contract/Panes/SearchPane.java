package org.alabourd.honors_contract.Panes;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.alabourd.honors_contract.FileInformation;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchPane extends BorderPane {

    // Declare the variables
    Text errorText, pathFieldDescription, totalSizeText, sizeCapDescription;
    TextField pathField;
    Button startTask, findFolder;
    ComboBox<String> sizeCap;

    GridPane gridPane;
    ListPane listPane;
    FlowPane fileChooser;

    ProgressBar progressBar;

    ArrayList<FileInformation> files;

    /**
     * Constructor
     * 
     * @param list     Initial list of files to display
     * @param listPane ListPane object to update
     */
    public SearchPane(ArrayList<FileInformation> list, ListPane listPane) {
        // Initialize the variables
        errorText = new Text();
        pathFieldDescription = new Text("Please Enter Path:");
        totalSizeText = new Text("Total Size of Files: ");
        startTask = new Button("Start File Searching");
        findFolder = new Button("Find the Folder");
        sizeCap = new ComboBox<>();
        sizeCapDescription = new Text("Minimum Size of Files");
        pathField = new TextField();
        gridPane = new GridPane();
        fileChooser = new FlowPane();
        this.listPane = listPane;
        progressBar = new ProgressBar();
        files = new ArrayList<>(0);

        // Infinite Loading for when the user is searching
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        // Error text as red
        errorText.setFill(Color.RED);

        // When button is clicked run the search
        startTask.setOnMouseClicked(new MouseHandler());
        startTask.setMinHeight(30);

        // Choose how big the files need to be, to be included
        sizeCap.setItems(FXCollections.observableArrayList("5 MB", "10 MB", "20 MB", "50 MB", "100 MB", "200 MB",
                "500 MB", "1 GB"));
        sizeCap.getSelectionModel().select("20 MB");
        sizeCap.setOnAction(event -> setBottom(startTask));

        // Opens file explorer to choose the folder to search
        findFolder.setOnMouseClicked(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Open Folder");
            Stage stage = new Stage();
            stage.requestFocus();
            stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    stage.close();
                }

                System.out.println("New Focus value: " + newValue);
            });
            File file = chooser.showDialog(stage);

            // Not Canceled
            if (file != null) {
                pathField.setText(file.getAbsolutePath());
            }

            setBottom(startTask);
        });

        // Resets the file search button if user wants to search again
        pathField.setOnMouseClicked(event -> setBottom(startTask));

        fileChooser.setHgap(10);
        fileChooser.getChildren().addAll(pathField, findFolder);

        gridPane.setVgap(5);
        gridPane.add(errorText, 0, 0);
        gridPane.add(pathFieldDescription, 0, 1);
        gridPane.add(fileChooser, 0, 2);
        gridPane.add(sizeCapDescription, 0, 3);
        gridPane.add(sizeCap, 0, 4);

        setCenter(gridPane);
        setBottom(startTask);

        gridPane.setTranslateY(5);
        gridPane.setTranslateX(50);
        progressBar.setTranslateX(50);
        progressBar.setTranslateY(-50);
        totalSizeText.setTranslateX(50);
        totalSizeText.setTranslateY(-50);
        startTask.setTranslateX(50);
        startTask.setTranslateY(-50);
    }

    /**
     * Get files from declared path
     * 
     * @param path Path to search
     */
    private void getFiles(String path) {
        files.clear();
        Path absPath;
        long size = 0L;
        long fileSize = (long) (2 * 1e7);

        // Get desired size of files to include
        switch (sizeCap.getValue()) {
        case "5 MB":
            fileSize = (long) (5 * 1e6);
            break;
        case "10 MB":
            fileSize = (long) (1e7);
            break;
        case "20 MB":
            fileSize = (long) (2 * 1e7);
            break;
        case "50 MB":
            fileSize = (long) (5 * 1e7);
            break;
        case "100 MB":
            fileSize = (long) (1e8);
            break;
        case "200 MB":
            fileSize = (long) (1 * 1e8);
            break;
        case "500 MB":
            fileSize = (long) (5 * 1e8);
            break;
        case "1 GB":
            fileSize = (long) (1e9);
            break;
        }

        try {
            absPath = Path.of(path);

            System.out.println("Path defined: " + path);

            size = getSize(absPath, files, fileSize);

            if (size == 0)
                throw new Exception("File Does Not Exist or Is Empty");

        } catch (Exception e) {
            errorText.setText(e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("The size of the directory \"" + path + "\" is: " + size + " bytes");

        // Change ending of text depending on size of files
        String ending = "bytes";
        if (size > 1e+9) {
            size *= 1e-9;
            ending = "GB";
        } else if (size > 1e6) {
            size *= 1e-6;
            ending = "MB";
        }

        // Format the size to 2 decimal places
        totalSizeText.setText("Current Size of Files: " + new DecimalFormat("#,##0.00").format(size) + " " + ending);

        // Allowing editing of information
        Platform.runLater(() -> {
            pathField.setEditable(true);
            pathField.setDisable(false);
            fileChooser.setDisable(false);
            sizeCap.setDisable(false);

            setBottom(totalSizeText);
        });
    }

    /**
     * Recursive method to get the size of the files and adds it to the list of
     * files
     * 
     * @param path     Path to search
     * @param files    List of files to add to
     * @param fileSize Minimum size of files to include
     * @return Size of files
     */
    private long getSize(Path path, ArrayList<FileInformation> files, long fileSize) {
        long size = 0L;
        File[] fileList = path.toFile().listFiles();

        if (fileList == null) {
            return 0L;
        }

        for (File file : fileList) {
            if (file.isDirectory()) {
                // have to print directory before unless all the directories print at the same
                // time
                System.out.println("Directory accessed: " + file.getName());
                size += getSize(file.toPath(), files, fileSize);
            } else {
                size += file.length();
                if (file.length() > fileSize && !file.getName().equals(".DS_Store")) {
                    files.add(new FileInformation(file));
                }

                System.out.println("Total Size of file: " + file.getName() + " : " + file.length() + " bytes");
            }
        }

        return size;
    }

    /**
     * When button is clicked, search files using parameters defined by user
     */
    private class MouseHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            // Disable editing of information to prevent errors
            pathField.setEditable(false);
            pathField.setDisable(true);
            fileChooser.setDisable(true);
            sizeCap.setDisable(true);

            // Reset error text
            errorText.setText("");

            // Show indefinite progress bar
            setBottom(progressBar);

            // Multi-threading
            Thread thread = new Thread(() -> getFiles(pathField.getText()));
            thread.setDaemon(true);
            thread.start();

            // Wait for thread to finish
            new Thread(() -> {
                while (thread.isAlive()) {
                    try {
                        System.out.println("Thread is still running");

                        // Busy Waiting, should remove but too lazy
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Platform.runLater(() -> errorText.setText(e.getMessage()));
                        e.printStackTrace();
                    }
                }

                // Once thread is finished, set the files in listPane
                Platform.runLater(() -> listPane.setFiles(files));

                Platform.runLater(() -> listPane.resetCanvas());
            }).start();
        }
    }
}
