package Iedk.interfaces;


import Iedk.Wave;

import java.util.HashMap;

public interface EmotivInterface {
    void onReceiveData(HashMap<String, Object> data);
    void onReceiveWavesData(HashMap<String, Wave> data);
}
