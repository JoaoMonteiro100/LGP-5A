package BrainLightFW;
import interfaces.HeadSetDataInterface;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
//import Analysis.*;
//import Analysis.Calculations;

import com.lgp5.Neurosky;

import Emotiv.Emotiv_SDK.EmotivDevice;

/**
 * Created by cenas on 23/04/16.
 */

public class BrainLightFW {
	protected BlockingQueue<Double[][]> queue = null;
	protected BlockingQueue<Double[]> queue2 = null;
	private Neurosky neuroDevice;
	private EmotivDevice emoDevice;
	private int wirelessSignal;
	public static  Double[][] finalDataArray;
	public static  Double[] finalRawData;
	private HashMap<String,HashMap<String,Object>> neuroData;
	private LinkedList<HashMap<String, HashMap<String, Object>>> sharedQ;
	private LinkedList<Double [][]> doubleQ;
	private boolean running;
	private int deviceNo;
	private boolean calculate; //ver se as analises estao a correr, e se sim parar de enviar informaçao toda TODO

	public BrainLightFW(int device,BlockingQueue<Double[][]> queue,BlockingQueue<Double[]> queue2){
		this.queue = queue;
		this.queue2 = queue2;
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
					initMerge(2, dataToSend);
					try {
						queue.put(finalDataArray);
					} catch (Exception e) {
						// TODO: handle exception
					}		
				}

				@Override
				public void onReceiveRawData(HashMap<String, Integer> rawData) {
					initGetRaw(2,rawData);
					try {
						queue2.put(finalRawData);
						//Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}					
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

					neuroDevice.connect();
					neuroDevice.run();
					//Thread neuroThread = new Thread(neuroDevice);
					//neuroThread.start();

					while (running) {
						if (deviceNo == 2) {
							getDeviceData(2);
						}
					}
				}
			}
		};
		receiveDataThread.setDaemon(true);
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

				if(getAllSensorsStatusOK(sharedQ.getFirst())) {
					// localQ.addLast(sharedQ.pop());
					initMerge (1,sharedQ.pop());
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
		//TypesOfCalculations[] types = new TypesOfCalculations[infoArray[1].length];


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
				/*
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
    			default : break;*/
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
		}
		System.out.println("END");
		fw.stopReceiving();
		fw.deviceDisconnect();

		 */
	}

	public static String[][] finalInfoFinal(int device){
		if(device == 1){
			String[][] finalInfo = new String[][] { {"AF3","F7","F3","FC5","T7","P7","O1","O2","P8","T8","FC6","F4","F8","AF4"},
				{"Theta","Alpha","LowBeta","HighBeta","Gamma"},
				{},
				{"LeftWink", "RightWink", "Blink", "EyesOpen","SmileExtension","ClenchExtension","LowerFaceExpression",
					"LowerFaceExpressionPower","UpperFaceExpression","UperFaceEXpressionPower"},
				{"Action","ActionPower","LookingLeft","LookingRight","LookingDown","LookingUp"},
				{"BatteryLevel","WirelessSignal"}
			};
			return finalInfo;}
		else if (device == 2){
			String[][] finalInfo = new String [][] { {"FP1"},
				{"Delta","Theta","LowAlpha","HighAlpha","LowBeta","HighBeta","LowGamma","MidGamma"},
				{"Attention","Meditation"},
				{"poor_signal"}
			};
			return finalInfo;
		}
		else return null;
	}
	public static void initGetRaw (int device,  HashMap <String,Integer> data)
	{
		Double[] finalRaw = new Double[1];

		if (device == 2){
			HashMap <String,Integer> neuroData;
			if (data instanceof HashMap) {
				neuroData = (HashMap <String,Integer>) data;
			} else {
				System.out.println("Wrong type of data for neurosky device");
				return;
			}
			finalRaw[0] = (Double) convertVolts((float)neuroData.get("Raw"));
			finalRaw[0] = finalRaw[0]*100000;//Micro de Volt
			//finalRaw[0] = Double.parseDouble(neuroData.get("Raw").toString());
		}
		finalRawData = finalRaw;
	}
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

			finalInfo = finalInfoFinal(2);
			finalData = new Double[3][];
			finalData[0]= new Double[8];
			finalData[1]= new Double[2];
			finalData[2]= new Double[1];

			for (int i = 1;i < finalInfo.length; i++){
				for (int k = 0;k < finalInfo[i].length; k++){
					if (i == 1){
						finalData[i-1][k]= convertVolts((Float)neuroData.get("Waves").get(finalInfo[i][k]));
						finalData[i-1][k]= finalData[i-1][k]*10;//D�cimo de Volt
					}
					else
					{
						finalData[i-1][k]=convertToDouble((Integer) neuroData.get("Waves").get(finalInfo[i][k]));			
					}
				}
			}
			finalDataArray = finalData;
		}



	}

	public static double convertVolts(Float value)
	{
		value = (float) ((value*(1.8/4096))/2000);    
		return value;
	}
	public static double convertToDouble(Integer value)
	{
		if(value != null)
			return value;
		else return -1;
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


	public static void cleanHistory() {
		String str = (System.getProperty("user.dir")+Integer.MAX_VALUE).replaceAll("BrainLightFW"+Integer.MAX_VALUE, "")+"FW\\src\\history";
		File dir = new File(str); 
		try{
			purgeDirectory(dir);
		} catch (Exception e){
			System.err.println("History folder not in correct directory ("+str+")");
		}
	}

	static void purgeDirectory(File dir) {
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) purgeDirectory(file);
			file.delete();
		}
	}
}
