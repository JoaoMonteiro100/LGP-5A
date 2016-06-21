package com.lgp5.fw.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import module.MainModule;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class AnalysisEmotivController extends MenuEmotivController{

    @FXML
    private ComboBox<String> waveToAnalyse;
    @FXML
    private ComboBox<String> lobeToAnalyse;
    @FXML
    private Button startAnalysis;
    @FXML
    private TextField analysisPeriodField;
    @FXML
    private Button startAnalysisButton;
    @FXML
    private CheckBox freqOfMinIntensity, freqOfMaxIntensity, amplitude, median, mode, mean, maxIntensity, minIntensity, maxAmplitude;

    public AnalysisEmotivController() {
    }

    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        waveToAnalyse.getItems().addAll( "Alpha", "Beta", "Theta", "Delta");
        lobeToAnalyse.getItems().addAll("Frontal lobe", "Parietal lobe", "Temporal lobe", "Occipital lobe", "Mean of all lobes (default)");

        // force the field to be numeric only
        analysisPeriodField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                analysisPeriodField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //if the user first clicks on a checkbox, and then selects a wave
        waveToAnalyse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(freqOfMinIntensity.isSelected() ||
                        freqOfMaxIntensity.isSelected() ||
                        amplitude.isSelected() ||
                        median.isSelected() ||
                        mode.isSelected() ||
                        mean.isSelected() ||
                        maxIntensity.isSelected() ||
                        minIntensity.isSelected() ||
                        maxAmplitude.isSelected())
                startAnalysisButton.setDisable(false);
                
                Boolean[] checkCalc = new Boolean[9];
                checkCalc[0] = freqOfMinIntensity.isSelected();
                checkCalc[1] = freqOfMaxIntensity.isSelected();
                checkCalc[2] = amplitude.isSelected();
                checkCalc[3] = median.isSelected();
                checkCalc[4] = mode.isSelected();
                checkCalc[5] = mean.isSelected();
                checkCalc[6] = maxIntensity.isSelected();
                checkCalc[7] = minIntensity.isSelected();
                checkCalc[8] = maxAmplitude.isSelected();
                
                fw.setCheckCalc(checkCalc);
                setTime(); //é aqui?
            }
        });

        //if the user first selects the wave and then the methods
        freqOfMinIntensity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        freqOfMaxIntensity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        amplitude.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        mean.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        mode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        median.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        maxIntensity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        minIntensity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        maxAmplitude.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });

        startAnalysisButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                launchAnalysisInProgressView();
                //get a handle to the stage
                Stage stage = (Stage) startAnalysisButton.getScene().getWindow();
                //close current window
                stage.close();
            }
        });
    }
//TODO por isto a enviar o tempo, já o guarda no modulo
    public void setTime() {
    	String time = analysisPeriodField.getText();
        fw.setTimeCalculate(Integer.parseInt(time)); 
    }
    protected void launchAnalysisInProgressView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/analysisInProgressView.fxml"));
            loader.setController(new AnalysisInProgressController(fw,queue2));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainLight - Analysis in progress...");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
