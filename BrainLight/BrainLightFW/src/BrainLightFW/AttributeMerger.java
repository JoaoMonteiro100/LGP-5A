package BrainLightFW;

import java.util.HashMap;


public class AttributeMerger {

	private HashMap<String, HashMap<String, Object>> finalHash;

	
	
	public void initMerge (int device, HashMap<String, HashMap<String,Object>> data, Double[][] finalData)
	{
		String[][] finalInfo;
		
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
				{"Action","ActionPower","LookingLeft","LookingRight","LookingDown","LookingUp"}
			};
			HashMap<String, Object> waves = emotivData.get("Waves");
			HashMap<String, Object> expressions = emotivData.get("FacialExprenssions");
			HashMap<String,Object> actions = emotivData.get("Actions");
			int tam = finalInfo[0].length + 3;
			finalData = new Double[tam][];
			//definir o tamanho de cada parte do array
			for (int a = 0; a < tam; a++){
			if (a == tam-2)
				finalData[a]= new Double[0];
			else if (a == tam-1)
				finalData[a] = new Double[10];
			else if (a == tam)
				finalData[a] = new Double[6];
			else 
				finalData[a] = new Double[5];
			}
			
			for (int i = 0; i < tam; i++){
				if (i == tam-2)
					break;
				else if (i == tam - 1)
					{
					for(int k = 0; k < finalData[i].length; k++)
						{
						finalData[i][k]=(Double)(expressions.get(finalData[3][k]));
						}
					}
				else if (i == tam)
					{
					for(int k = 0; k < finalData[i].length; k++)
						{
						finalData[i][k]=(Double)(expressions.get(finalData[4][k]));
						}
					}
				else 
					for(int k = 0; k < finalData[i].length; k++)
						{
						finalData[i][k]=((HashMap<String,Double>) waves.get(finalData[0][i])).get(finalData[1][k]);
						}
			
			}
			
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
				{"Delta","Theta","LowAlpha","HighAlpha","LowBeta","HighBeta","LowGamma","HighGamma"},
				{"Attention","Meditation"},
				{"Blink"},
				{}
			};
			
			finalData = new Double[4][];
			finalData[0]= new Double[8];
			finalData[1]= new Double[2];
			finalData[2]= new Double[1];
			finalData[3] = new Double[0];
			
			for (int i = 0;i < finalData.length; i++){
				for (int k = 0;k < finalData[i].length; k++){
					finalData[i][k]=(Double) neuroData.get("Waves").get(finalData[i][k]);
				}
				}
			}
			
		
			
			};
	}
	
	
	/*
	@SuppressWarnings("unchecked")
	public void initMerge(int device, Object data) {

		finalHash = new HashMap<String, HashMap<String, Object>>();
		// emotiv
		if (device == 1) {
			HashMap<String, HashMap<String, Object>> emotivData;
			if (data instanceof HashMap) {
				emotivData= (HashMap<String, HashMap<String, Object>>) data;
			} else {
				System.out.println("Wrong type of data for emotiv device");
				return;
			}

			// 
			//emotivData.forEach((k,v) -> addDelta(v));
			
			HashMap<String, Object> waves = emotivData.get("Waves");
			// Cast is always necessary since waves is an hash of an hash but saved as an hash of object
			((HashMap<String,Double>) waves.get("AF3")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("F7")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("F3")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("FC5")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("T7")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("P7")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("O1")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("O2")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("P8")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("T8")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("FC6")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("F4")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("F8")).put("Delta",new Double(0));
			((HashMap<String,Double>) waves.get("AF4")).put("Delta",new Double(0));
			
			
			//Channel read by neurosky according to research
			HashMap<String,Object> fp1 = new HashMap<String,Object>();
			fill(fp1);
			waves.put("FP1", fp1);;
			
			
			//System.out.println("Delta: " + ((HashMap<String,Double>) waves.get("AF3")).get("Delta") );
			//System.out.println("Alpha: " + ((HashMap<String,Double>)waves.get("AF3")).get("Alpha") );
			//END WAVES
			
			
			
			finalHash = (HashMap<String, HashMap<String, Object>>) emotivData.clone();

		} else if (device == 2){

			HeadsetData neuroData;
			if (data instanceof HeadsetData) {
				neuroData = (HeadsetData) data;
			} else {
				System.out.println("Wrong type of data for neurosky device");
				return;
			}
			
			float alfa = (neuroData.alpha1 + neuroData.alpha2)/2;
			float gamma = (neuroData.gamma1 + neuroData.alpha2)/2;
			
			HashMap<String,HashMap<String,Object>> neuroHash = new HashMap<String,HashMap<String,Object>>() ;
			
			HashMap<String, Object> wavesTemp = new HashMap<String,Object>();
			
			
			neuroHash.put("Waves", wavesTemp);
			
			HashMap<String,Object> fp1 = new HashMap<String,Object>();
			HashMap<String,Object> af3 = new HashMap<String,Object>();
			HashMap<String,Object> f7 = new HashMap<String,Object>();
			HashMap<String,Object> f3 = new HashMap<String,Object>();
			HashMap<String,Object> fc5 = new HashMap<String,Object>();
			HashMap<String,Object> t7 = new HashMap<String,Object>();
			HashMap<String,Object> p7 = new HashMap<String,Object>();
			HashMap<String,Object> o1 = new HashMap<String,Object>();
			HashMap<String,Object> o2 = new HashMap<String,Object>();
			HashMap<String,Object> p8 = new HashMap<String,Object>();
			HashMap<String,Object> t8 = new HashMap<String,Object>();
			HashMap<String,Object> fc6 = new HashMap<String,Object>();
			HashMap<String,Object> f4 = new HashMap<String,Object>();
			HashMap<String,Object> f8 = new HashMap<String,Object>();
			HashMap<String,Object> af4 = new HashMap<String,Object>();
			
			fill(af3);
			fill(f7);
			fill(f3);
			fill(fc5);
			fill(t7);
			fill(p7);
			fill(o1);
			fill(o2);
			fill(p8);
			fill(t8);
			fill(fc6);
			fill(f4);
			fill(f8);
			fill(af4);
			
			//FP1 isn't filled with blank values
			wavesTemp.put("FP1", fp1);
			wavesTemp.put("AF3", af3);
			wavesTemp.put("F7", f7);
			wavesTemp.put("F3", f3);
			wavesTemp.put("FC5", fc5);
			wavesTemp.put("T7", t7);
			wavesTemp.put("P7", p7);
			wavesTemp.put("O1", o1);
			wavesTemp.put("O2", o2);
			wavesTemp.put("P8", p8);
			wavesTemp.put("T8", t8);
			wavesTemp.put("FC6", fc6);
			wavesTemp.put("F4", f4);
			wavesTemp.put("F8", f8);
			wavesTemp.put("AF4", af4);
			
			
			fp1.put("Alpha", alfa);
			fp1.put("LowBeta", neuroData.beta1);
			fp1.put("HighBeta", neuroData.beta2);
			fp1.put("Theta", neuroData.theta);
			fp1.put("Delta", neuroData.delta);
			fp1.put("Gamma", gamma);
			
			HashMap<String,Object> expressions = new HashMap<String,Object>();
	        HashMap<String,Object> actions = new HashMap<String,Object>();
	        HashMap<String,Object> deviceInfo = new HashMap<String,Object>();
	        HashMap<String,Object> chanQuality = new HashMap<String,Object>();
	        HashMap<String,Object> waves = new HashMap<String,Object>();
			
	        chanQuality.put("AF3ChanQuality",new Double (0));
	        chanQuality.put("AF4ChanQuality",new Double (0));
	        chanQuality.put("CMSChanQuality",new Double (0));
	        chanQuality.put("F3ChanQuality",new Double (0));
	        chanQuality.put("F4ChanQuality",new Double (0));
	        chanQuality.put("F7ChanQuality",new Double (0));
	        chanQuality.put("F8ChanQuality",new Double (0));
	        chanQuality.put("FC5ChanQuality",new Double (0));
	        chanQuality.put("FP1ChanQuality",new Double (0));
	        chanQuality.put("FC6ChanQuality",new Double (0));
	        chanQuality.put("DRLChanQuality",new Double (0));
	        chanQuality.put("FP2ChanQuality",new Double (0));
	        chanQuality.put("O1ChanQuality",new Double (0));
	        chanQuality.put("O2ChanQuality",new Double (0));
	        chanQuality.put("PzChanQuality",new Double (0));
	        chanQuality.put("P7ChanQuality",new Double (0));
	        chanQuality.put("P8ChanQuality",new Double (0));
	        chanQuality.put("T7ChanQuality",new Double (0));
	        chanQuality.put("T8ChanQuality",new Double (0));
	        
	        expressions.put("LeftWink",new Double (0));
	        expressions.put("RightWink",new Double (0));
	        //TODO guardar valor do blink
	        expressions.put("Blink",new Double (0));
	        expressions.put("EyesOpen",new Double (0));
	        expressions.put("SmileExtension",new Double (0));
	        expressions.put("ClenchExtension",new Double (0));
	        expressions.put("LowerFaceExpression",new Double (0));
	        expressions.put("LowerFaceExpressionPower",new Double (0));
	        expressions.put("UperFaceExpression",new Double (0));
	        expressions.put("UperFaceExpressionPower",new Double (0));

	        
	        actions.put("Action",new Double (0));
	        actions.put("ActionPower",new Double (0));
	        actions.put("LookingLeft",new Double (0));
	        actions.put("LookingRight",new Double (0));
	        actions.put("LookingDown",new Double (0));
	        actions.put("LookingUp",new Double (0));
	        
	        deviceInfo.put("BatteryLevel", new Double (0));
	        deviceInfo.put("WirelessSignal",new Double (0));
	        
	        
	        
	        waves = (HashMap<String, Object>) wavesTemp.clone();
	        
	        finalHash.put("FacialExprenssions",expressions);
	        finalHash.put("Actions",actions);
	        finalHash.put("Waves",waves);
	        finalHash.put("DeviceInfo",deviceInfo);
	        finalHash.put("ChannelQuality",chanQuality);   
	        
		}
		
	}
	
	
	private void fill(HashMap<String, Object> channel) {
		channel.put("Alpha", new Double(0));
		channel.put("LowBeta", new Double(0));
		channel.put("HighBeta", new Double(0));
		channel.put("Gamma", new Double(0));
		channel.put("Theta", new Double(0));
		channel.put("Delta", new Double(0));
	}

/*
	//For testing
	
	
	/*public static void main(String[] args) {
		
		HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();
		
		HashMap<String, HashMap<String, Double>> wavesTemp = new HashMap<String, HashMap<String, Double>>();

		HashMap<String, Double> d1 = new HashMap<String,Double>();
		d1.put("Alpha",new Double(1));
        d1.put("LowBeta",new Double(2));
        d1.put("HighBeta",new Double(3));
        d1.put("Gamma",new Double(4));
        d1.put("Theta",new Double(5));
        
        wavesTemp.put("AF3",d1);
        wavesTemp.put("F7",d1);
        HashMap<String, Object> waves = (HashMap<String,Object>) wavesTemp.clone();
        data.put("Waves", waves);
        AttributeMerger merger = new AttributeMerger();
        merger.initMerge(1, data);
        
        
		
	}*/
	


