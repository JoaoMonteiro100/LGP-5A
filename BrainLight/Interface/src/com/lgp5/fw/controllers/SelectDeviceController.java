package com.lgp5.fw.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;


public class SelectDeviceController {
    @FXML
    private ComboBox selectDeviceComboBox;
    @FXML
    private Button selectDeviceStartButton;



    public SelectDeviceController() {
    }


    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        selectDeviceComboBox.getItems().addAll("NeuroSky Mindset", "Emotiv Epoc");


        selectDeviceStartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object comboBox = selectDeviceComboBox.getSelectionModel().getSelectedItem();
                if(comboBox != null) {
                    String comboBoxValue = comboBox.toString();
                    Stage stage;
                    switch (comboBoxValue) {
                        case "NeuroSky Mindset":
                            launchNeuroSkyView();
                            //get a handle to the stage
                            stage = (Stage) selectDeviceComboBox.getScene().getWindow();
                            //close current window
                            stage.close();
                            break;
                        case "Emotiv Epoc":
                        	launchEmotivView();
                            //get a handle to the stage
                            stage = (Stage) selectDeviceComboBox.getScene().getWindow();
                            //close current window
                            stage.close();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }



    private void launchNeuroSkyView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/fw/views/menuNeuroSkyView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainLight");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
    private void launchEmotivView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/fw/views/menuEmotivView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainLight");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}
