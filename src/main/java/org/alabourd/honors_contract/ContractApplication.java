package org.alabourd.honors_contract;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.alabourd.honors_contract.Panes.ListPane;
import org.alabourd.honors_contract.Panes.SearchPane;

import java.util.ArrayList;

/**
 * Created by Aidan Labourdette
 * 
 * This is the main class for the application.
 * 
 * @author Aidan Labourdette
 * @version 1.0
 */

public class ContractApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();

        // courseList to be used in both generatePane & selectPane
        ArrayList<FileInformation> courseList = new ArrayList<>(0);

        ListPane listPane = new ListPane(courseList);
        SearchPane searchPane = new SearchPane(courseList, listPane);

        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab();
        tab1.setText("Search Files");
        tab1.setClosable(false);
        tab1.setContent(searchPane);

        Tab tab2 = new Tab();
        tab2.setText("Analyze Files");
        tab2.setClosable(false);
        tab2.setContent(listPane);

        tabPane.getSelectionModel().select(0);
        tabPane.getTabs().addAll(tab1, tab2);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        root.getChildren().add(tabPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        stage.setTitle("FileSize Identifier");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }
}
