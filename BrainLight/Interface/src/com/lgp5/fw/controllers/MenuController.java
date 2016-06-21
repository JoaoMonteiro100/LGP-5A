package com.lgp5.fw.controllers;

import history.read.net.codejava.excel.ReadXLS;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import module.MainModule;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

public class MenuController {
	protected String[][] historic;
	protected Boolean putHistoric;
	BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(1);
	BlockingQueue queue2 = new ArrayBlockingQueue<Double[][]>(1);
	@FXML private AnchorPane paneLayoutRoot;
	@FXML private ImageView recordButton;
	@FXML private ImageView stopButton;
	@FXML private ImageView arrowLabel;
	@FXML private Pane painelHSA;
	@FXML protected Label historyLabel;
	@FXML protected Label sensorsLabel;
	@FXML protected Label analysisLabel;
	@FXML protected Label radarLabel;
	@FXML protected Button historyButton;
	@FXML protected Label brainWavesLabel;
	@FXML protected Label dataLabel;
	@FXML protected Label moodLabel;
	@FXML protected Label settingsLabel;
	@FXML private Text daysText;
	@FXML private Text daysLabel;
	@FXML private Text keepHistoryLabel;
	@FXML protected GridPane brainWavesPane;
	@FXML protected GridPane dataPane;
	@FXML protected GridPane moodPane;
	@FXML protected GridPane radarPane;
	@FXML protected GridPane historyPane;
	@FXML protected GridPane settingsPane;
	@FXML private Slider historyPeriodSlider;
	@FXML private CheckBox keepHistoryCheckBox;
	@FXML private CheckBox deleteFilesCheckBox;
	@FXML private Pane historyPeriodWrapper;
	@FXML private ProgressIndicator loadingDialog;
	protected MainModule fw;
	private Tooltip recordTooltip = new Tooltip("Start recording brainwave signals");
	private Tooltip stopTooltip = new Tooltip("Stop recording");
	protected SettingsPreferences prefs=new SettingsPreferences();
	public MenuController(){

	}
	public MenuController(int device){
		if(prefs.getNeverDeletePreference()){
			fw = new MainModule(device,queue,queue2,true,prefs.getDaysOfHistoryPreference());
		}else{
			fw = new MainModule(device,queue,queue2,false,prefs.getDaysOfHistoryPreference());
		}
		historic=null;
		setPutHistoric(false);
	}
	protected void settings()
	{
		historyPeriodSlider.setMin(120);
		historyPeriodSlider.setValue(120);
		historyPeriodSlider.setMax(1825);
		historyPeriodSlider.setShowTickLabels(false);
		historyPeriodSlider.setShowTickMarks(false);
		historyPeriodSlider.setMajorTickUnit(15);
		historyPeriodSlider.setMinorTickCount(0);
		historyPeriodSlider.setBlockIncrement(10);

		//get values from preferences
		historyPeriodSlider.adjustValue(prefs.getDaysOfHistoryPreference());
		daysText.setText(Integer.toString(prefs.getDaysOfHistoryPreference()));
		deleteFilesCheckBox.setSelected(prefs.getNeverDeletePreference());
		keepHistoryCheckBox.setSelected(prefs.getRecordHistoryPreference());;

		historyPeriodSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (newValue == null) {
					daysText.setText("");
					return;
				}
				daysText.setText(Math.round(newValue.intValue()) + "");
				prefs.setDaysOfHistoryPreference(newValue.intValue());
			}
		});

		//put buttons and options according to preferences
		recordButton.setVisible(keepHistoryCheckBox.isSelected());
		setDisableEffect(prefs.getNeverDeletePreference());

		//record button tooltip
		recordButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Node node = (Node) event.getSource();
				recordTooltip.show(node, event.getScreenX() + 50, event.getScreenY());
			}
		});
		recordButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				recordTooltip.hide();
			}
		});

		//stop button tooltip
		stopButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Node node = (Node) event.getSource();
				stopTooltip.show(node, event.getScreenX() + 50, event.getScreenY());
			}
		});
		stopButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stopTooltip.hide();
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

	public void setRecordButton(){
		if(recordButton.isVisible()) {
			recordButton.setVisible(false);
			prefs.setRecordHistoryPreference(false);
		}
		else if(stopButton.isVisible()) {
			stopButton.setVisible(false);
			prefs.setRecordHistoryPreference(false);
		}
		else {
			recordButton.setVisible(true);
			prefs.setRecordHistoryPreference(true);
		}
	}

	public void changeRecordButton(){
		if(recordButton.isVisible())
		{
			recordButton.setVisible(false);
			stopButton.setVisible(true);
			fw.setRecord(true);
		}
		else {
			recordButton.setVisible(true);
			stopButton.setVisible(false);
			fw.setRecord(false);
		}
	}
	public void setDisableEffect(Boolean b) {
		if (b) {
			historyPeriodWrapper.setDisable(true);
			daysLabel.setFill(javafx.scene.paint.Paint.valueOf("#cccccc"));
			keepHistoryLabel.setFill(javafx.scene.paint.Paint.valueOf("#cccccc"));
			daysText.setFill(javafx.scene.paint.Paint.valueOf("#cccccc"));
		}
		else {
			historyPeriodWrapper.setDisable(false);
			daysLabel.setFill(javafx.scene.paint.Paint.valueOf("#000000"));
			keepHistoryLabel.setFill(javafx.scene.paint.Paint.valueOf("#000000"));
			daysText.setFill(javafx.scene.paint.Paint.valueOf("#000000"));
		}
	}

	public void disableHistoryPeriod() {
		if (!historyPeriodWrapper.isDisabled()) {
			setDisableEffect(true);
			prefs.setNeverDeletePreference(true);
		}
		else {
			setDisableEffect(false);
			prefs.setNeverDeletePreference(false);
		}
	}

	public void confirmDeletingHistory() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the entire history?\n\n", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			fw.cleanHistory();
		}
		else {
			alert.close();
		}
	}

	public void resetPreferences(ActionEvent event) {
		prefs.reset();
		historyPeriodSlider.adjustValue(prefs.getDaysOfHistoryPreference());
		daysText.setText(Integer.toString(prefs.getDaysOfHistoryPreference()));
		deleteFilesCheckBox.setSelected(prefs.getNeverDeletePreference());
		keepHistoryCheckBox.setSelected(prefs.getRecordHistoryPreference());
		recordButton.setVisible(keepHistoryCheckBox.isSelected());
		setDisableEffect(prefs.getNeverDeletePreference());
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

	public void launchSelectDeviceView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/fw/views/selectDeviceView.fxml"));
			Parent parent = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(parent, 900, 600));
			stage.setTitle("BrainLight - Select Device");
			stage.show();
			fw.deviceDisconnect();
			//close current stage
			Stage current = (Stage) paneLayoutRoot.getScene().getWindow();
			current.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void fileChooser(MouseEvent event) throws IOException {
		Stage stage;
		stage = (Stage) historyButton.getScene().getWindow();
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open brainwave records file");
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx"),
				new FileChooser.ExtensionFilter("Excel 97-2003 file (*.xls)", "*.xls"));
		File defaultDirectory = new File("history/");
		//create a history folder if it hasn't been created yet
		if (!defaultDirectory.exists()){
			defaultDirectory.mkdir();
		}
		chooser.setInitialDirectory(defaultDirectory);
		File file = chooser.showOpenDialog(stage);
		if(file!=null)
			if(file.exists()){
				ReadXLS xlsRead = null;

				// Boolean as generic parameter since you want to return it
				Task<Boolean> task = new Task<Boolean>() {
					@Override public Boolean call() {
						historic = xlsRead.read("history/"+file.getName());
						putHistoric=true;
						return putHistoric;
					}
				};

				task.setOnRunning((e) -> loadingDialog.setVisible(true));
				task.setOnSucceeded((e) -> {
					loadingDialog.setVisible(false);
					try {
						Boolean returnValue = task.get();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						e1.printStackTrace();
					}
				});
				task.setOnFailed((e) -> {
					// eventual error handling by catching exceptions from task.get()
				});
				new Thread(task).start();
			}
	}

	public String[][] getHistoric() {
		return historic;
	}
	public void setHistoric(String[][] historic) {
		this.historic = historic;
	}
	public Boolean getPutHistoric() {
		return putHistoric;
	}
	public void setPutHistoric(Boolean putHistoric) {
		this.putHistoric = putHistoric;
	}
}
