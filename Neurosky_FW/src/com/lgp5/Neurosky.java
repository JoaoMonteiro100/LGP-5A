package com.lgp5;

import java.io.IOException;
import java.util.HashMap;

import interfaces.HeadSetDataInterface;
//import j2me.com.NeuroSky.ThinkGear.IO.DummyConnection;
import j2me.com.NeuroSky.ThinkGear.IO.HeadsetConnection;
import j2me.com.NeuroSky.ThinkGear.Util.DataListener;
import j2me.com.NeuroSky.ThinkGear.Util.HeadsetData;
import j2me.com.NeuroSky.ThinkGear.Util.StreamParser;
import utils.Constants;



public class Neurosky implements Runnable {
	private HashMap<String, HashMap<String,Object>> dataToSend;
	private HashMap<String, HashMap<String,Object>> finalData;
	private HashMap<String, Integer> rawToSend;
	HeadsetConnection headsetConnection;
	String deviceID;
	long waves[];
	boolean run;
	DataListener dataListener;
	HeadsetData headsetData;
	HeadSetDataInterface sendDataInterface;

	public Neurosky(String deviceID, HeadSetDataInterface sendDataInterface) {
		this.headsetConnection = new HeadsetConnection();
		this.deviceID = deviceID;
		waves = new long[8];
		this.run = false;
		headsetData = new HeadsetData();
		this.sendDataInterface = sendDataInterface;
		dataToSend = new HashMap<>();
		rawToSend = new HashMap<>();
	}
	public void connect() {
		try {
			headsetConnection.openConnection(deviceID, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void disconnect() {
		try {
			headsetConnection.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public HashMap<String, HashMap<String,Object>> getFinalData() {
		return finalData;
	}
	public void setFinalData(HashMap<String, HashMap<String,Object>> finalData) {
		this.finalData = finalData;
	}
	public void startReceivingData() {
		this.run = true;
		receivedData();
	}
	public void stopReceivingData() {
		this.run = false;
	}
	private void receivedData() {
		HashMap<String, Object> wavesMap = new HashMap<>();
		dataListener = new DataListener() {
			@Override
			public void dataValueReceived(int extendedCodeLevel, int code, int numBytes, byte[] valueBytes, Object customData) {
				int k =0;
				switch (code) 
				{
				case (0x7E):
					wavesMap.put(Constants.BATTERY_LEVEL, valueBytes[0] & 0xFF);
				break;
				case (0x02):
					wavesMap.put(Constants.POOR_SIGNAL, valueBytes[0] & 0xFF);				
				break;
				case 0x04:
					wavesMap.put(Constants.ATTENTION, valueBytes[0] & 0xFF);
					break;
				case 0x05:
					wavesMap.put(Constants.MEDITATION, valueBytes[0] & 0xFF);
					break;
				case (0x16):
					wavesMap.put(Constants.BLINK, valueBytes[0] & 0xFF);
				break;
				case (0x80):
					int highlow = (int)(valueBytes[0] & 0xFF);
					int highlow1 = (int)(valueBytes[1] & 0xFF);
					// Source: http://developer.neurosky.com/docs/doku.php?id=thinkgear_communications_protocol#packet_structure
					if(sendDataInterface != null){
						int raw = (highlow * 256) + highlow1;
						if( raw > 32768 ) raw -= 65536;	
						rawToSend.put(Constants.RAW,raw);
						sendDataInterface.onReceiveRawData(rawToSend);
						rawToSend.clear();						
					}
				break;
				case 0x83:
					for (int i = 0; i < 8; i++) {
						waves[i] = Math.abs((int) valueBytes[i * 3] << 16 | (int) valueBytes[i * 3 + 1] << 8
								| (int) valueBytes[i * 3]);					
						switch(i) {
						case 0:
							wavesMap.put(Constants.DELTA, Float.valueOf(waves[i]));
							break;
						case 1:
							wavesMap.put(Constants.THETA, Float.valueOf(waves[i]));
							break;
						case 2:
							wavesMap.put(Constants.LOW_ALPHA, Float.valueOf(waves[i]));
							break;
						case 3:
							wavesMap.put(Constants.HIGH_ALPHA, Float.valueOf(waves[i]));
							break;
						case 4:
							wavesMap.put(Constants.LOW_BETA, Float.valueOf(waves[i]));
							break;
						case 5:
							wavesMap.put(Constants.HIGH_BETA, Float.valueOf(waves[i]));
							break;
						case 6:
							wavesMap.put(Constants.LOW_GAMMA, Float.valueOf(waves[i]));
							break;
						case 7:							
							wavesMap.put(Constants.MID_GAMMA, Float.valueOf(waves[i]));
							break;
						}
					}
					break;
				}

				if(wavesMap.size() == 11) {
					dataToSend.put(Constants.WAVES, wavesMap);
					if(sendDataInterface != null) {
						sendDataInterface.onReceiveData(dataToSend);
						setFinalData(dataToSend);
						wavesMap.clear();
					}		
				}
			}
		};
		java.lang.Object obj = new java.lang.Object();
		StreamParser parser = new StreamParser(StreamParser.PARSER_TYPE_PACKETS, dataListener, obj);

		while(run) {
			byte[] byteArray = new byte[256];
			try {
				headsetConnection.read(byteArray);

				for (int i = 0; i < 256; i++)
					parser.parseByte(byteArray[i] & 0xFF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void run() {
		startReceivingData();
	}
}
