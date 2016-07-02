package com.lgp5.patient.controllers;


import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.lgp5.patient.utils.UserData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
                loginUser(usernameText, passwordText);
                selectLoginButton.setDisable(true);
            }
        });
    }


    private void loginUser(final String username, String password) {
        Firebase appRef = new Firebase("https://brainlight.firebaseio.com/");

        appRef.authWithPassword(username, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                selectLoginButton.setDisable(false);
                // Authenticated with success
                UserData.loggedIn = true;
                UserData.EMAIL = username;

                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Stage stage;
                        launchSelectDeviceView();
                        //get a handle to the stage
                        stage = (Stage) selectLoginButton.getScene().getWindow();
                        //close current window
                        stage.close();
                    }
                });
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Error on authentication
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login on BrainStream");
                        alert.setContentText("Wrong username or password! Please try again!");
                        alert.show();
                    }
                });
                selectLoginButton.setDisable(false);
            }
        });
    }


    private void launchSelectDeviceView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/patient/views/selectDeviceView.fxml"));
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
