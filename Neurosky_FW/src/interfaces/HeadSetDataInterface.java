package interfaces;

import java.util.HashMap;

public interface HeadSetDataInterface {
	public void onReceiveData(HashMap<String, HashMap<String,Object>> dataToSend);
	public void onReceiveRawData(HashMap<String,Integer> rawData);
}
