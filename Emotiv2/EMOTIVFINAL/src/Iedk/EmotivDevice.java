package Iedk;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;


/**
 * Created by cenas on 01/06/2016.
 */
public class EmotivDevice implements Runnable {

    private Pointer emotivEvent;
    private Pointer emotivState;
    private IntByReference userID, batteryLevelStatus, maxBatteryLevel, contactQualArray;

    DoubleByReference alpha ,low_beta ,high_beta ,gamma ,theta;
    private boolean receiveDataBool, onStateChanged, readytocollect;
    float timestamp;
    private int wirelessSignalStatus, nquality;
    Pointer contactQualityP;
    private int EE_CHAN_CMS, EE_CHAN_DRL, EE_CHAN_FP1, EE_CHAN_AF3, EE_CHAN_F7, EE_CHAN_F3, EE_CHAN_FC5, EE_CHAN_T7, EE_CHAN_P7,
            EE_CHAN_O1, EE_CHAN_O2, EE_CHAN_P8, EE_CHAN_T8, EE_CHAN_FC6, EE_CHAN_F4, EE_CHAN_F8, EE_CHAN_AF4, EE_CHAN_FP2;


    @Override
    public void run() {

        init();
        connectEmotiv();
        receiveDataEmotiv();

    }

    public static void main(String[] args) {
        EmotivDevice cenas = new EmotivDevice();
        cenas.run();
        cenas.emotivDeviceDisconnect();
    }

    public void connectEmotiv(){
        emotivEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
        emotivState = Edk.INSTANCE.EE_EmoStateCreate();

        if(Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()){
            System.out.println("Emotiv start up failed");
            return;
        }else
        System.out.println("Emotiv connected");

    }

    public void emotivDeviceDisconnect(){
        Edk.INSTANCE.EE_EngineDisconnect();
        Edk.INSTANCE.EE_EmoStateFree(emotivState);
        Edk.INSTANCE.EE_EmoEngineEventFree(emotivEvent);
        System.out.println("Disconnected");
    }

    private void init(){
        userID = new IntByReference(0);
        receiveDataBool = true;
        batteryLevelStatus = new IntByReference(0);
        maxBatteryLevel = new IntByReference(0);
        contactQualArray = new IntByReference(0);
        onStateChanged = false;
        readytocollect = false;
        timestamp=0;
        wirelessSignalStatus=0;
        nquality= EmoState.INSTANCE.ES_GetNumContactQualityChannels(emotivState);
        contactQualityP= new Memory(4*nquality);
        contactQualArray.setPointer(contactQualityP);
        EE_CHAN_CMS=0; EE_CHAN_DRL=0; EE_CHAN_FP1=0; EE_CHAN_AF3=0; EE_CHAN_F7=0; EE_CHAN_F3=0; EE_CHAN_FC5=0; EE_CHAN_T7=0; EE_CHAN_P7=0;
        EE_CHAN_O1=0; EE_CHAN_O2=0; EE_CHAN_P8=0; EE_CHAN_T8=0; EE_CHAN_FC6=0; EE_CHAN_F4=0; EE_CHAN_F8=0; EE_CHAN_AF4=0; EE_CHAN_FP2=0;
        alpha     = new DoubleByReference(0);
        low_beta  = new DoubleByReference(0);
        high_beta = new DoubleByReference(0);
        gamma     = new DoubleByReference(0);
        theta     = new DoubleByReference(0);
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
                Edk.INSTANCE.EE_EmoEngineEventGetEmoState(emotivEvent, emotivState);
                break;
            default:
                break;
        }

        if(readytocollect && onStateChanged)
            return true;
        else
            return false;
    }


    public void receiveDataEmotiv(){
        boolean newData;
        int state;
        int emotivHeadsetOn=0;
        while(receiveDataBool){
            newData = false;
            state = Edk.INSTANCE.EE_EngineGetNextEvent(emotivEvent);

            //new event to handle
            if(state == EdkErrorCode.EDK_OK.ToInt()){

               int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(emotivEvent);
                Edk.INSTANCE.EE_EmoEngineEventGetUserId(emotivEvent,userID);
                newData = eventTypeCheck(eventType);

                if(newData){

                    timestamp = EmoState.INSTANCE.ES_GetTimeFromStart(emotivState);

                   emotivHeadsetOn = EmoState.INSTANCE.ES_GetHeadsetOn(emotivState);

                   if(emotivHeadsetOn == 1){

                        //wireless status
                        wirelessSignalStatus = EmoState.INSTANCE.ES_GetWirelessSignalStatus(emotivState);
                        //bat status
                        EmoState.INSTANCE.ES_GetBatteryChargeLevel(emotivState,batteryLevelStatus,maxBatteryLevel);

                     // getEmotivFacialExpression();
                      // getEmotivHeadDirections();
                      //getAveragePowers();
                       getSensorsContactQuality();
                    }
                }

            }
        }
    }



    private void getSensorsContactQuality(){

        EmoState.INSTANCE.ES_GetContactQualityFromAllChannels(emotivState,contactQualArray,nquality);
        int[] f= contactQualityP.getIntArray(0, nquality);


        //EEG_CQ_NO_SIGNAL, EEG_CQ_VERY_BAD, EEG_CQ_POOR, EEG_CQ_FAIR, EEG_CQ_GOOD
        if(f[0] == 0){
            System.out.println("NO_SIGNAL");
        }else if(f[0] == 1) {
            System.out.println("VERY_BAD_SIG");
        }
        else if(f[0] == 2) {
            System.out.println("POOR_SIG");
        }
        else if(f[0] == 3) {
            System.out.println("FAIR_SIG");
        }else if(f[0] == 4) {
            System.out.println("GOOD_SIG");
        }

        EE_CHAN_CMS=f[0]; EE_CHAN_DRL=f[1]; EE_CHAN_FP1=f[2]; EE_CHAN_AF3=f[3]; EE_CHAN_F7=f[4]; EE_CHAN_F3=f[5]; EE_CHAN_FC5=f[6]; EE_CHAN_T7=f[7]; EE_CHAN_P7=f[8];
        EE_CHAN_O1=f[9]; EE_CHAN_O2=f[10]; EE_CHAN_P8=f[11]; EE_CHAN_T8=f[12]; EE_CHAN_FC6=f[13]; EE_CHAN_F4=f[14]; EE_CHAN_F8=f[15]; EE_CHAN_AF4=f[16]; EE_CHAN_FP2=f[17];

    }
}
