package Emotiv.Emotiv_SDK; /**
 * Created by cenas on 14/04/2016.
 */
import Emotiv.Emotiv_SDK.*;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;

import java.util.*;


public class EmotivDevice implements Runnable {

    private LinkedList sharedQ;
    private HashMap<String,HashMap<String,Object>> dataToSend;

    //vars
    public int wirelessSignalStatus=0;
    private int blinkStatus=0, leftWinkStatus=0, rightWinkStatus=0, emotivCurrentAction = 0, eyesOpenStatus = 0, lookingDownStatus = 0, lookingRightStatus = 0,
            lookingLeftStatus=0, lookingUpStatus=0, emotivHeadsetOn = 0, lowerFaceActionStatus = 0, uperFaceActionStatus = 0;
    private double alphaVal = 0, low_betaVal = 0, high_betaVal =0, gammaVal = 0, thetaVal = 0, smileExtentStatus = 0, clenchExtentStatus = 0, emotivCurrentActionPower=0,
            lowerFaceActionStatusPower = 0, uperFaceActionStatusPower = 0;

    float timestamp=0;

    private HashMap<String,HashMap<String,Double>> channelsAverageBandPowers;

    private int AF3ChanQuality=0, AF4ChanQuality=0, CMSChanQuality=0, F3ChanQuality=0, F4ChanQuality=0, F7ChanQuality=0, F8ChanQuality=0, FC5ChanQuality=0, FP1ChanQuality=0,
            FC6ChanQuality=0, DRLChanQuality=0, FP2ChanQuality=0, O1ChanQuality=0, O2ChanQuality=0, PzChanQuality=0, P7ChanQuality=0, P8ChanQuality=0, T7ChanQuality=0,
            T8ChanQuality=0;

    IntByReference batteryLevelStatus = null;
    IntByReference maxBatteryLevel = null;


    private Pointer emotivEvent;
    private Pointer emotivState;
    private boolean receiveDataBool = false;
    short composerPort = 1726;
    int state=0;
    boolean onStateChanged = false;
    boolean readytocollect = false;

    IntByReference userID = null;


    public void setQueue(LinkedList queueMsg){

        sharedQ = queueMsg;

    }


