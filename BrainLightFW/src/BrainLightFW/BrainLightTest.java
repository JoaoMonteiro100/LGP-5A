package BrainLightFW;

import java.util.HashMap;
public class BrainLightTest {
////////////////////////////////////////////////////EMOTIV TEST/////////////////////////////////////////////////
	
public void testeEmotiv() {
HashMap<String, HashMap<String, Object>> testeEmo;
HashMap<String, HashMap<String, Double>> testepower;
HashMap<String, Object> testewaves;
HashMap<String, Object> testeactions;
HashMap<String, Object> testeexpressions;
HashMap<String, Object> testeinfo;
HashMap<String, Double> testetypes1;
HashMap<String, Double> testetypes2;
HashMap<String, Double> testetypes3;


testeEmo = new HashMap<String, HashMap<String, Object>>();
testepower = new HashMap<String, HashMap <String,Double>>();
testewaves = new HashMap<String, Object>();
testeactions = new HashMap<String, Object>();
testeexpressions = new HashMap<String, Object>();
testeinfo = new HashMap<String, Object>();
testetypes1 = new HashMap<String, Double>();
testetypes2 = new HashMap<String, Double>();
testetypes3 = new HashMap<String, Double>();

testeEmo.put("Waves", testewaves);
testeEmo.put("Actions", testeactions);
testeEmo.put("FacialExpressions", testeexpressions);
testeEmo.put("DeviceInfo", testeinfo);

testetypes1.put("Theta", 1.0);
testetypes1.put("Alpha", 1.0);
testetypes1.put("LowBeta", 1.0);
testetypes1.put("HighBeta", 1.0);
testetypes1.put("Gamma", 1.0);

testetypes2.put("Theta", 2.0);
testetypes2.put("Alpha", 2.0);
testetypes2.put("LowBeta", 2.0);
testetypes2.put("HighBeta", 2.0);
testetypes2.put("Gamma", 2.0);

testetypes3.put("Theta", 3.0);
testetypes3.put("Alpha", 3.0);
testetypes3.put("LowBeta", 3.0);
testetypes3.put("HighBeta", 3.0);
testetypes3.put("Gamma", 3.0);

testewaves.put("AF3", testetypes1);
testewaves.put("F7", testetypes2);
testewaves.put("F3", testetypes3);
testewaves.put("FC5", testetypes1);
testewaves.put("T7", testetypes2);
testewaves.put("P7", testetypes3);
testewaves.put("O1", testetypes1);
testewaves.put("O2", testetypes2);
testewaves.put("P8", testetypes3);
testewaves.put("T8", testetypes1);
testewaves.put("FC6", testetypes2);
testewaves.put("F4", testetypes3);
testewaves.put("F8", testetypes1);
testewaves.put("AF4", testetypes2);

testeactions.put("Action", "Right");
testeactions.put("ActionPower", 1.0);
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

testeinfo.put("BatteryLevel", 1.0);
testeinfo.put("WirelessSignal", 0.0);

//BrainLightFW.initMerge (1, testeEmo);

//for (int i = 0; i < BrainLightFW.getFinalDataArray().length; i++) {
//for (int j = 0; j < BrainLightFW.getFinalDataArray()[i].length; j++) {
//System.out.print(BrainLightFW.getFinalDataArray()[i][j]);
//}
//}

//criar array de arrays com toda a informação do dispositivo
//BrainLightFW.finalInfoFinal(1);
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
	 
	//BrainLightFW.initMerge(2, testeNeuro);
}

public void main(String[] args) {
testeNeurosky();
}

}
