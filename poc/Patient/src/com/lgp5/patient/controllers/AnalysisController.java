package com.lgp5.patient.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


public class AnalysisController {
    @FXML
    private Button analysis;
    @FXML
    private Button messages;
    @FXML
    private Button settings;


    public AnalysisController() {
    }

    public void changeRecordButton(){

    }

    public void analysisColorToWhite(MouseEvent Event) {
        analysis.setStyle("-fx-background-color: #ffffff;");
        messages.setStyle("-fx-background-color: #EBF4FA;");
        settings.setStyle("-fx-background-color: #EBF4FA;");
    }

    public void messagesColorToWhite(MouseEvent Event) {
        messages.setStyle("-fx-background-color: #ffffff;");
        settings.setStyle("-fx-background-color: #EBF4FA;");
        analysis.setStyle("-fx-background-color: #EBF4FA;");
    }

    public void settingsColorToWhite(MouseEvent Event) {
        settings.setStyle("-fx-background-color: #ffffff;");
        messages.setStyle("-fx-background-color: #EBF4FA;");
        analysis.setStyle("-fx-background-color: #EBF4FA;");
    }
/*
    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        messages.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    Stage stage;
                            launchSelectDeviceView();
                            //get a handle to the stage
                            stage = (Stage) messages.getScene().getWindow();
                            //close current window
                            stage.close();
            }
        });
    }



    private void launchSelectDeviceView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/selectDeviceView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Select Device");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
