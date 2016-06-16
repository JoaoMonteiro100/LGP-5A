package com.lgp5.patient.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class AnalysisController {
    @FXML private Button analysis;
    @FXML private Button messages;
    @FXML private Button settings;
    @FXML private Button game;
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

    public AnalysisController() {
    }

    public void handle(MouseEvent Event) {
        settings.setCursor(Cursor.HAND);
        messages.setCursor(Cursor.HAND);
        game.setCursor(Cursor.HAND);
    }

    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        settings.setOnAction(new EventHandler<ActionEvent>() {
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
            if (colorNumber >= constants.Constants.colors.length)
                colorNumber = 0;
            series2.getData().get(i).getNode().setStyle("-fx-bar-fill: " + constants.Constants.colors[colorNumber] + ";-fx-cursor: hand;-fx-border-color: #000000; -fx-border-width: 2;	");
            colorNumber++;
        }
        barChartMoods.setLegendVisible(false);

        colorNumber = 0;
        createSeriesLineChartMoods(series2);
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
        for(XYChart.Series<Number,Number> series : lineChartMoods.getData()){
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
}
