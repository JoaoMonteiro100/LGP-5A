package com.lgp5.fw.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;


public class AnalysisController {
    @FXML
    private ComboBox waveToAnalyse;
    @FXML
    private Button startAnalysis;



    public AnalysisController() {
    }


    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        waveToAnalyse.getItems().addAll("Delta", "Theta", "Alfa 1", "Alfa 2", "Beta 1", "Beta 2", "Gamma 1", "Gamma 2");

/*
        selectDeviceStartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object comboBox = waveToAnalyse.getSelectionModel().getSelectedItem();
                if(comboBox != null) {
                    String comboBoxValue = comboBox.toString();
                    switch (comboBoxValue) {
                        case "NeuroSky Mindset":
                            launchNeuroSkyView();
                            //get a handle to the stage
                            Stage stage = (Stage) waveToAnalyse.getScene().getWindow();
                            //close current window
                            stage.close();
                            break;

                        default:
                            break;
                    }
                }
            }
        });*/
    }


    private void launchNeuroSkyView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/menuView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
