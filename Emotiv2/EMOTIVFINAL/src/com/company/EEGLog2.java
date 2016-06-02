package com.company;

import Iedk.Edk;
import Iedk.EdkErrorCode;
import Iedk.EmoState;
import calcs.FFT;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import static Iedk.Edk.EE_DataChannels_t.*;

public class EEGLog2 {
    public static void main(String[] args) {
        Pointer eEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
        Pointer eState = Edk.INSTANCE.EE_EmoStateCreate();
        FFT mfft;
        IntByReference userID = null;
        IntByReference nSamplesTaken = null;
        float timestamp, lastsampletime = 0;
        float mAlpha = 0.0f,
                mBeta = 0.0f,
                mDelta = 0.0f,
                mGamma = 0.0f,
                mTheta = 0.0f;

        int mTargetChannelList[] = new int[22];
        mTargetChannelList[0] = 0;
        mTargetChannelList[1] = 3;
        mTargetChannelList[2] = 4;
        mTargetChannelList[3] = 5;
        mTargetChannelList[4] = 6;
        mTargetChannelList[5] = 7;
        mTargetChannelList[6] = 8;
        mTargetChannelList[7] = 9;
        mTargetChannelList[8] = 10;
        mTargetChannelList[9] = 11;
        mTargetChannelList[10] = 12;
        mTargetChannelList[11] = 13;
        mTargetChannelList[12] = 14;
        mTargetChannelList[13] = 15;
        mTargetChannelList[14] = 16;
        mTargetChannelList[15] = 17;
        mTargetChannelList[16] = 18;
        mTargetChannelList[17] = 19;
        mTargetChannelList[18] = 21;
        mTargetChannelList[19] = 22;
        mTargetChannelList[20] = 23;
        mTargetChannelList[21] = 24;

        double mFreqData[];
        short composerPort = 1726;
        int option = 1;
        int state = 0;
        float secs = 1;
        boolean readytocollect = false;

        userID = new IntByReference(0);
        nSamplesTaken = new IntByReference(0);

        switch (option) {
            case 1: {
                if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
                        .ToInt()) {
                    System.out.println("Emotiv Engine start up failed.");
                    return;
                }
                break;
            }
            case 2: {
                System.out.println("Target IP of EmoComposer: [127.0.0.1] ");

                if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", composerPort,
                        "Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
                    System.out
                            .println("Cannot connect to EmoComposer on [127.0.0.1]");
                    return;
                }
                System.out.println("Connected to EmoComposer on [127.0.0.1]");
                break;
            }
            default:
                System.out.println("Invalid option...");
                return;
        }

        Pointer hData = Edk.INSTANCE.EE_DataCreate();
        Edk.INSTANCE.EE_DataSetBufferSizeInSec(secs);
        System.out.print("Buffer size in secs: ");
        System.out.println(secs);

        System.out.println("Start receiving EEG Data!");
        while (true) {
            state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);


            // New event needs to be handled
            if (state == EdkErrorCode.EDK_OK.ToInt()) {


                int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
                Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);
                Edk.INSTANCE.EE_EmoEngineEventGetEmoState(eEvent, eState);
                // Log the EmoState if it has been updated
                if (eventType == Edk.EE_Event_t.EE_UserAdded.ToInt())
                    if (userID != null) {
                        System.out.println("User added");
                        Edk.INSTANCE.EE_DataAcquisitionEnable(
                                userID.getValue(), true);
                        readytocollect = true;
                    }
            } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
                System.out.println("Internal error in Emotiv Engine!");
                break;
            }

            if (readytocollect) {

                timestamp = EmoState.INSTANCE.ES_GetTimeFromStart(eState);
                if (timestamp - lastsampletime > 0.5) {

                    mAlpha = 0.0f;
                    mBeta = 0.0f;
                    mDelta = 0.0f;
                    mGamma = 0.0f;
                    mTheta = 0.0f;

                    lastsampletime = EmoState.INSTANCE.ES_GetTimeFromStart(eState);

                    Edk.INSTANCE.EE_DataUpdateHandle(0, hData);

                    Edk.INSTANCE.EE_DataGetNumberOfSample(hData, nSamplesTaken);
                    System.out.println(nSamplesTaken.getValue());
                    if (nSamplesTaken != null) {
                        if (nSamplesTaken.getValue() != 0) {
                            double[] channelData = new double[nSamplesTaken.getValue()];
                            double[] data = new double[nSamplesTaken.getValue()];

                            for (int i = 0; i < mTargetChannelList.length; i++) {
                                Edk.INSTANCE.EE_DataGet(hData, mTargetChannelList[i], data, nSamplesTaken.getValue());
                                channelData[i] = data[i];
                            }

                            data = null;

                            mfft = new FFT(nSamplesTaken.getValue());
                            double arry[] = new double[nSamplesTaken.getValue()];
                            for (int i = 0; i < nSamplesTaken.getValue(); i++) {
                                arry[i] = 0;
                            }
                            mfft.fft(channelData, arry);
                            mFreqData = new double[channelData.length];
                            for (int i = 0; i < channelData.length; i++) {
                                mFreqData[i] = Math.pow(channelData[i], 2) + Math.pow(arry[i], 2);
                            }
                            channelData = null;


                            if (mfft.getWindow().length > 30) {
                                for (int i = 8; i < 12; i++) {
                                    mGamma += mFreqData[i];
                                }
                                mGamma *= 0.1666667f;
                                //mBeta  = mBeta / nSamplesTaken.getValue();
                                //mDelta *= 0.1666667f;
                                mGamma /= 100000000;
                                System.out.println(mGamma);
                            }

                        }
                    }

                }
            }
        }

        Edk.INSTANCE.EE_EngineDisconnect();
        Edk.INSTANCE.EE_EmoStateFree(eState);
        Edk.INSTANCE.EE_EmoEngineEventFree(eEvent);
        System.out.println("Disconnected!");
    }
}
