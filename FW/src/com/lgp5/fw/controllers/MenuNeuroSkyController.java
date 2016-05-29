package com.lgp5.fw.controllers;
/*
import com.lgp5.api.neurosky.Neurosky_FW.Neurosky;
import com.lgp5.api.neurosky.Neurosky_FW.interfaces.HeadSetDataInterface;
import com.lgp5.api.neurosky.Neurosky_FW.utils.Constants;
*/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;


public class MenuNeuroSkyController extends MenuController{

	private int colorNumber=0;
	@FXML private Label gamma1Data;
	@FXML private Label analysisLabel;
	@FXML private Label gamma2Data;
	@FXML private Label alfa1Data;
	@FXML private Label alfa2Data;
	@FXML private Label beta1Data;
	@FXML private Label beta2Data;
	@FXML private Label deltaData;
	@FXML private Label thetaData;
	@FXML private Label attentionData;
	@FXML private Label meditationData; 
	@FXML private Label errorRateData;
	@FXML private Label batteryLevelData;
	@FXML private Label signalQualityData;
	@FXML private Pane analysisWrapper;
	@FXML private WebView radarBrowser;
	//private HeadSetDataInterface headSetDataInterface;
	@FXML private BarChart<String, Float> barChartWaves;
	@FXML private BarChart<String, Float> barChartMoods;
	@FXML private StackedAreaChart<Float, Float> radarGraphA;
	@FXML private StackedAreaChart<Float, Float> radarGraphB;
	@FXML private StackedAreaChart<Float, Float> radarGraphC;
	@FXML private StackedAreaChart<Float, Float> radarGraphD;
	@FXML private CategoryAxis xAxisWaves;
	@FXML private CategoryAxis xAxisMood;
	@FXML private NumberAxis xAxisWavesLine;
	@FXML private NumberAxis xAxisMoodsLine;
	private ObservableList<String> brainwaves = FXCollections.observableArrayList();
	private ObservableList<String> moods = FXCollections.observableArrayList();
	@FXML private NumberAxis xAxisHistory;
	@FXML private NumberAxis yAxisHistory;
	@FXML private LineChart<Number, Number> lineChartHistory;
	@FXML private LineChart<Number, Number> lineChartWaves;
	@FXML private LineChart<Number, Number> lineChartMoods;
	@FXML private CheckBox historyCheckBox;
	Vector<ArrayList> wavesGroup = new Vector<ArrayList>(2);
	Vector<ArrayList> moodsGroup = new Vector<ArrayList>(2);
	ArrayList<String> deltaQueue =  new ArrayList<String>();
	ArrayList<String> thetaQueue =  new ArrayList<String>();
	ArrayList<String> highAlphaQueue =  new ArrayList<String>();
	ArrayList<String> lowAlphaQueue =  new ArrayList<String>();
	ArrayList<String> highBetaQueue =  new ArrayList<String>();
	ArrayList<String> lowBetaQueue =  new ArrayList<String>();
	ArrayList<String> lowGammaQueue =  new ArrayList<String>();
	ArrayList<String> highGammaQueue =  new ArrayList<String>();
	ArrayList<String> attentionQueue =  new ArrayList<String>();
	ArrayList<String> meditationQueue =  new ArrayList<String>();
	ArrayList<Number> queueTime = new ArrayList<Number>();
	private long time;
	private Tooltip unavailableFeatureTooltip = new Tooltip("This feature is unavailable for NeuroSky Mindset");

	public MenuNeuroSkyController() {
	}

