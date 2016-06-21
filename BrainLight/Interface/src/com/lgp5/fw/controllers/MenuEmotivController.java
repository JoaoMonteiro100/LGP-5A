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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;

public class MenuEmotivController extends MenuController {
    /*BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(1);
    BlockingQueue queue2 = new ArrayBlockingQueue<Double[][]>(1);*/
    private Double[][] finalRawDataArray = new Double[100][100];
    private int colorNumber = 0;
    @FXML
    private Label alphaData;
    @FXML
    private Label betaData;
    @FXML
    private Label deltaData;
    @FXML
    private Label thetaData;
    @FXML
    private Label longExcitementData;
    @FXML
    private Label engagementData;
    @FXML
    private Label shortExcitementData;
    @FXML
    private Label meditationData;
    @FXML
    private Label frustrationData;
    @FXML
    private Label signalQualityData;
    @FXML
    private Label actionsLabel;
    @FXML
    private GridPane actionsPane;
    @FXML
    private Slider historyPeriodSlider;
    @FXML
    private ImageView battery0;
    @FXML
    private ImageView battery1;
    @FXML
    private ImageView battery2;
    @FXML
    private ImageView battery3;
    @FXML
    private ImageView battery4;
    @FXML
    private ImageView battery5;
    @FXML
    private ImageView signal0;
    @FXML
    private ImageView signal1;
    @FXML
    private ImageView signal2;
    @FXML
    private ImageView signal3;
    @FXML
    private ImageView eyesStare;
    @FXML
    private ImageView eyesBlink;
    @FXML
    private ImageView eyesLookRight;
    @FXML
    private ImageView eyesLookLeft;
    @FXML
    private ImageView eyesLookUp;
    @FXML
    private ImageView eyesLookDown;
    @FXML
    private ImageView eyesWinkRight;
    @FXML
    private ImageView eyesWinkLeft;
    @FXML
    private WebView radarBrowser;
    @FXML
    private BarChart<String, Float> barChartWaves;
    @FXML
    private BarChart<String, Float> barChartMoods;
    @FXML
    private BarChart<String, Float> barChartMentalActions;
    @FXML
    private CategoryAxis xAxisWaves;
    @FXML    private NumberAxis yAxisWaves;
    @FXML
    private CategoryAxis xAxisMood;
    @FXML
    private NumberAxis xAxisWavesLine;
    @FXML private NumberAxis yAxisWavesLine;
    @FXML
    private NumberAxis xAxisMoodsLine;
    @FXML
    private NumberAxis yAxisMoodsLine;
    private ObservableList<String> brainwaves = FXCollections.observableArrayList();
    private ObservableList<String> moods = FXCollections.observableArrayList();
    private ObservableList<String> mentalActions = FXCollections.observableArrayList();
    @FXML
    private CategoryAxis xAxisHistory;
    @FXML
    private NumberAxis yAxisHistory;
    @FXML
    private LineChart<String, Number> lineChartHistory;
    @FXML
    private LineChart<Number, Number> lineChartWaves;
    @FXML
    private LineChart<Number, Number> lineChartMoods;
    @FXML
    private Button calibrationButton;
    private updateInterface updateInterface;
    Vector<ArrayList> wavesGroup = new Vector<ArrayList>(2);
    Vector<ArrayList> moodsGroup = new Vector<ArrayList>(2);
    ArrayList<String> deltaQueue = new ArrayList<String>();
    ArrayList<String> alphaQueue = new ArrayList<String>();
    ArrayList<String> betaQueue = new ArrayList<String>();
    ArrayList<String> thetaQueue = new ArrayList<String>();
    ArrayList<String> frustationQueue = new ArrayList<String>();
    ArrayList<String> meditationQueue = new ArrayList<String>();
    ArrayList<String> engagementQueue = new ArrayList<String>();
    ArrayList<String> excitementLQueue = new ArrayList<String>();
    ArrayList<String> excitementSQueue = new ArrayList<String>();
    ArrayList<Number> queueTime = new ArrayList<Number>();
    private long time;

    public MenuEmotivController() {
        super(1);
    }

