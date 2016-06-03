package com.lgp5.patient.controllers;


import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


public class AnalysisController {
    @FXML
    private Button analysis;
    @FXML
    private Button messages;
    @FXML
    private Button settings;
    @FXML
    private Button game;


    public AnalysisController() {
    }

    public void handle(MouseEvent Event) {
        settings.setCursor(Cursor.HAND);
        messages.setCursor(Cursor.HAND);
        game.setCursor(Cursor.HAND);
    }

    public void analysisColorToWhite(MouseEvent Event) {
        analysis.setStyle("-fx-background-color: #FEFDFC;");
        messages.setStyle("-fx-background-color: #F6F4F3;");
        settings.setStyle("-fx-background-color: #F6F4F3;");
        game.setStyle("-fx-background-color: #F6F4F3;");

    }

    public void messagesColorToWhite(MouseEvent Event) {
        messages.setStyle("-fx-background-color: #FEFDFC;");
        settings.setStyle("-fx-background-color: #F6F4F3;");
        analysis.setStyle("-fx-background-color: #F6F4F3;");
        game.setStyle("-fx-background-color: #F6F4F3;");

    }

    public void settingsColorToWhite(MouseEvent Event) {
        settings.setStyle("-fx-background-color: #FEFDFC;");
        messages.setStyle("-fx-background-color: #F6F4F3;");
        analysis.setStyle("-fx-background-color: #F6F4F3;");
        game.setStyle("-fx-background-color: #F6F4F3;");

    }

    public void gameColorToWhite(MouseEvent Event) {
        game.setStyle("-fx-background-color: #FEFDFC;");
        settings.setStyle("-fx-background-color: #F6F4F3;");
        messages.setStyle("-fx-background-color: #F6F4F3;");
        analysis.setStyle("-fx-background-color: #F6F4F3;");
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
