package me.skylerthompson;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        stage.setTitle("OSM Software");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}