    public void connectEmotiv() {
        emotivEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
        emotivState = Edk.INSTANCE.IEE_EmoStateCreate();

        System.out.println("Target IP of EmoComposer: [127.0.0.1]");

        /*
        	if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
					.ToInt()) {
				System.out.println("Emotiv Engine start up failed.");
				return;
			}
         */

        if (Edk.INSTANCE.IEE_EngineRemoteConnect("127.0.0.1", composerPort,
                "Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
            System.out
                    .println("Cannot connect to EmoComposer on [127.0.0.1]");
            return;
        }
        System.out.println("Connected to EmoComposer on [127.0.0.1]");
        init();
    }

    private void init(){
        userID = new IntByReference(0);
        receiveDataBool = true;
        batteryLevelStatus = new IntByReference(0);
        maxBatteryLevel = new IntByReference(0);
        initChannelsArray();
        dataToSend = new HashMap<String,HashMap<String,Object>>();

    }

    private void initChannelsArray(){

        channelsAverageBandPowers = new HashMap<String,HashMap<String, Double>>();
        HashMap<String,HashMap<String, Double>> mapChan = new HashMap<String,HashMap<String, Double>>();
        HashMap<String,Double> wavHash = new HashMap<String,Double>();

        wavHash.put("Alpha",new Double(0));
        wavHash.put("LowBeta",new Double(0));
        wavHash.put("HighBeta",new Double(0));
        wavHash.put("Gamma",new Double(0));
        wavHash.put("Theta",new Double(0));

        channelsAverageBandPowers.put("AF3", wavHash);
        channelsAverageBandPowers.put("F7", wavHash);
        channelsAverageBandPowers.put("F3", wavHash);
        channelsAverageBandPowers.put("FC5", wavHash);
        channelsAverageBandPowers.put("T7", wavHash);
        channelsAverageBandPowers.put("P7", wavHash);
        channelsAverageBandPowers.put("O1", wavHash);
        channelsAverageBandPowers.put("O2", wavHash);
        channelsAverageBandPowers.put("P8", wavHash);
        channelsAverageBandPowers.put("T8", wavHash);
        channelsAverageBandPowers.put("FC6", wavHash);
        channelsAverageBandPowers.put("F4", wavHash);
        channelsAverageBandPowers.put("F8", wavHash);
        channelsAverageBandPowers.put("AF4", wavHash);
    }


    private boolean eventTypeCheck(int eventType){
        switch(eventType)
        {
            case 0x0010:
                System.out.println("User added");
                readytocollect = true;
                break;
            case 0x0020:
                System.out.println("User removed");
                readytocollect = false; 		//just single connection
                break;
            case 0x0040:
                onStateChanged = true;
                Edk.INSTANCE.IEE_EmoEngineEventGetEmoState(emotivEvent, emotivState);
                break;
            default:
                break;
        }

        if(readytocollect && onStateChanged)
            return true;
        else
            return false;
    }

    public void receiveDataEmotiv() {
        boolean newData = false;
        while (receiveDataBool) {
            state = Edk.INSTANCE.IEE_EngineGetNextEvent(emotivEvent);
            //new event to handle
            if (state == EdkErrorCode.EDK_OK.ToInt()) {

                int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(emotivEvent);
                Edk.INSTANCE.IEE_EmoEngineEventGetUserId(emotivEvent, userID);
                newData = eventTypeCheck(eventType);


                if (eventType == Edk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()) {
                     timestamp = EmoState.INSTANCE.IS_GetTimeFromStart(emotivState);
                 //   System.out.println(timestamp + " : New EmoState from user " + userID.getValue());

                    if (newData) {
                        //??????????????????????????????????????????????????????????????????????????
                        emotivHeadsetOn = EmoState.INSTANCE.IS_GetHeadsetOn(emotivState);
                        AverageBandPowers();
                        //wireless signal status
                        wirelessSignalStatus = EmoState.INSTANCE.IS_GetWirelessSignalStatus(emotivState);
                    //    System.out.print("WirelessSignalStatus: ");
                    //    System.out.println(wirelessSignalStatus);

                        //battety level status
                        EmoState.INSTANCE.IS_GetBatteryChargeLevel(emotivState, batteryLevelStatus, maxBatteryLevel);
                     //   System.out.println("battery level: " + batteryLevelStatus.getValue());

                        getEmotivFacialExpression();
                        getEmotivHeadDirections();


                        //concentration level. return MentalCommand action power (0.0 to 1.0)
                     //   System.out.print("Get current Action: ");
                     //   System.out.println(EmoState.INSTANCE.IS_MentalCommandGetCurrentAction(emotivState));
                        emotivCurrentAction = EmoState.INSTANCE.IS_MentalCommandGetCurrentAction(emotivState);
                        // System.out.println(emotivCurrentAction);

                        //cognition action power. return detection state (0: Not Active, 1: Active)
                      //  System.out.print("CurrentActionPower: ");
                     //   System.out.println(EmoState.INSTANCE.IS_MentalCommandGetCurrentActionPower(emotivState));
                        emotivCurrentActionPower = EmoState.INSTANCE.IS_MentalCommandGetCurrentActionPower(emotivState);


                        //battety level status
                        EmoState.INSTANCE.IS_GetBatteryChargeLevel(emotivState, batteryLevelStatus, maxBatteryLevel);
                       // System.out.println("battery level: " + batteryLevelStatus.getValue());


                        AverageBandPowers();
                        getSensorsContactQuality();

                       sendData();
                    }
                }
            }
        }
    }

    private synchronized void sendData(){
        dataToSend = new HashMap<String,HashMap<String,Object>>();
        createDataArray();
        synchronized (sharedQ) {
            sharedQ.addLast(dataToSend);
            sharedQ.notify();
        }
    }

    public String parseAction(int act){

        String actionstr;

        if(act == 1){
            actionstr = new String("Neural");
        }else if(act == 2){
            actionstr = new String("Push");
        }else if(act == 4){
            actionstr = new String("Pull");
        }else if(act == 8){
            actionstr = new String("Lift");
        }else if(act == 16){
            actionstr = new String("Drop");
        }else if(act == 32){
            actionstr = new String("Left");
        }else if(act == 64){
            actionstr = new String("Right");
        } else if(act == 128){
            actionstr = new String("RotateLeft");
        } else if(act == 256){
            actionstr = new String("RotateRight");
        }else if(act == 512){
            actionstr = new String("RotateClockwise");
        }else if(act == 1024){
            actionstr = new String("RotateCounter-Clockwise");
        }else if(act == 2048){
            actionstr = new String("RotateForward");
        } else if(act == 4095){
            actionstr = new String("RotateReverse");
        }else if(act == 8192){
            actionstr = new String("Disappear");
        }
        else actionstr = new String("error");

        return actionstr;

    }

    private String parseExpression(int exp){
        String expstr;

        if(exp == 128){
            expstr  = new String("Smile");
        }else if(exp == 256){
            expstr = new String("Clench");
        }else if(exp == 1024){
            expstr = new String("SmirkLeft");
        }else if(exp == 512){
            expstr = new String("Laught");
        }else  if(exp == 32){
            expstr = new String("RaiseBrow");
        }else if(exp == 64){
            expstr = new String("FurrowBrow");
        }
        else expstr = "error";

        return expstr;
    }


    public void createDataArray(){

        HashMap<String,HashMap<String, Object>> mapType = new HashMap<String,HashMap<String, Object>>();
        HashMap<String,Object> expressionsMap = new HashMap<>();
        HashMap<String,Object> actionsMap = new HashMap<>();
        HashMap<String,Object> wavesMap = new HashMap<>();
        HashMap<String,Object> deviceInfoMap = new HashMap<>();
        HashMap<String,Object> chanQualityMap = new HashMap<>();
        HashMap<String,Object> timeMap = new HashMap<>();


        timeMap.put("Timestamp", timestamp);

        chanQualityMap.put("AF3ChanQuality",AF3ChanQuality);
        chanQualityMap.put("AF4ChanQuality",AF4ChanQuality);
        chanQualityMap.put("CMSChanQuality",CMSChanQuality);
        chanQualityMap.put("F3ChanQuality",F3ChanQuality);
        chanQualityMap.put("F4ChanQuality",F4ChanQuality);
        chanQualityMap.put("F7ChanQuality",F7ChanQuality);
        chanQualityMap.put("F8ChanQuality",F8ChanQuality);
        chanQualityMap.put("FC5ChanQuality",FC5ChanQuality);
        chanQualityMap.put("FP1ChanQuality",FP1ChanQuality);
        chanQualityMap.put("FC6ChanQuality",FC6ChanQuality);
        chanQualityMap.put("DRLChanQuality",DRLChanQuality);
        chanQualityMap.put("FP2ChanQuality",FP2ChanQuality);
        chanQualityMap.put("O1ChanQuality",O1ChanQuality);
        chanQualityMap.put("O2ChanQuality",O2ChanQuality);
        chanQualityMap.put("PzChanQuality",PzChanQuality);
        chanQualityMap.put("P7ChanQuality",P7ChanQuality);
        chanQualityMap.put("P8ChanQuality",P8ChanQuality);
        chanQualityMap.put("T7ChanQuality",T7ChanQuality);
        chanQualityMap.put("T8ChanQuality",T8ChanQuality);

        expressionsMap.put("LeftWink",leftWinkStatus);
        expressionsMap.put("RightWink",rightWinkStatus);
        expressionsMap.put("Blink",blinkStatus);
        expressionsMap.put("EyesOpen",eyesOpenStatus);
        expressionsMap.put("SmileExtension",smileExtentStatus);
        expressionsMap.put("ClenchExtension",clenchExtentStatus);
        expressionsMap.put("LowerFaceExpression",parseExpression(lowerFaceActionStatus));
        expressionsMap.put("LowerFaceExpressionPower",lowerFaceActionStatusPower);
        expressionsMap.put("UperFaceExpression",parseExpression(uperFaceActionStatus));
        expressionsMap.put("UperFaceExpressionPower",uperFaceActionStatusPower);

        //???????
        wavesMap = (HashMap<String, Object>) channelsAverageBandPowers.clone();

        actionsMap.put("Action",parseAction(emotivCurrentAction));
        actionsMap.put("ActionPower",emotivCurrentActionPower);
        actionsMap.put("LookingLeft",lookingLeftStatus);
        actionsMap.put("LookingRight",lookingRightStatus);
        actionsMap.put("LookingDown",lookingDownStatus);
        actionsMap.put("LookingUp",lookingUpStatus);

        deviceInfoMap.put("BatteryLevel", batteryLevelStatus.getValue());
        deviceInfoMap.put("WirelessSignal",wirelessSignalStatus);

        mapType.put("Time", timeMap);
        mapType.put("FacialExpressions",expressionsMap);
        mapType.put("Actions",actionsMap);
        mapType.put("Waves",wavesMap);
        mapType.put("DeviceInfo",deviceInfoMap);
        mapType.put("ChannelQuality",chanQualityMap);

        dataToSend = (HashMap<String, HashMap<String, Object>>) mapType.clone();

    }

    public void AverageBandPowers(){

        DoubleByReference alpha     = new DoubleByReference(0);
        DoubleByReference low_beta  = new DoubleByReference(0);
        DoubleByReference high_beta = new DoubleByReference(0);
        DoubleByReference gamma     = new DoubleByReference(0);
        DoubleByReference theta     = new DoubleByReference(0);

        Iterator iterator = channelsAverageBandPowers.entrySet().iterator();
            for (int i = 3; i < 17; i++) {
                Map.Entry map = (Map.Entry) iterator.next();
                int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userID.getValue(), i, theta, alpha, low_beta, high_beta, gamma);
                if (result == EdkErrorCode.EDK_OK.ToInt()) {
                    alphaVal = alpha.getValue();
                    low_betaVal = low_beta.getValue();
                    high_betaVal = high_beta.getValue();
                    gammaVal = gamma.getValue();
                    thetaVal = theta.getValue();
                    channelsAverageBandPowers.get(map.getKey()).put("Alpha", alphaVal);
                    channelsAverageBandPowers.get(map.getKey()).put("Theta", thetaVal);
                    channelsAverageBandPowers.get(map.getKey()).put("Gamma", gammaVal);
                    channelsAverageBandPowers.get(map.getKey()).put("LowBeta", low_betaVal);
                    channelsAverageBandPowers.get(map.getKey()).put("HighBeta", high_betaVal);
            }
        }
    }


