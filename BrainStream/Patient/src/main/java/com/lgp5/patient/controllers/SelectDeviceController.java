package com.lgp5.patient.controllers;

import com.firebase.client.*;
import com.lgp5.patient.utils.UserData;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SelectDeviceController {
    @FXML
    private ComboBox selectDeviceComboBox;
    @FXML
    private Button selectDeviceStartButton;



    public SelectDeviceController() {
    }

    public void handle(MouseEvent Event) {
        selectDeviceComboBox.setCursor(Cursor.HAND);
        selectDeviceStartButton.setCursor(Cursor.HAND);
    }


    private void extractUserID() {
        Firebase appRef = new Firebase("https://brainlight.firebaseio.com/users");
        Query queryRef = appRef.orderByChild("mail").equalTo(UserData.EMAIL);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserData.KEY = dataSnapshot.getKey();
                System.out.println(UserData.KEY);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }


    private void createNewInfoReading(String device) {
        Firebase appRef = new Firebase("https://brainlight.firebaseio.com/leiturasinfo");
        Calendar cal = Calendar.getInstance();


        Map<String, Object> values = new HashMap<>();
        values.put("Device", device.equals("NeuroSky Mindset") ? "neurosky" : "emotiv");
        values.put("Important", false);
        values.put("Live", false);
        values.put("Patient", UserData.KEY);
        values.put("Note", "");
        values.put("Leitura", "");

        String time = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH) + " - " +
                cal.get(Calendar.HOUR_OF_DAY) + "." + cal.get(Calendar.MINUTE) + "." + cal.get(Calendar.SECOND);

        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd - HH.mm.ss");
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long epoch = date.getTime();
        
        values.put("Time", time);
        values.put("Time2",epoch);

        Firebase newAppRef = appRef.push();
        newAppRef.setValue(values);
        System.out.println(newAppRef.getKey());
        UserData.READING_LAST_KEY = newAppRef.getKey();

    }


    // Called after the FXML has been initialized
    @FXML
    private void initialize() {
        selectDeviceComboBox.getItems().addAll("NeuroSky Mindset", "Emotiv Epoc");
        // The first thing to do is to extract the user ID from firebase
        extractUserID();



        selectDeviceStartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object comboBox = selectDeviceComboBox.getSelectionModel().getSelectedItem();
                if(comboBox != null) {
                    String comboBoxValue = comboBox.toString();
                    Stage stage;
                    switch (comboBoxValue) {
                        case "NeuroSky Mindset":
                            if(!UserData.KEY.isEmpty()) {
                                createNewInfoReading(comboBoxValue);
                                launchNeuroSkyView();
                                //get a handle to the stage
                                stage = (Stage) selectDeviceComboBox.getScene().getWindow();
                                //close current window
                                stage.close();
                            }
                            break;
                        case "Emotiv Epoc":
                            createNewInfoReading(comboBoxValue);
                            //launchEmotivView();
                            //get a handle to the stage
                            stage = (Stage) selectDeviceComboBox.getScene().getWindow();
                            //close current window
                            stage.close();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }


    private void launchNeuroSkyView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/analysisView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainLight");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void launchEmotivView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/menuEmotivView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainLight");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
