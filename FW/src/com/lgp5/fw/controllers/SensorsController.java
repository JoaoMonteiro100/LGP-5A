package com.lgp5.fw.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;


public class SensorsController {
    @FXML private ComboBox<String> sensorsList;
    @FXML private Button applySensors;

    public SensorsController() {
    }

    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        sensorsList.getItems().addAll("Frontal lobe (red)", "Parietal lobe (blue)", "Temporal lobe (yellow)", "Occipital lobe (green)", "Mean of all lobes");

        //if the user first clicks on a checkbox, and then selects a wave
        sensorsList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                applySensors.setDisable(false);
            }
        });
    }
}
