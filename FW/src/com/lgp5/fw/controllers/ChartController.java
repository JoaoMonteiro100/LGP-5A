package com.lgp5.fw.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

import java.util.Arrays;



public class ChartController {
    @FXML
    private BarChart<String, Float> barChart;
    @FXML
    private CategoryAxis xAxis;
    private ObservableList<String> brainwaves = FXCollections.observableArrayList();


    /**
     * Initializes the controller class and sets x axis of the bar chart with the appropriate values
     */
    @FXML
    private void initialize() {
        String[] waves = {"Delta", "Theta", "Alfa 1", "Alfa 2", "Beta 1", "Beta 2", "Gamma 1", "Gamma 2"};
        brainwaves.addAll(Arrays.asList(waves));
        xAxis.setCategories(brainwaves);

        float[] values = {35f, 43f, 27f, 12f, 9.2f, 32f, 16f, 20f};
        XYChart.Series<String,Float> series = createWaveDataSeries(values);
        barChart.getData().add(series);
    }

    /**
     * Creates a XYChart.Data object for each wave. All data is then returned as a series.
     *
     * @param values Array with a value for each brainwave. Must be the same length as the waves array
     * @return Series of brainwave data
     */
    private XYChart.Series<String,Float> createWaveDataSeries (float[] values) {

        XYChart.Series<String,Float> series = new XYChart.Series<>();

        for (int i = 0; i < values.length; i++) {
            XYChart.Data<String,Float> waveData = new XYChart.Data<>(brainwaves.get(i), values[i]);
            series.getData().add(waveData);
        }

        return series;
    }
}