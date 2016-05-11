package com.lgp5.fw.controllers;

import com.lgp5.api.neurosky.Neurosky_FW.Neurosky;
import com.lgp5.api.neurosky.Neurosky_FW.interfaces.HeadSetDataInterface;
import com.lgp5.api.neurosky.Neurosky_FW.utils.Constants;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashMap;


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
	@FXML private Label attentionData;
	@FXML private Label mediationData;
	@FXML private Label errorRateData;
	@FXML private Label batteryLevelData;
	@FXML private Label signalQualityData;
	@FXML private GridPane dataPane;
	private HeadSetDataInterface headSetDataInterface;


	public MenuController() {
	}


	@FXML
	private void initialize() {
		headSetDataInterface = new HeadSetDataInterface() {
			@Override
			public void onReceiveData(HashMap<String, HashMap<String, Object>> hashMap) {
				if(gamma1Data != null) {
					HashMap<String, Object> values = hashMap.get(Constants.WAVES);
					String gamma1 = values.get(Constants.LOW_GAMMA).toString();
					String gamma2 = values.get(Constants.MID_GAMMA).toString();
					String beta1 = values.get(Constants.LOW_BETA).toString();
					String beta2 = values.get(Constants.HIGH_BETA).toString();
					String alpha1 = values.get(Constants.LOW_ALPHA).toString();
					String alpha2 = values.get(Constants.HIGH_ALPHA).toString();
					String theta = values.get(Constants.THETA).toString();
					String delta = values.get(Constants.DELTA).toString();
					String attention = values.get(Constants.ATTENTION).toString();
					String meditation = values.get(Constants.MEDITATION).toString();
					String signal = values.get(Constants.POOR_SIGNAL).toString();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							gamma1Data.setText(gamma1);
							gamma2Data.setText(gamma2);
							beta1Data.setText(beta1);
							beta2Data.setText(beta2);
							alfa1Data.setText(alpha1);
							alfa2Data.setText(alpha2);
							thetaData.setText(theta);
							deltaData.setText(delta);
							attentionData.setText(attention);
							mediationData.setText(meditation);
							signalQualityData.setText(signal);
						}
					});
				}
			}
		};
		
		new Thread(new Neurosky("0013EF004809", headSetDataInterface)).start();
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
