package edu.uca;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public ListView<String> modulesListView;
    private String selectedItem = "";

    public void initialize() {
        ObservableList<String> modules = FXCollections.observableArrayList("Linear Programming", "Time Series Forecasting", "Casual Regression Forecasting");
        modulesListView.setItems(modules);
    }

    public void openModule(MouseEvent mouseEvent) {
        //Get the selected module
        selectedItem = modulesListView.getSelectionModel().getSelectedItem().toString();

        //Open the module in a new window
        try {
            if (selectedItem.equalsIgnoreCase("Linear Programming")) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("linear-programming.fxml"));

                Scene scene = new Scene(fxmlLoader.load(), 500, 500);
                Stage stage = new Stage();
                stage.setTitle("Linear Programming");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException ie) {
            System.out.println("IOException opening new window" + ie);
        }

        //Now deselect it in the UI
        modulesListView.getSelectionModel().clearSelection();


        System.out.println(selectedItem);
    }

}
