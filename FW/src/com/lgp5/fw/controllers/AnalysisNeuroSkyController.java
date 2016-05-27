package com.lgp5.fw.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class AnalysisNeuroSkyController {
    @FXML
    private ComboBox<String> waveToAnalyse;
    @FXML
    private Button startAnalysis;
    @FXML
    private TextField analysisPeriodField;
    @FXML
    private Button startAnalysisButton;
    @FXML
    private CheckBox wavelength, wavenumber, angWavenumber, angFrequency, period, amplitude, median, mode, mean, maxFrequency, minFrequency, maxAmplitude;

    public AnalysisNeuroSkyController() {
    }

    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        waveToAnalyse.getItems().addAll("Delta", "Theta", "Alfa 1", "Alfa 2", "Beta 1", "Beta 2", "Gamma 1", "Gamma 2");

        wavelength.setDisable(false);

        Tooltip mousePositionToolTip = new Tooltip("Alo alo");
        wavelength.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePositionToolTip.setText("heyyy");
                Node node = (Node) event.getSource();
                mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
            }
        });

        wavelength.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mousePositionToolTip.hide();
            }
        });

/*
        wavelength.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Node node =(Node)t.getSource();
                tp.show(node, ((Stage) startAnalysisButton.getScene().getWindow()).getX()+t.getSceneX(), ((Stage) startAnalysisButton.getScene().getWindow()).getY()+t.getSceneY());
            }
        });

        wavelength.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                tp.hide();
            }
        });
*/
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
                if(wavelength.isSelected() ||
                        wavenumber.isSelected() ||
                        angWavenumber.isSelected() ||
                        angFrequency.isSelected() ||
                        period.isSelected() ||
                        amplitude.isSelected() ||
                        median.isSelected() ||
                        mode.isSelected() ||
                        mean.isSelected() ||
                        maxFrequency.isSelected() ||
                        minFrequency.isSelected() ||
                        maxAmplitude.isSelected())
                startAnalysisButton.setDisable(false);
            }
        });

        //if the user first selects the wave and then the methods
        wavelength.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        wavenumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        angWavenumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        angFrequency.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        period.setOnAction(new EventHandler<ActionEvent>() {
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
        maxFrequency.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(waveToAnalyse.getSelectionModel().getSelectedItem()!=null) {
                    startAnalysisButton.setDisable(false);
                }
            }
        });
        minFrequency.setOnAction(new EventHandler<ActionEvent>() {
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


    private void launchAnalysisInProgressView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/analysisInProgressView.fxml"));
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
