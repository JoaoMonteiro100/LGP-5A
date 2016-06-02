package BrainLightFW;
import Emotiv.Emotiv_SDK.*;

import interfaces.HeadSetDataInterface;
import j2me.com.NeuroSky.ThinkGear.IO.*;
import j2me.com.NeuroSky.ThinkGear.Util.HeadsetData;
import com.lgp5.*;
import java.util.*;

/**
 * Created by cenas on 23/04/16.
 */
public class BrainLightFW{

    private EmotivDevice emoDevice;
    private Neurosky neuroDevice;
    private int wirelessSignal;
    private LinkedList sharedQ;
    private LinkedList localQ;
    private boolean running;
    private int deviceNo;


    public BrainLightFW(int device){
        sharedQ = new LinkedList<>();
        wirelessSignal=0;
        localQ = new LinkedList<>();
        running=true;
        deviceNo = device;

        if(device == 1){
            emoDevice = new EmotivDevice();
            emoDevice.setQueue(sharedQ);
        }
    }
        //outros devices
        
        
       
       
       /* else if(device == 2){
      // HeadSetDataInterface sendDataInterface = new HeadSetDataInterface() {
			@Override
		//	public void onReceiveData(HeadsetData headsetData) {
			//}
		};
		
		neuroDevice = new Neurosky("0013EF004809", sendDataInterface);
        }
         

    }*/

    public void reciveDeviceData(){

        Thread receiveDataThread = new Thread("ReceiveDeviceData"){
            public void run() {

                if (deviceNo == 1) {
                    Thread emotivThread = new Thread(emoDevice);
                    emoDevice.connectEmotiv();
                    emotivThread.start();

                    while (running) {
                        if (deviceNo == 1) {
                            getEmotivValues();
                        }
                    }
                }
            }
        };

    receiveDataThread.start();

        //emoDevice.emotivDeviceDisconnect();

    }

    public void deviceDisconnect(){

        if(deviceNo == 1){
            emoDevice.emotivDeviceDisconnect();
        }
        else if (deviceNo == 2)
        {
        	
        }

    }


    public synchronized void getEmotivValues() {
        synchronized (sharedQ) {

                try {
                    sharedQ.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }



            localQ.addLast(sharedQ.pop());
            
            System.out.println(localQ);
        }
    }

    public void stopReceiving(){

        running = false;
    }

    public static void convertVolts(double value)
    {
    	value = ((value*(1.8/4096)/2000));
    }
    
    public static void main(String[] args) {

        BrainLightFW fw = new BrainLightFW(1);

        fw.reciveDeviceData();

        Scanner scanner = new Scanner(System.in);
        String readString = scanner.nextLine();
        while(readString!=null) {
            System.out.println(readString);

            if (readString.isEmpty()) {
                System.out.println("Read Enter Key.");
                fw.stopReceiving();
                break;
            }

            if (scanner.hasNextLine()) {
    fw.deviceDisconnect();
            } else {
                readString = null;
            }
        }

    }
}
