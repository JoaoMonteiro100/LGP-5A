package BrainLightFW;
import Emotiv.Emotiv_SDK.*;

import interfaces.HeadSetDataInterface;
import j2me.com.NeuroSky.ThinkGear.IO.*;
import j2me.com.NeuroSky.ThinkGear.Util.HeadsetData;
import com.lgp5.*;
import java.util.*;
import BrainLightFW.AttributeMerger;

/**
 * Created by cenas on 23/04/16.
 */
public class BrainLightFW{
	private Double[][] finalDataArray;
	private HashMap<String,HashMap<String,Object>> finalDataHash;
	private HashMap<String,HashMap<String,Object>> emotivData;
    private EmotivDevice emoDevice;
    private Neurosky neuroDevice;
    private int wirelessSignal;
    private LinkedList sharedQ;
    private LinkedList localQ;
    private boolean running;
    private int deviceNo;

//done untested
    public BrainLightFW(int device){
        sharedQ = new LinkedList<>();
        wirelessSignal=0;
        localQ = new LinkedList<>();
        running=true;
        deviceNo = device;
        
        //device 1 = Emotiv
        
        if(device == 1){
            emoDevice = new EmotivDevice();
            emoDevice.setQueue(sharedQ);
        }
       
        //device 2 = Neurosky
        
        else if(device == 2){
        HeadSetDataInterface sendDataInterface;
    		
    		sendDataInterface = new HeadSetDataInterface(){
			
    			@Override
    			public void onReceiveData(HashMap<String, HashMap<String, Object>> dataToSend) {
    				System.err.println(dataToSend.toString());
    			}
    		};
      
		neuroDevice = new Neurosky("0013EF004809", sendDataInterface);        

        }  
    }

    
    //TODO
    public void receiveDeviceData(){

        Thread receiveDataThread = new Thread("ReceiveDeviceData"){
            public void run() {

                if (deviceNo == 1) {
                    Thread emotivThread = new Thread(emoDevice);
                    emoDevice.connectEmotiv();
                    emotivThread.start();

                    while (running) {
                        if (deviceNo == 1) {
                            getValues(1);
                        }
                    }
                }
                else if (deviceNo == 2) {
                	
                	
                	
                	while (running){
                		if (deviceNo == 2) {
                			neuroDevice.getFinalData();
                		}
                	}
                }
            }
        };

    receiveDataThread.start();

        //emoDevice.emotivDeviceDisconnect();

    }

    
    
    //done, untested
    public void deviceDisconnect(){

        if(deviceNo == 1){
            emoDevice.emotivDeviceDisconnect();
        }
        else if (deviceNo == 2)
        {
        	neuroDevice.stopReceivingData();
        	neuroDevice.disconnect();
        }

    }

    //TODO
    public void getValues(int device) {
    	
    	if (deviceNo == 1)
    		{
    		emotivData = emoDevice.getDataToSend();
    		initMerge(device, emotivData, finalDataArray);
    		}	
    	
    	else if (deviceNo == 2)
    		{	
    		finalDataHash = null;
    		initMerge(device, finalDataHash, finalDataArray);
    		}
    }
    

    /*public synchronized void getEmotivValues() {
        synchronized (sharedQ) {

                try {
                    sharedQ.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }



            localQ.addLast(sharedQ.pop());
            
            System.out.println(localQ);
        }
    	
    	
    }*/

    
    public void stopReceiving(){
        running = false;
    }
//done
    public static double convertVolts(double value)
    {
    	value = (value*(1.8/4096))/2000;    
    	return value;
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
    
    //done
    public void initMerge (int device, HashMap<String, HashMap<String,Object>> data, Double[][] finalData)
	{
		String[][] finalInfo;
		
		if (device == 1) {
			HashMap<String, HashMap<String, Object>> emotivData;
			if (data instanceof HashMap) {
				emotivData= (HashMap<String, HashMap<String,Object>>) data;
			} else {
				System.out.println("Wrong type of data for emotiv device");
				return;
			}
			
			//criar array de arrays com toda a informação do dispositivo
			finalInfo = new String [][] { {"AF3","F7","F3","FC5","T7","P7","O1","O2","P8","T8","FC6","F4","F8","AF4"},
				{"Theta","Alpha","LowBeta","HighBeta","Gamma"},
				{},
				{"LeftWink", "RightWink", "Blink", "EyesOpen","SmileExtension","ClenchExtension","LowerFaceExpression",
					"LowerFaceExpressionPower","UpperFaceExpression","UperFaceEXpressionPower"},
				{"Action","ActionPower","LookingLeft","LookingRight","LookingDown","LookingUp"}
			};
			HashMap<String, Object> waves = emotivData.get("Waves");
			HashMap<String, Object> expressions = emotivData.get("FacialExprenssions");
			HashMap<String,Object> actions = emotivData.get("Actions");
			int tam = finalInfo[0].length + 3;
			finalData = new Double[tam][];
			//definir o tamanho de cada parte do array
			for (int a = 0; a < tam; a++){
			if (a == tam-2)
				finalData[a]= new Double[0];
			else if (a == tam-1)
				finalData[a] = new Double[10];
			else if (a == tam)
				finalData[a] = new Double[6];
			else 
				finalData[a] = new Double[5];
			}
			
			for (int i = 0; i < tam; i++){
				if (i == tam-2)
					break;
				else if (i == tam - 1)
					{
					for(int k = 0; k < finalData[i].length; k++)
						{
						finalData[i][k]=(Double)(expressions.get(finalData[3][k]));
						}
					}
				else if (i == tam)
					{
					for(int k = 0; k < finalData[i].length; k++)
						{
						finalData[i][k]=(Double)(expressions.get(finalData[4][k]));
						}
					}
				else 
					for(int k = 0; k < finalData[i].length; k++)
						{
						finalData[i][k]=((HashMap<String,Double>) waves.get(finalData[0][i])).get(finalData[1][k]);
						}
			
			}
			
		}
		
		else if (device == 2){

			HashMap<String, HashMap<String,Object>> neuroData;
			if (data instanceof HashMap) {
				neuroData = (HashMap<String, HashMap<String,Object>>) data;
			} else {
				System.out.println("Wrong type of data for neurosky device");
				return;
			}
			
			
			finalInfo = new String [][] { {"FP1"},
				{"Delta","Theta","LowAlpha","HighAlpha","LowBeta","HighBeta","LowGamma","HighGamma"},
				{"Attention","Meditation"},
				{"Blink"},
				{}
			};
			
			finalData = new Double[4][];
			finalData[0]= new Double[8];
			finalData[1]= new Double[2];
			finalData[2]= new Double[1];
			finalData[3] = new Double[0];
			
			for (int i = 0;i < finalData.length; i++){
				for (int k = 0;k < finalData[i].length; k++){
					if (i == 0)
						finalData[i][k]=(Double) neuroData.get("Waves").get(convertVolts(finalData[i][k]));
					else
						finalData[i][k]=(Double) neuroData.get("Waves").get(finalData[i][k]);
				}
				}
			}
			
		
			
			}
    
}
