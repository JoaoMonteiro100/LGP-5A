package BrainLightFW;
import Emotiv.Emotiv_SDK.*;

import interfaces.HeadSetDataInterface;
import j2me.com.NeuroSky.ThinkGear.IO.*;
import j2me.com.NeuroSky.ThinkGear.Util.HeadsetData;
import com.lgp5.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import Analysis.*;
import Analysis.Calculations;

/**
 * Created by cenas on 23/04/16.
 */

public class BrainLightFW{

    private Neurosky neuroDevice;
    private EmotivDevice emoDevice;
    private int wirelessSignal;
    private static Double[][] finalDataArray;
	private HashMap<String,HashMap<String,Object>> neuroData;
    private LinkedList<HashMap<String, HashMap<String, Object>>> sharedQ;
    private LinkedList<Double [][]> doubleQ;
    private boolean running;
    private int deviceNo;
    private boolean calculate; //ver se as analises estao a correr, e se sim parar de enviar informaçao toda TODO

    public BrainLightFW(int device){
        sharedQ = new LinkedList<>();
        wirelessSignal=0;
        new LinkedList<>();
        doubleQ = new LinkedList<>();
        running=true;
        deviceNo = device;
        if(device == 1){
            emoDevice = new EmotivDevice();
            emoDevice.setQueue(sharedQ);
        }

        
        else if(device == 2){
            HeadSetDataInterface sendDataInterface;
        		//confirmar
        		sendDataInterface = new HeadSetDataInterface(){
    			
        			@Override
        			public void onReceiveData(HashMap<String, HashMap<String, Object>> dataToSend) {
        				
        				System.out.println(dataToSend.toString());

        				//thread
        			}
        		};
          
    		neuroDevice = new Neurosky("0013EF004809", sendDataInterface);
            }
         

    }

