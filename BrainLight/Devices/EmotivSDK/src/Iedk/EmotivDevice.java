package Iedk;

import Iedk.interfaces.EmotivInterface;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

import java.util.HashMap;

import static Iedk.Edk.EE_DataChannels_t.*;


public class EmotivDevice implements Runnable {

    private EmotivInterface emotivInterface;
    private Pointer emotivEvent;
    private Pointer emotivState;
    private IntByReference userID, batteryLevelStatus, maxBatteryLevel, contactQualArray;

    DoubleByReference alpha, low_beta, high_beta, gamma, theta;
    private boolean receiveDataBool, onStateChanged, readytocollect;
    float timestamp, secs, smileExtentStatus, lowerFaceActionStatus, lowerFaceActionStatusPower, uperFaceActionStatus, uperFaceActionStatusPower, clenchExtentStatus, engagementBoredomScore, excitementLongScore, excitementShortScore,
            frustationScore, meditationScore;
    private int wirelessSignalStatus, nquality;
    Pointer contactQualityP, hData;
    private int EE_CHAN_CMS, EE_CHAN_DRL, EE_CHAN_FP1, EE_CHAN_AF3, EE_CHAN_F7, EE_CHAN_F3, EE_CHAN_FC5, EE_CHAN_T7, EE_CHAN_P7,
            EE_CHAN_O1, EE_CHAN_O2, EE_CHAN_P8, EE_CHAN_T8, EE_CHAN_FC6, EE_CHAN_F4, EE_CHAN_F8, EE_CHAN_AF4, EE_CHAN_FP2, blinkStatus, leftWinkStatus,
            rightWinkStatus, eyesOpenStatus, lookingDownStatus, lookingUpStatus, lookingLeftStatus, lookingRightStatus, isEngActiv,isExcitementActiv,
            isFrustActiv, isMeditationActiv;

    Channel Counter, AF3, F7, F3, FC5, T7, P7, O1, O2, P8, T8, FC6, F4, F8, AF4;
    private double hamming[];
    int minFre, maxFre, nSamples;
    IntByReference nSamplesTaken;

    int[] f;

    // Declare the hashmaps to send to the interface
    HashMap<String, Object> deviceInfoMap;
    HashMap<String, Object> actionsMap;
    HashMap<String, Object> expressionsMap;
    HashMap<String, Object> affectivemap;
    HashMap<String, Wave> channelsAverageBandPowers;



    Edk.EE_DataChannels_t targetChannelList[] = {
            ED_COUNTER,    //0
            ED_AF3, ED_F7, ED_F3, ED_FC5, ED_T7, //1--5
            ED_P7, ED_O1, ED_O2, ED_P8, ED_T8,   //6--10
            ED_FC6, ED_F4, ED_F8, ED_AF4, ED_GYROX, ED_GYROY, ED_TIMESTAMP, //11-17
            ED_FUNC_ID, ED_FUNC_VALUE, ED_MARKER, ED_SYNC_SIGNAL    //18--21
    };


    public EmotivDevice() {
    }


    public EmotivDevice(EmotivInterface emotivInterface) {
        this.emotivInterface = emotivInterface;
    }


    @Override
    public void run() {
        connectEmotiv();
        init();

        receiveDataEmotiv();
    }


