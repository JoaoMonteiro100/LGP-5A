import Iedk.Channel;
import Iedk.Wave;
import module.MainModule;

import java.util.HashMap;
import java.util.Vector;

public class TestsModule {
////////////////////////////////////////////////////EMOTIV TEST/////////////////////////////////////////////////
	
public void testeEmotiv() {
HashMap<String, HashMap<String, Object>> testeEmo;
HashMap<String, HashMap<String, Object>> testeEmo2;
HashMap<String, HashMap<String, Double>> testepower;
HashMap<String, Object> testewaves;
HashMap<String, Object> testeactions;
HashMap<String, Object> testeaffections;
HashMap<String, Object> testeexpressions;
HashMap<String, Object> testeinfo;
HashMap<String, Double> testetypes1;
HashMap<String, Double> testetypes2;
HashMap<String, Double> testetypes3;


testeEmo = new HashMap<String, HashMap<String, Object>>();
testeEmo2 = new HashMap<String, HashMap<String, Object>>();
testepower = new HashMap<String, HashMap <String,Double>>();
testewaves = new HashMap<String, Object>();
testeaffections = new HashMap<String, Object>(); 
testeactions = new HashMap<String, Object>();
testeexpressions = new HashMap<String, Object>();
testeinfo = new HashMap<String, Object>();
testetypes1 = new HashMap<String, Double>();
testetypes2 = new HashMap<String, Double>();
testetypes3 = new HashMap<String, Double>();

Channel AF3, F7, F3, FC5, T7, P7, O1, O2, P8, T8, FC6, F4, F8, AF4;
AF3 = new Channel();
F7 = new Channel();
F3 = new Channel();
FC5 = new Channel();
T7 = new Channel();
P7 = new Channel();
O1 = new Channel();
O2 = new Channel();
P8 = new Channel();
T8 = new Channel();
FC6 = new Channel();
F4 = new Channel();
F8 = new Channel();
AF4 = new Channel();

testeEmo2.put("Waves", testewaves);
testeEmo.put("Actions", testeactions);
testeEmo.put("FacialExpressions", testeexpressions);
testeEmo.put("DeviceInfo", testeinfo);
testeEmo.put("AffectiveValues", testeaffections);


testewaves.put("AF3", new Wave(AF3, 2));
testewaves.put("F7", new Wave(F7, 2));
testewaves.put("F3", new Wave(F3, 2));
testewaves.put("FC5", new Wave(FC5, 2));
testewaves.put("T7", new Wave(T7, 2));
testewaves.put("P7", new Wave(P7, 2));
testewaves.put("O1", new Wave(O1, 2));
testewaves.put("O2", new Wave(O2, 2));
testewaves.put("P8", new Wave(P8, 2));
testewaves.put("T8", new Wave(T8, 2));
testewaves.put("FC6", new Wave(FC6, 2));
testewaves.put("F4", new Wave(F4, 2));
testewaves.put("F8", new Wave(F8, 2));
testewaves.put("AF4", new Wave(AF4, 2));


testeactions.put("LookingLeft", 2.0);
testeactions.put("LookingRight", 1.0);
testeactions.put("LookingDown", 2.0);
testeactions.put("LookingUp", 1.0);


testeexpressions.put("LeftWink", 45.0);
testeexpressions.put("RightWink", 45.0);
testeexpressions.put("Blink", 5.0);
testeexpressions.put("EyesOpen", 2.0);
testeexpressions.put("SmileExtension", 4.0);
testeexpressions.put("ClenchExtension", 45.0);
testeexpressions.put("LowerFaceExpression", "SmirkLeft");
testeexpressions.put("LowerFaceExpressionPower", 1.0);
testeexpressions.put("UpperFaceExpression", "Smile");
testeexpressions.put("UpperFaceExpressionPower", 4.0);

testeinfo.put("SignalQuality", 1.0);
testeinfo.put("Timestamp", 1.0);
testeinfo.put("BatteryLevel", 1.0);
testeinfo.put("WirelessSignal", 0.0);

testeaffections.put("EngagementActive",2.0);
testeaffections.put("Engagement",2.0);

//MainModule.initMerge (1, testeEmo);

//for (int i = 0; i < MainModule.getFinalDataArray().length; i++) {
//for (int j = 0; j < MainModule.getFinalDataArray()[i].length; j++) {
//System.out.print(MainModule.getFinalDataArray()[i][j]);
//}
//}

//criar array de arrays com toda a informaÃ§Ã£o do dispositivo
//MainModule.finalInfoFinal(1);
HashMap<String, Object> waves = testeEmo.get("Waves");
HashMap<String, Object> expressions = testeEmo.get("FacialExprenssions");
HashMap<String, Object> actions = testeEmo.get("Actions");
HashMap<String, Object> deviceInfo = testeEmo.get("DeviceInfo");



}

public void testeNeurosky() {
	HashMap<String, HashMap<String, Object>> testeNeuro;
	HashMap<String, Object> teste1;
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
	 
	//MainModule.initMerge(2, testeNeuro);
}

public static void testeCalcs() {

	
	Float [][] asd = new Float[31][2];
	Float [][] sdf = new Float[31][2];
	Float [][] dfg = new Float[31][2];
	
	asd = new Float[][] {{0f,17f},{1f,15f},{2f,17f},{3f,15f},{4f,17f},{5f,50f},{6f,15f},{7f,15f},{8f,50f},
		{9f,15f},{117f,2f},{11f,15f},{12f,15f},{13f,17f},{14f,50f},{15f,50f},{16f,15f},{17f,15f},{18f,2f},{19f,17f},
		{215f,50f},{21f,17f},{22f,2f},{23f,2f},{24f,15f},{25f,50f},{26f,17f},{27f,15f},{28f,17f},{29f,15f},{30f,2f}};
		
	sdf = new Float[][] {{0f,1f},{1f,1f},{2f,70f},{3f,30f},{4f,42f},{5f,12f},{6f,15f},{7f,15f},{8f,11f},
		{9f,15f},{117f,2f},{11f,11f},{12f,15f},{13f,174f},{14f,50f},{15f,0f},{16f,15f},{17f,157f},{18f,22f},{19f,17f},
		{215f,5f},{21f,17f},{22f,7f},{23f,2f},{24f,15f},{25f,50f},{26f,17f},{27f,15f},{28f,17f},{29f,145f},{30f,21f}};
			
	dfg = new Float[][] {{0f,17f},{1f,15f},{2f,17f},{3f,15f},{4f,17f},{5f,50f},{6f,15f},{7f,15f},{8f,50f},
		{9f,15f},{117f,2f},{11f,15f},{12f,15f},{13f,17f},{14f,50f},{15f,50f},{16f,15f},{17f,15f},{18f,2f},{19f,17f},
		{215f,50f},{21f,17f},{22f,2f},{23f,2f},{24f,15f},{25f,50f},{26f,17f},{27f,15f},{28f,17f},{29f,15f},{30f,2f}};		
	
		
		
	
	Vector<Float[][]> vector = new Vector<Float[][]>();
	vector.addElement(asd);
	vector.addElement(sdf);
	vector.addElement(dfg);
	
	

	int[][] fim = new int[][] {{1,2,3},{1,2,3,4,5,6,7,8,9,10,11,12,13},{1}};
	
	
	
	MainModule.calculate(fim);

}

public static void main(String[] args) {
	testeCalcs();
}

}
