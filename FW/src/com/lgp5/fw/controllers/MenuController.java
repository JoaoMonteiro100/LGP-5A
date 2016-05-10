package com.lgp5.fw.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by pluralism on 07-05-2016.
 */
public class MenuController {

	@FXML private ImageView arrowLabel;
	@FXML private Pane painelHSA;
	@FXML private Pane panelData;
	@FXML private SplitPane splitPaneMood;
	@FXML private Label historyLabel;
	@FXML private Label sensorsLabel;
	@FXML private Label analysisLabel;
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
		FadeTransition fadeTransition 
		= new FadeTransition(Duration.millis(1000), painelHSA);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.play();
	}
	public void showData(MouseEvent event){
		try {
			panelData.setVisible(true);	
		} catch (Exception e) {
			System.out.println("null");
		}
		splitPaneMood.setVisible(false);
			
	}
	public void showMood(MouseEvent event){
		
		try {
			
			panelData.setVisible(false);
		} catch (Exception e) {
			System.out.println("null");
		}
		
		splitPaneMood.setVisible(true);
	}
}