	/**
	 * Initializes the controller class and sets x axis of the bar chart with the appropriate values
	 */
	@FXML
	private void initialize() throws MalformedURLException {
		settings();

		time=System.currentTimeMillis()/1000;
		String[] waves = {"Delta", "Theta", "Alfa 1", "Alfa 2", "Beta 1", "Beta 2", "Gamma 1", "Gamma 2"};
		String[] moodsArray = {"Attention","Meditation"};

		analysisLabel.setDisable(true);

		analysisWrapper.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Node node = (Node) event.getSource();
				unavailableFeatureTooltip.show(node, event.getScreenX() + 50, event.getScreenY());
			}
		});

		analysisWrapper.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				unavailableFeatureTooltip.hide();
			}
		});

		brainwaves.addAll(Arrays.asList(waves));
		moods.addAll(Arrays.asList(moodsArray));
		xAxisWaves.setCategories(brainwaves);
		xAxisMood.setCategories(moods);
		XYChart.Series<String,Float> series = new XYChart.Series<>();
		series.getData().add(new XYChart.Data("Delta", 35f));
		series.getData().add(new XYChart.Data("Theta", 35f));
		series.getData().add(new XYChart.Data("Alfa 1", 35f));
		series.getData().add(new XYChart.Data("Alfa 2", 35f));
		series.getData().add(new XYChart.Data("Beta 1", 35f));
		series.getData().add(new XYChart.Data("Beta 2", 35f));
		series.getData().add(new XYChart.Data("Gamma 1", 35f));
		series.getData().add(new XYChart.Data("Gamma 2", 35f));
		XYChart.Series<String,Float> series2 = new XYChart.Series<>();
		series2.getData().add(new XYChart.Data("Attention", 35f));
		series2.getData().add(new XYChart.Data("Meditation", 35f));
		System.out.println(this.colorNumber);
		barChartWaves.getData().add(series);
		for (int i = 0; i < series.getData().size(); i++) {
			if(colorNumber>=constants.Constants.colors.length)						
				colorNumber=0;						
			series.getData().get(i).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[colorNumber]+";-fx-cursor: hand;-fx-border-color: #000000; -fx-border-width: 2;	"); 	
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
		barChartWaves.setLegendVisible(false);
		barChartMoods.setLegendVisible(false);
		colorNumber=0;
		createSeriesLineChartWaves(series);
		colorNumber=0;
		createSeriesLineChartMoods(series2);

		URL url = getClass().getResource("../views/web/radarChart.html");
		radarBrowser.getEngine().load(url.toExternalForm());
		/*
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
					updateSeriesLineChartMoods(attention,meditation);

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
							meditationData.setText(meditation);
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
							int serieNumber2=0;
							for(Series<Number,Number> series : lineChartMoods.getData()){							
								for(int i=0;i< series.getData().size();i++) 
								{									
									series.getData().get(i).setYValue(Float.parseFloat((String) moodsGroup.get(serieNumber2).get(i)));
									series.getData().get(i).setXValue(queueTime.get(i));
								}
								serieNumber2++;
							}
							for (Series<String, Float> series : barChartWaves.getData()) 
							{
								int i=0;
								for (Data<String, Float> data : series.getData()) 
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
						}
					});
				}
			}
		};

		new Thread(new Neurosky("0013EF004809", headSetDataInterface)).start();*/
	}
	private void createSeriesLineChartMoods(XYChart.Series<String,Float> seriesBarChart){
		xAxisMoodsLine.setLabel("Time");
		XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
		XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
		series1.setName("Attention");
		series2.setName("Meditation");
		for (int i = 0; i < 10; i++) {
			series1.getData().add(new XYChart.Data(0f, 0f));
			series2.getData().add(new XYChart.Data(0f, 0f));
			attentionQueue.add("0.0");
			meditationQueue.add("0.0");
		}
		moodsGroup.add(attentionQueue);
		moodsGroup.add(meditationQueue);
		lineChartMoods.getData().addAll(series1,series2);			
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
	private void updateSeriesLineChartMoods(String a,String m)
	{
		moodsGroup.get(0).add(a);					
		moodsGroup.get(1).add(m);		

		for (int i = 0; i < moodsGroup.size(); i++) {
			moodsGroup.get(i).remove(0);
		}
		xAxisMoodsLine.setLowerBound(Double.parseDouble(queueTime.get(0).toString()));
		xAxisMoodsLine.setUpperBound(Double.parseDouble(queueTime.get(9).toString()));	
	}

	private void createSeriesLineChartWaves(XYChart.Series<String,Float> seriesBarChart){
		xAxisWavesLine.setLabel("Time");	
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
			deltaQueue.add("0.0");
			thetaQueue.add("0.0");
			highAlphaQueue.add("0.0");
			lowAlphaQueue.add("0.0");
			highBetaQueue.add("0.0");
			lowBetaQueue.add("0.0");
			lowGammaQueue.add("0.0");
			highGammaQueue.add("0.0");
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
						System.out.println(seriesBarChart.getData().get(tmp).getNode().getStyle().toString());
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

	private void updateSeriesLineChartWaves(String d,String t,String g1,String g2,String a1,String a2,String b1,String b2)
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
}
