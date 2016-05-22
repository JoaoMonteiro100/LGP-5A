package com.lgp5.fw.controllers;

import com.lgp5.api.neurosky.Neurosky_FW.Neurosky;
import com.lgp5.api.neurosky.Neurosky_FW.interfaces.HeadSetDataInterface;
import com.lgp5.api.neurosky.Neurosky_FW.utils.Constants;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;


public class MenuController {
	private int colorNumber=0;
	@FXML private AnchorPane paneLayoutRoot;
	@FXML private ImageView arrowLabel;
	@FXML private Pane painelHSA;
	@FXML private Label historyLabel;
	@FXML private Label sensorsLabel;
	@FXML private Label analysisLabel;
	@FXML private Label radarLabel;
	@FXML private Label brainWavesLabel;
	@FXML private Label dataLabel;
	@FXML private Label moodLabel;
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
	@FXML private GridPane radarPane;
	@FXML private GridPane dataPane;
	@FXML private GridPane moodPane;
	@FXML private GridPane historyPane;
	private HeadSetDataInterface headSetDataInterface;
	@FXML private BarChart<String, Float> barChartWaves;
	@FXML private BarChart<String, Float> barChartMoods;
    @FXML private StackedAreaChart<Float, Float> radarGraphA;
    @FXML private StackedAreaChart<Float, Float> radarGraphB;
    @FXML private StackedAreaChart<Float, Float> radarGraphC;
    @FXML private StackedAreaChart<Float, Float> radarGraphD;
	@FXML private CategoryAxis xAxisWaves;
	@FXML private CategoryAxis xAxisMood;
	private ObservableList<String> brainwaves = FXCollections.observableArrayList();
	private ObservableList<String> moods = FXCollections.observableArrayList();



	public MenuController() {
	}