    public void connectEmotiv() {
        emotivEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
        emotivState = Edk.INSTANCE.EE_EmoStateCreate();

        if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
            System.out.println("Emotiv start up failed");
            return;
        }/* else
            System.out.println("Emotiv connected");*/
    }

    public void emotivDeviceDisconnect() {
        Edk.INSTANCE.EE_EngineDisconnect();
        Edk.INSTANCE.EE_EmoStateFree(emotivState);
        Edk.INSTANCE.EE_EmoEngineEventFree(emotivEvent);
        System.out.println("Disconnected");
    }


    private void init() {
        userID = new IntByReference(0);
        receiveDataBool = true;
        batteryLevelStatus = new IntByReference(0);
        maxBatteryLevel = new IntByReference(0);
        contactQualArray = new IntByReference(0);
        onStateChanged = false;
        readytocollect = false;
        timestamp = 0;
        wirelessSignalStatus = 0;
        nquality = EmoState.INSTANCE.ES_GetNumContactQualityChannels(emotivState);
        contactQualityP = new Memory(4 * nquality);
        contactQualArray.setPointer(contactQualityP);
        EE_CHAN_CMS = 0;
        EE_CHAN_DRL = 0;
        EE_CHAN_FP1 = 0;
        EE_CHAN_AF3 = 0;
        EE_CHAN_F7 = 0;
        EE_CHAN_F3 = 0;
        EE_CHAN_FC5 = 0;
        EE_CHAN_T7 = 0;
        EE_CHAN_P7 = 0;
        EE_CHAN_O1 = 0;
        EE_CHAN_O2 = 0;
        EE_CHAN_P8 = 0;
        EE_CHAN_T8 = 0;
        EE_CHAN_FC6 = 0;
        lookingDownStatus = 0;
        lookingUpStatus = 0;
        lookingLeftStatus = 0;
        lookingRightStatus = 0;
        isEngActiv=0;
        EE_CHAN_F4 = 0;
        EE_CHAN_F8 = 0;
        EE_CHAN_AF4 = 0;
        EE_CHAN_FP2 = 0;
        blinkStatus = 0;
        leftWinkStatus = 0;
        rightWinkStatus = 0;
        eyesOpenStatus = 0;
        smileExtentStatus = 0.0f;
        lowerFaceActionStatus = 0.0f;
        lowerFaceActionStatusPower = 0.0f;
        uperFaceActionStatus = 0.0f;
        uperFaceActionStatusPower = 0.0f;
        clenchExtentStatus = 0.0f;

        hamming = new double[128];
        minFre = 0;
        maxFre = 40;
        nSamples = 128;
        Counter = new Channel();
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

        isExcitementActiv=0; isFrustActiv=0; isMeditationActiv=0;
        engagementBoredomScore=0.0f; excitementLongScore=0.0f; excitementShortScore=0.0f; frustationScore=0.0f; meditationScore=0.0f;


        deviceInfoMap = new HashMap<>();
        actionsMap = new HashMap<>();
        expressionsMap = new HashMap<>();
        channelsAverageBandPowers = new HashMap<>();
        affectivemap = new HashMap<>();

        f = new int[18];

        secs = 1;
        nSamplesTaken = new IntByReference(0);
        Hamming(128);
        hData = Edk.INSTANCE.EE_DataCreate();
        Edk.INSTANCE.EE_DataSetBufferSizeInSec(secs);
    }

    private boolean eventTypeCheck(int eventType) {
        switch (eventType) {
            case 0x0010:
                //  System.out.println("User added");
                Edk.INSTANCE.EE_DataAcquisitionEnable(userID.getValue(), true);
                readytocollect = true;
                break;
            case 0x0020:
                // System.out.println("User removed");
                readytocollect = false;        //just single connection
                break;
            case 0x0040:
                onStateChanged = true;
                Edk.INSTANCE.EE_EmoEngineEventGetEmoState(emotivEvent, emotivState);
                break;
            default:
                break;
        }

        if (readytocollect && onStateChanged)
            return true;
        else
            return false;
    }

    void Hamming(int length) {
        for (int i = 0; i < 128; i++)
            hamming[i] = 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (length - 1));
    }


    public void receiveDataEmotiv() {
        boolean newData;
        int state;
        int emotivHeadsetOn = 0;
        while (receiveDataBool) {
            newData = false;
            state = Edk.INSTANCE.EE_EngineGetNextEvent(emotivEvent);

            //new event to handle
            if (state == EdkErrorCode.EDK_OK.ToInt()) {
                int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(emotivEvent);
                Edk.INSTANCE.EE_EmoEngineEventGetUserId(emotivEvent, userID);
                newData = eventTypeCheck(eventType);

                if (eventType == Edk.EE_Event_t.EE_EmoStateUpdated.ToInt()) {
                    timestamp = EmoState.INSTANCE.ES_GetTimeFromStart(emotivState);
                    emotivHeadsetOn = EmoState.INSTANCE.ES_GetHeadsetOn(emotivState);

                    if (emotivHeadsetOn == 1) {
                        //wireless status
                        wirelessSignalStatus = EmoState.INSTANCE.ES_GetWirelessSignalStatus(emotivState);
                        //bat status
                        EmoState.INSTANCE.ES_GetBatteryChargeLevel(emotivState, batteryLevelStatus, maxBatteryLevel);

                        getAffectiv();
                        getEmotivLookingdDirections();
                        getEmotivFacialExpression();
                        getWaves();
                        getSensorsContactQuality();
                    }

                    channelsAverageBandPowers.clear();
                    calculateDFT();
                    emotivInterface.onReceiveWavesData(channelsAverageBandPowers);

                    sendDataToInterface();
                }
            }
        }
    }

    private void getAffectiv(){

        isEngActiv = EmoState.INSTANCE.ES_AffectivIsActive(emotivState,EmoState.EE_AffectivAlgo_t.AFF_ENGAGEMENT_BOREDOM.ToInt());

        if(isEngActiv ==1) {

            engagementBoredomScore = EmoState.INSTANCE.ES_AffectivGetEngagementBoredomScore(emotivState);
        }

        isExcitementActiv = EmoState.INSTANCE.ES_AffectivIsActive(emotivState,EmoState.EE_AffectivAlgo_t.AFF_EXCITEMENT.ToInt());

        if(isExcitementActiv ==1) {

            excitementLongScore = EmoState.INSTANCE.ES_AffectivGetExcitementLongTermScore(emotivState);
            excitementShortScore = EmoState.INSTANCE.ES_AffectivGetExcitementShortTermScore(emotivState);
        }

        isFrustActiv = EmoState.INSTANCE.ES_AffectivIsActive(emotivState,EmoState.EE_AffectivAlgo_t.AFF_FRUSTRATION.ToInt());

        if(isFrustActiv ==1) {

            frustationScore = EmoState.INSTANCE.ES_AffectivGetFrustrationScore(emotivState);

        }


        isMeditationActiv = EmoState.INSTANCE.ES_AffectivIsActive(emotivState,EmoState.EE_AffectivAlgo_t.AFF_MEDITATION.ToInt());

        if(isMeditationActiv ==1) {

            meditationScore = EmoState.INSTANCE.ES_AffectivGetMeditationScore(emotivState);

        }


    }


    private void getEmotivLookingdDirections() {
        //looking down status

        if (EmoState.INSTANCE.ES_ExpressivIsLookingDown(emotivState) == 1) {
            lookingDownStatus = 1;
        } else
            lookingDownStatus = 0;

        //looking up status
        if (EmoState.INSTANCE.ES_ExpressivIsLookingRight(emotivState) == 1) {
            lookingUpStatus = 1;
        } else
            lookingUpStatus = 0;

        //looking left status
        if (EmoState.INSTANCE.ES_ExpressivIsLookingLeft(emotivState) == 1) {
            lookingLeftStatus = 1;
        } else
            lookingLeftStatus = 0;

        //looking right status
        if (EmoState.INSTANCE.ES_ExpressivIsLookingRight(emotivState) == 1) {
            lookingRightStatus = 1;
        } else
            lookingRightStatus = 0;

    }

    private void getEmotivFacialExpression() {
        //blink state
        if (EmoState.INSTANCE.ES_ExpressivIsBlink(emotivState) == 1) {
            blinkStatus = 1;
        } else
            blinkStatus = 0;

        //left wink state
        if (EmoState.INSTANCE.ES_ExpressivIsLeftWink(emotivState) == 1) {
            leftWinkStatus = 1;
        } else
            leftWinkStatus = 0;

        //right wink state
        if (EmoState.INSTANCE.ES_ExpressivIsRightWink(emotivState) == 1) {
            rightWinkStatus = 1;
        } else
            rightWinkStatus = 0;

        //eyes open state
        if (EmoState.INSTANCE.ES_ExpressivIsEyesOpen(emotivState) == 1) {
            eyesOpenStatus = 1;
        } else
            eyesOpenStatus = 0;

        //get smile extention
        smileExtentStatus = EmoState.INSTANCE.ES_ExpressivGetSmileExtent(emotivState);

        //get lower face action
        lowerFaceActionStatus = EmoState.INSTANCE.ES_ExpressivGetLowerFaceAction(emotivState);
        lowerFaceActionStatusPower = EmoState.INSTANCE.ES_ExpressivGetLowerFaceActionPower(emotivState);

        //get uper face action
        uperFaceActionStatus = EmoState.INSTANCE.ES_ExpressivGetUpperFaceAction(emotivState);
        uperFaceActionStatusPower = EmoState.INSTANCE.ES_ExpressivGetUpperFaceActionPower(emotivState);

        //get clench extent
        clenchExtentStatus = EmoState.INSTANCE.ES_ExpressivGetClenchExtent(emotivState);
    }


    private void createDataHash(HashMap<String, Object> data) {
        deviceInfoMap.put("BatteryLevel", batteryLevelStatus.getValue());
        deviceInfoMap.put("WirelessSignal", wirelessSignalStatus);
        deviceInfoMap.put("Timestamp", timestamp);
        deviceInfoMap.put("SignalQuality", EE_CHAN_CMS);


        //actionsMap.put("Action", parseAction(emotivCurrentAction));
        //actionsMap.put("ActionPower", emotivCurrentActionPower);
        actionsMap.put("LookingLeft", lookingLeftStatus);
        actionsMap.put("LookingRight", lookingRightStatus);
        actionsMap.put("LookingDown", lookingDownStatus);
        actionsMap.put("LookingUp", lookingUpStatus);



        expressionsMap.put("LeftWink", leftWinkStatus);
        expressionsMap.put("RightWink", rightWinkStatus);
        expressionsMap.put("Blink", blinkStatus);
        expressionsMap.put("EyesOpen", eyesOpenStatus);
        expressionsMap.put("SmileExtension", smileExtentStatus);
        expressionsMap.put("ClenchExtension", clenchExtentStatus);
        expressionsMap.put("LowerFaceExpression", parseExpression(lowerFaceActionStatus));
        expressionsMap.put("LowerFaceExpressionPower", lowerFaceActionStatusPower);
        expressionsMap.put("UperFaceExpression", parseExpression(uperFaceActionStatus));
        expressionsMap.put("UperFaceExpressionPower", uperFaceActionStatusPower);

        affectivemap.put("EngagementActive",isEngActiv);
        affectivemap.put("Engagement",engagementBoredomScore);
        affectivemap.put("ExcitementActive",isExcitementActiv);
        affectivemap.put("ExcitementLongTime",excitementLongScore);
        affectivemap.put("ExcitementShortTime",excitementShortScore);
        affectivemap.put("FrustationActive",isFrustActiv);
        affectivemap.put("Frustation",frustationScore);
        affectivemap.put("MeditationActive",isMeditationActiv);
        affectivemap.put("Meditation",meditationScore);

        data.put("DeviceInfo", deviceInfoMap);
        data.put("Actions", actionsMap);
        data.put("FacialExpressions", expressionsMap);
        data.put("AffectiveValues",affectivemap);
    }


    public String parseAction(int act) {
        String actionstr;

        if (act == 1) {
            actionstr = "Neural";
        } else if (act == 2) {
            actionstr = "Push";
        } else if (act == 4) {
            actionstr = "Pull";
        } else if (act == 8) {
            actionstr = "Lift";
        } else if (act == 16) {
            actionstr = "Drop";
        } else if (act == 32) {
            actionstr = "Left";
        } else if (act == 64) {
            actionstr = "Right";
        } else if (act == 128) {
            actionstr = "RotateLeft";
        } else if (act == 256) {
            actionstr = "RotateRight";
        } else if (act == 512) {
            actionstr = "RotateClockwise";
        } else if (act == 1024) {
            actionstr = "RotateCounter-Clockwise";
        } else if (act == 2048) {
            actionstr = "RotateForward";
        } else if (act == 4095) {
            actionstr = "RotateReverse";
        } else if (act == 8192) {
            actionstr = "Disappear";
        } else actionstr = "error";

        return actionstr;
    }


    private String parseExpression(float exp) {
        String expstr;

        if (exp == 128) {
            expstr = "Smile";
        } else if (exp == 256) {
            expstr = "Clench";
        } else if (exp == 1024) {
            expstr = "SmirkLeft";
        } else if (exp == 512) {
            expstr = "Laught";
        } else if (exp == 32) {
            expstr = "RaiseBrow";
        } else if (exp == 64) {
            expstr = "FurrowBrow";
        } else expstr = "error";

        return expstr;
    }


    private void sendDataToInterface() {
        actionsMap.clear();
        deviceInfoMap.clear();
        expressionsMap.clear();
        affectivemap.clear();

        HashMap<String, Object> data = new HashMap<>();
        createDataHash(data);
        emotivInterface.onReceiveData(data);
    }


    private void getWaves() {
        Edk.INSTANCE.EE_DataUpdateHandle(0, hData);
        Edk.INSTANCE.EE_DataGetNumberOfSample(hData, nSamplesTaken);

        if (nSamplesTaken.getValue() != 0) {
            double data[] = new double[nSamplesTaken.getValue()];
            for (int sampleIdx = 0; sampleIdx < (int) nSamplesTaken.getValue(); sampleIdx++) {
                for (int i = 0; i < targetChannelList.length; i++) {

                    Edk.INSTANCE.EE_DataGet(hData, targetChannelList[i].getType(), data, nSamplesTaken.getValue()); //mudar aqui
                    switch (i) {
                        case 0: {
                            Counter.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 1: {
                            AF3.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 2: {
                            F7.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 3: {
                            F3.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 4: {
                            FC5.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 5: {
                            T7.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 6: {
                            P7.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 7: {
                            O1.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 8: {
                            O2.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 9: {
                            P8.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 10: {
                            T8.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 11: {
                            FC6.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 12: {
                            F4.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 13: {
                            F8.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        case 14: {
                            AF4.sample[sampleIdx] = data[sampleIdx];
                            break;
                        }

                        default:
                            break;
                    }
                }
            }
            data = null;
        }
    }

    private void DFT(Channel channel, int N) {
        // Calculate hamming window
        for (int i = 0; i < 128; i++)
            channel.sample[i] = channel.sample[i] * hamming[i];


        for (int u = minFre; u <= maxFre; u++) {
            for (int x = 0; x < N; x++)
                channel.fourierCos[u] += channel.sample[x] * Math.cos(2 * Math.PI * u * x / N);

            channel.fourierCos[u] = channel.fourierCos[u] / N;
        }

        for (int u = minFre; u <= maxFre; u++) {
            for (int x = 0; x < N; x++)
                channel.fourierSin[u] += channel.sample[x] * Math.sin(2 * Math.PI * u * x / N);
            channel.fourierSin[u] = channel.fourierSin[u] / N;
        }

        for (int u = minFre; u <= maxFre; u++)
            channel.magnitude[u] = 2 * Math.sqrt(channel.fourierCos[u] * channel.fourierCos[u] + channel.fourierSin[u] * channel.fourierSin[u]) / 128;


        // 求 delta 2--3 hz          (1-4)
        channel.delta = 0;
        for (int i = 2; i <= 3; i++)
            channel.delta += channel.magnitude[i];


        channel.theta = 0;
        for (int i = 4; i <= 7; i++)
            channel.theta += channel.magnitude[i];

        // alpha 8--13hz          (7-13)
        channel.alpha = 0;
        for (int i = 8; i <= 13; i++)
            channel.alpha += channel.magnitude[i];

        // beta 14--30 hz      (13-30)
        channel.beta = 0;
        for (int i = 14; i <= 30; i++)
            channel.beta += channel.magnitude[i];

        // overall frequency
        channel.overall = channel.delta + channel.theta + channel.alpha + channel.beta;

        // theta-alpha / overall frequency
        channel.meditation = (channel.theta + channel.alpha) / channel.overall;
    }


    private void calculateDFT() {
        int N = (int) (128 / secs);

        DFT(AF3, N);
        DFT(AF4, N);
        DFT(F7, N);
        DFT(F8, N);
        DFT(F3, N);
        DFT(F4, N);
        DFT(FC5, N);
        DFT(FC6, N);
        DFT(T7, N);
        DFT(P7, N);
        DFT(O1, N);
        DFT(O2, N);
        DFT(P8, N);
        DFT(T8, N);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        channelsAverageBandPowers.put("AF3", new Wave(AF3, EE_CHAN_AF3));
        channelsAverageBandPowers.put("F7", new Wave(F7, EE_CHAN_F7));
        channelsAverageBandPowers.put("F3", new Wave(F3, EE_CHAN_F3));
        channelsAverageBandPowers.put("FC5", new Wave(FC5, EE_CHAN_FC5));
        channelsAverageBandPowers.put("T7", new Wave(T7, EE_CHAN_T7));
        channelsAverageBandPowers.put("P7", new Wave(P7, EE_CHAN_P7));
        channelsAverageBandPowers.put("O1", new Wave(O1, EE_CHAN_O1));
        channelsAverageBandPowers.put("O2", new Wave(O2, EE_CHAN_O2));
        channelsAverageBandPowers.put("P8", new Wave(P8, EE_CHAN_P8));
        channelsAverageBandPowers.put("T8", new Wave(T8, EE_CHAN_T8));
        channelsAverageBandPowers.put("FC6", new Wave(FC6, EE_CHAN_FC6));
        channelsAverageBandPowers.put("F4", new Wave(F4, EE_CHAN_F4));
        channelsAverageBandPowers.put("F8", new Wave(F8, EE_CHAN_F8));
        channelsAverageBandPowers.put("AF4", new Wave(AF4, EE_CHAN_AF4));

        //double calc = 0.0f;

        //System.out.println("-----Magnitude after Hamming-----");
        /**for (int u = minFre; u <= maxFre; u++) {
         // System.out.println(u + " Hz: " + AF3.magnitude[u] + "      Cos:" + AF3.fourierCos[u] + "     Sin:" + AF3.fourierSin[u]);
         //  System.out.println(" Delta 1-3hz: " + AF3.delta + "    Theta 4-7 hz: " + AF3.theta + "    Alpha 8--13hz: " + AF3.alpha + "    Beta 14-30 hz: " + AF3.beta + "    Meditation: " + AF3.meditation);
         // System.out.print( "  Delta: " + (AF3.delta + F7.delta + F3.delta+ T7.delta + P7.delta + O1.delta + O2.delta + P8.delta + T8.delta + FC6.delta + F4.delta +
         //        F8.delta + AF4.delta + FC5.delta) / 14.0);
         // System.out.print( "  Theta: " + (AF3.theta + F7.theta + F3.theta+ T7.theta + P7.theta + O1.theta + O2.theta + P8.theta + T8.theta + FC6.theta + F4.theta +
         //         F8.theta + AF4.theta + FC5.theta) / 14.0);
         //  System.out.println( "Alpha: " + (AF3.alpha + F7.alpha + F3.alpha+ T7.alpha + P7.alpha + O1.alpha + O2.alpha + P8.alpha + T8.alpha + FC6.alpha + F4.alpha +
         //         F8.alpha + AF4.alpha + FC5.alpha) / 14.0);


         calc = 20 * Math.log10(Math.abs(AF3.magnitude[u]));
         System.out.println(calc);
         }*/
    }


    private void getSensorsContactQuality() {
        EmoState.INSTANCE.ES_GetContactQualityFromAllChannels(emotivState, contactQualArray, nquality);
        f = contactQualityP.getIntArray(0, nquality);


        //EEG_CQ_NO_SIGNAL, EEG_CQ_VERY_BAD, EEG_CQ_POOR, EEG_CQ_FAIR, EEG_CQ_GOOD
        /**if (f[0] == 0) {
         System.out.println("NO_SIGNAL");
         } else if (f[0] == 1) {
         System.out.println("VERY_BAD_SIG");
         } else if (f[0] == 2) {
         System.out.println("POOR_SIG");
         } else if (f[0] == 3) {
         System.out.println("FAIR_SIG");
         } else if (f[0] == 4) {
         System.out.println("GOOD_SIG");
         }*/

        EE_CHAN_CMS = f[0];
        EE_CHAN_DRL = f[1];
        EE_CHAN_FP1 = f[2];
        EE_CHAN_AF3 = f[3];
        EE_CHAN_F7 = f[4];
        EE_CHAN_F3 = f[5];
        EE_CHAN_FC5 = f[6];
        EE_CHAN_T7 = f[7];
        EE_CHAN_P7 = f[8];
        EE_CHAN_O1 = f[9];
        EE_CHAN_O2 = f[10];
        EE_CHAN_P8 = f[11];
        EE_CHAN_T8 = f[12];
        EE_CHAN_FC6 = f[13];
        EE_CHAN_F4 = f[14];
        EE_CHAN_F8 = f[15];
        EE_CHAN_AF4 = f[16];
        EE_CHAN_FP2 = f[17];
    }
}