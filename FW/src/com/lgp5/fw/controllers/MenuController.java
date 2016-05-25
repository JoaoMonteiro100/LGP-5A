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
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;


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
	@FXML private NumberAxis xAxisWavesLine;
	private ObservableList<String> brainwaves = FXCollections.observableArrayList();
	private ObservableList<String> moods = FXCollections.observableArrayList();
	@FXML private NumberAxis xAxisHistory;
	@FXML private NumberAxis yAxisHistory;
	@FXML private LineChart<Number, Number> lineChartHistory;
	@FXML private LineChart<Number, Number> lineChartWaves;
	Vector<ArrayList> wavesGroup = new Vector<ArrayList>(2);
	ArrayList<String> deltaQueue =  new ArrayList<String>();
	ArrayList<String> thetaQueue =  new ArrayList<String>();
	ArrayList<String> highAlphaQueue =  new ArrayList<String>();
	ArrayList<String> lowAlphaQueue =  new ArrayList<String>();
	ArrayList<String> highBetaQueue =  new ArrayList<String>();
	ArrayList<String> lowBetaQueue =  new ArrayList<String>();
	ArrayList<String> lowGammaQueue =  new ArrayList<String>();
	ArrayList<String> highGammaQueue =  new ArrayList<String>();
	ArrayList<Number> queueTime = new ArrayList<Number>();
	private long time;

	public MenuController() {
	}

	/**
	 * Initializes the controller class and sets x axis of the bar chart with the appropriate values
	 */
	@FXML
	private void initialize() {
		time=System.currentTimeMillis()/1000;
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

		createSeriesLineChartWaves(series);


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

					updateSeriesLineChartWaves(delta,theta,gamma1,gamma2,alpha1,alpha2,beta1,beta2);

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
							int serieNumber=0;
							for(Series<Number,Number> series : lineChartWaves.getData()){							
								for(int i=0;i< series.getData().size();i++) 
								{									
									series.getData().get(i).setYValue(Float.parseFloat((String) wavesGroup.get(serieNumber).get(i)));
									series.getData().get(i).setXValue(queueTime.get(i));
								}
								serieNumber++;
							}
							for (Series<String, Float> series : barChartWaves.getData()) {
								int i=0;
								for (Data<String, Float> data : series.getData()) {
									//Falta Converter !!!!!!!!!!!!!!!!!!!!!!
									////Volts: [ rawValue * (1.8/4096) ] / 2000
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

	public void createSeriesLineChartWaves(XYChart.Series<String,Float> seriesBarChart){
		xAxisHistory.setLabel("Time");	
		XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series4 = new XYChart.Series<>();		
		XYChart.Series<Number, Number> series5 = new XYChart.Series<>();		
		XYChart.Series<Number, Number> series6 = new XYChart.Series<>();		
		XYChart.Series<Number, Number> series7 = new XYChart.Series<>();		
		XYChart.Series<Number, Number> series8 = new XYChart.Series<>();		
		XYChart.Series<Number, Number> series9 = new XYChart.Series<>();		
		XYChart.Series<Number, Number> series10 = new XYChart.Series<>();		
		series3.setName("Delta");
		series4.setName("Theta");
		series5.setName("highAlpha");
		series6.setName("lowAlpha");
		series7.setName("highBeta");
		series8.setName("lowBeta");
		series9.setName("lowGamma");
		series10.setName("highGamma");

		for (int i = 0; i < 10; i++) {
			series3.getData().add(new XYChart.Data(0f, 0f));
			series4.getData().add(new XYChart.Data(0f, 0f));
			series5.getData().add(new XYChart.Data(0f, 0f));
			series6.getData().add(new XYChart.Data(0f, 0f));
			series7.getData().add(new XYChart.Data(0f, 0f));
			series8.getData().add(new XYChart.Data(0f, 0f));
			series9.getData().add(new XYChart.Data(0f, 0f));
			series10.getData().add(new XYChart.Data(0f, 0f));
			deltaQueue.add("50.0");
			thetaQueue.add("50.0");
			highAlphaQueue.add("50.0");
			lowAlphaQueue.add("50.0");
			highBetaQueue.add("50.0");
			lowBetaQueue.add("50.0");
			lowGammaQueue.add("50.0");
			highGammaQueue.add("50.0");
			queueTime.add(0);
		}		
		wavesGroup.add(deltaQueue);
		wavesGroup.add(thetaQueue);		
		wavesGroup.add(highAlphaQueue);		
		wavesGroup.add(lowAlphaQueue);		
		wavesGroup.add(highBetaQueue);		
		wavesGroup.add(lowBetaQueue);		
		wavesGroup.add(lowGammaQueue);		
		wavesGroup.add(highGammaQueue);		

		lineChartWaves.getData().addAll(series3,series4,series6,series5,series8,series7,series9,series10);
		for (int i = 0; i < seriesBarChart.getData().size(); i++) {
			final int tmp = i;
			System.out.println(seriesBarChart.getData().get(i).toString());
			seriesBarChart.getData().get(i).getNode().setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(lineChartWaves.getData().get(tmp).nodeProperty().get().isVisible())
					{
						lineChartWaves.getData().get(tmp).nodeProperty().get().setVisible(false);
						Set<Node> lookupAll = lineChartWaves.lookupAll(".chart-line-symbol.series" + tmp);
						for (Node n : lookupAll) {
							n.setVisible(false);
						}
					}
					else 
					{
						Set<Node> lookupAll = lineChartWaves.lookupAll(".chart-line-symbol.series" + tmp);
						for (Node n : lookupAll) {
							n.setVisible(true);
						}
						lineChartWaves.getData().get(tmp).nodeProperty().get().setVisible(true);
					}
				}

			});
		}


		colorNumber=0;
		for(Series<Number,Number> series : lineChartWaves.getData()){
			if(colorNumber>=constants.Constants.colors.length)						
				colorNumber=0;		
			Set<Node> lookupAll = lineChartWaves.lookupAll(".chart-line-symbol.series" + colorNumber);
			for (Node n : lookupAll) {
				n.setStyle("-fx-background-color:"+constants.Constants.colors[colorNumber]+";");
			}
			series.nodeProperty().get().setStyle("-fx-stroke: " +constants.Constants.colors[colorNumber]+";");		
			colorNumber++;	
		}



		lineChartWaves.setLegendVisible(false);
		lineChartWaves.setAnimated(false);

		xAxisWavesLine.setLowerBound(0);
		xAxisWavesLine.setUpperBound(50);
		xAxisWavesLine.setAutoRanging(false);
	}

	public void updateSeriesLineChartWaves(String d,String t,String g1,String g2,String a1,String a2,String b1,String b2)
	{
		queueTime.add((System.currentTimeMillis()/1000)-time);
		queueTime.remove(0);
		wavesGroup.get(0).add(d);					
		wavesGroup.get(1).add(t);
		wavesGroup.get(2).add(a2);
		wavesGroup.get(3).add(a1);
		wavesGroup.get(4).add(b2);
		wavesGroup.get(5).add(b1);
		wavesGroup.get(6).add(g1);
		wavesGroup.get(7).add(g2);

		for (int i = 0; i < wavesGroup.size(); i++) {
			wavesGroup.get(i).remove(0);
		}
		xAxisWavesLine.setLowerBound(Double.parseDouble(queueTime.get(0).toString()));
		xAxisWavesLine.setUpperBound(Double.parseDouble(queueTime.get(9).toString()));					
		/*
		System.err.println(queueTime.toString());
		System.out.println(deltaQueue.toString());
		System.err.println(thetaQueue.toString());*/
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
		changePane(radarLabel,new Label[]{moodLabel,brainWavesLabel,dataLabel,historyLabel},radarPane,new Pane[]{moodPane,brainWavesPane,dataPane,historyPane});
	}


	public void showData(MouseEvent event){		
		changePane(dataLabel,new Label[]{moodLabel,brainWavesLabel,radarLabel,historyLabel},dataPane,new Pane[]{moodPane,brainWavesPane,radarPane,historyPane});
	}


	public void showMood(MouseEvent event){
		changePane(moodLabel,new Label[]{dataLabel,brainWavesLabel,radarLabel,historyLabel},moodPane,new Pane[]{dataPane,brainWavesPane,radarPane,historyPane});	
	}

	public void showBrainWaves(MouseEvent event) {
		changePane(brainWavesLabel,new Label[]{dataLabel,moodLabel,radarLabel,historyLabel},brainWavesPane,new Pane[]{dataPane,moodPane,radarPane,historyPane});	
	}

	public void showHistory(MouseEvent event) {
		changePane(historyLabel,new Label[]{dataLabel,moodLabel,radarLabel,brainWavesLabel},historyPane,new Pane[]{dataPane,moodPane,radarPane,brainWavesPane});	
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