	/**
	 * Initializes the controller class and sets x axis of the bar chart with the appropriate values
	 */
	@FXML
	private void initialize() {
		String[] waves = {"Delta", "Theta", "Alfa 1", "Alfa 2", "Beta 1", "Beta 2", "Gamma 1", "Gamma 2"};
		String[] moodsArray = {"Attention","Mediation"};
		brainwaves.addAll(Arrays.asList(waves));
		moods.addAll(Arrays.asList(moodsArray));
		xAxisWaves.setCategories(brainwaves);
		xAxisMood.setCategories(moods);
		float[] values = {35f, 43f, 27f, 12f, 9.2f, 32f, 16f, 20f};
		float[] values2 = {35f, 43f};
		XYChart.Series<String,Float> series = createWaveDataSeries(values,brainwaves);
		XYChart.Series<String,Float> series2 = createWaveDataSeries(values2,moods);
		barChartWaves.getData().add(series);
		barChartMoods.getData().add(series2);
		barChartWaves.setLegendVisible(false);
		barChartMoods.setLegendVisible(false);


        XYChart.Series seriesA = new XYChart.Series();
        seriesA.getData().add(new XYChart.Data(0, 100));
        seriesA.getData().add(new XYChart.Data(50, 50));
        seriesA.getData().add(new XYChart.Data(75, 0));
        radarGraphA.getData().addAll(seriesA);

        XYChart.Series seriesB = new XYChart.Series();
        seriesB.getData().add(new XYChart.Data(75, 0));
        seriesB.getData().add(new XYChart.Data(50, -50));
        seriesB.getData().add(new XYChart.Data(0, -90));
        radarGraphB.getData().addAll(seriesB);

        XYChart.Series seriesC = new XYChart.Series();
        seriesC.getData().add(new XYChart.Data(-20, 0));
        seriesC.getData().add(new XYChart.Data(-50, -50));
        seriesC.getData().add(new XYChart.Data(0, -90));
        radarGraphC.getData().addAll(seriesC);

        XYChart.Series seriesD = new XYChart.Series();
        seriesD.getData().add(new XYChart.Data(-20, 0));
        seriesD.getData().add(new XYChart.Data(-50, 50));
        seriesD.getData().add(new XYChart.Data(0, 100));
        radarGraphD.getData().addAll(seriesD);

        //look up first series fill
        Node node = radarGraphA.lookup(".default-color0.chart-series-area-fill");
        Node node1 = radarGraphB.lookup(".default-color0.chart-series-area-fill");
        Node node2 = radarGraphC.lookup(".default-color0.chart-series-area-fill");
        Node node3 = radarGraphD.lookup(".default-color0.chart-series-area-fill");
        //set the fill to transparent
        node.setStyle("-fx-fill: rgba(125, 125, 125, 0);");
        node1.setStyle("-fx-fill: rgba(125, 125, 125, 0);");
        node2.setStyle("-fx-fill: rgba(125, 125, 125, 0);");
        node3.setStyle("-fx-fill: rgba(125, 125, 125, 0);");

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
							for (Series<String, Float> series2 : barChartMoods.getData()) {
								int j=0;
								for (Data<String, Float> data2 : series2.getData()) {									
									switch (j) {
									case 0:
										data2.setYValue(Float.parseFloat(attention));
										break;
									case 1:
										data2.setYValue(Float.parseFloat(meditation));
										break;	
									default:
										break;
									}
									j++;
								}
							}
							for (Series<String, Float> series : barChartWaves.getData()) {
								int i=0;
								for (Data<String, Float> data : series.getData()) {									
									switch (i) {
									case 0:
										data.setYValue(Float.parseFloat(delta));
										break;
									case 1:
										data.setYValue(Float.parseFloat(theta));
										break;
									case 2:
										data.setYValue(Float.parseFloat(alpha1));
										break;
									case 3:
										data.setYValue(Float.parseFloat(alpha2));
										break;
									case 4:
										data.setYValue(Float.parseFloat(beta1));
										break;
									case 5:
										data.setYValue(Float.parseFloat(beta2));
										break;
									case 6:
										data.setYValue(Float.parseFloat(gamma1));
										break;
									case 7:
										data.setYValue(Float.parseFloat(gamma2));										
										break;
									default:
										break;
									}
									i++;																	
								}
							}								
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


	public void showRadar(MouseEvent event){
		changeLabel(radarLabel,new Label[]{moodLabel,brainWavesLabel,dataLabel,historyLabel},radarPane,new Pane[]{moodPane,brainWavesPane,dataPane,historyPane});
	}


	public void showData(MouseEvent event){		
		changeLabel(dataLabel,new Label[]{moodLabel,brainWavesLabel,radarLabel,historyLabel},dataPane,new Pane[]{moodPane,brainWavesPane,radarPane,historyPane});
	}


	public void showMood(MouseEvent event){
		changeLabel(moodLabel,new Label[]{dataLabel,brainWavesLabel,radarLabel,historyLabel},moodPane,new Pane[]{dataPane,brainWavesPane,radarPane,historyPane});	
	}


	public void showBrainWaves(MouseEvent event) {
		changeLabel(brainWavesLabel,new Label[]{dataLabel,moodLabel,radarLabel,historyLabel},brainWavesPane,new Pane[]{dataPane,moodPane,radarPane,historyPane});	
	}
	
	public void showHistory(MouseEvent event) {
		changeLabel(historyLabel,new Label[]{dataLabel,moodLabel,radarLabel,brainWavesLabel},historyPane,new Pane[]{dataPane,moodPane,radarPane,brainWavesPane});	
	}
	
	public void changeLabel(Label showL,Label[] hideL,Pane showP,Pane[] hideP) {
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

	/**
	 * Creates a XYChart.Data object for each wave. All data is then returned as a series.
	 *
	 * @param values Array with a value for each brainwave. Must be the same length as the waves array
	 * @return Series of brainwave data
	 */
	private XYChart.Series<String,Float> createWaveDataSeries (float[] values,ObservableList<String> list) {
		XYChart.Series<String,Float> series = new XYChart.Series<>();
		for (int i = 0; i < values.length; i++) {
			XYChart.Data<String,Float> waveData = new XYChart.Data<>(list.get(i), values[i]);
			waveData.nodeProperty().addListener(new ChangeListener<Node>() {
				@Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {					
					if (newNode != null) { 
						if(colorNumber>=constants.Constants.colors.length)						
							colorNumber=0;						
						newNode.setStyle("-fx-bar-fill: "+constants.Constants.colors[colorNumber]+";"); 	
						colorNumber++;						
					}					
				}
			});
			series.getData().add(waveData);         
		}
		return series;
	}

    public void launchSelectDeviceView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/selectDeviceView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 900, 600));
            stage.show();
            //close current stage
            Stage current = (Stage) paneLayoutRoot.getScene().getWindow();
            current.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
