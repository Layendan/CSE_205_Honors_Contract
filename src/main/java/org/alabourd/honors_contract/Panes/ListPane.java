package org.alabourd.honors_contract.Panes;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import org.alabourd.honors_contract.Comparator.FileDateComparator;
import org.alabourd.honors_contract.Comparator.FileNameComparator;
import org.alabourd.honors_contract.Comparator.FilePathComparator;
import org.alabourd.honors_contract.Comparator.FileSizeComparator;
import org.alabourd.honors_contract.FileInformation;
import org.alabourd.honors_contract.SortingTypes;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ListPane extends BorderPane {

    // Declare variables
    ArrayList<FileInformation> files;
    boolean isAscending;
    SortingTypes sortingType;
    ScrollPane scrollPane;
    GridPane gridPane;
    HBox topPane;
    ComboBox<String> sorting;
    Text sortingDescription;

    /**
     * Constructor for the ListPane class
     * 
     * @param list Initial list of files to display
     */
    public ListPane(ArrayList<FileInformation> list) {
        // Initialize variables
        files = list;
        sortingType = SortingTypes.NAME;
        isAscending = true;
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.autosize();
        gridPane.setCache(true);

        // Create grid pane
        scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);

        // Setting nodes of grid pane
        sortingDescription = new Text("Sorting by: ");
        sortingDescription.setTranslateX(10);
        sorting = new ComboBox<>();
        sorting.getItems().addAll("Name", "Path", "Size", "Last Modified");
        sorting.getSelectionModel().selectFirst();
        sorting.setOnAction(new ComboBoxActionHandler());
        HBox hBox = new HBox(sortingDescription, sorting);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER_LEFT);
        topPane = hBox;

        // Setting nodes of border pane
        setTop(topPane);
        setCenter(scrollPane);
        resetCanvas();
    }

    // Not Used
    public ListPane(ArrayList<FileInformation> list, SortingTypes sortingType, boolean isAscending) {
        files = list;
        this.sortingType = sortingType;
        this.isAscending = isAscending;
        resetCanvas();
    }

    /**
     * Resets the files
     * 
     * @param list List of files to display
     */
    public void setFiles(ArrayList<FileInformation> list) {
        files = list;
        sorting.getSelectionModel().selectFirst();
    }

    /**
     * Sets the sorting type
     * 
     * @param type The sorting type
     */
    public void setSortingType(SortingTypes type) {
        sortingType = type;
        resetCanvas();
    }

    /**
     * Get the Grid Pane
     * 
     * @return The Grid Pane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Sorts files, then recreates the grid pane
     */
    public void resetCanvas() {
        // Using CompleteableFuture to make sure the sorting is done before
        CompletableFuture<ArrayList<FileInformation>> future = new CompletableFuture<>();

        Thread thread = sort(future);

        future.whenComplete((fileInformations, throwable) -> {
            try {
                setPane(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    /**
     * Creates nodes for the grid pane and resets the grid pane
     * 
     * @param filesArray The list of files to display
     */
    private void setPane(ArrayList<FileInformation> filesArray) {
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.autosize();
        scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setCache(true);
        scrollPane.setCacheHint(CacheHint.SPEED);

        CompletableFuture<GridPane> completableFuture = new CompletableFuture<>();

        completableFuture.whenComplete((gridPane, throwable) -> Platform.runLater(() -> setCenter(scrollPane)));

        // Multi-threading
        new Thread(() -> {
            if (filesArray.size() == 0) {
                gridPane.add(new Text("There are no files that fit the given settings"), 0, 0);
            } else {
                boolean isOdd = false;
                for (FileInformation file : filesArray) {
                    boolean finalIsOdd = isOdd;
                    Platform.runLater(() -> createFilePane(file, finalIsOdd));
                    isOdd = !isOdd;
                }
            }
            completableFuture.complete(gridPane);
        }).start();
    }

    /**
     * Sorts the files
     * 
     * @param future The future to complete
     * @return The thread
     */
    private Thread sort(CompletableFuture<ArrayList<FileInformation>> future) {

        // Multi-threading
        return new Thread(() -> {
            System.out.println("Sorting...");
            switch (sortingType) {
            case NAME:
                files.sort(new FileNameComparator(isAscending));
                break;
            case PATH:
                files.sort(new FilePathComparator(isAscending));
                break;
            case SIZE:
                files.sort(new FileSizeComparator(isAscending));
                break;
            case LAST_MODIFIED:
                files.sort(new FileDateComparator(isAscending));
                break;
            default:
                System.out.println("Sorting error");
                break;
            }

            future.complete(files);
        });
    }

    /**
     * Creates a file pane
     * 
     * @param file  The file to display
     * @param isOdd Whether the current file is odd or not. Used for alternating
     *              colors
     */
    private synchronized void createFilePane(FileInformation file, boolean isOdd) {

        // Get last row to add text to
        int rowCount = gridPane.getRowCount();

        // File path
        Text filePath = createTextPane(file.getRelativeFilePath(), isOdd);
        filePath.setCursor(Cursor.HAND);
        GridPane.setHgrow(filePath, Priority.ALWAYS);
        filePath.setOnMouseClicked(new MouseHandler());

        // Setting the actual text in grid row
        gridPane.add(createTextPane(file.getFileName(), isOdd), 0, rowCount);
        gridPane.add(filePath, 1, rowCount);
        gridPane.add(createTextPane(file.getFileSizeAsString(), isOdd), 2, rowCount);
        gridPane.add(createTextPane(file.getLastModifiedAsString(), isOdd), 3, rowCount);
    }

    /**
     * Creates a text pane
     * 
     * @param text  The text to display
     * @param isOdd Whether the current file is odd or not. Used for alternating
     *              colors
     * @return The text pane
     */
    private Text createTextPane(String text, boolean isOdd) {
        Text textPane = new Text(text);

        // Setting the color of the text
        if (isOdd)
            textPane.setFill(Color.color(0.4, 0.4, 0.4));

        textPane.setFont(Font.font("Open Sans", FontWeight.THIN, 12));
        return textPane;
    }

    /**
     * Handles when user clicks on a file, opens the file in file explorer
     */
    private static class MouseHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            Text pane = (Text) event.getSource();
            String filePath = pane.getText();
            String OSName = System.getProperty("os.name");

            // Open file in file explorer
            // https://stackoverflow.com/questions/40963947/open-a-file-in-default-file-explorer-and-highlight-it-using-javafx-or-plain-java/51017185

            try {

                if (OSName.startsWith("Windows")) {
                    Runtime.getRuntime().exec("explorer /select, " + filePath);
                } else { // Mac OS and Linux are the same
                    Runtime.getRuntime().exec("open -R " + filePath);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the sorting type when changed in the combo box and resets the grid pane
     */
    private class ComboBoxActionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            switch (sorting.getValue()) {
            case "Name":
                sortingType = SortingTypes.NAME;
                break;
            case "Path":
                sortingType = SortingTypes.PATH;
                break;
            case "Size":
                sortingType = SortingTypes.SIZE;
                break;
            case "Last Modified":
                sortingType = SortingTypes.LAST_MODIFIED;
                break;
            default:
                break;
            }

            // Not asynchronous and freezes the UI if list is big, but it works
            resetCanvas();
        }
    }
}