    /**
     * Initializes the controller class and sets x axis of the bar chart with the appropriate values
     */
    @FXML
    private void initialize() throws MalformedURLException {
        settings();
        time = System.currentTimeMillis() / 1000;
        String[] waves = {"Alfa", "Beta", "Delta", "Theta"};
        String[] moodsArray = {"Engagement", "Excitement (long time)", "Excitement (short time)", "Frustration", "Meditation"};
        String[] actions = {"Neural", "Push", "Pull", "Lift", "Drop", "Left", "Right", "Rotate left", "Rotate right", "Rotate clockwise", "Rotate counter-clockwise", "Rotate forward", "Rotate reverse", "Disappear"};
        brainwaves.addAll(Arrays.asList(waves));
        moods.addAll(Arrays.asList(moodsArray));
        mentalActions.addAll(Arrays.asList(actions));
        xAxisWaves.setCategories(brainwaves);
        yAxisWaves.setLabel("Power");
        xAxisMood.setCategories(moods);
        XYChart.Series<String, Float> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data("Alfa", 35f));
        series.getData().add(new XYChart.Data("Beta", 35f));
        series.getData().add(new XYChart.Data("Delta", 35f));
        series.getData().add(new XYChart.Data("Theta", 35f));
        XYChart.Series<String, Float> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data("Engagement", 35f));
        series2.getData().add(new XYChart.Data("Excitement (long time)", 35f));
        series2.getData().add(new XYChart.Data("Excitement (short time)", 35f));
        series2.getData().add(new XYChart.Data("Frustration", 35f));
        series2.getData().add(new XYChart.Data("Meditation", 35f));

        XYChart.Series<String, Float> series3 = new XYChart.Series<>();
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
            if (colorNumber >= constants.Constants.colors.length)
                colorNumber = 0;
            series.getData().get(i).getNode().setStyle("-fx-bar-fill: " + constants.Constants.colors[colorNumber] + ";");
            colorNumber++;
        }
        colorNumber = 0;
        barChartMoods.getData().add(series2);
        for (int i = 0; i < series2.getData().size(); i++) {
            if (colorNumber >= constants.Constants.colors.length)
                colorNumber = 0;
            series2.getData().get(i).getNode().setStyle("-fx-bar-fill: " + constants.Constants.colors[colorNumber] + ";-fx-cursor: hand;-fx-border-color: #000000; -fx-border-width: 2;	");
            colorNumber++;
        }
        barChartMentalActions.getData().add(series3);
        for (int i = 0; i < series3.getData().size(); i++) {
            if (colorNumber >= constants.Constants.colors.length)
                colorNumber = 0;
            series3.getData().get(i).getNode().setStyle("-fx-bar-fill: " + constants.Constants.colors[colorNumber]);
            colorNumber++;
        }
        barChartWaves.setLegendVisible(false);
        barChartMoods.setLegendVisible(false);
        barChartMentalActions.setLegendVisible(false);
        colorNumber = 0;
        createSeriesLineChartFrequencies();
        colorNumber = 0;
        createSeriesLineChartMoods(series2);

        //URL url = getClass().getResource("../views/web/radarChart.html");
        URL url = new URL("http://localhost:8080/");
        radarBrowser.getEngine().load(url.toExternalForm());

        super.fw.receiveDeviceData();
        updateInterface = new updateInterface() {
            @Override
            public void update(Double[][] finalDataArray) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (finalDataArray[0][3] == 0.0) {
                            signalQualityData.setText("NO_SIGNAL");
                        } else if (finalDataArray[0][3] == 1.0) {
                            signalQualityData.setText("VERY_BAD_SIG");
                        } else if (finalDataArray[0][3] == 2.0) {
                            signalQualityData.setText("POOR_SIG");
                        } else if (finalDataArray[0][3] == 3.0) {
                            signalQualityData.setText("FAIR_SIG");
                        } else if (finalDataArray[0][3] == 4.0) {
                            signalQualityData.setText("GOOD_SIG");
                        }
                        showBatteryLevel(finalDataArray[0][0].intValue());
                        showWirelessSignal(finalDataArray[0][1].intValue());
                        showExpressionsAndActions(finalDataArray[1], finalDataArray[2]);

                        // showActions(finalDataArray[0][1].intValue());
                     /*   //show(finalDataArray[0][1].intValue());
                        System.out.println(finalDataArray[1][0]);
                        System.err.println(finalDataArray[1][1]);
                        System.out.println(finalDataArray[1][2]);
                        System.err.println(finalDataArray[1][3]);
                        System.out.println();*/
                       /* System.err.println(finalDataArray[2][0]);
                        System.err.println(finalDataArray[2][1]);
                        System.err.println(finalDataArray[2][2]);
                        System.err.println(finalDataArray[2][3]);
                        System.out.println();*/

                        updateSeriesLineChartMoods(finalDataArray[3][1].toString(), finalDataArray[3][3].toString(), finalDataArray[3][4].toString(), finalDataArray[3][6].toString(), finalDataArray[3][8].toString());
                        for (Series<String, Float> series2 : barChartMoods.getData()) {
                            int j = 0;
                            for (XYChart.Data<String, Float> data2 : series2.getData()) {
                                switch (j) {
                                    case 0:
                                        data2.setYValue(Float.parseFloat(finalDataArray[3][1].toString()));
                                        engagementData.setText(String.valueOf(Math.floor(finalDataArray[3][1] * 100)) + "%");
                                        break;
                                    case 1:
                                        data2.setYValue(Float.parseFloat(finalDataArray[3][3].toString()));
                                        longExcitementData.setText(String.valueOf(Math.floor(finalDataArray[3][3] * 100)) + "%");
                                        break;
                                    case 2:
                                        data2.setYValue(Float.parseFloat(finalDataArray[3][4].toString()));
                                        shortExcitementData.setText(String.valueOf(Math.floor(finalDataArray[3][4] * 100)) + "%");
                                        break;
                                    case 3:
                                        data2.setYValue(Float.parseFloat(finalDataArray[3][6].toString()));
                                        frustrationData.setText(String.valueOf(Math.floor(finalDataArray[3][6] * 100)) + "%");
                                        break;
                                    case 4:
                                        data2.setYValue(Float.parseFloat(finalDataArray[3][8].toString()));
                                        meditationData.setText(String.valueOf(Math.floor(finalDataArray[3][8] * 100)) + "%");
                                        break;
                                    default:
                                        break;
                                }
                                j++;
                            }
                        }
                        int serieNumber2 = 0;
                        for (Series<Number, Number> series : lineChartMoods.getData()) {
                            for (int i = 0; i < series.getData().size(); i++) {
                                series.getData().get(i).setYValue(Float.parseFloat((String) moodsGroup.get(serieNumber2).get(i)));
                                series.getData().get(i).setXValue(queueTime.get(i));
                            }
                            serieNumber2++;
                        }
                        if (getPutHistoric()) {
                            createSeriesLineChartHistoryWaves();
                        }
                    }
                });

            }

            @Override
            public void update2(Double[][] finalDataArray) {
                finalRawDataArray = finalDataArray;
                //updateSeriesLineChartRaw(Double.toString(finalDataArray[0][0]));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                       for (Series<Number, Number> series : lineChartWaves.getData()) {
                            for (int i = 0; i < series.getData().size(); i++) {
                                //System.out.println(series.getData().size());
                               series.getData().get(i).setYValue(Float.parseFloat(finalDataArray[2][i].toString()));
                                //System.out.println(finalDataArray[2][i]);
                            }
                        }
                        for (Series<String, Float> series : barChartWaves.getData()) {
                            int i = 0;
                            for (XYChart.Data<String, Float> data : series.getData()) {
                                switch (i) {
                                    case 0:
                                        //data.setYValue((float)(Math.floor(Float.parseFloat(finalDataArray[0][0].toString()))*100)/100);
                                        data.setYValue(Float.parseFloat(finalDataArray[0][0].toString()));
                                        break;
                                    case 1:
                                        //data.setYValue((float)(Math.floor(Float.parseFloat(finalDataArray[0][1].toString()))*100)/100);
                                        data.setYValue(Float.parseFloat(finalDataArray[0][1].toString()));
                                        break;
                                    case 2:
                                        //data.setYValue((float)(Math.floor(Float.parseFloat(finalDataArray[0][2].toString()))*100)/100);
                                        data.setYValue(Float.parseFloat(finalDataArray[0][2].toString()));
                                        break;
                                    case 3:
                                        //data.setYValue((float)(Math.floor(Float.parseFloat(finalDataArray[0][3].toString()))*100)/100);
                                        data.setYValue(Float.parseFloat(finalDataArray[0][3].toString()));
                                        break;
                                }
                                i++;
                            }
                        }
                        alphaData.setText(String.valueOf(Math.floor(finalDataArray[0][0] * 100) / 100) + "dB");
                        betaData.setText(String.valueOf(Math.floor(finalDataArray[0][1] * 100) / 100) + "dB");
                        deltaData.setText(String.valueOf(Math.floor(finalDataArray[0][2] * 100) / 100) + "dB");
                        thetaData.setText(String.valueOf(Math.floor(finalDataArray[0][3] * 100) / 100) + "dB");
                    }
                });
            }
        };
        ThreadInterface t = new ThreadInterface(queue, queue2, updateInterface, 2);
        Thread thread = new Thread(t);
        thread.setDaemon(true);
        thread.start();
    }

    public void showExpressionsAndActions(Double[] actions, Double[] expressions) {
        eyesStare.setVisible(false);
        eyesLookRight.setVisible(false);
        eyesLookUp.setVisible(false);
        eyesLookDown.setVisible(false);
        eyesLookLeft.setVisible(false);
        eyesBlink.setVisible(false);
        eyesWinkLeft.setVisible(false);
        eyesWinkRight.setVisible(false);
        if(expressions[2]==1.0){
            eyesBlink.setVisible(true);
        }
        else if(expressions[0]==1.0){
            eyesWinkLeft.setVisible(true);
        }
        else if(expressions[1]==1.0){
            eyesWinkRight.setVisible(true);
        }
        else {
            if(actions[0]==1.0)
                eyesLookLeft.setVisible(true);
            else if(actions[1]==1.0)
                eyesLookRight.setVisible(true);
            else if(actions[2]==1.0)
                eyesLookDown.setVisible(true);
            else if(actions[3]==1.0)
                eyesLookUp.setVisible(true);
            else
            eyesStare.setVisible(true);
        }
    }


    public void showWirelessSignal(int signal) {
        switch (signal) {
            case 0:
                signal0.setVisible(true);
                signal1.setVisible(false);
                signal2.setVisible(false);
                signal3.setVisible(false);
                break;
            case 1:
                signal0.setVisible(false);
                signal1.setVisible(true);
                signal2.setVisible(false);
                signal3.setVisible(false);
                break;
            case 2:
                signal0.setVisible(false);
                signal1.setVisible(false);
                signal2.setVisible(true);
                signal3.setVisible(false);
                break;
            case 3:
                signal0.setVisible(false);
                signal1.setVisible(false);
                signal2.setVisible(false);
                signal3.setVisible(true);
                break;
        }
    }

    public void showBatteryLevel(int level) {
        switch (level) {
            case 0:
                battery0.setVisible(true);
                battery1.setVisible(false);
                battery2.setVisible(false);
                battery3.setVisible(false);
                battery4.setVisible(false);
                battery5.setVisible(false);
                break;
            case 1:
                battery0.setVisible(false);
                battery1.setVisible(true);
                battery2.setVisible(false);
                battery3.setVisible(false);
                battery4.setVisible(false);
                battery5.setVisible(false);
                break;
            case 2:
                battery0.setVisible(false);
                battery1.setVisible(false);
                battery2.setVisible(true);
                battery3.setVisible(false);
                battery4.setVisible(false);
                battery5.setVisible(false);
                break;
            case 3:
                battery0.setVisible(false);
                battery1.setVisible(false);
                battery2.setVisible(false);
                battery3.setVisible(true);
                battery4.setVisible(false);
                battery5.setVisible(false);
                break;
            case 4:
                battery0.setVisible(false);
                battery1.setVisible(false);
                battery2.setVisible(false);
                battery3.setVisible(false);
                battery4.setVisible(true);
                battery5.setVisible(false);
                break;
            case 5:
                battery0.setVisible(false);
                battery1.setVisible(false);
                battery2.setVisible(false);
                battery3.setVisible(false);
                battery4.setVisible(false);
                battery5.setVisible(true);
                break;
        }
    }

    public void showActions(MouseEvent event) {
        changePane(actionsLabel, new Label[]{dataLabel, moodLabel, radarLabel, brainWavesLabel, settingsLabel, historyLabel}, actionsPane, new Pane[]{dataPane, moodPane, radarPane, brainWavesPane, settingsPane, historyPane});
    }

    @Override
    public void showRadar(MouseEvent event) {
        changePane(radarLabel, new Label[]{moodLabel, brainWavesLabel, dataLabel, historyLabel, settingsLabel, actionsLabel}, radarPane, new Pane[]{moodPane, brainWavesPane, dataPane, historyPane, settingsPane, actionsPane});
    }

    @Override
    public void showSettings(MouseEvent event) {
        changePane(settingsLabel, new Label[]{moodLabel, brainWavesLabel, dataLabel, historyLabel, radarLabel, actionsLabel}, settingsPane, new Pane[]{moodPane, brainWavesPane, dataPane, historyPane, radarPane, actionsPane});
    }

    @Override
    public void showData(MouseEvent event) {
        changePane(dataLabel, new Label[]{moodLabel, brainWavesLabel, radarLabel, historyLabel, settingsLabel, actionsLabel}, dataPane, new Pane[]{moodPane, brainWavesPane, radarPane, historyPane, actionsPane, actionsPane});
    }

    @Override
    public void showMood(MouseEvent event) {
        changePane(moodLabel, new Label[]{dataLabel, brainWavesLabel, radarLabel, historyLabel, settingsLabel, actionsLabel}, moodPane, new Pane[]{dataPane, brainWavesPane, radarPane, historyPane, settingsPane, actionsPane});
    }

    @Override
    public void showBrainWaves(MouseEvent event) {
        changePane(brainWavesLabel, new Label[]{dataLabel, moodLabel, radarLabel, historyLabel, settingsLabel, actionsLabel}, brainWavesPane, new Pane[]{dataPane, moodPane, radarPane, historyPane, settingsPane, actionsPane});
    }

    @Override
    public void showHistory(MouseEvent event) {
        changePane(historyLabel, new Label[]{dataLabel, moodLabel, radarLabel, brainWavesLabel, settingsLabel, actionsLabel}, historyPane, new Pane[]{dataPane, moodPane, radarPane, brainWavesPane, settingsPane, actionsPane});
    }

    public void createSeriesLineChartHistoryWaves() {
        lineChartHistory.getData().retainAll();
        String[][] historic = super.getHistoric();
        xAxisHistory.setLabel("Time");
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        XYChart.Series<String, Number> series5 = new XYChart.Series<>();
        XYChart.Series<String, Number> series6 = new XYChart.Series<>();
        series3.setName("Alpha");
        series4.setName("Beta");
        series5.setName("Delta");
        series6.setName("Theta");
        if (historic.length != 0) {
            for (int i = 1; i < historic.length; i++) {
                series3.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][1])));
                series4.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][2])));
                series5.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][4])));
                series6.getData().add(new XYChart.Data<String, Number>(historic[i][0], Float.parseFloat(historic[i][3])));
            }
        }

        this.colorNumber = 0;
        for (Series<String, Number> series : lineChartHistory.getData()) {
            if (this.colorNumber >= constants.Constants.colors.length)
                this.colorNumber = 0;
            Set<Node> lookupAll = lineChartHistory.lookupAll(".chart-line-symbol.series" + this.colorNumber);
            for (Node n : lookupAll) {
                n.setStyle("-fx-background-color:" + constants.Constants.colors[this.colorNumber] + ";");
            }
            series.nodeProperty().get().setStyle("-fx-stroke: " + constants.Constants.colors[this.colorNumber] + ";");
            this.colorNumber++;
        }
        lineChartHistory.getData().addAll(series3, series4, series5, series6);
        //lineChartHistory.setAnimated(false);
        super.setPutHistoric(false);
    }

    public void createSeriesLineChartMoods(XYChart.Series<String, Float> seriesBarChart) {
        xAxisMoodsLine.setLabel("Time");
        yAxisMoodsLine.setLabel("Mood");
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
        for (int i = 0; i < 50; i++) {
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
        lineChartMoods.getData().addAll(series1, series2, series3, series4, series5);
        for (int i = 0; i < seriesBarChart.getData().size(); i++) {
            final int tmp = i;
            final int tmp2 = colorNumber;
            seriesBarChart.getData().get(i).getNode().setOnMouseClicked(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (lineChartMoods.getData().get(tmp).nodeProperty().get().isVisible()) {
                        lineChartMoods.getData().get(tmp).nodeProperty().get().setVisible(false);
                        Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + tmp);
                        for (Node n : lookupAll) {
                            n.setVisible(false);
                        }
                        seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: " + constants.Constants.colors[tmp2] + ";-fx-cursor: hand;");
                    } else {
                        Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + tmp);
                        for (Node n : lookupAll) {
                            n.setVisible(true);
                        }
                        seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: " + constants.Constants.colors[tmp2] + ";-fx-cursor: hand; -fx-border-color: #000000; -fx-border-width: 2;");
                        lineChartMoods.getData().get(tmp).nodeProperty().get().setVisible(true);
                    }
                }
            });
            colorNumber++;
        }
        this.colorNumber = 0;
        for (Series<Number, Number> series : lineChartMoods.getData()) {
            if (this.colorNumber >= constants.Constants.colors.length)
                this.colorNumber = 0;
            Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + this.colorNumber);
            for (Node n : lookupAll) {
                n.setStyle("-fx-background-color:" + constants.Constants.colors[this.colorNumber] + ";");
            }
            series.nodeProperty().get().setStyle("-fx-stroke: " + constants.Constants.colors[this.colorNumber] + ";");
            this.colorNumber++;
        }
        lineChartMoods.setLegendVisible(false);
        lineChartMoods.setAnimated(false);

        xAxisMoodsLine.setLowerBound(0);
        xAxisMoodsLine.setUpperBound(50);
        xAxisMoodsLine.setAutoRanging(false);
    }

    public void updateSeriesLineChartMoods(String e, String el, String es, String f, String m) {
        queueTime.add((System.currentTimeMillis() / 1000) - time);
        queueTime.remove(0);
        moodsGroup.get(0).add(e);
        moodsGroup.get(1).add(el);
        moodsGroup.get(2).add(es);
        moodsGroup.get(3).add(f);
        moodsGroup.get(4).add(m);

        for (int i = 0; i < moodsGroup.size(); i++) {
            moodsGroup.get(i).remove(0);
        }
        //yAxisMoodsLine.setUpperBound(1.0);
        xAxisMoodsLine.setLowerBound(Double.parseDouble(queueTime.get(0).toString()));
        xAxisMoodsLine.setUpperBound(Double.parseDouble(queueTime.get(49).toString()));
    }

    public void createSeriesLineChartFrequencies() {
        xAxisWavesLine.setLabel("Frequency(Hz)");
        yAxisWavesLine.setLabel("Magnitude(dB)");
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();

        for (int i = 0; i < 31; i++) {
            series1.getData().add(new XYChart.Data(i+1, 0f));
        }
        wavesGroup.add(thetaQueue);
        lineChartWaves.getData().addAll(series1);

        this.colorNumber = 0;
        for (Series<Number, Number> series : lineChartWaves.getData()) {
            if (this.colorNumber >= constants.Constants.colors.length)
                this.colorNumber = 0;
            Set<Node> lookupAll = lineChartWaves.lookupAll(".chart-line-symbol.series" + this.colorNumber);
            for (Node n : lookupAll) {
                n.setStyle("-fx-background-color:" + constants.Constants.colors[this.colorNumber] + ";");
            }
            series.nodeProperty().get().setStyle("-fx-stroke: " + constants.Constants.colors[this.colorNumber] + ";");
            this.colorNumber++;
        }
        lineChartWaves.setLegendVisible(false);
        lineChartWaves.setAnimated(true);

        xAxisWavesLine.setLowerBound(1);
        xAxisWavesLine.setUpperBound(31);
        xAxisWavesLine.setAutoRanging(false);
    }

    public void launchAnalysisView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/fw/views/analysisView.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/fw/views/sensorsView.fxml"));
            loader.setController(new SensorsController(fw,queue2));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lgp5/fw/views/calibrationView.fxml"));
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
