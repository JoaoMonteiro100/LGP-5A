package com.lgp5;

import interfaces.HeadSetDataInterface;
import j2me.com.NeuroSky.ThinkGear.Util.HeadsetData;

public class Main {

	public static void main(String[] args) {
		HeadSetDataInterface sendDataInterface;
		
		sendDataInterface = new HeadSetDataInterface() {
			
			@Override
			public void onReceiveData(HeadsetData headsetData) {
				System.out.println(headsetData.attention);
			}
		};
		
		Neurosky neurosky = new Neurosky("0013EF004809", sendDataInterface);
		neurosky.connect();
		neurosky.run();
	}

}
