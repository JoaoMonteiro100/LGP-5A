import Iedk.EmotivDevice;
import Iedk.Wave;
import Iedk.interfaces.EmotivInterface;

import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        EmotivInterface emotivInterface = new EmotivInterface() {
            @Override
            public void onReceiveData(HashMap<String, Object> data) {
                HashMap<String, Object> s = (HashMap<String, Object>) data.get("FacialExpressions");
                System.out.println(s.toString());
            }

            @Override
            public void onReceiveWavesData(HashMap<String, Wave> data) {
                for (Map.Entry<String, Wave> entry : data.entrySet()) {
                    Wave wave = entry.getValue();
                    ///System.out.println(entry.getKey()+ " : " + wave.getFreqVals().get(3));
                }
            }
        };


        EmotivDevice emotivDevice = new EmotivDevice(emotivInterface);
        emotivDevice.run();
    }
}
