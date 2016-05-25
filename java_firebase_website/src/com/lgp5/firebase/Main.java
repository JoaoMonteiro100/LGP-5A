package com.lgp5.firebase;


import java.util.HashMap;
import com.firebase.client.Firebase;
import com.lgp5.api.neurosky.Neurosky_FW.utils.Constants;
import com.lgp5.api.neurosky.Neurosky_FW.interfaces.HeadSetDataInterface;


public class Main implements HeadSetDataInterface {
    public static void main(String[] args) {
        Firebase ref = new Firebase("https://brainlight.firebaseio.com/leituras");
    }

    @java.lang.Override
    public void onReceiveData(HashMap<String, HashMap<String, Object>> hashMap) {
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
    }
}
