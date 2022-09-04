package com.example.cameratest1_17.Control;

import com.example.cameratest1_17.jna.HCNetSDKByJNA;
import com.example.cameratest1_17.jna.HCNetSDKJNAInstance;
import com.hikvision.netsdk.AlarmCallBack_V30;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_ALARMER;
import com.hikvision.netsdk.NET_DVR_BASE_ALARM;
import com.hikvision.netsdk.NET_DVR_SETUPALARM_PARAM;
import com.sun.jna.Pointer;

import java.io.Serializable;

public class DevAlarmGuider implements Serializable {
    public class CGReturn{
        public byte[] time=new byte[32];
        public byte[] ip=new byte[128];
        public int command=0;
        public int result=0;
    }

    CGReturn ret=new CGReturn();
    public int iAlarmHandle=-1;
    private static AlarmCallBack_V30 AlarmCbf = null;
    private static void processAlarmData(int lCommand, NET_DVR_ALARMER Alarmer, NET_DVR_BASE_ALARM AlarmInfo)
    {
        String sIP = new String(Alarmer.sDeviceIP);
        System.out.println("recv alarm from:" + sIP);


//        if(lCommand == HCNetSDK.COMM_ITS_PLATE_RESULT)
//        {
//
//            NET_ITS_PLATE_RESULT strAlarmInfo = (NET_ITS_PLATE_RESULT)AlarmInfo;
//            System.out.println("recv Its Plate Result:" + strAlarmInfo.dwCustomIllegalType);
//            //ret.time=strAlarmInfo.struPicInfo[0].byAbsTime;
//        }
//        else if (lCommand == HCNetSDK.COMM_ALARM)
//        {
//            NET_DVR_ALARMINFO struAlarmInfo = (NET_DVR_ALARMINFO)AlarmInfo;
//
//        }
    }

    public CGReturn Test_SetupAlarm_jni(int iUserID)
    {
        //甯冮槻
        NET_DVR_SETUPALARM_PARAM struSetupAlarmParam = new NET_DVR_SETUPALARM_PARAM();
        struSetupAlarmParam.byAlarmInfoType = 1;
        struSetupAlarmParam.byLevel  = 1;
        iAlarmHandle = HCNetSDK.getInstance().NET_DVR_SetupAlarmChan_V41(iUserID, struSetupAlarmParam);
        if (-1 == iAlarmHandle)
        {
            ret.result=0;
            System.out.println("NET_DVR_SetupAlarmChan_V41 failed!" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.result=1;
            System.out.println("NET_DVR_SetupAlarmChan_V41 succeed!");
        }
        ret.command=12368;
        return ret;
    }

    public CGReturn Test_SetupAlarm_V41_jna(int iUserID)
    {
        //甯冮槻
        HCNetSDKByJNA.NET_DVR_SETUPALARM_PARAM struSetupAlarmParam = new HCNetSDKByJNA.NET_DVR_SETUPALARM_PARAM();
        struSetupAlarmParam.byAlarmInfoType = 1;
        struSetupAlarmParam.byLevel  = 1;
        iAlarmHandle = HCNetSDKJNAInstance.getInstance().NET_DVR_SetupAlarmChan_V41(iUserID, struSetupAlarmParam.getPointer());
        if (-1 == iAlarmHandle)
        {
            ret.result=0;
            System.out.println("NET_DVR_SetupAlarmChan_V41 failed!" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.result=1;
            System.out.println("NET_DVR_SetupAlarmChan_V41 succeed!");
        }
        ret.command=12368;
        return ret;
    }

    public CGReturn Test_SetupAlarm_V50_jna(int iUserID)
    {
        //甯冮槻
        HCNetSDKByJNA.NET_DVR_SETUPALARM_PARAM_V50 struSetupAlarmParam_V50 = new HCNetSDKByJNA.NET_DVR_SETUPALARM_PARAM_V50();
        struSetupAlarmParam_V50.dwSize = struSetupAlarmParam_V50.size();
        struSetupAlarmParam_V50.byAlarmInfoType = 1;
        struSetupAlarmParam_V50.byRetAlarmTypeV40 = 1;
        struSetupAlarmParam_V50.byRetDevInfoVersion = 1;
        struSetupAlarmParam_V50.byRetVQDAlarmType = 1;
        struSetupAlarmParam_V50.byFaceAlarmDetection = 1;
        struSetupAlarmParam_V50.bySupport = 4;
        struSetupAlarmParam_V50.byLevel  = 1;
        iAlarmHandle = HCNetSDKJNAInstance.getInstance().NET_DVR_SetupAlarmChan_V50(iUserID, struSetupAlarmParam_V50.getPointer(), Pointer.NULL, 0);
        if (-1 == iAlarmHandle)
        {
            ret.result=0;
            System.out.println("NET_DVR_SetupAlarmChan_V50 failed!" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.result=1;
            System.out.println("NET_DVR_SetupAlarmChan_V50 succeed!");
        }
        ret.command=12368;
        return ret;
    }

    public CGReturn Test_CloseAlarm_jni(int iAlarmHandle)
    {
        if(!HCNetSDK.getInstance().NET_DVR_CloseAlarmChan_V30(iAlarmHandle))
        {
            ret.result=0;
            System.out.println("NET_DVR_CloseAlarmChan_V30 failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.result=1;
            System.out.println("NET_DVR_CloseAlarmChan_V30 succeed!");
        }
        return ret;
    }

    public CGReturn Test_CloseAlarm_jna(int iAlarmHandle)
    {
        if(!HCNetSDKJNAInstance.getInstance().NET_DVR_CloseAlarmChan_V30(iAlarmHandle))
        {
            ret.result=0;
            System.out.println("NET_DVR_CloseAlarmChan_V30 failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.result=1;
            System.out.println("NET_DVR_CloseAlarmChan_V30 succeed!");
        }
        return ret;
    }

}
