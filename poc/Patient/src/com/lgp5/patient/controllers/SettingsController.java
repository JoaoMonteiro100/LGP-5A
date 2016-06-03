package com.lgp5.patient.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;



public class SettingsController {
    @FXML
    private Button analysis;
    @FXML
    private Button messages;
    @FXML
    private Button settings;
    @FXML
    private Button game;
    @FXML
    private Button editProfile;


    public SettingsController() {
    }

    public void changeRecordButton(){

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

    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        editProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    Stage stage;
                            lauchAnalysisView();
                            //get a handle to the stage
                            stage = (Stage) editProfile.getScene().getWindow();
                            //close current window
                            stage.close();
            }
        });
    }



    private void lauchAnalysisView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/analysis.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Analysis");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
