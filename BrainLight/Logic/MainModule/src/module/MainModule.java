package module;


import Iedk.EmotivDevice;
import Iedk.Wave;
import Iedk.interfaces.EmotivInterface;
import analysis.Calculations;
import analysis.TypesOfCalculations;
import com.lgp5.Neurosky;
import history.write.WriteXLS_Emotiv;
import history.write.WriteXLS_NeuroSky;
import interfaces.HeadSetDataInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class MainModule {
    private int selectedLobe;//[0->frontal,1->temporal,2->pariental,3->occipital,4->total]
    private Boolean neverDelete;
    private String fileName;
    private Boolean record;
    private int days;
    protected BlockingQueue<Double[][]> queue = null;
    protected BlockingQueue<Double[][]> queue2 = null;
    private Neurosky neuroDevice;
    private EmotivDevice emotivDevice;
    private int wirelessSignal;
    public static Double[][] finalDataArray;
    public static Double[][] finalWavesArray;
    public static Double[][] finalRawData;
    private HashMap<String, HashMap<String, Object>> neuroData;
    private LinkedList<HashMap<String, HashMap<String, Object>>> sharedQ;
    private LinkedList<Double[][]> doubleQ;
    private boolean running;
    private int deviceNo;
    private static boolean calculate = true;
    ; //ver se as analises estao a correr, e se sim parar de enviar informaçao toda TODO

    //initialize the module and the threads that connect it to the interface
    public MainModule(int device, BlockingQueue<Double[][]> queue, BlockingQueue<Double[][]> queue2, Boolean neverDeleteP, int daysP) {
        selectedLobe = 4;
        neverDelete = neverDeleteP;
        days = daysP;
        if (!neverDelete) {
            deleteOldFiles(days);
        }
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E_yyyy_MM_dd_'at'_hh_mm_ss");
        this.fileName = ft.format(dNow);
        this.record = false;
        this.queue = queue;
        this.queue2 = queue2;
        sharedQ = new LinkedList<>();
        wirelessSignal = 0;
        new LinkedList<>();
        doubleQ = new LinkedList<>();
        running = true;
        deviceNo = device;
        if (device == 1) {


            EmotivInterface sendDataInterface;
            //confirmar
            sendDataInterface = new EmotivInterface() {

                @Override
                public void onReceiveData(HashMap<String, Object> data) {
                    initMerge(1, (HashMap<String, Object>) data.clone());

                    try {
                        queue.put(finalDataArray);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

                @Override
                public void onReceiveWavesData(HashMap<String, Wave> data) {
                    initGetWaves(1, data);
                    try {
                        queue2.put(finalWavesArray);
                        if (record) {
                            Date dNow = new Date();
                            SimpleDateFormat ftTime =
                                    new SimpleDateFormat("hh:mm:ss");
                            //System.out.println("ola");

                            final Object[][] bookData = {
                                    {ftTime.format(dNow), finalWavesArray[0][0], finalWavesArray[0][1],
                                            finalWavesArray[0][2], finalWavesArray[0][3], finalDataArray[2][2],
                                            finalDataArray[2][0], finalDataArray[2][1], finalDataArray[2][3],
                                            finalDataArray[2][4], finalDataArray[2][5], finalDataArray[1][2],
                                            finalDataArray[1][3], finalDataArray[1][0], finalDataArray[1][1],
                                            finalDataArray[3][1], finalDataArray[3][3], finalDataArray[3][4],
                                            finalDataArray[3][6], finalDataArray[3][8]},
                            };
                            WriteXLS_Emotiv.writeXLS(fileName, bookData);
                        } else {
                            Date dNow = new Date();
                            SimpleDateFormat ft =
                                    new SimpleDateFormat("E_yyyy_MM_dd_'at'_hh_mm_ss");
                            fileName = ft.format(dNow);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            };


            emotivDevice = new EmotivDevice(sendDataInterface);
        } else if (device == 2) {
            HeadSetDataInterface sendDataInterface;
            //confirmar
            sendDataInterface = new HeadSetDataInterface() {

                @Override
                public void onReceiveData(HashMap<String, HashMap<String, Object>> dataToSend) {
                    initMerge(2, (HashMap<String, Object>) dataToSend.clone());
                    try {
                        queue.put(finalDataArray);
                        if (record) {
                            Date dNow = new Date();
                            SimpleDateFormat ftTime =
                                    new SimpleDateFormat("hh:mm:ss");
                            final Object[][] bookData = {
                                    {ftTime.format(dNow), finalDataArray[0][0], finalDataArray[0][1],
                                            finalDataArray[0][2], finalDataArray[0][3],
                                            finalDataArray[0][4], finalDataArray[0][5],
                                            finalDataArray[0][6], finalDataArray[0][7],
                                            finalDataArray[1][0], finalDataArray[1][1],
                                            finalDataArray[2][0]},
                            };
                            WriteXLS_NeuroSky.writeXLS(fileName, bookData);
                        } else {
                            Date dNow = new Date();
                            SimpleDateFormat ft =
                                    new SimpleDateFormat("E_yyyy_MM_dd_'at'_hh_mm_ss");
                            fileName = ft.format(dNow);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

                @Override
                public void onReceiveRawData(HashMap<String, Integer> rawData) {
                    initGetRaw(2, rawData);
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

    public boolean getAllSensorsStatusOK(HashMap<String, HashMap<String, Object>> Obj) {
        if (deviceNo == 1) {

            Iterator iterator = Obj.get("ChannelQuality").entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry map = (Map.Entry) iterator.next();

                if ((map.getKey().equals("CMSChanQuality") && !map.getValue().equals(4))
                        || (map.getKey().equals("DRLChanQuality") && !map.getValue().equals(4))) {
                    return false;
                } else if ((Integer) map.getValue() < 2) {
                    return false;
                }
            }
            return true;
        }

        return true;
    }

    //starts recieving data
    public void receiveDeviceData() {

        Thread receiveDataThread = new Thread("ReceiveDeviceData") {
            public void run() {

                if (deviceNo == 1) {
                    Thread emotivThread = new Thread(emotivDevice);
                    emotivDevice.connectEmotiv();
                    emotivThread.run();
                } else if (deviceNo == 2) {
                    try {
                        neuroDevice.connect();
                        neuroDevice.run();
                    } catch (Exception e) {
                        System.out.println("NeuroSky is not connected");
                    }
                }
            }
        };
        receiveDataThread.setDaemon(true);
        receiveDataThread.start();

        //emoDevice.emotivDeviceDisconnect();

    }

    //called to disconnect the device
    public void deviceDisconnect() {

        if (deviceNo == 1) {
            emotivDevice.emotivDeviceDisconnect();
        } else if (deviceNo == 2) {
            neuroDevice.stopReceivingData();
            neuroDevice.disconnect();
        }

    }

    //called to stop recieving data
    public void stopReceiving() {

        running = false;
    }

    //recieves an int array of arrays of size 3, with the first element being an array with all the indexes of the sensors read
//by the calculations, the second being an array with the indexes of all the types of calculations to be made, and the third
//an array with a single int that is the time the program saves and processes the data before doing the calculations
    public static void calculate(int[][] infoArray) {
        Float[][] floatArrayEmotiv;
        Double[] finalArray;
        float[][] finalArray1;
        finalArray = new Double[12];
        int[][] waveType = new int[3][];
        Vector<Float[][]> vector = new Vector<Float[][]>();
        TypesOfCalculations[] types = new TypesOfCalculations[infoArray[1].length];

        Double[][] tempArray;
        tempArray = new Double[14][36];
        for (Double[] row : tempArray)
            Arrays.fill(row, 0.0);

        while (calculate == true) {
            /*Double[][] testArray;
                        testArray = new Double[][]{
                                        {1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                        {1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 34.0, 35.0, 62.0, 12.0, 12.0},
                                       {43.0, 52.0, 53.0, 9.0, 25.0, 34.0, 35.0, 62.0, 12.0, 12.0, 53.0, 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 20.0, 6.0, 7.0, 3.0, 5.0, 3.0, 2.0, 3.0, 5.0, 6.0, 8.0, 24.0, 74.0, 73.0, 34.0, 35.0, 62.0, 12.0, 12.0}};
*/
            int count = 0;
            for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(infoArray[2][0]); stop > System.nanoTime(); )
                //Para testar deve trocar o array finalWavesArray para testArray nas 3 proximas ocorrencias.
                if (tempArray == finalWavesArray)
                    continue;
                else {
                    vector.add(averageOfInstance(infoArray[0], finalWavesArray));
                    tempArray = finalWavesArray;
                }

//makes types[] hold all the types of calculations to be made 
            for (int i = 0; i < infoArray[1].length; i++) {
                switch (infoArray[1][i]) {
                    case 0:
                        types[i] = TypesOfCalculations.WAVELENGTH;
                        break;
                    case 1:
                        types[i] = TypesOfCalculations.WAVENUMBER;
                        break;
                    case 2:
                        types[i] = TypesOfCalculations.ANG_WAVENUMBER;
                        break;
                    case 3:
                        types[i] = TypesOfCalculations.ANG_FREQUENCY;
                        break;
                    case 4:
                        types[i] = TypesOfCalculations.PERIOD;
                        break;
                    case 5:
                        types[i] = TypesOfCalculations.AMPLITUDE;
                        break;
                    case 6:
                        types[i] = TypesOfCalculations.MAX_AMPLITUDE;
                        break;
                    case 7:
                        types[i] = TypesOfCalculations.MIN;
                        break;
                    case 8:
                        types[i] = TypesOfCalculations.MAX;
                        break;
                    case 9:
                        types[i] = TypesOfCalculations.MEAN;
                        break;
                    case 10:
                        types[i] = TypesOfCalculations.MODE;
                        break;
                    case 11:
                        types[i] = TypesOfCalculations.MEDIAN;
                        break;
                    case 12:
                        types[i] = TypesOfCalculations.XFORMAXY;
                        break;
                    case 13:
                        types[i] = TypesOfCalculations.XFORMINY;
                        break;
                    default:
                        break;
                }
            }
            floatArrayEmotiv = finalFloatArray(vector);
            int width = floatArrayEmotiv.length;
            int height = floatArrayEmotiv[0].length;
            float[][] floatmatrix = new float[width][height];
            for (int w = 0; width > w; w++) {
                for (int h = 0; height > h; h++) {
                    floatmatrix[w][h] = (float) floatArrayEmotiv[w][h];
                }
            }
            System.out.println(Arrays.deepToString(floatmatrix));
            Calculations Calc = new Calculations(floatmatrix, types);
            System.out.println(Calc.getResult());
        }

    }

    //calculates the average between an array of floats
    public Float media(Float[] indexArray) {
        int x = 0;
        for (int i = 0; i < indexArray.length; i++)
            x += indexArray[i];
        return (float) (x / indexArray.length);
    }

    //transforms a Vector<Float[][]> into a Float[][]
    public static Float[][] finalFloatArray(Vector<Float[][]> vector) {
        Float[][] finalFloat = new Float[31][2];

        float y = 0;
        for (int j = 0; j < 31; j++) {
            for (int i = 0; i < vector.size(); i++) {
                y += (vector.get(i)[j][1]) / vector.size();
            }


            finalFloat[j][0] = (float) j;
            finalFloat[j][1] = y;
            y = 0;
        }
        return finalFloat;
    }

    public static Float[][] averageOfInstance(int[] sensorIdentifiers, Double[][] finalArray) {
        Float[][] dataToAnalyse = new Float[31][2];

        for (Float[] row : dataToAnalyse)
            Arrays.fill(row, 0f);

        for (int sensor : sensorIdentifiers) {
            for (int i = 5; i < 36; i++) {
                dataToAnalyse[i - 5][0] = (float) i;
                dataToAnalyse[i - 5][1] += (finalArray[sensor][i].floatValue() / sensorIdentifiers.length);
            }
        }
        return dataToAnalyse;
    }

    //gives the average of each sensor on the array int[] sensorIdentifiers, based on the Double[][] finalArray
    public static Double[] averageOfInstanceDouble(int[] sensorIdentifiers, Double[][] finalArray) {
        Double[] dataToAnalyse = new Double[31];
        for (int j = 0; j < dataToAnalyse.length; j++)
            dataToAnalyse[j] = 0.0;
        for (int sensor : sensorIdentifiers) {
            for (int i = 5; i < 36; i++) {
                dataToAnalyse[i - 5] += (finalArray[sensor][i] / sensorIdentifiers.length);
            }
        }

        return dataToAnalyse;
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

    //tool to see more easily the keys for the emotiv and neurosky hashmaps
    public static String[][] finalInfoFinal(int device) {
        if (device == 1) {
            String[][] finalInfo = new String[][]{{"BatteryLevel", "WirelessSignal", "Timestamp", "SignalQuality"},
                    {"LookingLeft", "LookingRight", "LookingDown", "LookingUp"},
                    {"LeftWink", "RightWink", "Blink", "EyesOpen", "SmileExtension", "ClenchExtension", "LowerFaceExpression",
                            "LowerFaceExpressionPower", "UpperFaceExpression", "UperFaceExpressionPower"},
                    {"EngagementActive", "Engagement", "ExcitementActive", "ExcitementLongTime", "ExcitementShortTime", "FrustationActive",
                            "Frustation", "MeditationActive", "Meditation"}
            };
            return finalInfo;
        } else if (device == 2) {
            String[][] finalInfo = new String[][]{{"FP1"},
                    {"Delta", "Theta", "LowAlpha", "HighAlpha", "LowBeta", "HighBeta", "LowGamma", "MidGamma"},
                    {"Attention", "Meditation"},
                    {"poor_signal"}
            };
            return finalInfo;
        } else return null;
    }

    //initializes the process of recieving the raw data from neurosky
    public static void initGetRaw(int device, HashMap<String, Integer> data) {
        Double[][] finalRaw = new Double[100][100];

        if (device == 2) {
            HashMap<String, Integer> neuroData;
            if (data instanceof HashMap) {
                neuroData = (HashMap<String, Integer>) data;
            } else {
                System.out.println("Wrong type of data for neurosky device");
                return;
            }
            finalRaw[0][0] = (Double) convertVolts((float) neuroData.get("Raw"));
            finalRaw[0][0] = finalRaw[0][0] * 100000;//Micro de Volt
            //finalRaw[0] = Double.parseDouble(neuroData.get("Raw").toString());
        }
        finalRawData = finalRaw;
    }

    //initializes the process of recieving the waves info from emotiv
    public void initGetWaves(int device, HashMap<String, Wave> data) {
        String[] finalInfo = new String[]{"AF3", "F7", "F3", "FC5", "T7", "P7",
                "O1", "O2", "P8", "T8", "FC6", "F4", "F8", "AF4"};

        Double[][] finalData;
        Double[][] allData;

        if (device == 1) {

            HashMap<String, Wave> emotivData;
            if (data instanceof HashMap) {
                emotivData = (HashMap<String, Wave>) data;
            } else {
                System.out.println("Wrong type of data for emotiv device");
                return;
            }

            allData = new Double[14][36];
            for (int i = 0; i < allData.length; i++) {
                try {
                    allData[i][0] = emotivData.get(finalInfo[i]).getTheta();
                    allData[i][1] = emotivData.get(finalInfo[i]).getDelta();
                    allData[i][2] = emotivData.get(finalInfo[i]).getAlpha();
                    allData[i][3] = emotivData.get(finalInfo[i]).getBeta();
                    allData[i][4] = (double) emotivData.get(finalInfo[i]).getSignalQuality();
                    for (int k = 0; k < 31; k++) {
                        allData[i][k + 5] = emotivData.get(finalInfo[i]).getFreqVals().get(k);
                    }
                } catch (Exception e) {

                }
            }
            finalData = new Double[3][];
            finalData[0] = new Double[4];// 4 waves
            finalData[1] = new Double[14];// 1 signal Quality per Sensor
            finalData[2] = new Double[31];// all 31 average frequency
            Double sum = 0.0;
            Double[] finalData3;
            switch (selectedLobe) {
                case 0:/*Lobo Frontal*/
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)F(.*)"))
                            sum += emotivData.get(finalInfo[k]).getAlpha();
                    }
                    finalData[0][0] = (sum / 8);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)F(.*)"))
                            sum += emotivData.get(finalInfo[k]).getBeta();
                    }
                    finalData[0][1] = (sum / 8);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)F(.*)"))
                            sum += emotivData.get(finalInfo[k]).getDelta();
                    }
                    finalData[0][2] = (sum / 8);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)F(.*)"))
                            sum += emotivData.get(finalInfo[k]).getTheta();
                    }
                    finalData[0][3] = (sum / 8);
                    int[] frontal = {0, 1, 2, 3, 10, 11, 12, 13};
                    finalData3 = averageOfInstanceDouble(frontal, allData);
                    for (int j = 0; j < finalData3.length; j++) {
                        finalData[2][j] = finalData3[j];
                    }
                    break;

                case 1:/*Lobo Temporal*/
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)T(.*)"))
                            sum += emotivData.get(finalInfo[k]).getAlpha();
                    }
                    finalData[0][0] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)T(.*)"))
                            sum += emotivData.get(finalInfo[k]).getBeta();
                    }
                    finalData[0][1] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)T(.*)"))
                            sum += emotivData.get(finalInfo[k]).getDelta();
                    }
                    finalData[0][2] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)T(.*)"))
                            sum += emotivData.get(finalInfo[k]).getTheta();
                    }
                    finalData[0][3] = (sum / 2);
                    int[] temporal = {4, 9};
                    finalData3 = averageOfInstanceDouble(temporal, allData);
                    for (int j = 0; j < finalData3.length; j++) {
                        finalData[2][j] = finalData3[j];
                    }
                    break;

                case 2:/*Lobo Parietal*/
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)P(.*)"))
                            sum += emotivData.get(finalInfo[k]).getAlpha();
                    }
                    finalData[0][0] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)P(.*)"))
                            sum += emotivData.get(finalInfo[k]).getBeta();
                    }
                    finalData[0][1] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)P(.*)"))
                            sum += emotivData.get(finalInfo[k]).getDelta();
                    }
                    finalData[0][2] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)P(.*)"))
                            sum += emotivData.get(finalInfo[k]).getTheta();
                    }
                    finalData[0][3] = (sum / 2);
                    int[] parietal = {5, 8};
                    finalData3 = averageOfInstanceDouble(parietal, allData);
                    for (int j = 0; j < finalData3.length; j++) {
                        finalData[2][j] = finalData3[j];
                    }
                    break;

                case 3:/*Lobo Occipital*/
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)O(.*)"))
                            sum += emotivData.get(finalInfo[k]).getAlpha();
                    }
                    finalData[0][0] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)O(.*)"))
                            sum += emotivData.get(finalInfo[k]).getBeta();
                    }
                    finalData[0][1] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)O(.*)"))
                            sum += emotivData.get(finalInfo[k]).getDelta();
                    }
                    finalData[0][2] = (sum / 2);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        if (finalInfo[k].matches("(.*)O(.*)"))
                            sum += emotivData.get(finalInfo[k]).getTheta();
                    }
                    finalData[0][3] = (sum / 2);
                    int[] occipital = {6, 7};
                    finalData3 = averageOfInstanceDouble(occipital, allData);
                    for (int j = 0; j < finalData3.length; j++) {
                        finalData[2][j] = finalData3[j];
                    }
                    break;

                case 4:/*Todos os lobos*/
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        try {
                            sum += emotivData.get(finalInfo[k]).getAlpha();
                        } catch (Exception e) {
                            //System.out.println(e.getStackTrace());
                        }
                    }
                    finalData[0][0] = (sum / 14);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        try {
                            sum += emotivData.get(finalInfo[k]).getBeta();
                        } catch (Exception e) {
                            //System.out.println(e.getStackTrace());
                        }
                    }
                    finalData[0][1] = (sum / 14);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        try {
                            sum += emotivData.get(finalInfo[k]).getDelta();
                        } catch (Exception e) {
                            // System.out.println(e.getStackTrace());
                        }
                    }
                    finalData[0][2] = (sum / 14);
                    sum = 0.0;
                    for (int k = 0; k < finalInfo.length; k++) {
                        try {
                            sum += emotivData.get(finalInfo[k]).getTheta();
                        } catch (Exception e) {
                            //System.out.println(e.getStackTrace());
                        }
                    }
                    finalData[0][3] = (sum / 14);

                    int[] total = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
                    finalData3 = averageOfInstanceDouble(total, allData);
                    for (int j = 0; j < finalData3.length; j++) {
                        finalData[2][j] = finalData3[j];
                    }
                    break;
            }
            for (int k = 0; k < finalInfo.length; k++) {
                //System.out.println(k+" "+emotivData.get(finalInfo[k]).getSignalQuality());
                finalData[1][k] = (double) emotivData.get(finalInfo[k]).getSignalQuality();
            }
            /*
            int[] frontal = {0, 1, 2, 3, 10, 11, 12, 13};
            int[] temporal = {4, 9};
            int[] parietal = {5, 8};
            int[] occipital = {6, 7};
            int[] total = {0, 1, 2, 3,4,5,6,7,8,9, 10, 11, 12, 13};

            for (int i = 0; i < finalData2.length; i++) {
                finalData2[i][0] = emotivData.get(finalInfo[i]).getTheta();
                finalData2[i][1] = emotivData.get(finalInfo[i]).getDelta();
                finalData2[i][2] = emotivData.get(finalInfo[i]).getAlpha();
                finalData2[i][3] = emotivData.get(finalInfo[i]).getBeta();
                finalData2[i][4] = (double) emotivData.get(finalInfo[i]).getSignalQuality();
                for (int k = 0; k <= 30; k++) {
                    finalData2[i][k + 5] = emotivData.get(finalInfo[i]).getFreqVals().get(k);
                }
            }

            Double[][] finalWavesArray2 = finalData2;
            Double[] finalData3 = averageOfInstanceDouble(frontal, finalWavesArray2);
            Double[] finalData4 = averageOfInstanceDouble(temporal, finalWavesArray2);
            Double[] finalData5 = averageOfInstanceDouble(parietal, finalWavesArray2);
            Double[] finalData6 = averageOfInstanceDouble(occipital, finalWavesArray2);
            Double[] finalData7 = averageOfInstanceDouble(total, finalWavesArray2);
            for (int j = 0; j < 6; j++) {
                for (int i = 0; i < 31; i++) {
                    if(j==5)
                        finalData[j][i]=finalData3[j];
                    if(j==6)
                        finalData[j][i]=finalData4[j];
                    if(j==7)
                        finalData[j][i]=finalData5[j];
                    if(j==8)
                        finalData[j][i]=finalData6[j];
                    if(j==8)
                        finalData[j][i]=finalData6[j];
                }
            }*/
            finalWavesArray = finalData;
        }
    }

    //initializes the process of recieving the data from either emotiv or neurosky, according to the device number provided
    public static void initMerge(int device, HashMap<String, Object> data) {
        String[][] finalInfo;
        Double[][] finalData;
        if (device == 1) {
            HashMap<String, Object> emotivData;
            if (data instanceof HashMap) {
                emotivData = (HashMap<String, Object>) data;
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

            finalData[0] = new Double[4];
            finalData[1] = new Double[4];
            finalData[2] = new Double[10];
            finalData[3] = new Double[9];
            for (int i = 0; i < finalData.length; i++) {
                if (i == 0) {
                    for (int k = 0; k < finalData[i].length; k++) {
                        if (k != 2)
                            finalData[i][k] = ((Integer) deviceInfo.get(finalInfo[0][k])).doubleValue();
                        else finalData[i][k] = ((Float) deviceInfo.get(finalInfo[0][k])).doubleValue();
                    }

                } else if (i == 1) {
                    for (int k = 0; k < finalData[i].length; k++) {
                        finalData[i][k] = ((Integer) actions.get(finalInfo[1][k])).doubleValue();
                    }
                } else if (i == 2) {
                    for (int k = 0; k < finalData[i].length; k++) {

                        if (k == 8 || k == 6)
                            finalData[i][k] = reverseExpression((String) (expressions.get(finalInfo[2][k])));
                        else if (k == 5 || k == 4 || k == 9 || k == 7)
                            finalData[i][k] = ((Float) expressions.get(finalInfo[2][k])).doubleValue();
                        else finalData[i][k] = ((Integer) expressions.get(finalInfo[2][k])).doubleValue();

                    }
                } else if (i == 3)
                    for (int k = 0; k < finalData[i].length; k++) {
                        if (k == 1 || k == 3 || k == 4 || k == 6 || k == 8)
                            if (k == 1 || k == 8 || k == 6 || k == 4 || k == 3)
                                finalData[i][k] = ((Float) affective.get(finalInfo[3][k])).doubleValue();
                            else finalData[i][k] = ((Integer) affective.get(finalInfo[3][k])).doubleValue();

                    }

            }
            finalDataArray = finalData;
        } else if (device == 2) {
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
            finalData[0] = new Double[8];
            finalData[1] = new Double[2];
            finalData[2] = new Double[1];
            //finalData[3]= new Double[]{(double) 2};

            for (int i = 1; i < finalInfo.length; i++) {
                for (int k = 0; k < finalInfo[i].length; k++) {
                    if (i == 1) {
                        finalData[i - 1][k] = convertVolts((Float) neuroData.get("Waves").get(finalInfo[i][k]));
                        finalData[i - 1][k] = finalData[i - 1][k] * 10;//Decimo de Volt
                    } else {
                        finalData[i - 1][k] = convertToDouble((Integer) neuroData.get("Waves").get(finalInfo[i][k]));
                    }
                }
            }
            finalDataArray = finalData;
        }


    }

    //covnerts a value to volts
    public static double convertVolts(Float value) {
        value = (float) ((value * (1.8 / 4096)) / 2000);
        return value;
    }

    //covnerts an integer to double
    public static double convertToDouble(Integer value) {
        if (value != null)
            return value;
        else return -1;
    }

    //converts a float to double
    public static double convertToDoubleFloat(Float value) {
        if (value != null)
            return value;
        else return -1;
    }

    //determines which action emotiv is recieveing
    public static Double reverseAction(String actStr) {

        Double act;

        if (actStr == "Neural") {
            act = 1.0;
        } else if (actStr == "Push") {
            act = 2.0;
        } else if (actStr == "Pull") {
            act = 4.0;
        } else if (actStr == "Lift") {
            act = 8.0;
        } else if (actStr == "Drop") {
            act = 16.0;
        } else if (actStr == "Left") {
            act = 32.0;
        } else if (actStr == "Right") {
            act = 64.0;
        } else if (actStr == "RotateLeft") {
            act = 128.0;
        } else if (actStr == "RotateRight") {
            act = 256.0;
        } else if (actStr == "RotateClockwise") {
            act = 512.0;
        } else if (actStr == "RotateCounter-Clockwise") {
            act = 1024.0;
        } else if (actStr == "RotateForward") {
            act = 2048.0;
        } else if (actStr == "RotateReverse") {
            act = 4096.0;
        } else if (actStr == "Disappear") {
            act = 8192.0;
        } else act = 0.0;

        return act;

    }

    //determines which expression emotiv is recieveing
    public static Double reverseExpression(String actStr) {

        Double act;

        if (actStr == "Smile") {
            act = 128.0;
        } else if (actStr == "Clench") {
            act = 256.0;
        } else if (actStr == "SmirkLeft") {
            act = 1024.0;
        } else if (actStr == "Laught") {
            act = 512.0;
        } else if (actStr == "RaiseBrow") {
            act = 3.0;
        } else if (actStr == "FurrowBrow") {
            act = 64.0;
        } else if (actStr == "error") {
            act = 128.0;
        } else act = 0.0;

        return act;

    }

    //deletes files with more then X days on the history folder since last time it was edited
    public static void deleteOldFiles(int days) {
        String str = (System.getProperty("user.dir")).replaceAll("BrainLight", "") + "\\history";
        File dir = new File(str);
        if (dir.exists()) {
            try {
                purgeOldFiles(dir, days);
            } catch (Exception e) {
                System.err.println("History folder not in correct directory (" + str + ")");
            }
        }
    }

    //deletes all files on the history folder
    public static void cleanHistory() {
        String str = (System.getProperty("user.dir")) + "\\history";
        File dir = new File(str);
        if (dir.exists()) {
            try {
                purgeDirectory(dir);
            } catch (Exception e) {
                System.err.println("History folder not in correct directory (" + str + ")");
            }
        }
    }

    static void purgeOldFiles(File dir, int x) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) purgeDirectory(file);
            long diff = new Date().getTime() - file.lastModified();

            if (diff > x * 24 * 60 * 60 * 1000) {
                file.delete();
            }
        }
    }

    static void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
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

    public int getSelectedLobe() {
        return selectedLobe;
    }

    public void setSelectedLobe(int selectedLobe) {
        this.selectedLobe = selectedLobe;
    }
}
