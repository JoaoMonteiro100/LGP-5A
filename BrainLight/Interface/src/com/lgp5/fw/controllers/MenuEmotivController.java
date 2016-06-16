package com.lgp5.fw.controllers;

import javafx.application.Platform;
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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import module.MainModule;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MenuEmotivController extends MenuController{

	BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(1);
	BlockingQueue queue2 = new ArrayBlockingQueue<Double[][]>(1);
	private Double[][] finalRawDataArray = new Double[100][100];
	private int colorNumber=0;
	@FXML private Label alfa1Data;
	@FXML private Label beta1Data;
	@FXML private Label beta2Data;
	@FXML private Label thetaData;
	@FXML private Label attentionData;
	@FXML private Label meditationData;
	@FXML private Label errorRateData;
	@FXML private Label batteryLevelData;
	@FXML private Label signalQualityData;
	@FXML private Label actionsLabel;
	@FXML private GridPane actionsPane;
	@FXML private Slider historyPeriodSlider;
	@FXML private WebView radarBrowser;
	@FXML private BarChart<String, Float> barChartWaves;
	@FXML private BarChart<String, Float> barChartMoods;
    @FXML private BarChart<String, Float> barChartMentalActions;
	@FXML private CategoryAxis xAxisWaves;
	@FXML private CategoryAxis xAxisMood;
	@FXML private NumberAxis xAxisWavesLine;
	@FXML private NumberAxis xAxisMoodsLine;
	private ObservableList<String> brainwaves = FXCollections.observableArrayList();
	private ObservableList<String> moods = FXCollections.observableArrayList();
    private ObservableList<String> mentalActions = FXCollections.observableArrayList();
	@FXML private NumberAxis xAxisHistory;
	@FXML private NumberAxis yAxisHistory;
	@FXML private LineChart<String, Number> lineChartHistory;
	@FXML private LineChart<Number, Number> lineChartWaves;
	@FXML private LineChart<Number, Number> lineChartMoods;
	@FXML private Button calibrationButton;
	private updateInterface updateInterface;
	Vector<ArrayList> wavesGroup = new Vector<ArrayList>(2);
	Vector<ArrayList> moodsGroup = new Vector<ArrayList>(2);
	ArrayList<String> deltaQueue =  new ArrayList<String>();
	ArrayList<String> alphaQueue =  new ArrayList<String>();
	ArrayList<String> betaQueue =  new ArrayList<String>();
	ArrayList<String> thetaQueue =  new ArrayList<String>();
	ArrayList<String> frustationQueue =  new ArrayList<String>();
	ArrayList<String> meditationQueue =  new ArrayList<String>();
	ArrayList<String> engagementQueue =  new ArrayList<String>();
	ArrayList<String> excitementLQueue =  new ArrayList<String>();
	ArrayList<String> excitementSQueue =  new ArrayList<String>();
	ArrayList<Number> queueTime = new ArrayList<Number>();
	private long time;

	public MenuEmotivController() {
	}

	/**
	 * Initializes the controller class and sets x axis of the bar chart with the appropriate values
	 */
	@FXML
	private void initialize() throws MalformedURLException {
		settings();
		time=System.currentTimeMillis()/1000;
		String[] waves = {"Alfa", "Beta","Delta", "Theta"};
		String[] moodsArray = {"Engagement", "Excitement (long time)", "Excitement (short time)", "Frustration", "Meditation"};
        String[] actions = {"Neural", "Push", "Pull", "Lift", "Drop", "Left", "Right", "Rotate left", "Rotate right", "Rotate clockwise", "Rotate counter-clockwise", "Rotate forward", "Rotate reverse", "Disappear"};
		brainwaves.addAll(Arrays.asList(waves));
		moods.addAll(Arrays.asList(moodsArray));
        mentalActions.addAll(Arrays.asList(actions));
		xAxisWaves.setCategories(brainwaves);
		xAxisMood.setCategories(moods);
		XYChart.Series<String,Float> series = new XYChart.Series<>();
		series.getData().add(new XYChart.Data("Alfa", 35f));
		series.getData().add(new XYChart.Data("Beta", 35f));
		series.getData().add(new XYChart.Data("Delta", 35f));
		series.getData().add(new XYChart.Data("Theta", 35f));
		XYChart.Series<String,Float> series2 = new XYChart.Series<>();
		series2.getData().add(new XYChart.Data("Engagement", 35f));
		series2.getData().add(new XYChart.Data("Excitement (long time)", 35f));
		series2.getData().add(new XYChart.Data("Excitement (short time)", 35f));
		series2.getData().add(new XYChart.Data("Frustration", 35f));
		series2.getData().add(new XYChart.Data("Meditation", 35f));

        XYChart.Series<String,Float> series3 = new XYChart.Series<>();
        series3.getData().add(new XYChart.Data("Neural", 35f));
        series3.getData().add(new XYChart.Data("Push", 35f));
        series3.getData().add(new XYChart.Data("Pull", 35f));
        series3.getData().add(new XYChart.Data("Lift", 35f));
        series3.getData().add(new XYChart.Data("Drop", 35f));
        series3.getData().add(new XYChart.Data("Left", 35f));
        series3.getData().add(new XYChart.Data("Right", 35f));
        series3.getData().add(new XYChart.Data("Rot. left", 35f));
        series3.getData().add(new XYChart.Data("Rot. right", 35f));
        series3.getData().add(new XYChart.Data("Rot. cw", 35f));
        series3.getData().add(new XYChart.Data("Rot. ccw", 35f));
        series3.getData().add(new XYChart.Data("Rotate fw", 35f));
        series3.getData().add(new XYChart.Data("Rotate rev", 35f));
        series3.getData().add(new XYChart.Data("Disappear", 35f));
		barChartWaves.getData().add(series);
		for (int i = 0; i < series.getData().size(); i++) {
			if(colorNumber>=constants.Constants.colors.length)
				colorNumber=0;
			series.getData().get(i).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[colorNumber]+";");
			colorNumber++;
		}
		colorNumber=0;
		barChartMoods.getData().add(series2);
		for (int i = 0; i < series2.getData().size(); i++) {
			if(colorNumber>=constants.Constants.colors.length)
				colorNumber=0;
			series2.getData().get(i).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[colorNumber]+";-fx-cursor: hand;-fx-border-color: #000000; -fx-border-width: 2;	");
			colorNumber++;
		}
        barChartMentalActions.getData().add(series3);
        for (int i = 0; i < series3.getData().size(); i++) {
            if(colorNumber>=constants.Constants.colors.length)
                colorNumber=0;
            series3.getData().get(i).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[colorNumber]);
            colorNumber++;
        }
		barChartWaves.setLegendVisible(false);
		barChartMoods.setLegendVisible(false);
        barChartMentalActions.setLegendVisible(false);
		colorNumber=0;
		//createSeriesLineChartWaves(series);
		colorNumber=0;
		createSeriesLineChartMoods(series2);

		//URL url = getClass().getResource("../views/web/radarChart.html");
		URL url = new URL("http://localhost:8080/");
		radarBrowser.getEngine().load(url.toExternalForm());
		if(prefs.getNeverDeletePreference()){
			super.fw = new MainModule(1,queue,queue2,true,120);
		}else{
			super.fw = new MainModule(1,queue,queue2,false,120);
		}

		super.fw.receiveDeviceData();

		updateInterface = new updateInterface() {
			@Override
			public void update(Double[][] finalDataArray) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
                        if(getPutHistoric()){
                            createSeriesLineChartHistoryWaves();
                        }
                        System.out.println(Arrays.toString(finalDataArray[3]));
						updateSeriesLineChartMoods(finalDataArray[3][1].toString(),finalDataArray[3][3].toString(),finalDataArray[3][4].toString(),finalDataArray[3][6].toString(),finalDataArray[3][8].toString());
						for (Series<String, Float> series2 : barChartMoods.getData()) {
							int j=0;
							for (XYChart.Data<String, Float> data2 : series2.getData()) {
								switch (j) {
									case 0:
										data2.setYValue(Float.parseFloat(finalDataArray[3][1].toString()));
										break;
									case 1:
										data2.setYValue(Float.parseFloat(finalDataArray[3][3].toString()));
										break;
									case 2:
										data2.setYValue(Float.parseFloat(finalDataArray[3][4].toString()));
										break;
									case 3:
										data2.setYValue(Float.parseFloat(finalDataArray[3][6].toString()));
										break;
									case 4:
										data2.setYValue(Float.parseFloat(finalDataArray[3][8].toString()));
										break;
									default:
										break;
								}
								j++;
							}
						}
						int serieNumber2=0;
						for(Series<Number,Number> series : lineChartMoods.getData()){
							for(int i=0;i< series.getData().size();i++)
							{
								series.getData().get(i).setYValue(Float.parseFloat((String) moodsGroup.get(serieNumber2).get(i)));
								series.getData().get(i).setXValue(queueTime.get(i));
							}
							serieNumber2++;
						}
						/*if(getPutHistoric()){
							createSeriesLineChartHistoryWaves();
						}*/
						/*if(!finalDataArray[0][0].equals("")){
							String delta = finalDataArray[0][0].toString();
							String theta = finalDataArray[0][1].toString();
							String alpha1 = finalDataArray[0][2].toString();
							String alpha2 = finalDataArray[0][3].toString();
							String beta1 = finalDataArray[0][4].toString();
							String beta2 = finalDataArray[0][5].toString();
							String gamma1 = finalDataArray[0][6].toString();
							String gamma2 = finalDataArray[0][7].toString();
							String attention = finalDataArray[1][0].toString();
							String meditation = finalDataArray[1][1].toString();
							String signal = finalDataArray[2][0].toString();
							updateSeriesLineChartWaves(delta,theta,gamma1,gamma2,alpha1,alpha2,beta1,beta2);
							updateSeriesLineChartMoods(attention,meditation);

							gamma1Data.setText(gamma1);
							gamma2Data.setText(gamma2);
							beta1Data.setText(beta1);
							beta2Data.setText(beta2);
							alfa1Data.setText(alpha1);
							alfa2Data.setText(alpha2);
							thetaData.setText(theta);
							deltaData.setText(delta);
							attentionData.setText(attention);
							meditationData.setText(meditation);
							signalQualityData.setText(signal);
							for (Series<String, Float> series2 : barChartMoods.getData()) {
								int j=0;
								for (XYChart.Data<String, Float> data2 : series2.getData()) {
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

							for (Series<String, Float> series : barChartWaves.getData())
							{
								int i=0;
								for (XYChart.Data<String, Float> data : series.getData())
								{
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
						}*/
					}
				});

			}

			@Override
			public void update2(Double[][] finalDataArray) {
				finalRawDataArray=finalDataArray;
				//updateSeriesLineChartRaw(Double.toString(finalDataArray[0][0]));
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						//System.out.println(getSelectedLobe());
						for (Series<String, Float> series : barChartWaves.getData()) {
							int i = 0;
							for (XYChart.Data<String, Float> data : series.getData()) {
								switch (i) {
									case 0:
										data.setYValue(Float.parseFloat(finalDataArray[0][0].toString()));
										break;
									case 1:
										data.setYValue(Float.parseFloat(finalDataArray[0][1].toString()));
										break;
									case 2:
										data.setYValue(Float.parseFloat(finalDataArray[0][2].toString()));
										break;
									case 3:
										data.setYValue(Float.parseFloat(finalDataArray[0][3].toString()));
										break;
								}
								i++;
							}
						}

						/*for (int i=0;i<finalRawDataArray.length;i++){
							System.out.println(Arrays.toString(finalRawDataArray[i]));
						}*/
						/*rawData.setText(Double.toString(finalRawDataArray[0][0]));
						for(Series<Number,Number> series : lineChartRaw.getData()){
							for(int i=0;i< series.getData().size();i++) {
								series.getData().get(i).setYValue(Float.parseFloat((String) rawGroup.get(0).get(i)));
								series.getData().get(i).setXValue(queueTime2.get(i));
							}
						}*/
					}
				});
			}
		};
		ThreadInterface t = new ThreadInterface(queue,queue2,updateInterface,2);
		new Thread(t).start();
	}

	public void showActions(MouseEvent event) {
		changePane(actionsLabel,new Label[]{dataLabel,moodLabel,radarLabel,brainWavesLabel,settingsLabel,historyLabel},actionsPane,new Pane[]{dataPane,moodPane,radarPane,brainWavesPane,settingsPane, historyPane});
	}

	@Override
	public void showRadar(MouseEvent event){
		changePane(radarLabel,new Label[]{moodLabel,brainWavesLabel,dataLabel,historyLabel,settingsLabel,actionsLabel},radarPane,new Pane[]{moodPane,brainWavesPane,dataPane,historyPane,settingsPane,actionsPane});
	}

	@Override
	public void showSettings(MouseEvent event){
		changePane(settingsLabel,new Label[]{moodLabel,brainWavesLabel,dataLabel,historyLabel,radarLabel,actionsLabel},settingsPane,new Pane[]{moodPane,brainWavesPane,dataPane,historyPane,radarPane,actionsPane});
	}

	@Override
	public void showData(MouseEvent event){
		changePane(dataLabel,new Label[]{moodLabel,brainWavesLabel,radarLabel,historyLabel,settingsLabel,actionsLabel},dataPane,new Pane[]{moodPane,brainWavesPane,radarPane,historyPane,actionsPane,settingsPane,actionsPane});
	}

	@Override
	public void showMood(MouseEvent event){
		changePane(moodLabel,new Label[]{dataLabel,brainWavesLabel,radarLabel,historyLabel,settingsLabel,actionsLabel},moodPane,new Pane[]{dataPane,brainWavesPane,radarPane,historyPane,settingsPane,actionsPane});
	}

	@Override
	public void showBrainWaves(MouseEvent event) {
		changePane(brainWavesLabel,new Label[]{dataLabel,moodLabel,radarLabel,historyLabel,settingsLabel,actionsLabel},brainWavesPane,new Pane[]{dataPane,moodPane,radarPane,historyPane,settingsPane,actionsPane});
	}

	@Override
	public void showHistory(MouseEvent event) {
		changePane(historyLabel,new Label[]{dataLabel,moodLabel,radarLabel,brainWavesLabel,settingsLabel,actionsLabel},historyPane,new Pane[]{dataPane,moodPane,radarPane,brainWavesPane,settingsPane,actionsPane});
	}

	public void createSeriesLineChartMoods(XYChart.Series<String,Float> seriesBarChart){
		xAxisMoodsLine.setLabel("Time");
		XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series4 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series5 = new XYChart.Series<>();
		series1.setName("Engagement");
		series2.setName("Excitement (long time)");
		series3.setName("Excitement (short time)");
		series4.setName("Frustration");
		series5.setName("Meditation");
		for (int i = 0; i < 1000; i++) {
			series1.getData().add(new XYChart.Data(0f, 0f));
			series2.getData().add(new XYChart.Data(0f, 0f));
			series3.getData().add(new XYChart.Data(0f, 0f));
			series4.getData().add(new XYChart.Data(0f, 0f));
			series5.getData().add(new XYChart.Data(0f, 0f));
			engagementQueue.add("0.0");
			excitementLQueue.add("0.0");
			excitementSQueue.add("0.0");
			frustationQueue.add("0.0");
			meditationQueue.add("0.0");
			queueTime.add(0);
		}

		moodsGroup.add(engagementQueue);
		moodsGroup.add(excitementLQueue);
		moodsGroup.add(excitementSQueue);
		moodsGroup.add(frustationQueue);
		moodsGroup.add(meditationQueue);
		lineChartMoods.getData().addAll(series1,series2,series3,series4,series5);
		for (int i = 0; i < seriesBarChart.getData().size(); i++) {
			final int tmp = i;
			final int tmp2 = colorNumber;
			seriesBarChart.getData().get(i).getNode().setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(lineChartMoods.getData().get(tmp).nodeProperty().get().isVisible())
					{
						lineChartMoods.getData().get(tmp).nodeProperty().get().setVisible(false);
						Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + tmp);
						for (Node n : lookupAll) {
							n.setVisible(false);
						}
						seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[tmp2]+";-fx-cursor: hand;");
					}
					else
					{
						Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + tmp);
						for (Node n : lookupAll) {
							n.setVisible(true);
						}
						seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[tmp2]+";-fx-cursor: hand; -fx-border-color: #000000; -fx-border-width: 2;");
						lineChartMoods.getData().get(tmp).nodeProperty().get().setVisible(true);
					}
				}
			});
			colorNumber++;
		}
		this.colorNumber=0;
		for(Series<Number,Number> series : lineChartMoods.getData()){
			if(this.colorNumber>=constants.Constants.colors.length)
				this.colorNumber=0;
			Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + this.colorNumber);
			for (Node n : lookupAll) {
				n.setStyle("-fx-background-color:"+constants.Constants.colors[this.colorNumber]+";");
			}
			series.nodeProperty().get().setStyle("-fx-stroke: " +constants.Constants.colors[this.colorNumber]+";");
			this.colorNumber++;
		}
		lineChartMoods.setLegendVisible(false);
		lineChartMoods.setAnimated(false);

		xAxisMoodsLine.setLowerBound(0);
		xAxisMoodsLine.setUpperBound(50);
		xAxisMoodsLine.setAutoRanging(false);
	}
	public void updateSeriesLineChartMoods(String e,String el,String es,String f,String m)
	{
		queueTime.add((System.currentTimeMillis()/1000)-time);
		queueTime.remove(0);
		moodsGroup.get(0).add(e);
		moodsGroup.get(1).add(el);
		moodsGroup.get(2).add(es);
		moodsGroup.get(3).add(f);
		moodsGroup.get(4).add(m);

		for (int i = 0; i < moodsGroup.size(); i++) {
			moodsGroup.get(i).remove(0);
		}
		/*xAxisMoodsLine.setLowerBound(Double.parseDouble(queueTime.get(0).toString()));
		xAxisMoodsLine.setUpperBound(Double.parseDouble(queueTime.get(9).toString()));*/
	}

	public void createSeriesLineChartHistoryWaves(){
		String[][] historic=super.getHistoric();
		xAxisHistory.setLabel("Time");
		XYChart.Series<String, Number> series3 = new XYChart.Series<>();
		XYChart.Series<String, Number> series4 = new XYChart.Series<>();
		XYChart.Series<String, Number> series5 = new XYChart.Series<>();
		XYChart.Series<String, Number> series6 = new XYChart.Series<>();
		series3.setName("Alfa");
		series4.setName("Beta");
		series5.setName("Delta");
		series6.setName("Teta");
		if(historic.length!=0){
			for (int i = 1; i < historic.length; i++) {
				series3.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][1])));
				series4.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][2])));
				series5.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][4])));
				series6.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][3])));
			}
		}

		this.colorNumber=0;
		for(Series<String, Number> series : lineChartHistory.getData()){
			if(this.colorNumber>=constants.Constants.colors.length)
				this.colorNumber=0;
			Set<Node> lookupAll = lineChartHistory.lookupAll(".chart-line-symbol.series" + this.colorNumber);
			for (Node n : lookupAll) {
				n.setStyle("-fx-background-color:"+constants.Constants.colors[this.colorNumber]+";");
			}
			series.nodeProperty().get().setStyle("-fx-stroke: " +constants.Constants.colors[this.colorNumber]+";");
			this.colorNumber++;
		}
		lineChartHistory.getData().addAll(series3,series4,series5,series6);
		//lineChartHistory.setAnimated(false);
		super.setPutHistoric(false);
	}

	public void createSeriesLineChartWaves(XYChart.Series<String,Float> seriesBarChart){
		xAxisWavesLine.setLabel("Freq(Hz)");
		XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series4 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series5 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series6 = new XYChart.Series<>();

		series3.setName("Alpha");
		series4.setName("Beta");
		series5.setName("Thelta");
		series6.setName("Theta");

		for (int i = 0; i < 10; i++) {
			series3.getData().add(new XYChart.Data(0f, 0f));
			series4.getData().add(new XYChart.Data(0f, 0f));
			series5.getData().add(new XYChart.Data(0f, 0f));
			series6.getData().add(new XYChart.Data(0f, 0f));
			thetaQueue.add("0.0");
			alphaQueue.add("0.0");
			deltaQueue.add("0.0");
			betaQueue.add("0.0");
			queueTime.add(0);
		}
		wavesGroup.add(alphaQueue);
		wavesGroup.add(betaQueue);
		wavesGroup.add(deltaQueue);
		wavesGroup.add(thetaQueue);
		lineChartWaves.getData().addAll(series3,series4,series5,series6);
		for (int i = 0; i < seriesBarChart.getData().size(); i++) {
			final int tmp = i;
			final int tmp2 = colorNumber;
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
						seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[tmp2]+";-fx-cursor: hand;");
					}
					else
					{
						Set<Node> lookupAll = lineChartWaves.lookupAll(".chart-line-symbol.series" + tmp);
						for (Node n : lookupAll) {
							n.setVisible(true);
						}
						seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[tmp2]+";-fx-cursor: hand; -fx-border-color: #000000; -fx-border-width: 2;");
						lineChartWaves.getData().get(tmp).nodeProperty().get().setVisible(true);
					}
				}
			});
			colorNumber++;
		}

		this.colorNumber=0;
		for(Series<Number,Number> series : lineChartWaves.getData()){
			if(this.colorNumber>=constants.Constants.colors.length)
				this.colorNumber=0;
			Set<Node> lookupAll = lineChartWaves.lookupAll(".chart-line-symbol.series" + this.colorNumber);
			for (Node n : lookupAll) {
				n.setStyle("-fx-background-color:"+constants.Constants.colors[this.colorNumber]+";");
			}
			series.nodeProperty().get().setStyle("-fx-stroke: " +constants.Constants.colors[this.colorNumber]+";");
			this.colorNumber++;
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
	}

	public void launchAnalysisView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/analysisView.fxml"));
			Parent parent = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(parent, 462, 439));
			stage.setTitle("BrainLight - Analysis");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launchSensorsView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/sensorsView.fxml"));
			Parent parent = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(parent, 462, 450));
			stage.setTitle("BrainLight - Sensors");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launchCalibrationView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/calibrationView.fxml"));
			Parent parent = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(parent, 462, 226));
			stage.setTitle("BrainLight - Calibration");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
