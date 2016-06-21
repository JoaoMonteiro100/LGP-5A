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
<<<<<<< HEAD:BrainStream/Patient/src/main/java/com/lgp5/patient/controllers/SettingsController.java
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/gameView.fxml"));
=======
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/game.fxml"));
>>>>>>> origin/final-dev:BrainStream/Patient/src/com/lgp5/patient/controllers/SettingsController.java
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
<<<<<<< HEAD:BrainStream/Patient/src/main/java/com/lgp5/patient/controllers/SettingsController.java
}
=======
}
>>>>>>> origin/final-dev:BrainStream/Patient/src/com/lgp5/patient/controllers/SettingsController.java
