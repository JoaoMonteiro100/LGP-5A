package BrainLightFW;
import Emotiv.Emotiv_SDK.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by cenas on 23/04/16.
 */
public class BrainLightFW implements Runnable{

    private EmotivDevice emoDevice;
    private int wirelessSignal;
    private Queue sharedQ;
    private boolean running;
    private int deviceNo;
    private ArrayList receivedData;


    public BrainLightFW(int device){

        wirelessSignal=0;
        sharedQ = new LinkedList<>();
        running=true;
        deviceNo = device;

        if(device == 1){
            emoDevice = new EmotivDevice();
            emoDevice.setQueue(sharedQ);
        }

    }


    public synchronized void getEmotivValues() {
        synchronized (sharedQ) {

                try {
                    sharedQ.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            System.out.println(sharedQ.iterator().next());
            System.out.println(sharedQ.size());
        }
    }

    @Override
    public void run() {


                Thread emotivThread = new Thread(emoDevice);
                emoDevice.connectEmotiv();
                emotivThread.start();


        while (running) {
            if(deviceNo==1) {
                getEmotivValues();
            }
        }

        emoDevice.emotivDeviceDisconnect();


    }

    public static void main(String[] args) {

        BrainLightFW fw = new BrainLightFW(1);

        Thread t1 = new Thread(fw);

        t1.start();

    }
}
