package com.example.cameratest1_17.Control;

import android.util.Log;

import com.example.cameratest1_17.jna.HCNetSDKJNAInstance;
import com.hikvision.netsdk.HCNetSDK;

public class SDKGuider{
    static public SDKGuider g_sdkGuider = new SDKGuider();

    //ISAPI协议透传接口
    public DevTransportGuider m_comTransportGuider = new com.example.cameratest1_17.Control.DevTransportGuider();
    //串口透传接口
    public DevPassThroughGuider m_comSerialTransGuider = new com.example.cameratest1_17.Control.DevPassThroughGuider();
    //设备管理接口
    public com.example.cameratest1_17.Control.DevManageGuider m_comDMGuider = new com.example.cameratest1_17.Control.DevManageGuider();
    //设备远程设置接口
    public DevConfigGuider m_comConfGuider = new DevConfigGuider();
    //设备报警接口
    public DevAlarmGuider m_comDevAlarmGuider = new DevAlarmGuider();
    //关于回放接口
    public com.example.cameratest1_17.Control.DevPlayBackGuider m_comPBGuider = new com.example.cameratest1_17.Control.DevPlayBackGuider();
    //预览相关接口
    public com.example.cameratest1_17.Control.DevPreviewGuider m_comPreviewGuider = new com.example.cameratest1_17.Control.DevPreviewGuider();
    //SDK初始化
    public SDKGuider(){
        initNetSdk_jna();
    }
    //清理
    public void finalize()
    {
        cleanupNetSdk_jna();
    }

    public int GetLastError_jni()
    {
        return HCNetSDK.getInstance().NET_DVR_GetLastError();
    }

    private boolean initNetSdk_jni()
    {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init())
        {
            Log.e("[NetSDKSimpleDemo]", "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        return true;
    }

    private boolean initNetSdk_jna()
    {
        // init net sdk
        if (!HCNetSDKJNAInstance.getInstance().NET_DVR_Init())
        {
            Log.e("[NetSDKSimpleDemo]", "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDKJNAInstance.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        return true;
    }

    private boolean cleanupNetSdk_jni()
    {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Cleanup())
        {
            Log.e("[NetSDKSimpleDemo]", "HCNetSDK cleanup is failed!");
            return false;
        }
        return true;
    }

    private boolean cleanupNetSdk_jna()
    {
        // init net sdk
        if (!HCNetSDKJNAInstance.getInstance().NET_DVR_Cleanup())
        {
            Log.e("[NetSDKSimpleDemo]", "HCNetSDK cleanup is failed!");
            return false;
        }
        return true;
    }
}
