package BrainLightFW;
import Emotiv.Emotiv_SDK.*;

import java.util.*;

/**
 * Created by cenas on 23/04/16.
 */
public class BrainLightFW{

    private EmotivDevice emoDevice;
    private int wirelessSignal;
    private LinkedList<HashMap<String, HashMap<String, Object>>> sharedQ;
    private LinkedList<HashMap<String, HashMap<String, Object>>> localQ;
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
        //outros devices
        /*
        else if(device == 2){
        }
         */

    }

    //return true if all sensors are ok
    public boolean getAllSensorsStatusOK(HashMap<String,HashMap<String,Object>> Obj){

        if (deviceNo == 1){

            Iterator iterator = Obj.get("ChannelQuality").entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry map = (Map.Entry) iterator.next();

                if((map.getKey().equals("CMSChanQuality") && !map.getValue().equals(4))
                        || (map.getKey().equals("DRLChanQuality") && !map.getValue().equals(4))){
                    return false;
                }else if((Integer)map.getValue() < 2){
                    return false;
                }
            }
            return true;
        }

return true;
    }

    public void receiveDeviceData(){

        Thread receiveDataThread = new Thread("ReceiveDeviceData"){
            public void run() {

                if (deviceNo == 1) {
                    Thread emotivThread = new Thread(emoDevice);
                    emoDevice.connectEmotiv();
                    emotivThread.start();

                    while (running) {
                        if (deviceNo == 1) {
                            getDeviceData();
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

    }


    public synchronized void getDeviceData() {
        synchronized (sharedQ) {

                try {
                    sharedQ.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            if(getAllSensorsStatusOK((HashMap<String, HashMap<String, Object>>) sharedQ.getFirst())) {
                localQ.addLast(sharedQ.pop());
                System.out.println(localQ.getLast().get("DeviceInfo"));
            }
            else sharedQ.pop();

        }
    }

    public void stopReceiving(){

        running = false;
    }

    public static void main(String[] args) {

        BrainLightFW fw = new BrainLightFW(1);

        fw.receiveDeviceData();

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
