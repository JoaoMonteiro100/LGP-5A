package com.lgp5;

import java.io.IOException;
import java.util.Arrays;

//import j2me.com.NeuroSky.ThinkGear.IO.DummyConnection;
import j2me.com.NeuroSky.ThinkGear.IO.HeadsetConnection;
import j2me.com.NeuroSky.ThinkGear.Util.DataListener;
import j2me.com.NeuroSky.ThinkGear.Util.StreamParser;

public class Main {
	// Dummy
	/*
	 * public static void main(String[] args) throws IOException {
	 * DummyConnection simulator = new DummyConnection();
	 * simulator.openConnection("teste",1); byte[] byteArray = new byte[10];
	 * while(true) { simulator.read(byteArray); for(int i = 0; i < 10; i++)
	 * System.out.println(byteArray[i]); } }
	 */
	

	public static void main(String[] args) throws IOException {
		HeadsetConnection connection = new HeadsetConnection();
		connection.openConnection("0013EF004809", 3);

		DataListener dataListener;
		dataListener = new DataListener() {
			
			@Override
			public void dataValueReceived(int extendedCodeLevel, int code, int numBytes, byte[] valueBytes, Object customData) {
				System.out.println(code+ " "+numBytes + " "+ extendedCodeLevel);
				System.err.println(Arrays.toString(valueBytes));
				//if( extendedCodeLevel == 0 ) {
					switch (code)
					{
					case 0x04:
						System.out.println("Attention: "+ (valueBytes[0] & 0xFF));
						break;
					case 0x05:
						System.out.println("Meditation: "+ (valueBytes[0] & 0xFF));
						break;
					case 0x81:
						System.out.println("EEG_POWER: "+ StreamParser.bigEndianBytesToFloat(valueBytes));
						break;
					}
				//}
			}
		};
		java.lang.Object obj = new java.lang.Object();
		StreamParser parser = new StreamParser(StreamParser.PARSER_TYPE_PACKETS, dataListener, obj);
		
			
		while (true) {
			byte[] byteArray = new byte[256];
			connection.read(byteArray);
			for (int i = 0; i < 256; i++)
			{
				int k = parser.parseByte(byteArray[i] & 0xFF);
				//System.err.println(byteArray[i] & 0xFF);
			
				//if(k==1 || k== -2)
				//System.err.println(k);
			//	System.err.println(parser.state);
//				if(i % 2 == 0)
//					System.out.println("0x"+Integer.toHexString(byteArray[i]));
//				else
//					System.err.println("0x"+Integer.toHexString(byteArray[i]));
			}
			//System.out.println("NEXT");		
		}		
		
		//System.out.println(Arrays.toString(parser.payload));
	}
}