    //return true if all sensors are ok - emotiv only
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
                            getDeviceData(1);
                        }
                    }
                }
                else if (deviceNo == 2) {
                	Thread neuroThread = new Thread(neuroDevice);
                	neuroDevice.connect();
                    neuroDevice.run();
                    neuroThread.start();

                    while (running) {
                        if (deviceNo == 2) {
                            getDeviceData(2);
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

    public synchronized void getDeviceData(int device) {
    	
    	if (device == 1){
        synchronized (sharedQ) {

                try {
                    sharedQ.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            if(getAllSensorsStatusOK((HashMap<String, HashMap<String, Object>>) sharedQ.getFirst())) {
              // localQ.addLast(sharedQ.pop());
                initMerge (1,(HashMap<String, HashMap<String,Object>>)sharedQ.pop());
                doubleQ.addLast(finalDataArray);
                System.out.println(doubleQ.getLast()[5]);
            }
            else sharedQ.pop();

        }
    	}
       
    	else if (device == 2){
    		initMerge(2,neuroDevice.getFinalData());
    		
    		doubleQ.addLast(finalDataArray);
    		
    	
        }
    }

    public void stopReceiving(){

        running = false;
    }

    public /*Double[] TODO*/void calculate(int[][] infoArray)
    {
    	
    	Double[] finalArray;
    	float[][] finalArray1;
    	finalArray = new Double[12];
    	int[][] waveType = new int[3][];
		Vector<Double> vector = new Vector<Double>();
		TypesOfCalculations[] types = new TypesOfCalculations[infoArray[1].length];
		
    	
    	while(calculate = true)
    	{
    		for(long stop=System.nanoTime()+TimeUnit.SECONDS.toNanos(infoArray[2][0]);stop>System.nanoTime();)
    		{
    	    	vector.add(mediaIndex(infoArray[0][0]));
    		}
    		
    		for(int i = 0; i < infoArray[1].length;i++)
    		{
    			switch (infoArray[1][i])
    			{
    			case 0 : types[i] = TypesOfCalculations.WAVELENGTH;
    			case 1 : types[i] = TypesOfCalculations.WAVENUMBER;
    			case 2 : types[i] = TypesOfCalculations.ANG_WAVENUMBER;
    			case 3 : types[i] = TypesOfCalculations.ANG_FREQUENCY;
    			case 4 : types[i] = TypesOfCalculations.PERIOD;
    			case 5 : types[i] = TypesOfCalculations.AMPLITUDE;
    			case 6 : types[i] = TypesOfCalculations.MAX_AMPLITUDE;
    			case 7 : types[i] = TypesOfCalculations.MIN;
    			case 8 : types[i] = TypesOfCalculations.MAX;
    			case 9 : types[i] = TypesOfCalculations.MEAN;
    			case 10 : types[i] = TypesOfCalculations.MODE;
    			case 11 : types[i] = TypesOfCalculations.MEDIAN;
    			default : break;
    			}
    			 
    		}
    		//a mudar no analysis TODO
    		
    		
    		
    			//Calculations Calc = new Calculations(vector.toArray(), types);
    	}
    	
    	
    	
    	
    }   
    
    
    public Double media (Double[] finalArray)
    {
    	int x = 0;
    	for (int i = 0; i < finalArray.length; i++)
    		 x += finalArray[i];
		return (double) (x/finalArray.length);
    }
    
    public Double mediaIndex (int index)
    {
    	Double[] indexArray = new Double[14];
    	
    	for (int i = 0; i < 15; i++)
    	{
    		indexArray[i] = finalDataArray[i][index]; 
    	}
    	
    	return media(indexArray);
    }
    
    public static void main(String[] args) {
////////////////////////////////////////////////////EMOTIV TEST/////////////////////////////////////////////////
    	
//    	Double[][] testeArray;
//    	testeArray = new Double[18][];
//    	testeArray[0]= new Double[5];
//    	testeArray[1]= new Double[5];
//    	testeArray[2]= new Double[5];
//    	testeArray[3] = new Double[5];
//    	testeArray[4] = new Double[5];
//    	testeArray[5] = new Double[5];
//    	testeArray[6] = new Double[5];
//    	testeArray[7] = new Double[5];
//    	testeArray[8] = new Double[5];
//    	testeArray[9] = new Double[5];
//    	testeArray[10] = new Double[5];
//    	testeArray[11] = new Double[5];
//    	testeArray[12] = new Double[5];
//    	testeArray[13] = new Double[5];
//    	testeArray[14] = new Double[5];
//    	testeArray[15] = new Double[2];
//    	testeArray[16] = new Double[2];
//    	testeArray[17] = new Double[2];
//    	testeArray[18] = new Double[2];
//    	
//    	
//    	HashMap<String, HashMap<String, Object>> testeEmo;
//    	HashMap<String, HashMap<String, Double>> testepower;
//    	HashMap<String, Object> testewaves;
//    	HashMap<String, Object> testeactions;
//    	HashMap<String, Object> testeexpressions;
//    	HashMap<String, Object> testeinfo;
//    	HashMap<String, Double> testetypes1;
//    	HashMap<String, Double> testetypes2;
//    	HashMap<String, Double> testetypes3;
//    	
//    	
//    	testeEmo = new HashMap<String, HashMap<String, Object>>();
//    	testepower = new HashMap<String, HashMap <String,Double>>();
//    	testewaves = new HashMap<String, Object>();
//    	testeactions = new HashMap<String, Object>();
//    	testeexpressions = new HashMap<String, Object>();
//    	testeinfo = new HashMap<String, Object>();
//    	testetypes1 = new HashMap<String, Double>();
//    	testetypes2 = new HashMap<String, Double>();
//    	testetypes3 = new HashMap<String, Double>();
//    	
//    	testeEmo.put("Waves", testewaves);
//    	testeEmo.put("Actions", testeactions);
//    	testeEmo.put("FacialExpressions", testeexpressions);
//    	testeEmo.put("DeviceInfo", testeinfo);
//    	
//    	testetypes1.put("Theta", 1.0);
//    	testetypes1.put("Alpha", 1.0);
//    	testetypes1.put("LowBeta", 1.0);
//    	testetypes1.put("HighBeta", 1.0);
//    	testetypes1.put("Gamma", 1.0);
//    	
//    	testetypes2.put("Theta", 2.0);
//    	testetypes2.put("Alpha", 2.0);
//    	testetypes2.put("LowBeta", 2.0);
//    	testetypes2.put("HighBeta", 2.0);
//    	testetypes2.put("Gamma", 2.0);
//    	
//    	testetypes3.put("Theta", 3.0);
//    	testetypes3.put("Alpha", 3.0);
//    	testetypes3.put("LowBeta", 3.0);
//    	testetypes3.put("HighBeta", 3.0);
//    	testetypes3.put("Gamma", 3.0);
//    	
//    	testewaves.put("AF3", testetypes1);
//        testewaves.put("F7", testetypes2);
//        testewaves.put("F3", testetypes3);
//        testewaves.put("FC5", testetypes1);
//        testewaves.put("T7", testetypes2);
//        testewaves.put("P7", testetypes3);
//        testewaves.put("O1", testetypes1);
//        testewaves.put("O2", testetypes2);
//        testewaves.put("P8", testetypes3);
//        testewaves.put("T8", testetypes1);
//        testewaves.put("FC6", testetypes2);
//        testewaves.put("F4", testetypes3);
//        testewaves.put("F8", testetypes1);
//        testewaves.put("AF4", testetypes2);
//        
//        testeactions.put("Action", "Right");
//        testeactions.put("ActionPower", 1.0);
//        testeactions.put("LookingLeft", 2.0);
//        testeactions.put("LookingRight", 1.0);
//        testeactions.put("LookingDown", 2.0);
//        testeactions.put("LookingUp", 1.0);
//        
//        testeexpressions.put("LeftWink", 45.0);
//        testeexpressions.put("RightWink", 45.0);
//        testeexpressions.put("Blink", 5.0);
//        testeexpressions.put("EyesOpen", 2.0);
//        testeexpressions.put("SmileExtension", 4.0);
//        testeexpressions.put("ClenchExtension", 45.0);
//        testeexpressions.put("LowerFaceExpression", "SmirkLeft");
//        testeexpressions.put("LowerFaceExpressionPower", 1.0);
//        testeexpressions.put("UpperFaceExpression", "Smile");
//        testeexpressions.put("UpperFaceExpressionPower", 4.0);
//        
//        testeinfo.put("BatteryLevel", 1.0);
//        testeinfo.put("WirelessSignal", 0.0);
//        
//        initMerge (1, testeEmo);
//    	
//        for (int i = 0; i < finalDataArray.length; i++) {
//            for (int j = 0; j < finalDataArray[i].length; j++) {
//                System.out.print(finalDataArray[i][j]);
//            }
//        }
        
//    	//criar array de arrays com toda a informação do dispositivo
//		finalInfo = new String [][] { {"AF3","F7","F3","FC5","T7","P7","O1","O2","P8","T8","FC6","F4","F8","AF4"},
//			{"Theta","Alpha","LowBeta","HighBeta","Gamma"},
//			{},
//			{"LeftWink", "RightWink", "Blink", "EyesOpen","SmileExtension","ClenchExtension","LowerFaceExpression",
//				"LowerFaceExpressionPower","UpperFaceExpression","UperFaceEXpressionPower"},
//			{"Action","ActionPower","LookingLeft","LookingRight","LookingDown","LookingUp"},
//			{"BatteryLevel","WirelessSignal"}
//		};
//		HashMap<String, Object> waves = emotivData.get("Waves");
//		HashMap<String, Object> expressions = emotivData.get("FacialExprenssions");
//		HashMap<String, Object> actions = emotivData.get("Actions");
//		HashMap<String, Object> deviceInfo = emotivData.get("DeviceInfo");
    	
    	
    	
    	
    	
    	
    	
    	
    	
/////////////////////////////////NEUROSKY TEST//////////////////////////////////////////////////////////
    	HashMap<String, HashMap<String, Object>> testeNeuro;
    	HashMap<String, Object> teste1;
//    	Double[][] cenas = new Double[][];
    	teste1 = new HashMap<String,Object>();
    	testeNeuro = new HashMap<String,HashMap<String,Object>>();
    	
    	teste1.put("BatteryLevel", (double) 2);
    	teste1.put("PoorSignal", (double) 4);
    	teste1.put("Attention", (double) 2);
    	teste1.put("Meditation",(double) 70);
    	teste1.put("Blink", (double) 5);
    	teste1.put("Delta", (double) 2);
    	teste1.put("Theta", (double) 2);
    	teste1.put("LowAlpha",(double)  2);
    	teste1.put("HighAlpha", (double) 2);
    	teste1.put("LowBeta",(double)  26);
    	teste1.put("HighBeta", (double) 345);
    	teste1.put("LowGamma", (double) 4);
    	teste1.put("MidGamma", (double) 2);
    	
    	 testeNeuro.put("Waves", teste1);
    			 
    	//System.out.println(asd[0][0]); 
    initMerge(2, testeNeuro);
    		
    
    	
  
        /*BrainLightFW fw = new BrainLightFW(2);

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
        }*/
    	
    	
    	 

    }
    //done
    public static void initMerge (int device, HashMap<String, HashMap<String,Object>> data)
	{
		String[][] finalInfo;
		Double [][] finalData;
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
				{"Action","ActionPower","LookingLeft","LookingRight","LookingDown","LookingUp"},
				{"BatteryLevel","WirelessSignal"}
			};
			
			
			HashMap<String, Object> waves = emotivData.get("Waves");
			HashMap<String, Object> expressions = emotivData.get("FacialExpressions");
			HashMap<String, Object> actions = emotivData.get("Actions");
			HashMap<String, Object> deviceInfo = emotivData.get("DeviceInfo");
//			HashMap<String, Object> time = emotivData.get("Time"); TODO
			
			int tam = finalInfo[0].length + 4;
			finalData = new Double[tam][];
			//definir o tamanho de cada parte do array
			for (int a = 0; a < tam; a++){
			if (a == tam-4)
				finalData[a]= new Double[0];
			else if (a == tam-3)
				finalData[a] = new Double[10];
			else if (a == tam - 2)
				finalData[a] = new Double[6];
			else if (a == tam - 1)
				finalData[a] = new Double[2];
			else 
				finalData[a] = new Double[5];
			}
			
			for (int i = 0; i < tam; i++){
				 if (i == tam - 3)
					{
					for(int k = 0; k < finalData[i].length; k++)
						{
						if (k == 8 || k == 6)
							finalData[i][k]=reverseExpression((String)(expressions.get(finalInfo[3][k])));
						else 
						finalData[i][k]=(Double)(expressions.get(finalInfo[3][k]));
						}
					}
				else if (i == tam - 2)
					{
					for(int k = 0; k < finalData[i].length; k++)
						{
						if (k == 0)
							finalData[i][k]=reverseAction((String)(actions.get(finalInfo[4][k])));
						else
						finalData[i][k]=(Double)(actions.get(finalInfo[4][k]));
						}
					}
				else if (i == tam - 1)
				{
				for(int k = 0; k < finalData[i].length; k++)
					{
					finalData[i][k]=(Double)(deviceInfo.get(finalInfo[5][k]));
					}
				}
				else 
					for(int k = 0; k < finalData[i].length; k++)
						{
						finalData[i][k]=((HashMap<String,Double>) waves.get(finalInfo[0][i])).get(finalInfo[1][k]);
						}
			
			}

			finalDataArray = finalData;
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
				{"Delta","Theta","LowAlpha","HighAlpha","LowBeta","HighBeta","LowGamma","MidGamma"},
				{"Attention","Meditation"},
				{"Blink"},
				{},
				{"BatteryLevel","PoorSignal"}
			};
			
			finalData = new Double[5][];
			finalData[0]= new Double[8];
			finalData[1]= new Double[2];
			finalData[2]= new Double[1];
			finalData[3] = new Double[0];
			finalData[4] = new Double[2];
			
			for (int i = 1;i < finalInfo.length; i++){
				System.out.print("[");
				if (i == 4)
					System.out.print("]");
				for (int k = 0;k < finalInfo[i].length; k++){
					if (i == 1){
						finalData[i-1][k]= convertVolts((Double) neuroData.get("Waves").get(finalInfo[i][k]));
						if (k == finalInfo[i].length-1){
						System.out.print(finalData[i-1][k]);
						System.out.print("]");}
						else
						System.out.print(finalData[i-1][k]+ " , ");
					}
					else
						{finalData[i-1][k]=(Double) neuroData.get("Waves").get(finalInfo[i][k]);
						if (k == finalInfo[i].length-1){
						System.out.print(finalData[i-1][k]);
						System.out.print("]");}
						else
						System.out.print(finalData[i-1][k]+ " , ");
					}
				}
				System.out.println();
				}

			System.out.println();
			finalDataArray = finalData;
			}
			
		
			
			}
    
  //done
    public static double convertVolts(double value)
    {
    	value = (value*(1.8/4096))/2000;    
    	return value;
    }
    


public static Double reverseAction(String actStr){

    Double act;

    if(actStr == "Neural"){
        act = 1.0;
    }else if(actStr == "Push"){
    	act = 2.0;
    }else if(actStr == "Pull"){
    	act = 4.0;
    }else if(actStr == "Lift"){
    	act = 8.0;
    }else if(actStr == "Drop"){
    	act = 16.0;
    }else if(actStr == "Left"){
    	act = 32.0;
    }else if(actStr == "Right"){
    	act = 64.0;
    } else if(actStr == "RotateLeft"){
    	act = 128.0;
    } else if(actStr == "RotateRight"){
    	act = 256.0;
    }else if(actStr == "RotateClockwise"){
    	act = 512.0;
    }else if(actStr == "RotateCounter-Clockwise"){
    	act = 1024.0;
    }else if(actStr == "RotateForward"){
    	act = 2048.0;
    } else if(actStr == "RotateReverse"){
    	act = 4096.0;
    }else if(actStr == "Disappear"){
    	act = 8192.0;
    }
    else act = 0.0;

    return act;

}

public static Double reverseExpression(String actStr){

    Double act;

    if(actStr == "Smile"){
        act = 128.0;
    }else if(actStr == "Clench"){
    	act = 256.0;
    }else if(actStr == "SmirkLeft"){
    	act = 1024.0;
    }else if(actStr == "Laught"){
    	act = 512.0;
    }else if(actStr == "RaiseBrow"){
    	act = 3.0;
    }else if(actStr == "FurrowBrow"){
    	act = 64.0;
    } else if(actStr == "error"){
    	act = 128.0;
    } 
    else act = 0.0;

    return act;

}

}


