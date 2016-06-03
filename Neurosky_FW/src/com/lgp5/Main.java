package com.lgp5;

import java.util.HashMap;

import interfaces.HeadSetDataInterface;

public class Main {

	public static void main(String[] args) {
		HeadSetDataInterface sendDataInterface;
		
		sendDataInterface = new HeadSetDataInterface() {
			
			@Override
			public void onReceiveData(HashMap<String, HashMap<String, Object>> dataToSend) {
				//System.err.println(dataToSend.toString());
				
			}

			@Override
			public void onReceiveRawData(HashMap<String, Integer> rawData) {
				// TODO Auto-generated method stub
				
			}
		};
		
		Neurosky neurosky = new Neurosky("0013EF004809", sendDataInterface);
		neurosky.connect();
		neurosky.run();
	}

}