    private void getEmotivHeadDirections(){

        //looking down status
        if (EmoState.INSTANCE.IS_FacialExpressionIsLookingDown(emotivState) == 1){
         //   System.out.println("Looking Down");
            lookingDownStatus = 1;
        } else
            lookingDownStatus = 0;

        //looking up status
        if (EmoState.INSTANCE.IS_FacialExpressionIsLookingUp(emotivState) == 1){
          //  System.out.println("Looking Up");
            lookingUpStatus = 1;
        } else
            lookingUpStatus = 0;

        //looking left status
        if (EmoState.INSTANCE.IS_FacialExpressionIsLookingLeft(emotivState) == 1){
           // System.out.println("Looking Left");
            lookingLeftStatus = 1;
        } else
            lookingLeftStatus = 0;

        //looking right status
        if (EmoState.INSTANCE.IS_FacialExpressionIsLookingRight(emotivState) == 1){
           // System.out.println("Looking Right");
            lookingRightStatus = 1;
        } else
            lookingRightStatus = 0;
    }

    private void getEmotivFacialExpression(){
        //blink state
        if (EmoState.INSTANCE.IS_FacialExpressionIsBlink(emotivState) == 1) {
            //System.out.println("Blink");
            blinkStatus = 1;
        } else
            blinkStatus = 0;

        //left wink state
        if (EmoState.INSTANCE.IS_FacialExpressionIsLeftWink(emotivState) == 1) {
            //System.out.println("LeftWink");
            leftWinkStatus = 1;
        } else
            leftWinkStatus = 0;

        //right wink state
        if (EmoState.INSTANCE.IS_FacialExpressionIsRightWink(emotivState) == 1) {
           // System.out.println("RightWink");
            rightWinkStatus = 1;
        } else
            rightWinkStatus = 0;

        //eyes open state
        if (EmoState.INSTANCE.IS_FacialExpressionIsEyesOpen(emotivState) == 1){
           // System.out.println("Eyes open");
            eyesOpenStatus = 1;
        } else
            eyesOpenStatus = 0;

        //get smile extention
        smileExtentStatus = EmoState.INSTANCE.IS_FacialExpressionGetSmileExtent(emotivState);
       // System.out.println("Smile Extention: " + smileExtentStatus);

        //get lower face action
        lowerFaceActionStatus = EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceAction(emotivState);
        lowerFaceActionStatusPower = EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceActionPower(emotivState);
       // System.out.println("Lower Face Action: " + lowerFaceActionStatus);

        //get uper face action
        uperFaceActionStatus = EmoState.INSTANCE.IS_FacialExpressionGetUpperFaceAction(emotivState);
        uperFaceActionStatusPower = EmoState.INSTANCE.IS_FacialExpressionGetUpperFaceActionPower(emotivState);
        //System.out.println("Uper Face Action: " + uperFaceActionStatus);

        //get clench extent
        clenchExtentStatus = EmoState.INSTANCE.IS_FacialExpressionGetClenchExtent(emotivState);
        //System.out.println("Clench Extention: " + clenchExtentStatus);

    }


    //sensor contact quality from 0 to 4
    private void getSensorsContactQuality(){
        AF3ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_AF3.getType());
        AF4ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_AF4.getType());
        CMSChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_CMS.getType());
        DRLChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_DRL.getType());
        F3ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_F3.getType());
        F4ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_F4.getType());
        F7ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_F7.getType());
        F8ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_F8.getType());
        FC5ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_FC5.getType());
        FC6ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_FC6.getType());
        FP1ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_FP1.getType());
        FP2ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_FP2.getType());
        O1ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_O1.getType());
        O2ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_O2.getType());
        PzChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_Pz.getType());
        P7ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_P7.getType());
        P8ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_P8.getType());
        T7ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_T7.getType());
        T8ChanQuality = EmoState.INSTANCE.IS_GetContactQuality(emotivState, EmoState.IEE_InputChannels_t.IEE_CHAN_T8.getType());
    }



    public void emotivDeviceDisconnect(){
        Edk.INSTANCE.IEE_EngineDisconnect();
        Edk.INSTANCE.IEE_EmoStateFree(emotivState);
        Edk.INSTANCE.IEE_EmoEngineEventFree(emotivEvent);
        System.out.println("Disconnected!");
    }

    @Override
    public void run() {
        receiveDataEmotiv();
    }
}





