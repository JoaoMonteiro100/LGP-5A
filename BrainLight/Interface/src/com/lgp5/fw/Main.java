package com.lgp5.fw;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("/assets/fonts/Roboto-Medium.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/assets/fonts/Roboto-Bold.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/assets/fonts/Roboto-Light.ttf"), 16);

        Parent root = FXMLLoader.load(getClass().getResource("views/selectDeviceView.fxml"));
        primaryStage.setTitle("BrainLight - Select Device");
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {    	
        launch(args);
    }   
   
}
