package com.lgp5.fw.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;


public class SensorsController {
    @FXML private ComboBox<String> sensorsList;
    @FXML private Button applySensors;
    @FXML private CheckBox historyCheckBox;
    @FXML private Circle sensorAF3;
    @FXML private Circle sensorAF4;
    @FXML private Circle sensorF3;
    @FXML private Circle sensorF4;
    @FXML private Circle sensorF7;
    @FXML private Circle sensorF8;
    @FXML private Circle sensorFC5;
    @FXML private Circle sensorFC6;
    @FXML private Circle sensorT7;
    @FXML private Circle sensorT8;
    @FXML private Circle sensorCMS;
    @FXML private Circle sensorDRL;
    @FXML private Circle sensorP7;
    @FXML private Circle sensorP8;
    @FXML private Circle sensorO1;
    @FXML private Circle sensorO2;
    @FXML private CheckBox lobeLegendCheckBox;
    @FXML private Pane lobeLegend;
    @FXML private ImageView brain;
    private Image blankBrain = new Image("/com/lgp5/fw/assets/brain-blank.png");
    private Image lobesBrain = new Image("/com/lgp5/fw/assets/brain-lobes.png");

    public SensorsController() {
    }

    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        sensorsList.getItems().addAll("Frontal lobe", "Parietal lobe", "Temporal lobe", "Occipital lobe", "Mean of all lobes (default)");
        setSensorColor(this.sensorAF3, 0);
        setSensorColor(this.sensorF7, 1);
        setSensorColor(this.sensorCMS, 2);
        setSensorColor(this.sensorO2, 3);
        setSensorColor(this.sensorP7, 4);

        //enable button only when a choice is made
        sensorsList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                applySensors.setDisable(false);
            }
        });

        /**
         * tooltips for each sensor
         */
        Tooltip.install(sensorAF3, new Tooltip("Sensor AF3"));
        Tooltip.install(sensorAF4, new Tooltip("Sensor AF4"));
        Tooltip.install(sensorF3, new Tooltip("Sensor F3"));
        Tooltip.install(sensorF4, new Tooltip("Sensor F4"));
        Tooltip.install(sensorF7, new Tooltip("Sensor F7"));
        Tooltip.install(sensorF8, new Tooltip("Sensor F8"));
        Tooltip.install(sensorFC5, new Tooltip("Sensor FC5"));
        Tooltip.install(sensorFC6, new Tooltip("Sensor FC6"));
        Tooltip.install(sensorT7, new Tooltip("Sensor T7"));
        Tooltip.install(sensorT8, new Tooltip("Sensor T8"));
        Tooltip.install(sensorCMS, new Tooltip("Sensor CMS"));
        Tooltip.install(sensorDRL, new Tooltip("Sensor DRL"));
        Tooltip.install(sensorP7, new Tooltip("Sensor P7"));
        Tooltip.install(sensorP8, new Tooltip("Sensor P8"));
        Tooltip.install(sensorO1, new Tooltip("Sensor O1"));
        Tooltip.install(sensorO2, new Tooltip("Sensor O2"));
    }

    /**
     * Changes the colors of the sensors according to the strength of their signal
     * @param sensor what to change the color of
     * @param value strength of the signal, which will determine the color
     */
    private void setSensorColor(Circle sensor, int value) {
        switch(value) {
            //black
            case 0:
                sensor.setStyle("-fx-fill:#000000;");
                break;
            //red
            case 1:
                sensor.setStyle("-fx-fill:#C3260C;");
                break;
            //orange
            case 2:
                sensor.setStyle("-fx-fill:#FF8021;");
                break;
            //yellow
            case 3:
                sensor.setStyle("-fx-fill:#FFFF00;");
                break;
            //green
            case 4:
                sensor.setStyle("-fx-fill:#81D31A;");
                break;
            default:
                break;
        }
    }

    /**
     * Shows or hides the legend of the brain lobes, also making them appear in the image shown
     */
    @FXML
    private void setLegend() {
        lobeLegend.setVisible(!lobeLegend.isVisible());

        if(lobeLegend.isVisible()) {
            brain.setImage(lobesBrain);
        }
        else {
            brain.setImage(blankBrain);
        }
    }
}
