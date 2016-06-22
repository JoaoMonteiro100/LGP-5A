package com.lgp5.patient.controllers;


<<<<<<< HEAD
import com.firebase.client.Firebase;
import com.lgp5.Neurosky;
import com.lgp5.patient.utils.UserData;
import interfaces.HeadSetDataInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
=======
import Iedk.EmotivDevice;
import Iedk.Wave;
import Iedk.interfaces.EmotivInterface;
import com.firebase.client.Firebase;
import com.lgp5.Neurosky;
import com.lgp5.patient.utils.UserData;
import com.lgp5.patient.utils.Constants;
import interfaces.HeadSetDataInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
<<<<<<< HEAD
import module.MainModule;
import utils.Constants;
=======
import javafx.stage.WindowEvent;
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
<<<<<<< HEAD
import java.util.concurrent.LinkedBlockingDeque;


public class AnalysisController {
    @FXML private Button analysis;
    @FXML private Button messages;
    @FXML private Button settings;
    @FXML private Button game;
    private updateInterface updateInterface;
    @FXML private Button analyseSignalsButton;
    BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(1);
    BlockingQueue queue2 = new ArrayBlockingQueue<Double[][]>(1);
    private Double[][] finalRawDataArray = new Double[100][100];
    private int colorNumber=0;
    @FXML private BarChart<String, Float> barChartMoods;
    @FXML private CategoryAxis xAxisMood;
    @FXML private NumberAxis xAxisMoodsLine;
    private ObservableList<String> moods = FXCollections.observableArrayList();
    @FXML private LineChart<Number, Number> lineChartMoods;
    Vector<ArrayList> wavesGroup = new Vector<ArrayList>(2);
    Vector<ArrayList> rawGroup = new Vector<ArrayList>(2);
    Vector<ArrayList> moodsGroup = new Vector<ArrayList>(2);
    ArrayList<String> attentionQueue =  new ArrayList<String>();
    ArrayList<String> meditationQueue =  new ArrayList<String>();
    ArrayList<Number> queueTime = new ArrayList<Number>();
    private HeadSetDataInterface headSetDataInterface;
    private Firebase appRef;


