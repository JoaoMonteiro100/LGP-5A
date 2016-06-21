package com.lgp5.patient.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import java.io.IOException;


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
/*
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
    }*/

    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    Stage stage;
                            launchSettingsView();
                            //get a handle to the stage
                            stage = (Stage) settings.getScene().getWindow();
                            //close current window
                            stage.close();
            }
        });
        game.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage;
                lauchGameView();
                //get a handle to the stage
                stage = (Stage) game.getScene().getWindow();
                //close current window
                //stage.close();
            }
        });
    }



    private void launchSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/settings.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Settings");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lauchGameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/game.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Game");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
