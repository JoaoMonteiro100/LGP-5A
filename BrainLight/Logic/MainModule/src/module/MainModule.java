package module;


import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
//import Analysis.*;
//import Analysis.Calculations;

import com.lgp5.Neurosky;

import Iedk.EmotivDevice;
import Iedk.Wave;
import Iedk.interfaces.EmotivInterface;
import history.write.WriteXLS_NeuroSky;
import interfaces.HeadSetDataInterface;

public class MainModule {
	private String fileName;
	private Boolean record;
	protected BlockingQueue<Double[][]> queue = null;
	protected BlockingQueue<Double[]> queue2 = null;
	private Neurosky neuroDevice;
	private EmotivDevice emotivDevice;
	private int wirelessSignal;
	public static  Double[][] finalDataArray;
	public static  Double[] finalRawData;
	private HashMap<String,HashMap<String,Object>> neuroData;
	private LinkedList<HashMap<String, HashMap<String, Object>>> sharedQ;
	private LinkedList<Double [][]> doubleQ;
	private boolean running;
	private int deviceNo;
	private boolean calculate; //ver se as analises estao a correr, e se sim parar de enviar informaçao toda TODO


public MainModule(int device, BlockingQueue<Double[][]> queue, BlockingQueue<Double[]> queue2, int days){
		deleteOldFiles(days);	
		Date dNow = new Date( );
		SimpleDateFormat ft = new SimpleDateFormat ("E_yyyy_MM_dd_'at'_hh_mm_ss");
		this.fileName=ft.format(dNow);
		this.record=false;
		this.queue = queue;
		this.queue2 = queue2;
		sharedQ = new LinkedList<>();
		wirelessSignal=0;
		new LinkedList<>();
		doubleQ = new LinkedList<>();
		running=true;
		deviceNo = device;
		if(device == 1){


			EmotivInterface sendDataInterface;
			//confirmar
			sendDataInterface = new EmotivInterface(){

				@Override
				public void onReceiveData(HashMap<String, Object> data) {
					System.out.println("data");
					System.out.println(data.toString());

				}

				@Override
				public void onReceiveWavesData(HashMap<String, Wave> data) {
					// TODO Auto-generated method stub

				}


			};


			emotivDevice = new EmotivDevice(sendDataInterface);
		}




		else if(device == 2){
			HeadSetDataInterface sendDataInterface;
			WriteXLS_NeuroSky wNeuroSky = new WriteXLS_NeuroSky();
			//confirmar
			sendDataInterface = new HeadSetDataInterface(){

				@Override
				public void onReceiveData(HashMap<String, HashMap<String, Object>> dataToSend) {
					initMerge(2, (HashMap<String, Object>) dataToSend.clone());
					try {
						queue.put(finalDataArray);
						if(record){	
							Date dNow = new Date( );					 
							SimpleDateFormat ftTime = 
									new SimpleDateFormat ("hh:mm:ss");
							final Object[][] bookData = {
									{ftTime.format(dNow), finalDataArray[0][0], finalDataArray[0][1],
										finalDataArray[0][2],finalDataArray[0][3],
										finalDataArray[0][4],finalDataArray[0][5],
										finalDataArray[0][6],finalDataArray[0][7],	
										finalDataArray[1][0],finalDataArray[1][1],
										finalDataArray[2][0]},
							};
							WriteXLS_NeuroSky.writeXLS(fileName, bookData);
						}else{
							Date dNow = new Date( );
							SimpleDateFormat ft = 
									new SimpleDateFormat ("E_yyyy_MM_dd_'at'_hh_mm_ss");
							fileName=ft.format(dNow);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}		
				}

				@Override
				public void onReceiveRawData(HashMap<String, Integer> rawData) {
					initGetRaw(2,rawData);
					try {
						queue2.put(finalRawData);
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
					Thread emotivThread = new Thread(emotivDevice);
					emotivDevice.connectEmotiv();
					/*emotivThread.run();*/

					System.err.println("ola");
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
			emotivDevice.emotivDeviceDisconnect();
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
					initMerge (1,(HashMap<String,Object>)sharedQ.pop().clone());
					doubleQ.addLast(finalDataArray);
					System.out.println(doubleQ.getLast()[5]);
				}
				else sharedQ.pop();

			}
		}

		else if (device == 2){
			initMerge(2,((HashMap<String,Object>)neuroDevice.getFinalData().clone()));

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
		/*MainModule fw = new MainModule(2);
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
//		BlockingQueue<Double[][]> queue = null;
//		BlockingQueue<Double[]> queue2 = null;
//		MainModule fw = new MainModule(1,queue,queue2);
//		fw.receiveDeviceData();
	}

	public static String[][] finalInfoFinal(int device){
		if(device == 1){
			String[][] finalInfo = new String[][] { {"BatteryLevel","WirelessSignal","Timestamp","SignalQuality"},
				{"LookingLeft","LookingRight","LookingDown","LookingUp"},
				{"LeftWink", "RightWink", "Blink", "EyesOpen","SmileExtension","ClenchExtension","LowerFaceExpression",
					"LowerFaceExpressionPower","UpperFaceExpression","UperFaceEXpressionPower"},
				{"EngagementActive","Engagement","ExcitementActive","ExcitementLongTime","ExcitementShortTime","FrustationActive",
					"Frustation","MeditationActive","Meditation"}
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
	
		public static void initGetWaves (int device,  HashMap <String,Wave> data)
	{	
		String[] finalInfo = new String [] { "AF3","F7","F3","FC5","T7","P7",
			"O1","O2","P8","T8","FC6","F4","F8","AF4"};
		Double [][] finalData;

		if (device == 1){
			
			HashMap <String,Wave> emotivData;
			if (data instanceof HashMap) {
				emotivData = (HashMap <String,Wave>) data;
			} else {
				System.out.println("Wrong type of data for emotiv device");
				return;
			}

		finalData = new Double[14][36];
		
			for (int i = 0; i < finalData.length; i++){
				
						finalData[i][0]=emotivData.get(finalInfo[i]).getTheta();
						finalData[i][1]=emotivData.get(finalInfo[i]).getDelta();
						finalData[i][2]=emotivData.get(finalInfo[i]).getAlpha();
						finalData[i][3]=emotivData.get(finalInfo[i]).getBeta();
						finalData[i][4]=(double) emotivData.get(finalInfo[i]).getSignalQuality();
						for (int k = 0; k <= 30; k++)
						{
							finalData[i][k+5]=emotivData.get(finalInfo[i]).getFreqVals().get(k);
						}
				}
		finalDataArray = finalData;
		}
	}
	
	public static void initMerge (int device, HashMap<String, Object> data)
	{
		String[][] finalInfo;
		Double [][] finalData;
		if (device == 1) {
			HashMap<String, Object> emotivData;
			if (data instanceof HashMap) {
				emotivData= (HashMap<String, Object>) data;
			} else {
				System.out.println("Wrong type of data for emotiv device");
				return;
			}

			//criar array de arrays com toda a informação do dispositivo
			finalInfo = finalInfoFinal(1);


			HashMap<String, Object> affective = (HashMap<String, Object>) emotivData.get("AffectiveValues");
			HashMap<String, Object> expressions = (HashMap<String, Object>) emotivData.get("FacialExpressions");
			HashMap<String, Object> actions = (HashMap<String, Object>) emotivData.get("Actions");
			HashMap<String, Object> deviceInfo = (HashMap<String, Object>) emotivData.get("DeviceInfo");

			finalData = new Double[4][];
			//definir o tamanho de cada parte do array
			
			finalData[0]= new Double[4];
			finalData[1] = new Double[4];
			finalData[2] = new Double[10];
			finalData[3] = new Double[9];
			

			for (int i = 0; i < finalData.length; i++){
				if (i == 0)
				{
					for(int k = 0; k < finalData[i].length; k++)
					{
						finalData[i][k]=(Double)(deviceInfo.get(finalInfo[0][k]));
					}
						
				}
				else if (i == 1)
				{
					for(int k = 0; k < finalData[i].length; k++)
					{
						finalData[i][k]=(Double)(actions.get(finalInfo[1][k]));
					}
				}
				else if (i == 2)
				{
					for(int k = 0; k < finalData[i].length; k++){
				
					if (k == 8 || k == 6)
						finalData[i][k]=reverseExpression((String)(expressions.get(finalInfo[2][k])));
					else 
						finalData[i][k]=(Double)(expressions.get(finalInfo[2][k]));
					}
				}
				else if (i == 3)
					for(int k = 0; k < finalData[i].length; k++)
					{
						finalData[i][k]=(Double) affective.get(finalInfo[3][k]);
					}

			}

			finalDataArray = finalData;
		}

		else if (device == 2){
			HashMap<String, HashMap<String, Object>> neuroData;
			if (data instanceof HashMap) {
				neuroData = (HashMap<String, HashMap<String, Object>>) data.clone();
			} else {
				System.out.println("Wrong type of data for neurosky device");
				return;
			}

			finalInfo = finalInfoFinal(2);
			finalData = new Double[3][];
			//finalData = new Double[4][];
			finalData[0]= new Double[8];
			finalData[1]= new Double[2];
			finalData[2]= new Double[1];
			//finalData[3]= new Double[]{(double) 2};

			for (int i = 1;i < finalInfo.length; i++){
				for (int k = 0;k < finalInfo[i].length; k++){
					if (i == 1){
						finalData[i-1][k]= convertVolts((Float)neuroData.get("Waves").get(finalInfo[i][k]));
						finalData[i-1][k]= finalData[i-1][k]*10;//Decimo de Volt
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


public static void deleteOldFiles(int days){
		String str = (System.getProperty("user.dir")).replaceAll("BrainLight", "")+"\\history";
		File dir = new File(str); 
		if(dir.exists()){
			try{
				purgeOldFiles(dir,days);
			} catch (Exception e){
				System.err.println("History folder not in correct directory ("+str+")");
			}
		}
	}

	public static void cleanHistory() {
		String str = (System.getProperty("user.dir")).replaceAll("BrainLight", "")+"\\history";
		File dir = new File(str); 
		if(dir.exists()){
			try{
				purgeDirectory(dir);
			} catch (Exception e){
				System.err.println("History folder not in correct directory ("+str+")");
			}
		}
	}

	static void purgeOldFiles(File dir, int x) {
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) purgeDirectory(file);
			long diff = new Date().getTime() - file.lastModified();

			if (diff > x * 60 * 1000) {
				file.delete();
			}
		}
	}

	static void purgeDirectory(File dir) {
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) purgeDirectory(file);
			file.delete();
		}
	}


	public Boolean getRecord() {
		return record;
	}


	public void setRecord(Boolean record) {
		this.record = record;
	}
}
