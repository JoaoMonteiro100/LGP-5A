package com.lgp5;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import interfaces.HeadSetDataInterface;
//import j2me.com.NeuroSky.ThinkGear.IO.DummyConnection;
import j2me.com.NeuroSky.ThinkGear.IO.HeadsetConnection;
import j2me.com.NeuroSky.ThinkGear.Util.DataListener;
import j2me.com.NeuroSky.ThinkGear.Util.HeadsetData;
import j2me.com.NeuroSky.ThinkGear.Util.StreamParser;



public class Neurosky implements Runnable {
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
	}
	
	
	public void connect() {
		try {
			headsetConnection.openConnection(deviceID, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void startReceivingData() {
		this.run = true;
		receivedData();
	}
	
	
	public void stopReceivingData() {
		this.run = false;
	}
	
	
	private void receivedData() {
		dataListener = new DataListener() {
			
			@Override
			public void dataValueReceived(int extendedCodeLevel, int code, int numBytes, byte[] valueBytes, Object customData) {
				switch (code) {
				case (0x02):
					System.out.println("\npoorSignal: " + (valueBytes[0] & 0xFF));
					break;
				case 0x04:
					headsetData.attention = valueBytes[0] & 0xFF;
					System.out.println("Attention: " + (valueBytes[0] & 0xFF));
					break;
				case 0x05:
					headsetData.meditation = valueBytes[0] & 0xFF;
					System.out.println("Meditation: " + (valueBytes[0] & 0xFF));
					break;
				case (0x16):
					System.out.println("blink: " + (valueBytes[0] & 0xFF));
				case 0x83:
					for (int i = 0; i < 8; i++) {
						waves[i] = Math.abs((int) valueBytes[i * 3] << 16 | (int) valueBytes[i * 3 + 1] << 8
								| (int) valueBytes[i * 3]);
						// waves[i]=Math.abs(waves[i]/10000);
						String wave = null;
						switch(i) {
						case 0:
							wave = "\nDelta: ";
							headsetData.delta = Float.valueOf(waves[i]);
							break;
						case 1:
							wave = "Theta: ";
							headsetData.theta = Float.valueOf(waves[i]);
							break;
						case 2:
							wave = "low-alpha: ";
							headsetData.alpha1 = Float.valueOf(waves[i]);
							break;
						case 3:
							wave = "high-alpha: ";
							headsetData.alpha2 = Float.valueOf(waves[i]);
							break;
						case 4:
							wave = "low-beta: ";
							headsetData.beta1 = Float.valueOf(waves[i]);
							break;
						case 5:
							wave = "high-beta: ";
							headsetData.beta2 = Float.valueOf(waves[i]);
							break;
						case 6:
							wave = "low-gamma: ";
							headsetData.gamma1 = Float.valueOf(waves[i]);
							break;
						case 7:
							wave = "mid-gamma: ";
							headsetData.gamma2 = Float.valueOf(waves[i]);
							break;
						}
					}
					
					if(sendDataInterface != null)
						sendDataInterface.onReceiveData(headsetData);
					break;
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
