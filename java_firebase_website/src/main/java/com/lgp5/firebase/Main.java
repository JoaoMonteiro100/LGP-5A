package com.lgp5.firebase;

import java.util.HashMap;

import com.firebase.client.Firebase;
import com.lgp5.api.neurosky.Neurosky_FW.Neurosky;
import com.lgp5.api.neurosky.Neurosky_FW.utils.Constants;
import com.lgp5.api.neurosky.Neurosky_FW.interfaces.HeadSetDataInterface;


public class Main {
    public static void main(String[] args) {
      /*  Thread t2 = new Thread(new Runnable() {
            public void run() {
                HashMap cenas = new HashMap();
                java.util.Date date= new java.util.Date();
                cenas.put("Leitura","-KIoFDfjh79UaguOX54E");
                cenas.put("Patient","-KIoFDfjh79UaguOXLA8");
                cenas.put("Live",false); //mudar para true
                cenas.put("Important",false);
                cenas.put("Note","");
                cenas.put("time",  new SimpleDateFormat("yyyy.MM.dd - HH.mm.ss").format(new Date()));

                for (int i = 0; i < 1; i++) {
                    Firebase ref = new Firebase("https://brainlight.firebaseio.com/leiturasinfo");
                    ref.push().setValue(cenas);
                }
            }
        });
        t2.start();*/
        Thread t1 = new Thread(new Runnable() {
            public void run()
            {
              final long tStart = System.currentTimeMillis();
                final Firebase ref = new Firebase("https://brainlight.firebaseio.com/leituras");
                HeadSetDataInterface headSetDataInterface = new HeadSetDataInterface() {
                    @Override
                    public void onReceiveData(HashMap<String, HashMap<String, Object>> hashMap) {
                        HashMap<String, Object> values = hashMap.get(Constants.WAVES);
                        long tEnd = System.currentTimeMillis();
                        long tDelta = tEnd - tStart;
                        double elapsedSeconds = tDelta / 1000.0;
                        System.out.println("oi");
                        values.put("elapsedTime",elapsedSeconds);
                        ref.child("-KIoFDfjh79UaguOX54E").push().setValue(values);
                    }
                };
                new Thread(new Neurosky("0013EF004809", headSetDataInterface)).start();
            }});
        t1.start();
    }
}