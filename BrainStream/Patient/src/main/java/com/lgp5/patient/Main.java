package com.lgp5.patient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Font.loadFont(getClass().getResourceAsStream("/assets/fonts/Roboto-Medium.ttf"), 16);
        //Font.loadFont(getClass().getResourceAsStream("/assets/fonts/Roboto-Bold.ttf"), 16);
        //Font.loadFont(getClass().getResourceAsStream("/assets/fonts/Roboto-Light.ttf"), 16);

        Parent root = FXMLLoader.load(getClass().getResource("views/loginView.fxml"));
        primaryStage.setTitle("BrainStream - Login");
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}