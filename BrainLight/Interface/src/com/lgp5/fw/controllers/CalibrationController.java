package com.lgp5.fw.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;

public class CalibrationController {
    @FXML
    private Label expressionToMake;
    @FXML
    private ProgressBar progressBarCalibration;
    @FXML
    private Button nextCalibrationButton;

    public void launchCalibrationView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/fw/views/calibrationView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 462, 226));
            stage.setTitle("BrainLight - Calibration");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
