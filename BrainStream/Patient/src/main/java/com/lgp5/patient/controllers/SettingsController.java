package com.lgp5.patient.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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


    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        analysis.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    Stage stage;
                        lauchAnalysisView();
                        //get a handle to the stage
                        stage = (Stage) analysis.getScene().getWindow();
                        //close current window
                        stage.close();
            }
        });
    }



    public void handle(MouseEvent Event) {
        analysis.setCursor(Cursor.HAND);
        messages.setCursor(Cursor.HAND);
        game.setCursor(Cursor.HAND);
        editProfile.setCursor(Cursor.HAND);
    }

    private void lauchAnalysisView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/analysisView.fxml"));
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

    private void lauchGameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/gameView.fxml"));
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
<<<<<<< HEAD
}
=======
}
>>>>>>> parent of e10a700... Merge
