package com.lgp5.patient.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private Button selectLoginButton;
    @FXML
    private PasswordField password;
    @FXML
    private CheckBox rememberPassword;


    public LoginController() {
    }

    public void handle(MouseEvent Event) {
        rememberPassword.setCursor(Cursor.HAND);
        selectLoginButton.setCursor(Cursor.HAND);
    }


    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        selectLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Extract username and password
                String usernameText = username.getText().trim();
                String passwordText = password.getText().trim();

                Stage stage;
                launchSelectDeviceView();
                //get a handle to the stage
                stage = (Stage) selectLoginButton.getScene().getWindow();
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
    }
}
