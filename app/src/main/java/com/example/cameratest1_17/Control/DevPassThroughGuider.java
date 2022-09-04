package com.example.cameratest1_17.Control;

import android.util.Log;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.SerialDataCallBack;

public class DevPassThroughGuider {

    public int NET_DVR_SerialStart_jni(int iUserID, int iSerialType, SerialDataCallBack fSerialDataCallBack)
    {
        if (iUserID < 0 || iSerialType < 0) {
            Log.println(Log.ERROR, "SimpleDemo", "NET_DVR_SerialStart_jni failed with error param");
            return -1;
        }

        return HCNetSDK.getInstance().NET_DVR_SerialStart(iUserID, iSerialType, fSerialDataCallBack);
    }

    public boolean NET_DVR_SerialStop_jni(int iSerialSendHandle)
    {
        if(iSerialSendHandle == -1){
            Log.println(Log.ERROR, "SimpleDemo", "NET_DVR_SerialStop_jni failed with error param");
            return false;
        }

        return HCNetSDK.getInstance().NET_DVR_SerialStop(iSerialSendHandle);
    }

    public boolean NET_DVR_SerialSend_jni(int iSerialSendHandle, int iSerialChannel, byte[] byInputBuf, int iInputBufSize)
    {
        if(iSerialSendHandle == -1 || iSerialChannel < 0 || iInputBufSize == 0){
            Log.println(Log.ERROR, "SimpleDemo", "NET_DVR_SerialSend_jni failed with error param");
            return false;
        }
        Log.println(Log.ERROR, "Send", iSerialSendHandle + " " + iSerialChannel);
        return HCNetSDK.getInstance().NET_DVR_SerialSend(iSerialSendHandle, iSerialChannel, byInputBuf, iInputBufSize);
    }

    public boolean NET_DVR_SendToSerialPort_jni(int iUserID, int iSerialType, int iSerialChannel, byte[] byInputBuf, int iInputBufSize)
    {
        if(iUserID == -1 || iSerialType <= 0 || iSerialChannel < 0 || iInputBufSize == 0){
            Log.println(Log.ERROR, "SimpleDemo", "NET_DVR_SendToSerialPort_jni failed with error param");
            return false;
        }
        Log.println(Log.ERROR, "Send", iSerialType + " " + iSerialChannel);
        return HCNetSDK.getInstance().NET_DVR_SendToSerialPort(iUserID, iSerialType, iSerialChannel, byInputBuf, iInputBufSize);
    }

    public boolean NET_DVR_SendTo232Port_jni(int iUserID, byte[] byInputBuf, int iInputBufSize)
    {
        if(iUserID == -1 || iInputBufSize == 0){
            Log.println(Log.ERROR, "SimpleDemo", "NET_DVR_SendTo232Port_jni failed with error param");
            return false;
        }

        return HCNetSDK.getInstance().NET_DVR_SendTo232Port(iUserID, byInputBuf, iInputBufSize);
    }
}
