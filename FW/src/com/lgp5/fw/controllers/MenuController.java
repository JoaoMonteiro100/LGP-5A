package com.lgp5.fw.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;


public class MenuController {

	@FXML private AnchorPane paneLayoutRoot;
	@FXML private ImageView arrowLabel;
	@FXML private Pane painelHSA;
	@FXML private Label historyLabel;
	@FXML private Label sensorsLabel;
	@FXML private Label analysisLabel;
	@FXML private Pane paneLayout;
	private FadeTransition fadeIn = new FadeTransition(Duration.millis(1000));


	public MenuController(){ 
	}


	@FXML
	private void initialize() {
	}



	public void openMenu(MouseEvent event){		
		painelHSA.setVisible(true);
		FadeTransition fadeTransition 
		= new FadeTransition(Duration.millis(1000), painelHSA);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();				
	}


	public void closeMenu(MouseEvent event){		
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), painelHSA);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.play();
	}


	public void showData(MouseEvent event){
		try {
			paneLayout = (Pane) FXMLLoader.load(getClass().getResource("../views/dataView.fxml"));
			paneLayoutRoot.getChildren().add(paneLayout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showMood(MouseEvent event){
		
	}
}
