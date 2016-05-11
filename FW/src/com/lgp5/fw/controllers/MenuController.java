package com.lgp5.fw.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Scanner;


public class MenuController {

	@FXML private AnchorPane paneLayoutRoot;
	@FXML private ImageView arrowLabel;
	@FXML private Pane painelHSA;
	@FXML private Label historyLabel;
	@FXML private Label sensorsLabel;
	@FXML private Label analysisLabel;
	@FXML private GridPane brainWavesPane;
	@FXML private Label gamma1Data;
	@FXML private Label gamma2Data;
	@FXML private Label alfa1Data;
	@FXML private Label alfa2Data;
	@FXML private Label beta1Data;
	@FXML private Label beta2Data;
	@FXML private Label deltaData;
	@FXML private Label thetaData;
	@FXML private Label errorRateData;
	@FXML private Label batteryLevelData;
	@FXML private Label signalQualityData;
	@FXML private GridPane dataPane;
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
			if(!dataPane.isVisible()) {
				brainWavesPane.setVisible(false);
				dataPane.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showMood(MouseEvent event){

	}


	public void showBrainWaves(MouseEvent event) {
		try {
			if(!brainWavesPane.isVisible()) {
				dataPane.setVisible(false);
				brainWavesPane.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