    public AnalysisController() {
        final long start = System.currentTimeMillis();
        appRef = new Firebase("https://brainlight.firebaseio.com/leituras").push();
        System.out.println(appRef.getKey());

        Firebase readingRef = new Firebase("https://brainlight.firebaseio.com/leiturasinfo/" + UserData.READING_LAST_KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("Live", true);
        values.put("Leitura", appRef.getKey());
        readingRef.updateChildren(values);
/*
        BlockingQueue<Double[][]> queue = null;
        BlockingQueue<Double[][]> queue2 = null;

        MainModule fw = new MainModule(2,queue,queue2,false,120);
        fw.receiveDeviceData();

        updateInterface = new updateInterface() {
            @Override
            public void update(Double[][] finalDataArray) {
                Thread cenas = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("vida loka");
                    }

                });
            }

            @Override
            public void update2(Double[][] finalDataArray) {

            }
        };
        ThreadInterface t = new ThreadInterface(queue,queue2,updateInterface,2);
        new Thread(t).start();
*/

        Firebase newAppRef = new Firebase("https://brainlight.firebaseio.com/leituras/" + appRef.getKey());




        headSetDataInterface = new HeadSetDataInterface() {
            @Override
            public void onReceiveData(HashMap<String, HashMap<String, Object>> hashMap) {
                double end = System.currentTimeMillis();
                double diff = end - start;
                HashMap<String, Object> data = hashMap.get(Constants.WAVES);
                data.put("elapsedTime", diff);
                newAppRef.push().setValue(data);
            }

            @Override
            public void onReceiveRawData(HashMap<String, Integer> hashMap) {

            }
        };

        new Thread(new Neurosky("0013EF004809", headSetDataInterface)).start();
=======


public class AnalysisController {
    @FXML
    private Button analysis;
    @FXML
    private Button messages;
    @FXML
    private Button settings;
    @FXML
    private Button game;
    @FXML
    private Button analyseSignalsButton;
    @FXML
    private BarChart<String, Float> barChartMoods;
    @FXML
    private CategoryAxis xAxisMood;
    @FXML
    private NumberAxis xAxisMoodsLine;
    @FXML
    private LineChart<Number, Number> lineChartMoods;
    BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(1);
    BlockingQueue queue2 = new ArrayBlockingQueue<Double[][]>(1);
    private Double[][] finalRawDataArray = new Double[100][100];
    private int colorNumber = 0;
    private ObservableList<String> moods = FXCollections.observableArrayList();
    Vector<ArrayList> wavesGroup = new Vector<ArrayList>(2);
    Vector<ArrayList> rawGroup = new Vector<ArrayList>(2);
    Vector<ArrayList> moodsGroup = new Vector<ArrayList>(2);
    ArrayList<String> attentionQueue = new ArrayList<String>();
    ArrayList<String> meditationQueue = new ArrayList<String>();
    ArrayList<Number> queueTime = new ArrayList<Number>();
    private HeadSetDataInterface headSetDataInterface;
    @FXML private Stage stage;


    public AnalysisController() {
    }


    private void startRecordingData(Firebase appRef, Firebase readingRef) {
        if (UserData.DEVICE.equals(Constants.DeviceConstants.EMOTIV)) {
            Thread emotivThread = null;
            EmotivInterface emotivInterface = new EmotivInterface() {
                @Override
                public void onReceiveData(HashMap<String, Object> data) {

                }

                @Override
                public void onReceiveWavesData(HashMap<String, Wave> data) {
                    try {
                        appRef.push().setValue(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };


            EmotivDevice emotivDevice = new EmotivDevice(emotivInterface);
            emotivThread = new Thread(emotivDevice);
            emotivThread.setDaemon(true);
            emotivThread.start();

            // K3agb_
            Thread finalEmotivThread = emotivThread;
            stage.setOnCloseRequest(event -> {
                // We're closing the window, let's set the Live field of the current reading to false
                Map<String, Object> vals = new HashMap<>();
                vals.put("Live", false);
                readingRef.updateChildren(vals);
                finalEmotivThread.interrupt();
            });
        } else {
            Thread neuroskyThread = null;
            // We are using Neurosky
            HeadSetDataInterface headSetDataInterface = new HeadSetDataInterface() {
                @Override
                public void onReceiveData(HashMap<String, HashMap<String, Object>> hashMap) {
                    appRef.push().setValue(hashMap);
                }

                @Override
                public void onReceiveRawData(HashMap<String, Integer> hashMap) {

                }
            };

            Neurosky neurosky = new Neurosky("0013EF004809", headSetDataInterface);
            neuroskyThread = new Thread(neurosky);
            neuroskyThread.setDaemon(true);
            neuroskyThread.start();


            Thread finalNeuroskyThread = neuroskyThread;
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    // We're closing the window, let's set the Live field of the current reading to false
                    Map<String, Object> vals = new HashMap<>();
                    vals.put("Live", false);
                    readingRef.updateChildren(vals);
                    finalNeuroskyThread.interrupt();
                }
            });
        }
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
    }


    public void handle(MouseEvent Event) {
        settings.setCursor(Cursor.HAND);
        messages.setCursor(Cursor.HAND);
        game.setCursor(Cursor.HAND);
    }

<<<<<<< HEAD
    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        settings.setOnAction(new EventHandler<ActionEvent>() {
=======

    private void createNewFirebaseRecord() {
        Firebase appRef = new Firebase("https://brainlight.firebaseio.com/leituras").push();
        Firebase readingRef = new Firebase("https://brainlight.firebaseio.com/leiturasinfo/" + UserData.READING_LAST_KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("Live", true);
        values.put("Patient", UserData.KEY);
        values.put("Leitura", appRef.getKey());
        readingRef.updateChildren(values);


        startRecordingData(appRef, readingRef);
    }


    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        /**settings.setOnAction(new EventHandler<ActionEvent>() {
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
            @Override
            public void handle(ActionEvent event) {
                Stage stage;
                launchSettingsView();
                //get a handle to the stage
                stage = (Stage) settings.getScene().getWindow();
                //close current window
                stage.close();
            }
        });

        game.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage;
                launchGameView();
                //get a handle to the stage
                stage = (Stage) game.getScene().getWindow();
                //close current window
                //stage.close();
            }
        });

<<<<<<< HEAD
=======

        createNewFirebaseRecord();*/


>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
        /** barChart things **/
        String[] moodsArray = {"Attention", "Meditation"};
        moods.addAll(Arrays.asList(moodsArray));
        xAxisMood.setCategories(moods);
        XYChart.Series<String, Float> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data("Attention", 35f));
        series2.getData().add(new XYChart.Data("Meditation", 35f));
        XYChart.Series<String, Float> series3 = new XYChart.Series<>();

        barChartMoods.getData().add(series2);
        for (int i = 0; i < series2.getData().size(); i++) {
<<<<<<< HEAD
            if (colorNumber >= constants.Constants.colors.length)
                colorNumber = 0;
            series2.getData().get(i).getNode().setStyle("-fx-bar-fill: " + constants.Constants.colors[colorNumber] + ";-fx-cursor: hand;-fx-border-color: #000000; -fx-border-width: 2;	");
=======
            if (colorNumber >= Constants.colors.length)
                colorNumber = 0;
            series2.getData().get(i).getNode().setStyle("-fx-bar-fill: " + Constants.colors[colorNumber] + ";-fx-cursor: hand;-fx-border-color: #000000; -fx-border-width: 2;	");
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
            colorNumber++;
        }
        barChartMoods.setLegendVisible(false);

        colorNumber = 0;
        createSeriesLineChartMoods(series2);
    }

<<<<<<< HEAD
    private void createSeriesLineChartMoods(XYChart.Series<String,Float> seriesBarChart){
=======
    private void createSeriesLineChartMoods(XYChart.Series<String, Float> seriesBarChart) {
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
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
<<<<<<< HEAD
        lineChartMoods.getData().addAll(series1,series2);
=======
        lineChartMoods.getData().addAll(series1, series2);
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
        for (int i = 0; i < seriesBarChart.getData().size(); i++) {
            final int tmp = i;
            final int tmp2 = colorNumber;
            seriesBarChart.getData().get(i).getNode().setOnMouseClicked(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
<<<<<<< HEAD
                    if(lineChartMoods.getData().get(tmp).nodeProperty().get().isVisible())
                    {
=======
                    if (lineChartMoods.getData().get(tmp).nodeProperty().get().isVisible()) {
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
                        lineChartMoods.getData().get(tmp).nodeProperty().get().setVisible(false);
                        Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + tmp);
                        for (Node n : lookupAll) {
                            n.setVisible(false);
                        }
<<<<<<< HEAD
                        seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[tmp2]+";-fx-cursor: hand;");
                    }
                    else
                    {
=======
                        seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: " + Constants.colors[tmp2] + ";-fx-cursor: hand;");
                    } else {
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
                        Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + tmp);
                        for (Node n : lookupAll) {
                            n.setVisible(true);
                        }
<<<<<<< HEAD
                        seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: "+constants.Constants.colors[tmp2]+";-fx-cursor: hand; -fx-border-color: #000000; -fx-border-width: 2;");
=======
                        seriesBarChart.getData().get(tmp).getNode().setStyle("-fx-bar-fill: " + Constants.colors[tmp2] + ";-fx-cursor: hand; -fx-border-color: #000000; -fx-border-width: 2;");
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
                        lineChartMoods.getData().get(tmp).nodeProperty().get().setVisible(true);
                    }
                }
            });
            colorNumber++;
        }
<<<<<<< HEAD
        this.colorNumber=0;
        for(XYChart.Series<Number,Number> series : lineChartMoods.getData()){
            if(this.colorNumber>=constants.Constants.colors.length)
                this.colorNumber=0;
            Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + this.colorNumber);
            for (Node n : lookupAll) {
                n.setStyle("-fx-background-color:"+constants.Constants.colors[this.colorNumber]+";");
            }
            series.nodeProperty().get().setStyle("-fx-stroke: " +constants.Constants.colors[this.colorNumber]+";");
=======
        this.colorNumber = 0;
        for (XYChart.Series<Number, Number> series : lineChartMoods.getData()) {
            if (this.colorNumber >= Constants.colors.length)
                this.colorNumber = 0;
            Set<Node> lookupAll = lineChartMoods.lookupAll(".chart-line-symbol.series" + this.colorNumber);
            for (Node n : lookupAll) {
                n.setStyle("-fx-background-color:" + Constants.colors[this.colorNumber] + ";");
            }
            series.nodeProperty().get().setStyle("-fx-stroke: " + Constants.colors[this.colorNumber] + ";");
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
            this.colorNumber++;
        }
        lineChartMoods.setLegendVisible(false);
        lineChartMoods.setAnimated(false);

        xAxisMoodsLine.setLowerBound(0);
        xAxisMoodsLine.setUpperBound(50);
        xAxisMoodsLine.setAutoRanging(false);
    }

<<<<<<< HEAD
    private void updateSeriesLineChartMoods(String a, String m)
    {
=======
    private void updateSeriesLineChartMoods(String a, String m) {
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
        moodsGroup.get(0).add(a);
        moodsGroup.get(1).add(m);

        for (int i = 0; i < moodsGroup.size(); i++) {
            moodsGroup.get(i).remove(0);
        }
        xAxisMoodsLine.setLowerBound(Double.parseDouble(queueTime.get(0).toString()));
        xAxisMoodsLine.setUpperBound(Double.parseDouble(queueTime.get(9).toString()));
    }

    private void launchSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/settingsView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Settings");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void launchGameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/gameView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Game");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void launchAnalysisWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/chooseAnalysisView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Choose analysis");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
}
=======

    public void createRecordAfterStart() {
        this.stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                createNewFirebaseRecord();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
