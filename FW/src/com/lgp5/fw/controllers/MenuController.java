package com.lgp5.fw.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuController {
	private Desktop desktop = Desktop.getDesktop();
	@FXML private AnchorPane paneLayoutRoot;
	@FXML private ImageView recordButton;
	@FXML private ImageView stopButton;
	@FXML private ImageView arrowLabel;
	@FXML private Pane painelHSA;
	@FXML private Label historyLabel;
	@FXML private Label sensorsLabel;
	@FXML private Label analysisLabel;
	@FXML private Label radarLabel;
	@FXML private Label brainWavesLabel;
	@FXML private Label dataLabel;
	@FXML private Label moodLabel;
	@FXML private Label settingsLabel;
	@FXML private Text daysText;
	@FXML private GridPane brainWavesPane;
	@FXML private GridPane dataPane;
	@FXML private GridPane moodPane;
	@FXML private GridPane radarPane;
	@FXML private GridPane historyPane;
	@FXML private GridPane settingsPane;
	@FXML private Slider historyPeriodSlider;
	
	public MenuController(){
		
	}
	public void settings()
	{
		historyPeriodSlider.setMin(120);
		historyPeriodSlider.setValue(120);
		historyPeriodSlider.setMax(1825);
		historyPeriodSlider.setShowTickLabels(false);
		historyPeriodSlider.setShowTickMarks(false);
		historyPeriodSlider.setMajorTickUnit(15);
		historyPeriodSlider.setMinorTickCount(0);
		historyPeriodSlider.setBlockIncrement(10);

		historyPeriodSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (newValue == null) {
					daysText.setText("");
					return;
				}
				daysText.setText(Math.round(newValue.intValue()) + "");
			}
		});
	}
	public void openMenu(MouseEvent event){
		if(!painelHSA.isVisible()) {
			painelHSA.setVisible(true);
			FadeTransition fadeTransition
			= new FadeTransition(Duration.millis(100), painelHSA);
			fadeTransition.setFromValue(0.0);
			fadeTransition.setToValue(1.0);
			fadeTransition.play();
		}
		else {
			painelHSA.setVisible(false);
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), painelHSA);
			fadeTransition.setFromValue(1.0);
			fadeTransition.setToValue(0.0);
			fadeTransition.play();
		}

		RotateTransition rotation = new RotateTransition(Duration.seconds(0.1), arrowLabel);
		rotation.setCycleCount(1);
		rotation.setByAngle(180);
		rotation.play();
	}

	public void showRadar(MouseEvent event){
		changePane(radarLabel,new Label[]{moodLabel,brainWavesLabel,dataLabel,historyLabel,settingsLabel},radarPane,new Pane[]{moodPane,brainWavesPane,dataPane,historyPane,settingsPane});
	}

	public void showSettings(MouseEvent event){
		changePane(settingsLabel,new Label[]{moodLabel,brainWavesLabel,dataLabel,historyLabel,radarLabel},settingsPane,new Pane[]{moodPane,brainWavesPane,dataPane,historyPane,radarPane});
	}

	public void showData(MouseEvent event){		
		changePane(dataLabel,new Label[]{moodLabel,brainWavesLabel,radarLabel,historyLabel,settingsLabel},dataPane,new Pane[]{moodPane,brainWavesPane,radarPane,historyPane,settingsPane});
	}


	public void showMood(MouseEvent event){
		changePane(moodLabel,new Label[]{dataLabel,brainWavesLabel,radarLabel,historyLabel,settingsLabel},moodPane,new Pane[]{dataPane,brainWavesPane,radarPane,historyPane,settingsPane});	
	}

	public void showBrainWaves(MouseEvent event) {
		changePane(brainWavesLabel,new Label[]{dataLabel,moodLabel,radarLabel,historyLabel,settingsLabel},brainWavesPane,new Pane[]{dataPane,moodPane,radarPane,historyPane,settingsPane});	
	}

	public void showHistory(MouseEvent event) {
		changePane(historyLabel,new Label[]{dataLabel,moodLabel,radarLabel,brainWavesLabel,settingsLabel},historyPane,new Pane[]{dataPane,moodPane,radarPane,brainWavesPane,settingsPane});	
	}
	public void showRecordButton(){
		if(recordButton.isVisible())
			recordButton.setVisible(false);
		else recordButton.setVisible(true);
	}
	public void changeRecordButton(){
		if(recordButton.isVisible())
		{
			recordButton.setVisible(false);
			stopButton.setVisible(true);
		}
		else {
			recordButton.setVisible(true);
			stopButton.setVisible(false);
		}
	}

	public void changePane(Label showL,Label[] hideL,Pane showP,Pane[] hideP) {
		for (int i = 0; i < hideL.length; i++) {
			hideL[i].setDisable(false);
		}
		showL.setDisable(true);
		try {
			if(!showP.isVisible()) {
				for (int i = 0; i < hideP.length; i++) {
					hideP[i].setVisible(false);
				}
				showP.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void fileChooser(MouseEvent event){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		Stage current = (Stage) paneLayoutRoot.getScene().getWindow();
		File file = fileChooser.showOpenDialog(current);
		if (file != null) {
			openFile(file);
		}
	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {

		}
	}
	public void launchSelectDeviceView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/selectDeviceView.fxml"));
			Parent parent = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(parent, 900, 600));
			stage.setTitle("BrainLight - Select Device");
			stage.show();
			//close current stage
			Stage current = (Stage) paneLayoutRoot.getScene().getWindow();
			current.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
