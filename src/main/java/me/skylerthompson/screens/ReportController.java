package me.skylerthompson.screens;

import com.google.common.collect.Maps;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Map;

public class ReportController {

    public TextArea textArea;
    public Button save;

    private String reportContent;

    public void initialize() {
        handleEvents();
    }

    public void initData(String reportContent) {
        this.reportContent = reportContent;
    }

    public void generateReport() {
        textArea.setText(reportContent);
    }

    public void handleEvents() {
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveReport();
            }
        });

        textArea.setEditable(false);
    }

    public void saveReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        File selectedFile = fileChooser.showSaveDialog(save.getScene().getWindow());
        if (selectedFile != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                writer.write(reportContent);
                writer.close();
            } catch (IOException ie) {
                System.out.println("Error writing file!");
            }
        }
    }

}
