package com.lgp5.fw.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
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

public class MenuController{
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
    @FXML private Text daysLabel;
    @FXML private Text keepHistoryLabel;
	@FXML private GridPane brainWavesPane;
	@FXML private GridPane dataPane;
	@FXML private GridPane moodPane;
	@FXML private GridPane radarPane;
	@FXML private GridPane historyPane;
	@FXML private GridPane settingsPane;
	@FXML private Slider historyPeriodSlider;
    @FXML private CheckBox keepHistoryCheckBox;
    @FXML private CheckBox deleteFilesCheckBox;
    @FXML private Pane historyPeriodWrapper;
	private Tooltip recordTooltip = new Tooltip("Start recording brainwave signals");
	private Tooltip stopTooltip = new Tooltip("Stop recording");
    private SettingsPreferences prefs = new SettingsPreferences();
	
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

        //get values from preferences
        historyPeriodSlider.adjustValue(prefs.getDaysOfHistoryPreference());
        daysText.setText(Integer.toString(prefs.getDaysOfHistoryPreference()));
        deleteFilesCheckBox.setSelected(prefs.getNeverDeletePreference());
        keepHistoryCheckBox.setSelected(prefs.getRecordHistoryPreference());

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
		}
		else {
			recordButton.setVisible(true);
			stopButton.setVisible(false);
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
            //do stuff
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
	public void fileChooser(MouseEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open brainwave records file");
		Stage current = (Stage) paneLayoutRoot.getScene().getWindow();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx"),
				new FileChooser.ExtensionFilter("Excel 97-2003 file (*.xls)", "*.xls"));

		String path = "\\LGP-5A\\FW\\src\\history";
		File f = new File(new File(".").getCanonicalPath().concat(path));
		fileChooser.setInitialDirectory(f);

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
