package com.example.cameratest1_17.Control;

import android.util.Log;

import com.example.cameratest1_17.jna.HCNetSDKByJNA;
import com.example.cameratest1_17.jna.HCNetSDKJNAInstance;
import com.example.cameratest1_17.Control.CommonMethod;
import com.hikvision.netsdk.*;

import java.io.File;
import java.io.Serializable;

public class DevConfigGuider implements Serializable {

    public class CGReturn{
        public int status_1=0;
        public int status_2=0;
        public int status_3=0;
    }

    /***************************************ScreenTest********************************************************/
    /*case 0*/
    public CGReturn test_ControlScreen_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_SCREEN_CONTROL_V41 struCtrlControl_V41 = new NET_DVR_SCREEN_CONTROL_V41();
        struCtrlControl_V41.byProtocol = 1;
        struCtrlControl_V41.bySerialNo = 1;
        struCtrlControl_V41.byWallNo = 1;
        struCtrlControl_V41.dwCommand = 1;
        struCtrlControl_V41.struControlParam.byInputSourceType = 0;
        struCtrlControl_V41.struControlParam.byColorType = 1;
        struCtrlControl_V41.struControlParam.byColorScale = 1;
        struCtrlControl_V41.struControlParam.byPosition = 1;
        struCtrlControl_V41.struControlParam.byPositionScale = 1;
        struCtrlControl_V41.struRect.dwHeight = 1920;
        struCtrlControl_V41.struRect.dwWidth = 100;
        struCtrlControl_V41.struRect.dwXCoordinate =0;
        struCtrlControl_V41.struRect.dwYCoordinate = 0;
        if(HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID,HCNetSDK.getInstance().NET_DVR_CONTROL_SCREEN,struCtrlControl_V41))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_CONTROL_SCREEN success!");
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_CONTROL_SCREEN fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 1*/
    public CGReturn test_SignalList_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_INPUT_SIGNAL_LIST struList = new NET_DVR_INPUT_SIGNAL_LIST();
        if(HCNetSDK.getInstance().NET_DVR_GetInputSignalList_V40(iUserID, 0, struList))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GetInputSignalList_V40 success!"+struList.dwInputSignalNums);
            String string = new String(struList.struSignalList[0].sGroupName);
            for(int i=0;i<32;i++)
            {
                System.out.print(struList.struSignalList[0].sGroupName[i]);
            }
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GetInputSignalList_V40 fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 2*/
    public CGReturn test_FileInfo_jni(int iUserID){
        NET_DVR_COND_INT condInt = new NET_DVR_COND_INT();
        NET_DVR_SCREEN_FILE_INFO screenFileInfo = new NET_DVR_SCREEN_FILE_INFO();

        condInt.iValue = 4;

        CGReturn ret=new CGReturn();

        if (!HCNetSDK.getInstance().NET_DVR_GetSTDConfig(iUserID, HCNetSDK.NET_DVR_GET_SCREEN_FILEINFO, condInt, null, screenFileInfo))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_SCREEN_FILEINFO" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
            //return false;
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            String filename = new String(screenFileInfo.byFileName);
            Log.i("[NetSDKSimpleDemo]","java out filename:"+filename);
            //Log.i("[NetSDKSimpleDemo]","Filename;");
            for(int i=0;i<256;i++)
            {
                Log.i("[NetSDKSimpleDemo]"," java fileName:"+screenFileInfo.byFileName[i]);
            }
            Log.i("[NetSDKSimpleDemo]","type:"+screenFileInfo.byFileType);
            Log.i("[NetSDKSimpleDemo]","byPictureFormat:"+screenFileInfo.byPictureFormat);
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetSTDConfig(iUserID, HCNetSDK.NET_DVR_SET_SCREEN_FILEINFO, condInt, screenFileInfo, null))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SET_SCREEN_FILEINFO" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
            //return false;
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SET_SCREEN_FILEINFO success");
        }
        return ret;
    }

    /*case 3*/
    public CGReturn test_SerialAbility_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        byte lpInBuf [] = new byte[1024];
        String str = "<ScreenExchangeAbility version=\"2.0\"></ScreenExchangeAbility>";
        for(int i=0;i<str.length();i++)
        {
            lpInBuf[i]=(byte)str.charAt(i);
        }
        byte lpOutBuf[] = new byte[100*1024];

        if(HCNetSDK.getInstance().NET_DVR_GetDeviceAbility(iUserID,HCNetSDK.getInstance().DEVICE_SERIAL_ABILITY,lpInBuf,1024,lpOutBuf,100*1024))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GetDeviceAbility success");
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GetDeviceAbility fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 4*/
    public CGReturn test_loginCfg_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        String strUrl = "GET /ISAPI/DisplayDev/Auxiliary/ScreenServer/loginCfg\r\n";
        NET_DVR_XML_CONFIG_INPUT struInput = new NET_DVR_XML_CONFIG_INPUT();

        struInput.lpRequestUrl= strUrl.getBytes();

        String strInputString = "<ServerLoginCfg version=\"2.0\" xmlns=\"http://www.std-cgi.org/ver20/XMLSchema\">"+
                "<id>1</id><IpAddress><ipVersion >v4</ipVersion><ipAddress>10.17.132.113</ipAddress></IpAddress><portNo>8000</portNo><userName>admin</userName>"+
                "<password>12345</password><inputNo>2</inputNo></ServerLoginCfg>";

        struInput.lpInBuffer = strInputString.getBytes();
        //struInput.lpInBuffer = null;

        NET_DVR_XML_CONFIG_OUTPUT struOutput = new NET_DVR_XML_CONFIG_OUTPUT();
        //struOutput.lpOutBuffer = null;
        if(HCNetSDK.getInstance().NET_DVR_STDXMLConfig(iUserID,struInput,struOutput))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_STDXMLConfig success");
            Log.i("[NetSDKSimpleDemo]","dwReturnXMLSize="+struOutput.dwReturnedXMLSize);

        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_STDXMLConfig fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        String string = new String(struOutput.lpStatusBuffer);
        System.out.println("-------------StatusBuffer-------------");
        System.out.println(string);
        System.out.println("--------------StatusBuffer-------------");
        return ret;
    }

    /*case 5*/
    public CGReturn test_PlayingPlan_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        INT_PTR lpOutPtr = new INT_PTR();
        if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.getInstance().NET_DVR_GET_PLAYING_PLAN, 1, lpOutPtr))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_PLAYING_PLAN success");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_PLAYING_PLAN fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 6*/
    public CGReturn test_CtrlPlan_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_CONTROL_PARAM struCtrlParam = new NET_DVR_CONTROL_PARAM();
        struCtrlParam.byIndex = 1;
        struCtrlParam.dwControlParam = 2;
        struCtrlParam.sDeviceID[0]='a';
        struCtrlParam.sDeviceID[1]='b';
        if(HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID, HCNetSDK.getInstance().NET_DVR_CTRL_PLAN, struCtrlParam))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_CTRL_PLAN success");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_CTRL_PLAN fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 7*/
    public CGReturn test_Plan_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_PLAN_CFG struPlanCfg= new NET_DVR_PLAN_CFG();
        struPlanCfg.byValid = 1;
        struPlanCfg.byWallNo = 1;
        struPlanCfg.byWorkMode = 1;
        struPlanCfg.byPlanName[0]='a';
        struPlanCfg.byPlanName[1]='b';
        struPlanCfg.byPlanName[2]='c';
        NET_DVR_TIME_EX struTimeEx = new NET_DVR_TIME_EX();
        struTimeEx.wYear = 2015;
        struTimeEx.byMonth = 10;
        struTimeEx.byDay = 17;
        struTimeEx.byHour =7;
        struTimeEx.byMinute = 30;
        struTimeEx.bySecond =1;
        struPlanCfg.struTime = struTimeEx;
        NET_DVR_CYCLE_TIME []pCycleTime = new NET_DVR_CYCLE_TIME[7];
        for(int i=0;i<7;i++)
        {
            pCycleTime[i]=new NET_DVR_CYCLE_TIME();
            pCycleTime[i].byValid = 1;
            pCycleTime[i].struTime = struTimeEx;
        }
        struPlanCfg.dwWorkCount = 3;
        struPlanCfg.struTimeCycle = pCycleTime;
        NET_DVR_PLAN_INFO []lpPlanInfo = new NET_DVR_PLAN_INFO[32];
        for(int i=0;i<32;i++)
        {
            lpPlanInfo[i] = new NET_DVR_PLAN_INFO();
            lpPlanInfo[i].byValid = 1;
            lpPlanInfo[i].byType = 1;
            lpPlanInfo[i].wLayoutNo =1;
            lpPlanInfo[i].byScreenStyle =1;
            lpPlanInfo[i].dwDelayTime = 20;
        }

        struPlanCfg.strPlanEntry = lpPlanInfo;
        if(HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_PLAN, 1,struPlanCfg))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SET_PLAN success");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SET_PLAN fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 8*/
    public CGReturn test_VWParam_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int dwCount = 1 ;
        NET_DVR_VIDEO_WALL_INFO [] pstruWallInfo = new NET_DVR_VIDEO_WALL_INFO[1];
        pstruWallInfo[0]= new NET_DVR_VIDEO_WALL_INFO();
        //pstruWallInfo[0].dwWindowNo = 0x01010001;
        pstruWallInfo[0].dwWindowNo = 1<<24;
        pstruWallInfo[0].dwSceneNo = 1;
        NET_DVR_WALLSCENECFG []lpOut1 = new NET_DVR_WALLSCENECFG[1];
        for(int i=0;i<1;i++)
        {
            lpOut1[i]= new NET_DVR_WALLSCENECFG();
        }
        int []arrStatus1 = new int[1];
        INT_PTR intPtr1 = new INT_PTR();

        if(HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_VW_SCENE_PARAM, dwCount, arrStatus1,pstruWallInfo,lpOut1,intPtr1))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_VW_SCENE_PARAM success");
            Log.i("[NetSDKSimpleDemo]","intPtr.iValue="+intPtr1.iValue);
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_VW_SCENE_PARAM fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        dwCount = 1;
        int[] arrStatus = new int[1];

        if(HCNetSDK.getInstance().NET_DVR_SetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_VW_SCENE_PARAM, dwCount,pstruWallInfo, arrStatus,lpOut1))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SET_VW_SCENE_PARAM success");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SET_VW_SCENE_PARAM fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }

        int videoWallNo = 1;
        int sceneNo = 1;
        NET_DVR_VIDEO_WALL_INFO[] wallInfo = new NET_DVR_VIDEO_WALL_INFO[1];
        wallInfo[0] = new NET_DVR_VIDEO_WALL_INFO();
        wallInfo[0].dwWindowNo = videoWallNo << 24;
        wallInfo[0].dwSceneNo = sceneNo;

        int[] status = new int[1];
        status[0]=0;

        NET_DVR_WALLSCENECFG[] sceneCfgArray = new NET_DVR_WALLSCENECFG[1];
        sceneCfgArray[0] = new NET_DVR_WALLSCENECFG();

        INT_PTR outLenPtr = new INT_PTR();

        if (!HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.NET_DVR_GET_VW_SCENE_PARAM, 1,
                status, wallInfo, sceneCfgArray, outLenPtr))
        {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_VW_SCENE_PARAM fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
            //return ;
        }
        else {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_VW_SCENE_PARAM success");

        }
        return ret;
    }

    /*case 9*/
    public CGReturn test_CurrentScene_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_VIDEO_WALL_INFO  struWallInfo1 = new NET_DVR_VIDEO_WALL_INFO();
        struWallInfo1.dwWindowNo = 0x01000001;
        struWallInfo1.dwSceneNo = 1;
        INT_PTR lpOut = new INT_PTR();
        if(HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID,HCNetSDK.NET_DVR_GET_CURRENT_SCENE,struWallInfo1,lpOut))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_CURRENT_SCENE success!");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_CURRENT_SCENE fail,error code="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 10*/
    public CGReturn test_SceneControl_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_SCENE_CONTROL_INFO struCtrlInfo = new NET_DVR_SCENE_CONTROL_INFO();
        NET_DVR_VIDEO_WALL_INFO struWallInfo = new NET_DVR_VIDEO_WALL_INFO();
        struWallInfo.dwWindowNo = 0x01000001;
        struWallInfo.dwSceneNo = 1;
        struCtrlInfo.struVideoWallInfo = struWallInfo;
        struCtrlInfo.dwCmd = 2;
        if(HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID, HCNetSDK.getInstance().NET_DVR_SCENE_CONTROL, struCtrlInfo))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SCENE_CONTROL success!");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SCENE_CONTROL fail,error code="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 11*/
    public CGReturn test_DecChanEnable_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int dwDecChanNum = 0x01010001;
        INT_PTR lpEnable = new INT_PTR();
        lpEnable.iValue = 1;

        if(HCNetSDK.getInstance().NET_DVR_MatrixGetDecChanEnable(iUserID,dwDecChanNum,lpEnable))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","lpEnalbe="+lpEnable.iValue);
            Log.i("[NetSDKSimpleDemo]","NET_DVR_MatrixGetDecChanEnable success!");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_MatrixGetDecChanEnable  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        lpEnable.iValue = 1;
        if(HCNetSDK.getInstance().NET_DVR_MaxtrixSetDecChanEnable(iUserID,dwDecChanNum,lpEnable.iValue))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_MaxtrixSetDecChanEnable success!");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_MaxtrixSetDecChanEnable  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 12*/
    public CGReturn test_SwitchWin_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        INT_PTR lpWallNo = new INT_PTR();
        lpWallNo.iValue = 0x01010001;

        if(HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID,HCNetSDK.getInstance().NET_DVR_SWITCH_WIN_TOP,lpWallNo))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SWITCH_WIN_TOP success!");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SWITCH_WIN_TOP  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        if(HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID,HCNetSDK.getInstance().NET_DVR_SWITCH_WIN_BOTTOM,lpWallNo))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SWITCH_WIN_BOTTOM success!");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SWITCH_WIN_BOTTOM  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 13*/
    public CGReturn test_WallInParam_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int iChannel = 0x01010001;
        NET_DVR_WALLWINPARAM struWallParam = new NET_DVR_WALLWINPARAM();
        if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_WALLWINPARAM_GET, iChannel, struWallParam))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_WALLWINPARAM_GET success!");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_WALLWINPARAM_GET  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        struWallParam.byWinMode = 4;
        if(HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.getInstance().NET_DVR_WALLWINPARAM_SET,iChannel,struWallParam))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_WALLWINPARAM_SET success!");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_WALLWINPARAM_SET  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 14*/
    public CGReturn test_CloseAll_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        INT_PTR lpNum = new INT_PTR();
        lpNum.iValue = 0x01000001;
        boolean bRet = HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID,HCNetSDK.getInstance().NET_DVR_VIDEOWALLWINDOW_CLOSEALL,lpNum);
        if(bRet)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_VIDEOWALLWINDOW_CLOSEALL success!");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_VIDEOWALLWINDOW_CLOSEALL  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 15*/
    public CGReturn test_Position_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int dwCount = 0xffffffff;
        COND_INT_PTR [] arrWallNo = new COND_INT_PTR[1];
        arrWallNo[0] = new COND_INT_PTR();
        arrWallNo[0].iValue=0x01000001;

        int [] arrStatus = new int[64];
        NET_DVR_VIDEOWALLWINDOWPOSITION [] arrV= new NET_DVR_VIDEOWALLWINDOWPOSITION[64];
        for(int i=0;i<64;i++)
        {
            arrV[i]=new NET_DVR_VIDEOWALLWINDOWPOSITION();
        }

        int [] arrOut = new int[64];
        INT_PTR nRet = new INT_PTR();

        boolean bRet = HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_VIDEOWALLWINDOWPOSITION,dwCount, null, arrWallNo, arrV,nRet);
        if(bRet)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_VIDEOWALLWINDOWPOSITION success!");
            Log.i("[NetSDKSimpleDemo]","nRet ="+nRet.iValue);
            System.out.println("arrV[0].byWndOperateMode="+arrV[0].byWndOperateMode);
            System.out.println("arrV[0].struRect="+arrV[0].struRect.dwHeight+","+arrV[0].struRect.dwWidth+","+arrV[0].struRect.dwXCoordinate+","+arrV[0].struRect.dwYCoordinate);
            System.out.println("arrV[0].struResolution="+arrV[0].struResolution.dwHeight+","+arrV[0].struResolution.dwWidth+","+arrV[0].struResolution.dwXCoordinate+","+arrV[0].struResolution.dwYCoordinate);
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_VIDEOWALLWINDOWPOSITION fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }


        dwCount = 1;
        arrV[0].byEnable = 0;
        arrV[0].byWndOperateMode = 1;
        arrV[0].struResolution.dwHeight = 2000;
        arrV[0].struResolution.dwWidth =2000;
        arrV[0].struResolution.dwXCoordinate=1;
        arrV[0].struResolution.dwYCoordinate = 1;
        bRet = HCNetSDK.getInstance().NET_DVR_SetDeviceConfigEx(iUserID, HCNetSDK.getInstance().NET_DVR_SET_VIDEOWALLWINDOWPOSITION, dwCount, arrWallNo, arrV, arrStatus, 0,arrOut);
        if(bRet)
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SET_VIDEOWALLWINDOWPOSITION success!");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SET_VIDEOWALLWINDOWPOSITION  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 16*/
    public CGReturn test_DisplayPosition_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int dwCount = 1;
        COND_INT_PTR [] arrWallNo = new COND_INT_PTR[1];
        arrWallNo[0] = new COND_INT_PTR();
        arrWallNo[0].iValue=0x01010001;

        int [] arrStatus = new int[1];
        NET_DVR_VIDEOWALLDISPLAYPOSITION [] arrV= new NET_DVR_VIDEOWALLDISPLAYPOSITION[1];
        for(int i=0;i<1;i++)
        {
            arrV[i]=new NET_DVR_VIDEOWALLDISPLAYPOSITION();
        }

        int [] arrOut = new int[1];
        INT_PTR nRet = new INT_PTR();
        boolean bRet = HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_VIDEOWALLDISPLAYPOSITION,dwCount, arrStatus, arrWallNo, arrV,nRet);
        if(bRet)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_VIDEOWALLDISPLAYPOSITION success!");
            Log.i("[NetSDKSimpleDemo]","nRet="+nRet.iValue);
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_VIDEOWALLDISPLAYPOSITION fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }


        dwCount = 1;

        //閻犱礁澧介悿鍡涙偨娴ｅ喚娼掑褎鐟ч悰銉╁矗閿濆懎妫橀柡渚婃嫹
        bRet = HCNetSDK.getInstance().NET_DVR_SetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_VIDEOWALLDISPLAYPOSITION, dwCount,arrWallNo, arrStatus,arrV);
        if(bRet)
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SET_VIDEOWALLDISPLAYPOSITION success!");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SET_VIDEOWALLDISPLAYPOSITION  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 17*/
    public CGReturn test_WallOutput_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int dwCount = 1;
        Log.i("[NetSDKSimpleDemo]","dwCount ="+dwCount);
        COND_INT_PTR [] arrWallNo = new COND_INT_PTR[2];
        arrWallNo[0] = new COND_INT_PTR();
        arrWallNo[0].iValue=0x01080001;
        arrWallNo[1] = new COND_INT_PTR();
        arrWallNo[1].iValue=0x01080002;

        int [] arrStatus = new int[2];
        NET_DVR_WALLOUTPUTPARAM [] arrV= new NET_DVR_WALLOUTPUTPARAM[2];
        for(int i=0;i<2;i++)
        {
            arrV[i]=new NET_DVR_WALLOUTPUTPARAM();
        }

        int [] arrOut = new int[2];
        INT_PTR nRet = new INT_PTR();
        boolean bRet = HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_WALLOUTPUT_GET,dwCount, arrStatus, arrWallNo, arrV,nRet);
        if(bRet)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_WALLOUTPUT_GET success!");
            Log.i("[NetSDKSimpleDemo]","nRet="+nRet.iValue);
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_WALLOUTPUT_GET fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }


        bRet = HCNetSDK.getInstance().NET_DVR_SetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_WALLOUTPUT_SET, dwCount,arrWallNo, arrStatus,arrV);
        if(bRet)
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_WALLOUTPUT_SET success!");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_WALLOUTPUT_SET  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 18*/
    public CGReturn test_PlanList_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int dwDevNum = 0;
        NET_DVR_PLAN_LIST planList = new NET_DVR_PLAN_LIST();

        boolean bRet = HCNetSDK.getInstance().NET_DVR_GetPlanList(iUserID, dwDevNum,planList);
        if(bRet)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GetPlanList success!");
            Log.i("[NetSDKSimpleDemo]","planList.dwPlanNums="+planList.dwPlanNums);
            for(int i=0;i<planList.dwPlanNums;i++)
            {
                System.out.println("planList.struPlanCfg["+i+"]"+planList.struPlanCfg[i].byPlanNo);
                String strName = new String(planList.struPlanCfg[i].byPlanName);
                System.out.println("name="+strName);
            }

        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GetPlanList fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

//    /**
//     * @fn     getRemoteConfigCbf
//     * @author huyongxu
//     * @brief  get remote configuration callback instance
//     * @param   [in]
//     * @param  NULL [out]
//     * @return callback instance
//     */
    private RemoteConfigCallback getRemoteConfigCbf()
    {
        RemoteConfigCallback cbf = new RemoteConfigCallback()
        {
            public void fRemoteConfigCallback(int dwType, NET_DVR_CONFIG oConfig, byte[] pUserData)
            {
                processRemoteConfigData(dwType, oConfig, pUserData);
            }
        };
        return cbf;
    }

    public void processRemoteConfigData(int dwType, NET_DVR_CONFIG oConfig, byte[] pUserData)
    {
        if(oConfig == null)
        {
            return;
        }
        switch(dwType)
        {
            case NET_SDK_CALLBACK_TYPE.NET_SDK_CALLBACK_TYPE_DATA:
            {
                NET_DVR_SCREEN_RESPONSE_CMD oScreenRespone = (NET_DVR_SCREEN_RESPONSE_CMD)oConfig;
                if(oScreenRespone.byResponseCmd == 1 && oScreenRespone.struResonseParam.struPPTParam.byCurrentState == 9)
                {
                    Log.i("[NetSDKSimpleDemo]","Response parameter callback succeed");
                }
                if(oScreenRespone.byResponseCmd == 2)
                {
                    if(oScreenRespone.struResonseParam.struFileParam.byFileState == 5)
                    {
                        Log.i("[NetSDKSimpleDemo]","open program succeed");
                    }
                    else if(oScreenRespone.struResonseParam.struFileParam.byFileState == 6)
                    {
                        Log.e("[NetSDKSimpleDemo]","open program failed");
                    }
                    else if(oScreenRespone.struResonseParam.struFileParam.byFileState == 7)
                    {
                        Log.i("[NetSDKSimpleDemo]","close program succeed");
                    }
                    else if(oScreenRespone.struResonseParam.struFileParam.byFileState == 8)
                    {
                        Log.e("[NetSDKSimpleDemo]","close program failed");
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    /*case 19*/
    public CGReturn test_ScreenCtrl_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        RemoteConfigCallback fRemoteConfigCallback = getRemoteConfigCbf();
        int lHandle = HCNetSDK.getInstance().NET_DVR_StartRemoteConfig(iUserID, HCNetSDK.NET_DVR_START_SCREEN_CRTL, null, fRemoteConfigCallback, null);
        if(lHandle < 0)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_StartRemoteConfig NET_DVR_START_SCREEN_CRTL failed"+HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret;
        }
        ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
        Log.i("[NetSDKSimpleDemo]","NET_DVR_StartRemoteConfig NET_DVR_START_SCREEN_CRTL succeed");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        NET_DVR_SCREEN_CTRL_CMD struCmd = new NET_DVR_SCREEN_CTRL_CMD();

        struCmd.byCmdType = 5;
        NET_DVR_REMOTE_CTRL_PARAM struRemoteCtrl = new NET_DVR_REMOTE_CTRL_PARAM();
        struRemoteCtrl.byRemoteCtrlCmd = 9; //open program
        struRemoteCtrl.dwCtrlParam = 0; //program index
        struCmd.struScreenCtrlParam.struRemoteCtrlParam = struRemoteCtrl;
        if(!HCNetSDK.getInstance().NET_DVR_SendRemoteConfig((int)lHandle, LONG_CFG_SEND_DATA_TYPE_ENUM.ENUM_DVR_SCREEN_CTRL_CMD, struCmd))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SendRemoteConfig NET_DVR_START_SCREEN_CRTL failed");
            return ret;
        }
        ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
        Log.i("[NetSDKSimpleDemo]","NET_DVR_SendRemoteConfig NET_DVR_START_SCREEN_CRTL succeed");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(!HCNetSDK.getInstance().NET_DVR_StopRemoteConfig(lHandle))
        {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_StopRemoteConfig NET_DVR_START_SCREEN_CRTL failed");
        }
        else
        {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_StopRemoteConfig NET_DVR_START_SCREEN_CRTL succeed");
        }
        return ret;
    }

    /*case 20*/
    public CGReturn test_UploadFile_jni(int iUserID)
    {
        CGReturn ret1=new CGReturn();
        NET_DVR_SCREEM_FILE_UPLOAD_PARAM uploadParam = new NET_DVR_SCREEM_FILE_UPLOAD_PARAM();
        uploadParam.byFileType = 1;
        uploadParam.byPictureFormat = 2;
        String str = "test_54815.png";
        System.arraycopy(str.getBytes(), 0, uploadParam.byFileName, 0, str.length());
        ///mnt/sdcard/Pictures/Screenshots/Screenshot_2015-10-12-21-27-05.png
        String strFileName = "/mnt/sdcard/test_54815.png";
        int dwUploadType =NET_SDK_UPLOAD_TYPE.UPLOAD_SCREEN_FILE;
        NET_DVR_SCREEN_UPLOAD_RET ret =new NET_DVR_SCREEN_UPLOAD_RET();

        int lHandle = HCNetSDK.getInstance().NET_DVR_UploadFile_V40(iUserID, dwUploadType, uploadParam, strFileName, ret);
        if (lHandle < 0)
        {
            ret1.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]", "NET_DVR_UploadFile_V40 UPLOAD_SCREEN_FILE is failed!Err:"+HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret1;
        }
        else
        {
            ret1.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]", "NET_DVR_UploadFile_V40 UPLOAD_SCREEN_FILE Success!");
            return ret1;
        }
    }

    /*case 21*/
    public CGReturn test_Download_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_SCREEM_FILE_DOWNLOAD_PARAM downloadParam = new NET_DVR_SCREEM_FILE_DOWNLOAD_PARAM();
        downloadParam.dwFileIndex = 4;

        int dwDownloadType = NET_SDK_DOWNLOAD_TYPE.NET_DVR_DOWNLOAD_SCREEN_FILE;
        int dwInBufferSize = 113602;
        String sFileName = "/mnt/sdcard/ctrip.android.view/Share/test.bmp";
        System.out.println("m_iLogID=" + iUserID);
        System.out.println("dwDownloadType=" + dwDownloadType);
        System.out.println("downloadParam=" + downloadParam);
        System.out.println("dwInBufferSize=" + dwInBufferSize);
        System.out.println("sFileName=" + sFileName);

        int lHandle = HCNetSDK.getInstance().NET_DVR_StartDownload(iUserID,
                dwDownloadType, downloadParam, sFileName);

        if (lHandle < 0) {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]", "NET_DVR_StartDownload is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret;
        }
        ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();

        for (int i = 0; i < 100; i++) {
            INT_PTR press = new INT_PTR();
            HCNetSDK.getInstance().NET_DVR_GetDownloadState(
                    (int) lHandle, press);
            Log.i("[NetSDKSimpleDemo]", "i=" + i + "  =" + press.iValue);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (press.iValue == 100) {
                boolean bRet = HCNetSDK.getInstance().NET_DVR_StopDownload(
                        (int) lHandle);
                if (bRet) {
                    ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    Log.i("[NetSDKSimpleDemo]", "NET_DVR_StopDownload Success!");
                } else {
                    ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    Log.i("[NetSDKSimpleDemo]", "NET_DVR_StopDownload error!");
                }

                break;
            }
        }
        return ret;
    }

    /*case 22*/
    public CGReturn test_ScreenFileList_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_SCREEN_FILE_COND struCond = new NET_DVR_SCREEN_FILE_COND();
        struCond.byFileType = 1;
        int lHandle = HCNetSDK.getInstance().NET_DVR_StartRemoteConfig(iUserID, HCNetSDK.NET_DVR_GET_SCREEN_FLIE_LIST, struCond, null, null);
        if(lHandle < 0)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_StartRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST failed");
            return ret;
        }
        ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
        Log.i("[NetSDKSimpleDemo]","NET_DVR_StartRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST succeed");

        INT_PTR pState = new INT_PTR();
        if(!HCNetSDK.getInstance().NET_DVR_GetRemoteConfigState((int)lHandle, pState))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GetRemoteConfigState NET_DVR_GET_SCREEN_FLIE_LIST failed");
            return ret;
        }
        ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
        Log.i("[NetSDKSimpleDemo]","NET_DVR_GetRemoteConfigState NET_DVR_GET_SCREEN_FLIE_LIST succeed");

        NET_DVR_SCREEN_FILE_INFO struFileInfo = new NET_DVR_SCREEN_FILE_INFO();
        int lRet = -1;
        int iCond = 1;
        while(iCond == 1)
        {
            lRet = HCNetSDK.getInstance().NET_DVR_GetNextRemoteConfig((int)lHandle, HCNetSDK.NET_DVR_GET_SCREEN_FLIE_LIST, struFileInfo);
            if(lRet < 0)
            {
                ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
                Log.e("[NetSDKSimpleDemo]","NET_DVR_GetNextRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST failed "+HCNetSDK.getInstance().NET_DVR_GetLastError());
                return ret;
            }
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GetNextRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST succeed"+struFileInfo.byFileName);
            if(lRet == NET_SDK_GET_NEXT_STATUS.NET_SDK_GET_NEXT_STATUS_SUCCESS || lRet == NET_SDK_GET_NEXT_STATUS.NET_SDK_GET_NETX_STATUS_NEED_WAIT)
            {
                continue;
            }
            else if(lRet == NET_SDK_GET_NEXT_STATUS.NET_SDK_GET_NEXT_STATUS_FINISH)
            {
                Log.i("[NetSDKSimpleDemo]","NET_DVR_GetNextRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST finished");
                break;
            }
            else if(lRet == NET_SDK_GET_NEXT_STATUS.NET_SDK_GET_NEXT_STATUS_FAILED)
            {
                Log.e("[NetSDKSimpleDemo]","NET_DVR_GetNextRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST error");
                break;
            }
        }

        if(!HCNetSDK.getInstance().NET_DVR_StopRemoteConfig((int)lHandle))
        {
            Log.e("[NetSDKSimpleDemo]","NET_DVR_StopRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST failed");
            return ret;
        }
        Log.i("[NetSDKSimpleDemo]","NET_DVR_StopRemoteConfig NET_DVR_GET_SCREEN_FLIE_LIST succeed");
        return ret;
    }

    /*case 23*/
    public CGReturn test_ScreenConfig_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_COND_INT condInt = new NET_DVR_COND_INT();
        NET_DVR_SCREEN_CONFIG screenConfig = new NET_DVR_SCREEN_CONFIG();

        if (!HCNetSDK.getInstance().NET_DVR_GetSTDConfig(iUserID, HCNetSDK.NET_DVR_GET_SCREEN_CONFIG, null, null, screenConfig))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_SCREEN_CONFIG" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_SCREEN_CONFIG succ" + "volume: " +screenConfig.byVolume);
        }
        return ret;
    }

    /*case 24*/
    public CGReturn test_ScreenConfigCap_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        byte[] byAbility = new byte[1*1024*1024];
        INT_PTR iRetLen = new INT_PTR();
        iRetLen.iValue = 0;
        int i = 0;
        if (!HCNetSDK.getInstance().NET_DVR_GetSTDAbility(iUserID, HCNetSDK.NET_DVR_GET_SCREEN_CONFIG_CAP, null, 0, byAbility, 1024*1024, iRetLen))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GET_SCREEN_CONFIG_CAP" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GET_SCREEN_CONFIG_CAP success");
        }
        i = HCNetSDK.getInstance().NET_DVR_GetLastError();
        return ret;
    }

    /*case 25*/
    public CGReturn test_WallAbility_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        byte lpInBuf [] = new byte[1024];
        String str = "<WallAbility version=\"2.0\"></WallAbility>";
        for(int i=0;i<str.length();i++)
        {
            lpInBuf[i]=(byte)str.charAt(i);
        }
        byte lpOutBuf[] = new byte[100*1024];

        if(HCNetSDK.getInstance().NET_DVR_GetDeviceAbility(iUserID,HCNetSDK.getInstance().WALL_ABILITY,lpInBuf,1024,lpOutBuf,100*1024))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GetDeviceAbility WallAbility success");
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GetDeviceAbility WallAbility fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 26*/
    public CGReturn test_LEDCard_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        String strInput = new String("PUT /ISAPI/LED/RecvCard/System/restore\r\n");
        byte[] byInput = strInput.getBytes();
        NET_DVR_XML_CONFIG_INPUT    struInput = new NET_DVR_XML_CONFIG_INPUT();
        NET_DVR_XML_CONFIG_OUTPUT   struOuput = new NET_DVR_XML_CONFIG_OUTPUT();

        struInput.lpRequestUrl = strInput.getBytes();
        struInput.dwRequestUrlLen = byInput.length;
        String strInBuffer = new String("<RecvCardArea version=\"2.0\"><outputNo>1</outputNo><Area><startLine>1</startLine><startColumn>1</startColumn><endLine>2</endLine><endColumn>2</endColumn></Area></RecvCardArea>\r\n");
        //byte[] byOutput1 = new byte[100*1024];
        //byOutput1 = strInBuffer.getBytes();
        struInput.lpInBuffer = strInBuffer.getBytes();
        //struInput.dwInBufferSize = struInput.lpInBuffer.length;
        struInput.dwInBufferSize = strInBuffer.length();
//        struOuput.lpOutBuffer = null;
//        struOuput.dwOutBufferSize = 0;
        if (!HCNetSDK.getInstance().NET_DVR_STDXMLConfig(iUserID, struInput, struOuput))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_STDXMLConfig(PUT) faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_STDXMLConfig(PUT) Success!:");
        }
        return ret;
    }



    /****************************************ManageTest******************************************************/
    /*case0*/
    public CGReturn Test_SearchLog_jni(int iUserID)
        {
            CGReturn ret=new CGReturn();
            NET_DVR_TIME struStartTime = new NET_DVR_TIME();
            NET_DVR_TIME struEndTime = new NET_DVR_TIME();
            struStartTime.dwYear = 2014;
            struStartTime.dwMonth = 11;
            struStartTime.dwDay = 26;
            struEndTime.dwYear = 2014;
            struEndTime.dwMonth = 11;
            struEndTime.dwDay = 27;
            int nSearchHandle = (int)HCNetSDK.getInstance().NET_DVR_FindDVRLog_V30(iUserID, 0, 0, 0, struStartTime, struEndTime, false);
            if(nSearchHandle >= 0)
            {
                NET_DVR_LOG_V30	struLog = new NET_DVR_LOG_V30();
                int nState = -1;
                int nCount = 0;
                while(true)
                {
                    nState = (int)HCNetSDK.getInstance().NET_DVR_FindNextLog_V30((long)nSearchHandle, struLog);
                    if(nState != HCNetSDK.NET_DVR_FILE_SUCCESS && nState != HCNetSDK.NET_DVR_ISFINDING)
                    {
                        ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                        break;
                    }
                    else if(nState == HCNetSDK.NET_DVR_FILE_SUCCESS)
                    {
                        ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                        nCount++;
                        System.out.println("find log time:" + struLog.strLogTime.dwHour + "-" + struLog.strLogTime.dwMinute + "-" + struLog.strLogTime.dwSecond);
                    }
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                HCNetSDK.getInstance().NET_DVR_FindLogClose_V30(nSearchHandle);
            }
            return ret;
        }

    /*case1*/
    public CGReturn Test_ShutDown_jni(int iUserID)
        {
            CGReturn ret=new CGReturn();
            if(!HCNetSDK.getInstance().NET_DVR_ShutDownDVR(iUserID))
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_ShutDownDVR faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
            else
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_ShutDownDVR succ!");
            }
            return ret;
        }

    /*case2*/
    public CGReturn Test_RebootDVR_jni(int iUserID)
        {
            CGReturn ret=new CGReturn();
            if(!HCNetSDK.getInstance().NET_DVR_RebootDVR(iUserID))
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_RebootDVR faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
            else
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_RebootDVR succ!");
            }
            return ret;
        }

    /*case3*/
    public CGReturn Test_ClickKey_jni(int iUserID)
        {
            CGReturn ret=new CGReturn();
            if(!HCNetSDK.getInstance().NET_DVR_ClickKey(iUserID, NET_DVR_KEY_CODE.KEY_CODE_MENU))
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_ClickKey faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
            else
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_ClickKey succ!");
            }
            return ret;
        }

    /*case4*/
    public CGReturn Test_FormatDisk_jni(int iUserID)
        {
            CGReturn ret=new CGReturn();
            int lFormatHandle = HCNetSDK.getInstance().NET_DVR_FormatDisk(iUserID, 0);
            if(lFormatHandle >= 0)
            {
                INT_PTR ptrCurrentDisk = new INT_PTR();
                INT_PTR ptrCurrentPos = new INT_PTR();
                INT_PTR ptrFormatStatic = new INT_PTR();
                ptrCurrentDisk.iValue = 0;
                ptrCurrentPos.iValue = 0;
                while(ptrFormatStatic.iValue == 0)
                {
                    if(!HCNetSDK.getInstance().NET_DVR_GetFormatProgress(lFormatHandle, ptrCurrentDisk, ptrCurrentPos, ptrFormatStatic))
                    {
                        ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                        System.out.println("NET_DVR_GetFormatProgress failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                        break;
                    }
                    else
                    {
                        ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                        System.out.println("NET_DVR_GetFormatProgress succ Disk:" + ptrCurrentDisk.iValue + " Pos:" + ptrCurrentPos.iValue + " Static:" + ptrFormatStatic.iValue);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                System.out.println("Get progress over Disk:" + ptrCurrentDisk.iValue + " Pos:" + ptrCurrentPos.iValue + " Static:" + ptrFormatStatic.iValue);

                if(!HCNetSDK.getInstance().NET_DVR_CloseFormatHandle(lFormatHandle))
                {
                    ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    System.out.println("NET_DVR_CloseFormatHandle failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                }
            }
            else
            {
                ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_FormatDisk failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
            return ret;
        }

    /*case5*/
    public CGReturn Test_Upgrade_jni(int iUserID)
        {
            CGReturn ret=new CGReturn();
            if(!HCNetSDK.getInstance().NET_DVR_SetNetworkEnvironment(0))
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_SetNetworkEnvironment err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
            else{
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            }
            int nUpgradeHandle = HCNetSDK.getInstance().NET_DVR_Upgrade(iUserID, "/mnt/sdcard/digicap.dav");
            if(nUpgradeHandle == -1)
            {
                ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_Upgrade err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                return ret;
            }
            else
            {
                ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                int nProgress = 0;
                int nState = 0;
                INT_PTR ptrSubProgress = new INT_PTR();
                while(nProgress != -1 && nProgress != 100)
                {
                    nProgress = HCNetSDK.getInstance().NET_DVR_GetUpgradeProgress(nUpgradeHandle);
                    System.out.println("NET_DVR_GetUpgradeProgress with:" + nProgress);
                    nState = HCNetSDK.getInstance().NET_DVR_GetUpgradeState(nUpgradeHandle);
                    System.out.println("NET_DVR_GetUpgradeState with:" + nState);
                    nState = HCNetSDK.getInstance().NET_DVR_GetUpgradeStep(nUpgradeHandle, ptrSubProgress);
                    System.out.println("NET_DVR_GetUpgradeStep with SubProgress:" + ptrSubProgress.iValue + " return value:" + nState);

                    try {
                        Thread.sleep(1*1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if(nProgress == -1)
                {
                    ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    System.out.println("NET_DVR_GetUpgradeProgress err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                }
                else{
                    ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
                }
                if(!HCNetSDK.getInstance().NET_DVR_CloseUpgradeHandle(nUpgradeHandle))
                {
                    System.out.println("NET_DVR_CloseUpgradeHandle err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                }
            }
            return ret;
        }

    /*case6*/
    public CGReturn Test_ActivateDevice_jni(int iUserID)
        {
            CGReturn ret=new CGReturn();
            NET_DVR_ACTIVATECFG	activateCfg = new NET_DVR_ACTIVATECFG();
            System.arraycopy("Abcd1234".getBytes(), 0, activateCfg.sPassword, 0, "Abcd1234".getBytes().length);
            if(!HCNetSDK.getInstance().NET_DVR_ActivateDevice("10.10.35.16", 8000, activateCfg))
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_ActivateDevice failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
            else
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_ActivateDevice succ");
            }
            return ret;
        }

    /****************************************ConfigTest******************************************************/
    private final static String 	TAG						= "ConfigTest";
    private final static INT_PTR ptrINT = new INT_PTR();

    public static void Test_Time(int iUserID)
    {
        NET_DVR_TIME struTimeCfg = new NET_DVR_TIME();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_TIMECFG, 0, struTimeCfg))
        {
            System.out.println("NET_DVR_GET_TIMECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
        }
        else
        {
            System.out.println("NET_DVR_GET_TIMECFG succ:" + struTimeCfg.ToString());
        }
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_TIMECFG, 0, struTimeCfg))
        {
            System.out.println("NET_DVR_SET_TIMECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
        }
        else
        {
            System.out.println("NET_DVR_SET_TIMECFG succ:" + struTimeCfg.ToString());
        }
    }
    public static void Test_XMLAbility(int iUserID)
    {
//		NET_DVR_SDKLOCAL_CFG	sdkCfg = new NET_DVR_SDKLOCAL_CFG();
//		HCNetSDK.getInstance().NET_DVR_GetSDKLocalConfig(sdkCfg);
//		sdkCfg.byEnableAbilityParse = 1;
//		HCNetSDK.getInstance().NET_DVR_SetSDKLocalConfig(sdkCfg);
//		String strSDCard = Environment.getExternalStorageDirectory().getPath();
//		String path=(getApplicationContext()).getPackageResourcePath();
//		HCNetSDK.getInstance().NET_DVR_SetSimAbilityPath(path, strSDCard);

        byte[] arrayOutBuf = new byte[64*1024];
        INT_PTR intPtr = new INT_PTR();
        String strInput = new String("<AudioVideoCompressInfo><AudioChannelNumber>1</AudioChannelNumber><VoiceTalkChannelNumber>1</VoiceTalkChannelNumber><VideoChannelNumber>1</VideoChannelNumber></AudioVideoCompressInfo>");
        byte[] arrayInBuf = new byte[8*1024];
        arrayInBuf = strInput.getBytes();
        if(!HCNetSDK.getInstance().NET_DVR_GetXMLAbility(iUserID, HCNetSDK.DEVICE_ENCODE_ALL_ABILITY_V20,arrayInBuf, strInput.length(), arrayOutBuf, 64*1024, intPtr))
        {
            System.out.println("get DEVICE_ENCODE_ALL_ABILITY_V20 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("get DEVICE_ENCODE_ALL_ABILITY_V20 succ!");
        }
    }
    public static void Test_PTZProtocol(int iUserID)
    {
        NET_DVR_PTZCFG struPtzCfg = new NET_DVR_PTZCFG();
        if (!HCNetSDK.getInstance().NET_DVR_GetPTZProtocol(iUserID, struPtzCfg))
        {
            System.out.println("NET_DVR_GetPTZProtocol faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GetPTZProtocol succ!");
        }
    }
    public static void Test_PresetName(int iUserID, int iChan)
    {
        NET_DVR_PRESET_NAME_ARRAY struPresetNameArray = new NET_DVR_PRESET_NAME_ARRAY();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_PRESET_NAME, iChan, struPresetNameArray))
        {
            System.out.println("NET_DVR_GET_PRESET_NAME faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_PRESET_NAME succ!");
        }
    }
    public static void Test_ShowString(int iUserID, int iChan)
    {
        NET_DVR_SHOWSTRING_V30 struShowString = new NET_DVR_SHOWSTRING_V30();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_SHOWSTRING_V30, iChan, struShowString))
        {
            System.out.println("NET_DVR_GET_SHOWSTRING_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_SHOWSTRING_V30 succ!");
        }

        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_SHOWSTRING_V30, iChan, struShowString))
        {
            System.out.println("NET_DVR_SET_SHOWSTRING_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_SHOWSTRING_V30 succ!");
        }
    }
    public static void Test_DigitalChannelState(int iUserID)
    {
        NET_DVR_DIGITAL_CHANNEL_STATE struChanState = new NET_DVR_DIGITAL_CHANNEL_STATE();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_DIGITAL_CHANNEL_STATE, 0, struChanState))
        {
            System.out.println("NET_DVR_GET_DIGITAL_CHANNEL_STATE faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_DIGITAL_CHANNEL_STATE succ!");
            System.out.println("analog channel 1 and 2:" + (int)struChanState.byAnalogChanState[0] + "-" + (int)struChanState.byAnalogChanState[1] +
                    ",digital channel 1 and 2:" + (int)struChanState.byDigitalChanState[0] + "-" + (int)struChanState.byDigitalChanState[1]);
        }
    }

    public static void Test_DDNSPara(int iUserID)
    {
        NET_DVR_DDNSPARA_V30	struDDNS = new NET_DVR_DDNSPARA_V30();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_DDNSCFG_V30, 0, struDDNS))
        {
            System.out.println("NET_DVR_GET_DDNSCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_DDNSCFG_V30 succ!");
        }

        struDDNS.struDDNS[4].sDomainName = CommonMethod.string2ASCII("111222333444", 64);
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_DDNSCFG_V30, 0, struDDNS))
        {
            System.out.println("NET_DVR_SET_DDNSCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_DDNSCFG_V30 succ!");
        }
    }
    public static void Test_APInfoList(int iUserID)
    {
        NET_DVR_AP_INFO_LIST struAPInfoList = new NET_DVR_AP_INFO_LIST();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_AP_INFO_LIST, 0, struAPInfoList))
        {
            System.out.println("NET_DVR_GET_AP_INFO_LIST faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_AP_INFO_LIST succ!");
            int i = 0;
            for(i = 0; i < struAPInfoList.dwCount; i++)
            {
                System.out.println("AP[" + i + "]SSID:[" + new String(struAPInfoList.struApInfo[i].sSsid) + "]");
            }
        }
    }
    public static void Test_WifiCfg(int iUserID)
    {
        NET_DVR_WIFI_CFG struWifiCfg = new NET_DVR_WIFI_CFG();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_WIFI_CFG, 0, struWifiCfg))
        {
            System.out.println("NET_DVR_GET_WIFI_CFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_WIFI_CFG succ!");
        }

        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_WIFI_CFG, 0, struWifiCfg))
        {
            System.out.println("NET_DVR_SET_WIFI_CFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_WIFI_CFG succ!");
        }
    }
    public static void Test_WifiStatus(int iUserID)
    {
        NET_DVR_WIFI_CONNECT_STATUS struStatus = new NET_DVR_WIFI_CONNECT_STATUS();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_WIFI_STATUS, 0, struStatus))
        {
            System.out.println("NET_DVR_GET_WIFI_STATUS faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_WIFI_STATUS succ!");
        }
    }
    public static void Test_UpnpNatState(int iUserID)
    {
        NET_DVR_UPNP_NAT_STATE struUpnpNat = new NET_DVR_UPNP_NAT_STATE();
        if(!HCNetSDK.getInstance().NET_DVR_GetUpnpNatState(iUserID, struUpnpNat))
        {
            System.out.println("NET_DVR_GetUpnpNatState faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GetUpnpNatState succ!");
        }
    }
    public static void Test_UserCfg(int iUserID)
    {
        NET_DVR_USER_V30 struUserCfg = new NET_DVR_USER_V30();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_USERCFG_V30, 0, struUserCfg))
        {
            System.out.println("NET_DVR_GET_USERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_USERCFG_V30 succ!" + new String(struUserCfg.struUser[0].sUserName));
        }
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_USERCFG_V30, 0, struUserCfg))
        {
            System.out.println("NET_DVR_SET_USERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_USERCFG_V30 succ!");
        }
    }

    public static void Test_DeviceCfg(int iUserID)
    {
        NET_DVR_DEVICECFG struDeviceCfg = new NET_DVR_DEVICECFG();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_DEVICECFG, 0, struDeviceCfg))
        {
            System.out.println("NET_DVR_GET_DEVICECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_DEVICECFG succ!" );
        }
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_DEVICECFG, 0, struDeviceCfg))
        {
            System.out.println("NET_DVR_SET_DEVICECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
        }
        else
        {
            System.out.println("NET_DVR_SET_DEVICECFG succ!" );
        }

    }

    public static void Test_DeviceCfg_V40(int iUserID)
    {
        NET_DVR_DEVICECFG_V40 struDeviceCfg = new NET_DVR_DEVICECFG_V40();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_DEVICECFG_V40, 0, struDeviceCfg))
        {
            System.out.println("NET_DVR_GET_DEVICECFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_DEVICECFG_V40 succ!" + new String(struDeviceCfg.byDevTypeName));
        }
//	    if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_DEVICECFG_V40, 0, struDeviceCfg))
//	    {
//			System.out.println("NET_DVR_SET_DEVICECFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
//		}
//		else
//		{
//			System.out.println("NET_DVR_SET_DEVICECFG_V40 succ:" + struDeviceCfg.byDevTypeName);
//		}

    }

    public static void Test_ExceptionCfg_V40(int iUserID, int iChan)
    {
        NET_DVR_EXCEPTION_V40 struExceptionCfg = new NET_DVR_EXCEPTION_V40();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_EXCEPTIONCFG_V40, iChan, struExceptionCfg))
        {
            System.out.println("NET_DVR_GET_EXCEPTIONCFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_EXCEPTIONCFG_V40 succ! " );
        }

    }

    public static void Test_PicCfg(int iUserID, int iChan)
    {
        NET_DVR_PICCFG_V30 struPiccfg = new NET_DVR_PICCFG_V30();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_PICCFG_V30, iChan, struPiccfg))
        {
            System.out.println("NET_DVR_GET_PICCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_PICCFG_V30 succ!" + new String(struPiccfg.sChanName));
        }
        for(int i = 0; i <= 14; i++)
        {
            for(int j = 0; j <= 21; j++)
            {
                struPiccfg.struMotion.byMotionScope[i][j] = 1;
            }
        }
        struPiccfg.struMotion.byEnableHandleMotion = 0;
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_PICCFG_V30, iChan, struPiccfg))
        {
            System.out.println("NET_DVR_SET_PICCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_PICCFG_V30 succ!");
        }
    }
    public static void Test_ZeroChanCfg(int iUserID)
    {
        NET_DVR_ZEROCHANCFG struZeroChanCfg = new NET_DVR_ZEROCHANCFG();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_ZEROCHANCFG, 1, struZeroChanCfg)){
            System.out.println("NET_DVR_GET_ZEROCHANCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            System.out.println("NET_DVR_GET_ZEROCHANCFG succ!");
        }
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_ZEROCHANCFG, 1, struZeroChanCfg)){
            System.out.println("NET_DVR_SET_ZEROCHANCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            System.out.println("NET_DVR_SET_ZEROCHANCFG succ!");
        }
    }
    public static void Test_WorkState(int iUserID)
    {
        NET_DVR_WORKSTATE_V30 struWorkState = new NET_DVR_WORKSTATE_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRWorkState_V30(iUserID, struWorkState))
        {
            System.out.println("NET_DVR_GetDVRWorkState_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GetDVRWorkState_V30 succ!");
        }
    }
    public static void Test_RecordCfg(int iUserID, int iChan)
    {
        //jni
        NET_DVR_RECORD_V30 struRecordCfg = new NET_DVR_RECORD_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_RECORDCFG_V30, iChan, struRecordCfg))
        {
            System.out.println("NET_DVR_GET_RECORDCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_RECORDCFG_V30 succ!");
            System.out.println("AudioRec: "+struRecordCfg.byAudioRec + "  StreamType: " + struRecordCfg.byStreamType);
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_DVR_SET_RECORDCFG_V30, iChan, struRecordCfg))
        {
            System.out.println("NET_DVR_SET_RECORDCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_SET_RECORDCFG_V30 succ!");
        }

        //jna
//        HCNetSDKByJNA.NET_DVR_RECORD_V30 struRecordCfg = new HCNetSDKByJNA.NET_DVR_RECORD_V30();
//        struRecordCfg.dwSize = struRecordCfg.size();
//        IntByReference pInt = new IntByReference(0);
//        if (!HCNetSDKJNAInstance.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDKByJNA.NET_DVR_GET_RECORDCFG_V30, iChan, struRecordCfg.getPointer(), struRecordCfg.size(), pInt))
//        {
//            System.out.println("NET_DVR_GET_RECORDCFG_V30 faild!" + " err: " + HCNetSDKJNAInstance.getInstance().NET_DVR_GetLastError());
//        }else
//        {
//            struRecordCfg.read();
//            System.out.println("NET_DVR_GET_RECORDCFG_V30 succ!");
//        }
//        if(!HCNetSDKJNAInstance.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDKByJNA.NET_DVR_SET_RECORDCFG_V30,iChan,struRecordCfg.getPointer(),struRecordCfg.size()))
//        {
//            System.out.println("NET_DVR_SET_RECORDCFG_V30 faild!" + " err: " + HCNetSDKJNAInstance.getInstance().NET_DVR_GetLastError());
//        } else
//        {
//            System.out.println("NET_DVR_SET_RECORDCFG_V30 succ!");
//        }
    }
    public static void Test_AuxAlarmCfg(int iUserID, int iChan)
    {
        NET_IPC_AUX_ALARMCFG struAuxAlarm = new NET_IPC_AUX_ALARMCFG();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_IPC_GET_AUX_ALARMCFG, iChan, struAuxAlarm))
        {
            System.out.println("NET_IPC_GET_AUX_ALARMCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_IPC_GET_AUX_ALARMCFG succ!");
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_IPC_SET_AUX_ALARMCFG, iChan, struAuxAlarm))
        {
            System.out.println("NET_IPC_GET_AUX_ALARMCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            System.out.println("NET_IPC_GET_AUX_ALARMCFG succ!");
        }
    }
    public static void Test_AlarminCfg(int iUserID)
    {
        NET_DVR_ALARMINCFG_V30 struAlarmIn = new NET_DVR_ALARMINCFG_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMINCFG_V30, 0, struAlarmIn))
        {
            System.out.println("NET_DVR_GET_ALARMINCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_ALARMINCFG_V30 succ!");
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_DVR_SET_ALARMINCFG_V30, 0, struAlarmIn))
        {
            System.out.println("NET_DVR_SET_ALARMINCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_SET_ALARMINCFG_V30 succ!");
        }
    }
    public static void Test_AlarmOutCfg(int iUserID)
    {
        NET_DVR_ALARMOUTCFG_V30 struAlarmOut = new NET_DVR_ALARMOUTCFG_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMOUTCFG_V30, 0, struAlarmOut))
        {
            System.out.println("NET_DVR_GET_ALARMOUTCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_ALARMOUTCFG_V30 succ!");
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_DVR_SET_ALARMOUTCFG_V30, 0, struAlarmOut))
        {
            System.out.println("NET_DVR_SET_ALARMOUTCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_SET_ALARMOUTCFG_V30 succ!");
        }
    }
    public static void Test_DecoderCfg(int iUserID)
    {
        NET_DVR_DECODERCFG_V30  struDecoderCfg = new NET_DVR_DECODERCFG_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_DECODERCFG_V30, 1, struDecoderCfg))
        {
            System.out.println("NET_DVR_GET_DECODERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_DECODERCFG_V30 succ!");
        }
        struDecoderCfg.wDecoderAddress = 1;
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_DVR_SET_DECODERCFG_V30, 1, struDecoderCfg))
        {
            System.out.println("NET_DVR_SET_DECODERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_SET_DECODERCFG_V30 succ!");
        }
    }
    public static void Test_NTPPara(int iUserID)
    {
        NET_DVR_NTPPARA NtpPara = new NET_DVR_NTPPARA();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_NTPCFG, 0, NtpPara))
        {
            System.out.println("get NtpPara faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("get NtpPara succ!");
        }
        NtpPara.byEnableNTP = 1;
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_DVR_SET_NTPCFG, 0, NtpPara))
        {
            System.out.println("Set NtpPara faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("Set NtpPara succ!");
        }
    }
    public static void Test_IPAlarmOutCfg(int iUserID)
    {
        NET_DVR_IPALARMOUTCFG struIpAlarmOut = new NET_DVR_IPALARMOUTCFG();
        if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_IPALARMOUTCFG, 0, struIpAlarmOut))
        {
            System.out.println("NET_DVR_GET_IPALARMOUTCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_IPALARMOUTCFG succ!");
        }
    }
    public static void Test_IPParaCfg(int iUserID)
    {
        NET_DVR_IPPARACFG_V40 IpPara = new NET_DVR_IPPARACFG_V40();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, IpPara))
        {
            System.out.println("NET_DVR_GET_IPPARACFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_IPPARACFG_V40 succ!AChan:" + IpPara.dwAChanNum + ",DChan:" + IpPara.dwDChanNum);
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_DVR_SET_IPPARACFG_V40, 0, IpPara))
        {
            System.out.println("NET_DVR_SET_IPPARACFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_SET_IPPARACFG_V40 succ!");
        }
    }
    public static void Test_NetCfg(int iUserID)
    {
        NET_DVR_NETCFG_V30 NetCfg = new NET_DVR_NETCFG_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_NETCFG_V30, 0, NetCfg))
        {
            System.out.println("get net cfg faied!"+ " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("get net cfg succ!");
            System.out.println("alarm host ip: " + new String(NetCfg.struAlarmHostIpAddr.sIpV4));
            System.out.println("Etherner host ip: " + new String(NetCfg.struEtherNet[0].struDVRIP.sIpV4));
            System.out.println("Etherner mask: " + new String(NetCfg.struEtherNet[0].struDVRIPMask.sIpV4));
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_NETCFG_V30, 0 , NetCfg))
        {
            System.out.println("Set net cfg faied!"+ " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("Set net cfg succ!");
        }
    }
    public static void Test_CompressionCfg(int iUserID, int iChan)
    {
        NET_DVR_COMPRESSIONCFG_V30 CompressionCfg = new NET_DVR_COMPRESSIONCFG_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_COMPRESSCFG_V30, iChan, CompressionCfg))
        {
            System.out.println("get CompressionCfg failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("get CompressionCfg succ! resolution: " + CompressionCfg.struNetPara.byResolution);
        }
        CompressionCfg.struNetPara.dwVideoFrameRate = 1;
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,HCNetSDK.NET_DVR_SET_COMPRESSCFG_V30, iChan, CompressionCfg))
        {
            System.out.println("Set CompressionCfg failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("Set CompressionCfg succ!" );
        }
    }
    public static void Test_CompressCfgAud(int iUserID)
    {
        NET_DVR_COMPRESSION_AUDIO AudioCompress = new NET_DVR_COMPRESSION_AUDIO();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_COMPRESSCFG_AUD, 1, AudioCompress))
        {
            System.out.println("get AudioCompress failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("get AudioCompress succ! type: " + AudioCompress.byAudioEncType);
        }
    }
    public static void Test_AlarmOutStatus(int iUserID)
    {
        NET_DVR_ALARMOUTSTATUS_V30 AlarmOut = new NET_DVR_ALARMOUTSTATUS_V30();
        if (!HCNetSDK.getInstance().NET_DVR_SetAlarmOut(iUserID, 0x00ff, 1))
        {
            System.out.println("Set alarm out failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("Set alarm out succ!");
        }
        if (!HCNetSDK.getInstance().NET_DVR_GetAlarmOut_V30(iUserID, AlarmOut))
        {
            System.out.println("Get alarm out failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("Get alarm out succ!");
            for (int i=0; i<HCNetSDK.MAX_ALARMOUT_V30; i++)
            {
                System.out.print((int)AlarmOut.Output[i]);
            }
            System.out.println();
        }


//		if (!HCNetSDK.getInstance().NET_DVR_GetAlarmOut_V30(iUserID, null))
//		{
//			System.out.println("Get alarm out failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}else
//		{
//			System.out.println("Get alarm out succ!");
//			for (int i=0; i<HCNetSDK.MAX_ALARMOUT_V30; i++)
//			{
//				System.out.print((int)AlarmOut.Output[i]);
//			}
//			System.out.println();
//		}
    }
    public static void Test_VideoEffect(int iPreviewID)
    {
        NET_DVR_VIDEOEFFECT VideoEffect = new NET_DVR_VIDEOEFFECT();
        if (!HCNetSDK.getInstance().NET_DVR_ClientGetVideoEffect(iPreviewID, VideoEffect))
        {
            System.out.println("NET_DVR_ClientGetVideoEffect failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            //System.out.println("NET_DVR_ClientGetVideoEffect succ"+ VideoEffect.iBrightValue +
            ///VideoEffect.iContrastValue+VideoEffect.iSaturationValue+VideoEffect.iHueValue);
            System.out.println("NET_DVR_ClientGetVideoEffect succ"+ VideoEffect.byBrightnessLevel +
                    VideoEffect.byContrastLevel+VideoEffect.bySaturationLevel+VideoEffect.byHueLevel);
        }
        //VideoEffect.iBrightValue += 1;
        VideoEffect.byBrightnessLevel += 1;
        if(!HCNetSDK.getInstance().NET_DVR_ClientSetVideoEffect(iPreviewID, VideoEffect))
        {
            System.out.println("NET_DVR_ClientSetVideoEffect failed:" +  + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_ClientSetVideoEffect succ");
        }
    }

//	public static void Test_InquestControlCDW(int iUserId)
//	{
//		NET_DVR_INQUEST_ROOM InquestRoom = new NET_DVR_INQUEST_ROOM();
//
//		InquestRoom.byRoomIndex = 1;
//		InquestRoom.byFileType = 1;
//		if (!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserId, HCNetSDK.NET_DVR_INQUEST_PAUSE_CDW, InquestRoom))
//		{
//			System.out.println("NET_DVR_INQUEST_PAUSE_CDW" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_INQUEST_PAUSE_CDW success");
//		}
//
//		if (!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserId, HCNetSDK.NET_DVR_INQUEST_RESUME_CDW, InquestRoom))
//		{
//			System.out.println("NET_DVR_INQUEST_RESUME_CDW" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_INQUEST_RESUME_CDW success");
//		}
//
//	}

//	public static void Test_AreaMaskCfg(int iUserId, int iChan)
//	{
//		NET_DVR_AREA_MASK_CFG AreaMaskCfg = new NET_DVR_AREA_MASK_CFG();
//
//		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserId, HCNetSDK.NET_DVR_GET_AREA_MASK_CFG, 34, AreaMaskCfg))
//		{
//			System.out.println("NET_DVR_GET_AREA_MASK_CFG" + iChan + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_GET_AREA_MASK_CFG success");
//		}
//
//		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserId, HCNetSDK.NET_DVR_SET_AREA_MASK_CFG, 34, AreaMaskCfg))
//		{
//			System.out.println("NET_DVR_SET_AREA_MASK_CFG" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_SET_AREA_MASK_CFG success");
//		}
//	}

    //	public static void Test_AudioDiaCritical(int iUserId, int iChan)
//	{
//		NET_DVR_AUDIO_DIACRITICAL_CFG AudioDiaCritical = new NET_DVR_AUDIO_DIACRITICAL_CFG();
//
//		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserId, HCNetSDK.NET_DVR_GET_AUDIO_DIACRITICAL_CFG, 34, AudioDiaCritical))
//		{
//			System.out.println("NET_DVR_GET_AUDIO_DIACRITICAL_CFG" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_GET_AUDIO_DIACRITICAL_CFG success");
//		}
//
//		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserId, HCNetSDK.NET_DVR_SET_AUDIO_DIACRITICAL_CFG, 34, AudioDiaCritical))
//		{
//			System.out.println("NET_DVR_SET_AUDIO_DIACRITICAL_CFG" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_SET_AUDIO_DIACRITICAL_CFG success");
//		}
//	}
    public static void Test_Preview_display(int iUserID, int iChan)
    {
//		NET_DVR_PREVIEW_DISPLAYCFG struPreviewDisplay = new NET_DVR_PREVIEW_DISPLAYCFG();
//		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_PREVIEW_DISPLAYCFG, 1, struPreviewDisplay))
//		{
//			System.out.println("get Preview Display failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}else
//		{
//			System.out.println("get Preview Display succ! Mode: " + struPreviewDisplay.byCorrectMode + " MountType:" + struPreviewDisplay.byMountType);
//			System.out.println("get Preview Display succ! RealTimeOutput: " + struPreviewDisplay.byRealTimeOutput);
//		}
//		//Set
//		struPreviewDisplay.byMountType = 2;
//		struPreviewDisplay.byRealTimeOutput = 3;
//		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_PREVIEW_DISPLAYCFG, 1, struPreviewDisplay))
//		{
//			System.out.println("Set Preview Display failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_SET_PREVIEW_DISPLAYCFG succ");
//		}
        NET_DVR_PREVIEW_DISPLAYCFG struPreviewDisplay = new NET_DVR_PREVIEW_DISPLAYCFG();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_PREVIEW_DISPLAYCFG, 1, struPreviewDisplay))
        {
            System.out.println("get Preview Display failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("get Preview Display succ! Mode: " + struPreviewDisplay.byCorrectMode + " MountType:" + struPreviewDisplay.byMountType);
            System.out.println("get Preview Display succ! RealTimeOutput: " + struPreviewDisplay.byRealTimeOutput);
        }
        //Set
        //struPreviewDisplay.byMountType = 2;
        //struPreviewDisplay.byRealTimeOutput = 3;
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_PREVIEW_DISPLAYCFG, 1, struPreviewDisplay))
        {
            System.out.println("Set Preview Display failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_PREVIEW_DISPLAYCFG succ");
        }
    }
    public static void Text_FISHEYE_ABILITY(int iUserID)
    {

        byte[] arrayOutBuf = new byte[64*1024];
        INT_PTR intPtr = new INT_PTR();
        String strInput = new String("<FishEyeIPCAbility version==\"2.0\"><channelNO>1</channelNO></FishEyeIPCAbility>");
        byte[] arrayInBuf = new byte[8*1024];
        arrayInBuf = strInput.getBytes();
        if(!HCNetSDK.getInstance().NET_DVR_GetXMLAbility(iUserID, HCNetSDK.FISHEYE_ABILITY,arrayInBuf, strInput.length(), arrayOutBuf, 64*1024, intPtr))
        {
            System.out.println("get FISHEYE_ABILITY faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("get FISHEYE_ABILITY succ!");
        }
    }
    public static void Test_CAMERAPARAMCFG_EX(int iUserID, int iChan)
    {
        NET_DVR_CAMERAPARAMCFG_EX struCameraParamCfgEX = new NET_DVR_CAMERAPARAMCFG_EX();
        int i = 0;
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_CCDPARAMCFG_EX, iChan, struCameraParamCfgEX))
        {
            System.out.println("NET_DVR_GET_CCDPARAMCFG_EX failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_CCDPARAMCFG_EX succ!");
        }
        struCameraParamCfgEX.struVideoEffect.byBrightnessLevel = 39;
        struCameraParamCfgEX.struVideoEffect.byContrastLevel = 80;
        struCameraParamCfgEX.struVideoEffect.bySaturationLevel = 26;
        struCameraParamCfgEX.struVideoEffect.bySharpnessLevel = 82;
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_CCDPARAMCFG_EX, iChan, struCameraParamCfgEX))
        {
            System.out.println("NET_DVR_SET_CCDPARAMCFG_EX!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_CCDPARAMCFG_EX succ");
        }
        i = HCNetSDK.getInstance().NET_DVR_GetLastError();
    }
    public static void Test_WIRELESSDIAL_CFG(int iUserID)
    {
        NET_DVR_COND_INT struCardNo = new NET_DVR_COND_INT();
        struCardNo.iValue = 1;
        NET_DVR_WIRELESSSERVER_FULLVERSION_CFG struWirelessDialCfg = new NET_DVR_WIRELESSSERVER_FULLVERSION_CFG();
        int i = 0;
        if(!HCNetSDK.getInstance().NET_DVR_GetSTDConfig(iUserID, HCNetSDK.NET_DVR_GET_WIRELESSSERVER_FULLVERSION_CFG, struCardNo, null, struWirelessDialCfg)){
            System.out.println("NET_DVR_GET_WIRELESSSERVER_FULLVERSION_CFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            System.out.println("NET_DVR_GET_WIRELESSSERVER_FULLVERSION_CFG succ!");
        }
        i = HCNetSDK.getInstance().NET_DVR_GetLastError();
        if(!HCNetSDK.getInstance().NET_DVR_SetSTDConfig(iUserID, HCNetSDK.NET_DVR_SET_WIRELESSSERVER_FULLVERSION_CFG, struCardNo, struWirelessDialCfg, null)){
            System.out.println("NET_DVR_SET_WIRELESSSERVER_FULLVERSION_CFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            System.out.println("NET_DVR_SET_WIRELESSSERVER_FULLVERSION_CFG succ!");
        }
        i = HCNetSDK.getInstance().NET_DVR_GetLastError();
    }
    public static void Test_PostRadar_Capabilities(int iUserID)
    {
        byte[] byAbility = new byte[1*1024*1024];
        INT_PTR iRetLen = new INT_PTR();
        iRetLen.iValue = 0;
        int i = 0;
        if (!HCNetSDK.getInstance().NET_DVR_GetSTDAbility(iUserID, HCNetSDK.NET_DVR_GET_POSTRADAR_CAPABILITIES, null, 0, byAbility, 1024*1024, iRetLen))
        {
            System.out.println("NET_DVR_GET_POSTRADAR_CAPABILITIES" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_POSTRADAR_CAPABILITIES success");
        }
        i = HCNetSDK.getInstance().NET_DVR_GetLastError();
    }


    public static void Test_Its_Overlap_Cfg_V50(int iUserID, int iChan)
    {
		/*
		NET_DVR_MULTI_STREAM_COMPRESSIONCFG_COND struItsOverlapCfgCond = new NET_DVR_MULTI_STREAM_COMPRESSIONCFG_COND();
		struItsOverlapCfgCond.struStreamInfo.dwChannel = iChan;
		struItsOverlapCfgCond.dwStreamType = 1;
		NET_DVR_MULTI_STREAM_COMPRESSIONCFG struItsOverlapCfgV50 = new NET_DVR_MULTI_STREAM_COMPRESSIONCFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID,HCNetSDK.NET_DVR_GET_MULTI_STREAM_COMPRESSIONCFG, struItsOverlapCfgCond, struItsOverlapCfgV50))
		{
			System.out.println("NET_ITS_GET_OVERLAP_CFG_V50 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_ITS_GET_OVERLAP_CFG_V50 succ: ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		if(!HCNetSDK.getInstance().NET_DVR_SetDeviceConfig(iUserID, HCNetSDK.NET_DVR_SET_MULTI_STREAM_COMPRESSIONCFG, struItsOverlapCfgCond, struItsOverlapCfgV50))
		{
			System.out.println("NET_ITS_SET_OVERLAP_CFG_V50 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_ITS_SET_OVERLAP_CFG_V50 succ");
		}
		*/
    }
    public static void TextOSD(int iUserID, int iChan)
    {
        NET_VCA_FACESNAPCFG struPreviewDisplay = new NET_VCA_FACESNAPCFG();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_FACESNAPCFG, 1, struPreviewDisplay))
        {
            System.out.println("NET_DVR_GET_FACESNAPCFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_FACESNAPCFG succ");
        }
        //Set
        //struPreviewDisplay.byMountType = 2;
        //struPreviewDisplay.byRealTimeOutput = 3;
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_FACESNAPCFG, 1, struPreviewDisplay))
        {
            System.out.println("NET_DVR_SET_FACESNAPCFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_FACESNAPCFG succ");
        }
    }

    public static void Test_EzvizCreate()
    {
        NET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO struLoginInfo = new NET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO();
        NET_DVR_DEVICEINFO_V30 struDeviceInfo = new NET_DVR_DEVICEINFO_V30();

        String strInput = new String("open.ys7.com");
        byte[] byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sEzvizServerAddress, 0, byInput.length);

        struLoginInfo.wPort = 443;

        strInput = new String("at.8gebs2w79fiz3km4arwjec9i3kblmlpi-13ifmla84v-1j7webb-an1n5tl3w");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sAccessToken, 0, byInput.length);

        strInput = new String("ae1b9af9dcac4caeb88da6dbbf2dd8d5");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sAppID, 0, byInput.length);

        strInput = new String("78313dadecd92bd11623638d57aa5128");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sFeatureCode, 0, byInput.length);

        strInput = new String("https://open.ys7.com:443/api/device/transmission");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sUrl, 0, byInput.length);

        strInput = new String("201606271");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sDeviceID, 0, byInput.length);

        strInput = new String("0");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sClientType, 0, byInput.length);

        strInput = new String("UNKNOWN");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sNetType, 0, byInput.length);

        strInput = new String("5.0.1");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sOsVersion, 0, byInput.length);

        strInput = new String("v.5.1.5.30");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struLoginInfo.sSdkVersion, 0, byInput.length);
        int iUserID = -1;
//        int iUserID = HCNetSDK.getInstance().NET_DVR_CreateOpenEzvizUser(struLoginInfo, struDeviceInfo);
        //if (-1 == iUserID)
//    	{
//    		System.out.println("NET_DVR_CreateEzvizUser" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//    	else
//    	{
//			System.out.println("NET_DVR_CreateEzvizUser success");
//		}
//
//		NET_DVR_XML_CONFIG_INPUT    struInput = new NET_DVR_XML_CONFIG_INPUT();
//        NET_DVR_XML_CONFIG_OUTPUT   struOuput = new NET_DVR_XML_CONFIG_OUTPUT();
//        strInput = new String("GET /ISAPI/SecurityCP/AlarmControlByPhone\r\n");
//        byInput = strInput.getBytes();
//        System.arraycopy(byInput, 0, struInput.lpRequestUrl, 0, byInput.length);
//        struInput.dwRequestUrlLen = byInput.length;
//        struOuput.dwOutBufferSize = HCNetSDK.MAX_XML_CONFIG_LEN;
//        struOuput.dwStatusSize = struOuput.dwOutBufferSize;
//        if (!HCNetSDK.getInstance().NET_DVR_STDXMLConfig(iUserID, struInput, struOuput))
//		{
//			System.out.println("NET_DVR_STDXMLConfig" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_STDXMLConfig success");
//		}
//        byte[] byOutput = new byte[struOuput.dwReturnedXMLSize];
//        System.arraycopy(struOuput.lpOutBuffer, 0, byOutput, 0, struOuput.dwReturnedXMLSize);
//        System.out.println(new String(byOutput));
//
//        struInput = new NET_DVR_XML_CONFIG_INPUT();
//        struOuput = new NET_DVR_XML_CONFIG_OUTPUT();
//
//        strInput = new String("PUT /ISAPI/VideoIntercom/passwordAuthentication\r\n");
//        byInput = strInput.getBytes();
//        System.arraycopy(byInput, 0, struInput.lpRequestUrl, 0, byInput.length);
//        struInput.dwRequestUrlLen = byInput.length;
//
//        String strInBuffer = new String("<PasswordAuthenticationCfg version=\"2.0\" xmlns=\"http://www.isapi.org/ver20/XMLSchema\">\r\n<password>123456</password>\r\n</PasswordAuthenticationCfg>\r\n");
//        byte[] byInBuffer = strInBuffer.getBytes();
//        System.arraycopy(byInBuffer,  0, struInput.lpInBuffer, 0, byInBuffer.length);
//        struInput.dwInBufferSize = strInBuffer.length();
//        struOuput.dwOutBufferSize = HCNetSDK.MAX_XML_CONFIG_LEN;
//        struOuput.dwStatusSize = struOuput.dwOutBufferSize;
//
//        if (!HCNetSDK.getInstance().NET_DVR_STDXMLConfig(iUserID, struInput, struOuput))
//		{
//			System.out.println("NET_DVR_STDXMLConfig" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_STDXMLConfig success");
//		}
        /*
        NET_DVR_VIDEO_CALL_PARAM struTest1 = new NET_DVR_VIDEO_CALL_PARAM();
		//Set
        struTest1.dwCmdType = 0;
        struTest1.wUnitNumber = 2;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_CALL_SIGNAL, 1, struTest1))
		{
			System.out.println("NET_DVR_SET_FACESNAPCFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_FACESNAPCFG succ");
		}

		NET_DVR_CONTROL_GATEWAY struTest2 = new NET_DVR_CONTROL_GATEWAY();
		//Set
		struTest2.byCommand = 0;
		struTest2.byControlType = 1;
		String strTempString = new String("123456");
		byte[] byTemp = strTempString.getBytes();
        System.arraycopy(byTemp, 0, struTest2.byPassword, 0, byTemp.length);
		if(!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID, HCNetSDK.NET_DVR_REMOTECONTROL_GATEWAY, struTest2))
		{
			System.out.println("NET_DVR_SET_FACESNAPCFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_FACESNAPCFG succ");
		}*/

        NET_DVR_ALARMIN_PARAM struTest3 = new NET_DVR_ALARMIN_PARAM();
        //Set
        struTest3.byAlarmType = 1;
        struTest3.wDetectorType = 1;
        struTest3.byType = 0;
        String strName = new String("123456");
        byte[] byName = strName.getBytes();
        System.arraycopy(byName, 0, struTest3.byName, 0, byName.length);
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_ALARMIN_PARAM, 1, struTest3))
        {
            System.out.println("NET_DVR_SET_ALARMIN_PARAM failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_ALARMIN_PARAM succ");
        }

        NET_DVR_MULTI_ALARMIN_COND struCond = new NET_DVR_MULTI_ALARMIN_COND();
        NET_DVR_ALARMIN_PARAM_LIST struList = new NET_DVR_ALARMIN_PARAM_LIST();

        for (int k = 0; k < 64; k++)
        {
            struCond.iZoneNo[k] = -1;
        }
        for(int m = 0; m < 32; m++)
        {
            struCond.iZoneNo[m] = m;
        }

        if (!HCNetSDK.getInstance().NET_DVR_GetSTDConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMIN_PARAM_LIST, struCond, null, struList))
        {
            System.out.println("NET_DVR_GetSTDConfig" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GetSTDConfig success");
        }

        NET_DVR_CALL_STATUS struCallStatus = new NET_DVR_CALL_STATUS();
        if (!HCNetSDK.getInstance().NET_DVR_GetDeviceStatus(iUserID, HCNetSDK.NET_DVR_GET_CALL_STATUS, null, struCallStatus))
        {
            System.out.println("NET_DVR_GET_CALL_STATUS failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_GET_CALL_STATUS succ");
        }
    }

    public static void Test_EzvizXMLConfig(int iUserID)
    {
        NET_DVR_XML_CONFIG_INPUT    struInput = new NET_DVR_XML_CONFIG_INPUT();
        NET_DVR_XML_CONFIG_OUTPUT   struOuput = new NET_DVR_XML_CONFIG_OUTPUT();
        String strInput = new String("GET /ISAPI/SecurityCP/AlarmControlByPhone\r\n");
        byte[] byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struInput.lpRequestUrl, 0, byInput.length);
        struInput.dwRequestUrlLen = byInput.length;
        struOuput.dwOutBufferSize = HCNetSDK.MAX_XML_CONFIG_LEN;
        struOuput.dwStatusSize = struOuput.dwOutBufferSize;
        //struInput.lpRequestUrl = strInput.getBytes();
        //struInput.lpInBuffer = null;
        if (!HCNetSDK.getInstance().NET_DVR_STDXMLConfig(iUserID, struInput, struOuput))
        {
            System.out.println("NET_DVR_STDXMLConfig" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_STDXMLConfig success");
        }
        byte[] byOutput = new byte[struOuput.dwReturnedXMLSize];
        System.arraycopy(struOuput.lpOutBuffer, 0, byOutput, 0, struOuput.dwReturnedXMLSize);
        System.out.println(new String(byOutput));

        //Set
        struInput = new NET_DVR_XML_CONFIG_INPUT();
        struOuput = new NET_DVR_XML_CONFIG_OUTPUT();
        strInput = new String("PUT /ISAPI/SecurityCP/AlarmControlByPhone\r\n");
        byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struInput.lpRequestUrl, 0, byInput.length);
        struInput.dwRequestUrlLen = byInput.length;
        String strInBuffer = new String("<AlarmControlByPhoneCfg version=\"2.0\" xmlns=\"http://www.isapi.org/ver20/XMLSchema\">\r\n<commandType>closeAlarm</commandType>\r\n</AlarmControlByPhoneCfg>\r\n");
        byte[] byInBuffer = strInBuffer.getBytes();
        System.arraycopy(byInBuffer,  0, struInput.lpInBuffer, 0, byInBuffer.length);
        struInput.dwInBufferSize = strInBuffer.length();
        struOuput.dwOutBufferSize = HCNetSDK.MAX_XML_CONFIG_LEN;
        struOuput.dwStatusSize = struOuput.dwOutBufferSize;
        if (!HCNetSDK.getInstance().NET_DVR_STDXMLConfig(iUserID, struInput, struOuput))
        {
            System.out.println("NET_DVR_STDXMLConfig" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_STDXMLConfig success");
            //strOutput = new String(struOuput.lpStatusBuffer);
        }
    }

    public static void Test_EzvizServerDeviceInfo(int iUserID)
    {
        NET_DVR_SERVER_DEVICE_INFO struServerDeviceInfo = new NET_DVR_SERVER_DEVICE_INFO();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_SERVER_DEVICE_INFO, 0, struServerDeviceInfo))
        {
            System.out.println("NET_DVR_GET_SERVER_DEVICE_INFO failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_SERVER_DEVICE_INFO succ" + "deviceNum: " + struServerDeviceInfo.dwDeviceNum);
        }

        NET_DVR_CALLER_INFO struCallerInfo = new NET_DVR_CALLER_INFO();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_CALLER_INFO, 1, struCallerInfo))
        {
            System.out.println("NET_DVR_GET_CALLER_INFO failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_CALLER_INFO succ" + "BuildingNo: " + struCallerInfo.wBuildingNo);
        }
    }

    public static void Test_EzvizAlarmInParamList(int iUserID)
    {
        NET_DVR_MULTI_ALARMIN_COND struCond = new NET_DVR_MULTI_ALARMIN_COND();
        for (int k = 0; k < 64; k++)
        {
            struCond.iZoneNo[k] = -1;
        }
        for(int m = 0; m < 32; m++)
        {
            struCond.iZoneNo[m] = m;
        }
        NET_DVR_ALARMIN_PARAM_LIST struAlarmInParam = new NET_DVR_ALARMIN_PARAM_LIST();

        if(!HCNetSDK.getInstance().NET_DVR_GetSTDConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMIN_PARAM_LIST, struCond, null, struAlarmInParam))
        {
            System.out.println("NET_DVR_GET_ALARMIN_PARAM_LIST faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_ALARMIN_PARAM_LIST succ!" + "type: " + struAlarmInParam.struSingleAlarmInParam[0].byType);
        }
    }

    public static void Test_EzvizCallerInfo(int iUserID)
    {
        NET_DVR_CALLER_INFO struCallerInfo = new NET_DVR_CALLER_INFO();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_CALLER_INFO, 1, struCallerInfo))
        {
            System.out.println("NET_DVR_GET_CALLER_INFO failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_CALLER_INFO succ" + "BuildingNo: " + struCallerInfo.wBuildingNo);
        }
    }

    public static void Test_EzvizRemoteGatway(int iUserID)
    {
        NET_DVR_CONTROL_GATEWAY struTest2 = new NET_DVR_CONTROL_GATEWAY();
        //Set
        struTest2.byCommand = 0;
        struTest2.byControlType = 1;
        String strTempString = new String("123456");
        byte[] byTemp = strTempString.getBytes();
        System.arraycopy(byTemp, 0, struTest2.byPassword, 0, byTemp.length);
        if(!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID, HCNetSDK.NET_DVR_REMOTECONTROL_GATEWAY, struTest2))
        {
            System.out.println("NET_DVR_REMOTECONTROL_GATEWAY failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_REMOTECONTROL_GATEWAY succ");
        }
    }

    public static void Test_EzvizCallSignal(int iUserID)
    {
        NET_DVR_VIDEO_CALL_PARAM struTest1 = new NET_DVR_VIDEO_CALL_PARAM();
        //Set
        struTest1.dwCmdType = 0;
        struTest1.wUnitNumber = 2;
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_CALL_SIGNAL, 1, struTest1))
        {
            System.out.println("NET_DVR_SET_CALL_SIGNAL failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_CALL_SIGNAL succ");
        }
    }

    public static void Test_EzvizPawdAuth(int iUserID)
    {
        NET_DVR_XML_CONFIG_INPUT    struInput = new NET_DVR_XML_CONFIG_INPUT();
        NET_DVR_XML_CONFIG_OUTPUT   struOuput = new NET_DVR_XML_CONFIG_OUTPUT();

        String strInput = new String("PUT /ISAPI/VideoIntercom/passwordAuthentication\r\n");
        byte[] byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struInput.lpRequestUrl, 0, byInput.length);
        struInput.dwRequestUrlLen = byInput.length;

        String strInBuffer = new String("<PasswordAuthenticationCfg version=\"2.0\" xmlns=\"http://www.isapi.org/ver20/XMLSchema\">\r\n<password>123456</password>\r\n</PasswordAuthenticationCfg>\r\n");
        byte[] byInBuffer = strInBuffer.getBytes();
        System.arraycopy(byInBuffer,  0, struInput.lpInBuffer, 0, byInBuffer.length);
        struInput.dwInBufferSize = strInBuffer.length();
        struOuput.dwOutBufferSize = HCNetSDK.MAX_XML_CONFIG_LEN;
        struOuput.dwStatusSize = struOuput.dwOutBufferSize;
        if (!HCNetSDK.getInstance().NET_DVR_STDXMLConfig(iUserID, struInput, struOuput))
        {
            System.out.println("NET_DVR_STDXMLConfig PUT /ISAPI/VideoIntercom/passwordAuthentication" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_STDXMLConfig PUT /ISAPI/VideoIntercom/passwordAuthentication success");
            //strOutput = new String(struOuput.lpStatusBuffer);
        }
    }

    public static void TestAlarmHostMainStatus(int iUserID)
    {
        NET_DVR_ALARMHOST_MAIN_STATUS_V40 struStatus = new NET_DVR_ALARMHOST_MAIN_STATUS_V40();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_ALARMHOST_MAIN_STATUS_V40, 0, struStatus))
        {
            System.out.println("NET_DVR_GET_ALARMHOST_MAIN_STATUS_V40 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_ALARMHOST_MAIN_STATUS_V40 succ");
        }
    }

    //mutli
    public static void TestScreenConfig(int iUserID)
    {
//		NET_DVR_ALARMHOST_MAIN_STATUS_V40 struStatus = new NET_DVR_ALARMHOST_MAIN_STATUS_V40();
//		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_ALARMHOST_MAIN_STATUS_V40, 0, struStatus))
//		{
//			System.out.println("NET_DVR_GET_ALARMHOST_MAIN_STATUS_V40 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}else
//		{
//			System.out.println("NET_DVR_GET_ALARMHOST_MAIN_STATUS_V40 succ");
//		}
    }

    public static void TestMCUAbility(int iUserID)
    {
        byte lpInBuf [] = new byte[1024];
        byte lpOutBuf[] = new byte[100*1024*10];

        INT_PTR iRetLen = new INT_PTR();
        //iRetLen.iValue = 0;
        iRetLen.iValue = lpOutBuf.length;
        if(HCNetSDK.getInstance().NET_DVR_GetSTDAbility(iUserID ,9152,null,0,lpOutBuf,100*1024*100,iRetLen))
        {
            System.out.println("NET_DVR_GetSTDAbility Success!" );
        }
        else
        {
            System.out.println("NET_DVR_GetSTDAbility Failed!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
        }
    }

    public static void TextTrialMachine(int iUserID, int iChan)
    {
		/*
		NET_DVR_AUDIO_ACTIVATION_CFG struAudioActivation = new NET_DVR_AUDIO_ACTIVATION_CFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_AUDIO_ACTIVATION_CFG, 1, struAudioActivation))
		{
			System.out.println("NET_DVR_GET_AUDIO_ACTIVATION_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_AUDIO_ACTIVATION_CFG succ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_AUDIO_ACTIVATION_CFG, 1, struAudioActivation))
		{
			System.out.println("NET_DVR_SET_AUDIO_ACTIVATION_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_AUDIO_ACTIVATION_CFG succ");
		}

		NET_DVR_AUDIO_DIACRITICAL_CFG struAudioDiacriticl = new NET_DVR_AUDIO_DIACRITICAL_CFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_AUDIO_DIACRITICAL_CFG, 33, struAudioDiacriticl))
		{
			System.out.println("NET_DVR_GET_AUDIO_DIACRITICAL_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_AUDIO_DIACRITICAL_CFG succ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		struAudioDiacriticl.byEnable = 1;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_AUDIO_DIACRITICAL_CFG, 33, struAudioDiacriticl))
		{
			System.out.println("NET_DVR_SET_AUDIO_DIACRITICAL_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_AUDIO_DIACRITICAL_CFG succ");
		}

		NET_DVR_AREA_MASK_CFG struAreaMask = new NET_DVR_AREA_MASK_CFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_AREA_MASK_CFG, 33, struAreaMask))
		{
			System.out.println("NET_DVR_GET_AREA_MASK_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_AREA_MASK_CFG succ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		struAreaMask.byEnable = 1;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_AREA_MASK_CFG, 33, struAreaMask))
		{
			System.out.println("NET_DVR_SET_AREA_MASK_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_AREA_MASK_CFG succ");
		}

		NET_DVR_VOLUME_CFG struVolumeIn = new NET_DVR_VOLUME_CFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_AUDIOIN_VOLUME_CFG, 1, struVolumeIn))
		{
			System.out.println("NET_DVR_GET_AUDIOIN_VOLUME_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_AUDIOIN_VOLUME_CFG succ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_AUDIOIN_VOLUME_CFG, 1, struVolumeIn))
		{
			System.out.println("NET_DVR_SET_AUDIOIN_VOLUME_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_AUDIOIN_VOLUME_CFG succ");
		}

		NET_DVR_VOLUME_CFG struVolumeOut = new NET_DVR_VOLUME_CFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_AUDIOOUT_VOLUME_CFG, 1, struVolumeOut))
		{
			System.out.println("NET_DVR_GET_AUDIOOUT_VOLUME_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_AUDIOOUT_VOLUME_CFG succ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_AUDIOOUT_VOLUME_CFG, 1, struVolumeOut))
		{
			System.out.println("NET_DVR_SET_AUDIOOUT_VOLUME_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_AUDIOOUT_VOLUME_CFG succ");
		}
		*/
        NET_DVR_INFRARED_CMD_NAME_CFG struInfraredCmdName = new NET_DVR_INFRARED_CMD_NAME_CFG();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_INFRARED_CMD_NAME_CFG, 1, struInfraredCmdName))
        {
            System.out.println("NET_DVR_GET_INFRARED_CMD_NAME_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_INFRARED_CMD_NAME_CFG succ");
        }
        //Set
        //struPreviewDisplay.byMountType = 2;
        //struPreviewDisplay.byRealTimeOutput = 3;
        if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_INFRARED_CMD_NAME_CFG, 1, struInfraredCmdName))
        {
            System.out.println("NET_DVR_SET_INFRARED_CMD_NAME_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("NET_DVR_SET_INFRARED_CMD_NAME_CFG succ");
        }
		/*

		NET_DVR_USER_V30 struUserV30 = new NET_DVR_USER_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_USERCFG_V30, 1, struUserV30))
		{
			System.out.println("NET_DVR_GET_USERCFG_V30 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_USERCFG_V30 succ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_USERCFG_V30, 1, struUserV30))
		{
			System.out.println("NET_DVR_SET_USERCFG_V30 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_USERCFG_V30 succ");
		}

		NET_DVR_TRIAL_SYSTEM_INFO struTrialSystemInfo = new NET_DVR_TRIAL_SYSTEM_INFO();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.NET_DVR_GET_TRIAL_SYSTEM_CFG, 1, struTrialSystemInfo))
		{
			System.out.println("NET_DVR_GET_TRIAL_SYSTEM_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_TRIAL_SYSTEM_CFG succ");
		}

		NET_DVR_INQUEST_ROOM struInquestRoom = new NET_DVR_INQUEST_ROOM();
		struInquestRoom.byRoomIndex = 1;
		if (!HCNetSDK.getInstance().NET_DVR_InquestStartCDW_V30(iUserID, struInquestRoom, false))
		{
			System.out.println("NET_DVR_InquestStartCDW_V30 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_InquestStartCDW_V30 succ");
		}

		if (!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID, HCNetSDK.NET_DVR_INQUEST_PAUSE_CDW, struInquestRoom))
		{
			System.out.println("NET_DVR_INQUEST_PAUSE_CDW" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_INQUEST_PAUSE_CDW success");
		}

		if (!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID, HCNetSDK.NET_DVR_INQUEST_RESUME_CDW, struInquestRoom))
		{
			System.out.println("NET_DVR_INQUEST_RESUME_CDW" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_INQUEST_RESUME_CDW success");
		}

		if (!HCNetSDK.getInstance().NET_DVR_InquestStopCDW_V30(iUserID, struInquestRoom, false))
		{
			System.out.println("NET_DVR_InquestStopCDW_V30 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_InquestStopCDW_V30 succ");
		}


		NET_DVR_INQUEST_PIP_STATUS_V40 struInquestPipStatus_V40 = new NET_DVR_INQUEST_PIP_STATUS_V40();
		if (!HCNetSDK.getInstance().NET_DVR_InquestGetPIPStatus_V40(iUserID, struInquestRoom, struInquestPipStatus_V40))
		{
			System.out.println("NET_DVR_InquestGetPIPStatus_V40 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_InquestGetPIPStatus_V40 succ");
		}
		if (!HCNetSDK.getInstance().NET_DVR_InquestSetPIPStatus_V40(iUserID, struInquestRoom, struInquestPipStatus_V40))
		{
			System.out.println("NET_DVR_InquestSetPIPStatus_V40 failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_InquestSetPIPStatus_V40 succ");
		}

		NET_DVR_INFRARED_OUTPUT_CTRL_CFG struInfraredOutputCtrlCfg = new NET_DVR_INFRARED_OUTPUT_CTRL_CFG();
		struInfraredOutputCtrlCfg.byIRCmdIndex = 1;
		struInfraredOutputCtrlCfg.byIROutPort = 1;
		if (!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserID, HCNetSDK.NET_DVR_INFRARED_OUTPUT_CONTROL, struInfraredOutputCtrlCfg))
		{
			System.out.println("NET_DVR_INFRARED_OUTPUT_CONTROL" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_INFRARED_OUTPUT_CONTROL success");
		}


		NET_DVR_PREVIEW_SWITCH_COND struPreviewSwitchCond = new NET_DVR_PREVIEW_SWITCH_COND();
		NET_DVR_PREVIEW_SWITCH_CFG struPreviewSwitchCfg= new NET_DVR_PREVIEW_SWITCH_CFG();
		struPreviewSwitchCond.byGroup = 0;
		struPreviewSwitchCond.byVideoOutType = 1;
		if (!HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, NET_DVR_DEVICE_CONFIG_COMMAND.NET_DVR_GET_PREVIEW_SWITCH_CFG, struPreviewSwitchCond, struPreviewSwitchCfg))
		{
			System.out.println("NET_DVR_GET_PREVIEW_SWITCH_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_PREVIEW_SWITCH_CFG succ: ");
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		for (int i = 0; i < HCNetSDK.MAX_WINDOW_V40; i++)
		{
			struPreviewSwitchCfg.wSwitchSeq[i] = 0xFFFF;
		}
		if(!HCNetSDK.getInstance().NET_DVR_SetDeviceConfig(iUserID,  NET_DVR_DEVICE_CONFIG_COMMAND.NET_DVR_SET_PREVIEW_SWITCH_CFG, struPreviewSwitchCond, struPreviewSwitchCfg))
		{
			System.out.println("NET_DVR_SET_PREVIEW_SWITCH_CFG failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_PREVIEW_SWITCH_CFG succ");
		}
		*/
    }

    public static void TestWIRELESSDIAL_CFG(int iUserID, int iChan)
    {
        NET_DVR_WIRELESSDIAL_CFG struWirelessDial = new NET_DVR_WIRELESSDIAL_CFG();
        NET_DVR_COND_INT condInt = new NET_DVR_COND_INT();
        condInt.iValue = 1;

//	       struWirelessDial.

        String strInput = new String("鐤�");
        byte[] byInput = strInput.getBytes();
        System.arraycopy(byInput, 0, struWirelessDial.byUserName, 0, byInput.length);
//	       struWirelessDial.byUserName = "鐤�";

        if (!HCNetSDK.getInstance().NET_DVR_SetSTDConfig(iUserID,HCNetSDK.NET_DVR_SET_WIRELESS_DIAL, condInt, struWirelessDial, null))
        {
            System.out.println("NET_DVR_GET_WIRELESS_DIAL failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_WIRELESS_DIAL succ" + struWirelessDial.byUserName.toString());
        }

    }

    public static void Text_Trail_ABILITY(int iUserID)
    {

        byte[] arrayOutBuf = new byte[64*1024];
        INT_PTR intPtr = new INT_PTR();
        String strInput = new String("<TrialHostAbility version='2.0'></TrialHostAbility>");
        byte[] arrayInBuf = new byte[8*1024];
        arrayInBuf = strInput.getBytes();
        if(!HCNetSDK.getInstance().NET_DVR_GetXMLAbility(iUserID, HCNetSDK.DEVICE_ABILITY_INFO,arrayInBuf, strInput.length(), arrayOutBuf, 64*1024, intPtr))
        {
            System.out.println("get Trail_ABILITY faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            System.out.println("get Trail_ABILITY succ!");
        }
    }

    public static void Test_LEDArea(int iUserID)
    {
        HCNetSDKByJNA.NET_DVR_STD_CONFIG struSTDConfig = new HCNetSDKByJNA.NET_DVR_STD_CONFIG();

        HCNetSDKByJNA.NET_DVR_LED_AREA_COND struCond = new HCNetSDKByJNA.NET_DVR_LED_AREA_COND();
        struCond.dwSize = struCond.size();
        struCond.dwVideoWallNo = 1;
        struCond.write();

        HCNetSDKByJNA.NET_DVR_LED_AREA_INFO struArea = new HCNetSDKByJNA.NET_DVR_LED_AREA_INFO();
        struArea.dwSize = struArea.size();
        struArea.write();

        HCNetSDKByJNA.NET_DVR_LED_AREA_INFO_LIST struAreaList = new HCNetSDKByJNA.NET_DVR_LED_AREA_INFO_LIST();
        struAreaList.dwSize = struAreaList.size();
        struAreaList.lpstruBuffer = struArea.getPointer();
        struAreaList.dwBufferSize = struArea.dwSize;
        struAreaList.write();

        struSTDConfig.lpCondBuffer = struCond.getPointer();
        struSTDConfig.dwCondSize = struCond.dwSize;
        struSTDConfig.lpOutBuffer = struAreaList.getPointer();
        struSTDConfig.dwOutSize = struAreaList.dwSize;
        struSTDConfig.write();

        if (!HCNetSDKJNAInstance.getInstance().NET_DVR_GetSTDConfig(iUserID,HCNetSDKByJNA.NET_DVR_GET_LED_AREA_INFO_LIST, struSTDConfig.getPointer()))
        {
            System.out.println("NET_DVR_GET_LED_AREA_INFO_LIST failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            System.out.println("NET_DVR_GET_LED_AREA_INFO_LIST succ");
        }
    }


    /****************************************PictureTest******************************************************/
    //private final static String 	TAG		= "PicutreTest";
    private static int m_iHandle = -1;

    /*case0*/
    public CGReturn PicUpload(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_PICTURECFG struPic= new NET_DVR_PICTURECFG();
        struPic.byUseType = 1;
        struPic.bySequence = 1;
        struPic.sPicName[0]='a';
        struPic.sPicName[1]='b';
        struPic.dwVideoWallNo = 0x0100000;
        struPic.struBasemapCfg.byScreenIndex = 1;
        struPic.struBasemapCfg.byMapNum = 1;
        struPic.struBasemapCfg.wSourHeight = 1;
        struPic.struBasemapCfg.wSourWidth = 1;
        String sFileName = "/mnt/sdcard/Pictures/Screenshots/Screenshot_2015-10-12-21-27-05.png";

        File f = new File(sFileName);
        if (f.exists()) {
            Log.i("[NetSDKSimpleDemo]", "exist "+sFileName);

        } else {
            Log.i("[NetSDKSimpleDemo]", "not exist "+sFileName);
        }

        m_iHandle = HCNetSDK.getInstance().NET_DVR_PicUpload(iUserID, sFileName, struPic);
        if(m_iHandle == -1)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_PicUpload fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret;
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_PicUpload success");
        }

        for(int i=0;i<100;i++)
        {
            int iprogress = HCNetSDK.getInstance().NET_DVR_GetPicUploadProgress(m_iHandle);
            Log.i("[NetSDKSimpleDemo]","i="+i+",---iprogress="+iprogress);
            //Thread.sleep(10);
        }

        if(HCNetSDK.getInstance().NET_DVR_CloseUploadHandle(m_iHandle))
        {
            Log.i("[NetSDKSimpleDemo]","NET_DVR_CloseUploadHandle success");
        }
        else {
            Log.e("[NetSDKSimpleDemo]","NET_DVR_CloseUploadHandle fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case1*/
    public CGReturn BaseMap(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int lChannel = 0x01000001;
        NET_DVR_BASEMAP_WINCFG struBaseMap = new NET_DVR_BASEMAP_WINCFG();
        if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_BASEMAP_WIN_CFG, lChannel, struBaseMap))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_BASEMAP_WIN_CFG success");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_BASEMAP_WIN_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        struBaseMap.byEnable = 1;
        struBaseMap.struWinPosition.dwHeight = 1920;
        struBaseMap.struWinPosition.dwWidth = 1920;
        struBaseMap.struWinPosition.dwXCoordinate = 0;
        struBaseMap.struWinPosition.dwYCoordinate = 0;

        if(HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_BASEMAP_WIN_CFG, lChannel, struBaseMap))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SET_BASEMAP_WIN_CFG success");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SET_BASEMAP_WIN_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case2*/
    public CGReturn BasemapCfg(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int lChannel = 1;
        NET_DVR_BASEMAP_PIC_INFO struPicInfo = new NET_DVR_BASEMAP_PIC_INFO();
        if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.getInstance().NET_DVR_GET_BASEMAP_PIC_INFO,lChannel, struPicInfo))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_BASEMAP_PIC_INFO success");
        }
        else {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_BASEMAP_PIC_INFO fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
        }

        NET_DVR_BASEMAP_CONTROL_CFG strCtrlCfg = new NET_DVR_BASEMAP_CONTROL_CFG();
        lChannel = 0x01000001;
        if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_BASEMAP_CFG, lChannel, strCtrlCfg))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_GET_BASEMAP_CFG success");
        }
        else {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_GET_BASEMAP_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());

        }

        if(HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_BASEMAP_CFG, lChannel, strCtrlCfg))
        {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.i("[NetSDKSimpleDemo]","NET_DVR_SET_BASEMAP_CFG success");
        }
        else {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            Log.e("[NetSDKSimpleDemo]","NET_DVR_SET_BASEMAP_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());

        }
        return ret;
    }
    /****************************************PTZTest****************************************************************/
    /*case 0*/
    public CGReturn Test_PTZControl(int iPreviewID)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZControl(iPreviewID, PTZCommand.PAN_LEFT, 0))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControl  PAN_LEFT 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControl  PAN_LEFT 0 succ");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!HCNetSDK.getInstance().NET_DVR_PTZControl(iPreviewID, PTZCommand.PAN_LEFT, 1))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControl  PAN_LEFT 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControl  PAN_LEFT 1 succ");
        }
        return ret;
    }

    /*case 1*/
    public CGReturn Test_PTZControlWithSpeed(int iPreviewID)
    {
        CGReturn ret=new CGReturn();
        if(! HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed(iPreviewID,PTZCommand.PAN_RIGHT, 0, 4))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControlWithSpeed  PAN_RIGHT 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControlWithSpeed  PAN_RIGHT 0 succ");
        }
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(! HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed(iPreviewID,PTZCommand.PAN_RIGHT, 1, 4))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControlWithSpeed  PAN_RIGHT 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZControlWithSpeed  PAN_RIGHT 1 succ");
        }
        return ret;
    }

    /*case 2*/
    public CGReturn Test_PTZPreset(int iPreviewID)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZPreset(iPreviewID, PTZPresetCmd.GOTO_PRESET, 1))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZPreset  GOTO_PRESET faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZPreset  GOTO_PRESET succ");
        }
        return ret;
    }

    /*case 3*/
    public CGReturn Test_PTZCruise(int iPreviewID)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZCruise(iPreviewID, PTZCruiseCmd.RUN_SEQ, (byte)1,(byte)1,(short)1))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZCruise  RUN_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZCruise  RUN_SEQ succ");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!HCNetSDK.getInstance().NET_DVR_PTZCruise(iPreviewID, PTZCruiseCmd.STOP_SEQ, (byte)1,(byte)1,(short)1))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZCruise  STOP_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("PTZCruise  STOP_SEQ succ");
        }
        return ret;
    }

    /*case 4*/
    public CGReturn Test_PTZTrack(int iPreviewID)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZTrack(iPreviewID, PTZTrackCmd.RUN_CRUISE))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZTrack  RUN_CRUISE faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZTrack  RUN_CRUISE succ");
        }
        return ret;
    }

    /*case 5*/
    public CGReturn Test_PTZSelZoomIn(int iPreviewID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_POINT_FRAME strPointFrame = new NET_DVR_POINT_FRAME();
        strPointFrame.xTop = 10;
        strPointFrame.yTop = 20;
        strPointFrame.xBottom = 30;
        strPointFrame.yBottom = 40;
        if(!HCNetSDK.getInstance().NET_DVR_PTZSelZoomIn(iPreviewID, strPointFrame))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZSelZoomIn!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZSelZoomIn! succeed");
        }
        return ret;
    }

    /*case 6*/
    public CGReturn Test_PTZControl_Other(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(iUserID, iChan, PTZCommand.TILT_UP, 0))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControl_Other  TILT_UP 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControl_Other  TILT_UP 0 succ");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(iUserID, iChan, PTZCommand.TILT_UP, 1))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControl_Other  TILT_UP 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControl_Other  TILT_UP 1 succ");
        }
        return ret;
    }

    /*case 7*/
    public CGReturn Test_PTZControlWithSpeed_Other(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed_Other(iUserID, iChan,PTZCommand.PAN_RIGHT, 0, 4))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 0 succ");
        }
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed_Other(iUserID, iChan,PTZCommand.PAN_RIGHT, 1, 4))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 1 succ");
        }
        return ret;
    }

    /*case8*/
    public CGReturn Test_PTZPreset_Other(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZPreset_Other(iUserID, iChan, PTZPresetCmd.GOTO_PRESET, 1))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZPreset_Other  GOTO_PRESET faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZPreset_Other  GOTO_PRESET succ");
        }
        return ret;
    }

    /*case 9*/
    public CGReturn Test_PTZCruise_Other(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZCruise_Other(iUserID, iChan, PTZCruiseCmd.RUN_SEQ, (byte)1,(byte)1,(short)1))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZCruise_Other  RUN_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZCruise_Other  RUN_SEQ succ");
        }
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!HCNetSDK.getInstance().NET_DVR_PTZCruise_Other(iUserID, iChan, PTZCruiseCmd.STOP_SEQ, (byte)1,(byte)1,(short)1))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZCruise_Other  STOP_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZCruise_Other  STOP_SEQ succ");
        }
        return ret;
    }

    /*case 10*/
    public CGReturn Test_PTZTrack_Other(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_PTZTrack_Other(iUserID, iChan, PTZTrackCmd.RUN_CRUISE))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZTrack_Other  RUN_CRUISE faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZTrack_Other  RUN_CRUISE succ");
        }
        return ret;
    }

    /*case 11*/
    public CGReturn Test_PTZSelZoomIn_EX(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_POINT_FRAME strPointFrame = new NET_DVR_POINT_FRAME();
        strPointFrame.xTop = 10;
        strPointFrame.yTop = 20;
        strPointFrame.xBottom = 30;
        strPointFrame.yBottom = 40;
        if(!HCNetSDK.getInstance().NET_DVR_PTZSelZoomIn_EX(iUserID, iChan, strPointFrame))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZSelZoomIn_EX failed" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PTZSelZoomIn_EX! succeed");
        }
        return ret;
    }

    /****************************************OtherFunctionTest******************************************************/
    private static SerialDataCallBackV40 SerailDataCbfV40 = null;
    /*case 0*/
    public CGReturn Test_FindFile_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int iFindHandle = -1;
        NET_DVR_FILECOND lpSearchInfo = new NET_DVR_FILECOND();
        lpSearchInfo.lChannel = 1;
        lpSearchInfo.dwFileType = 0xff;
        lpSearchInfo.dwIsLocked = 0xff;
        lpSearchInfo.dwUseCardNo = 0;
        lpSearchInfo.struStartTime.dwYear = 2017;
        lpSearchInfo.struStartTime.dwMonth = 9;
        lpSearchInfo.struStartTime.dwDay = 1;
        lpSearchInfo.struStopTime.dwYear = 2017;
        lpSearchInfo.struStopTime.dwMonth = 9;
        lpSearchInfo.struStopTime.dwDay = 11;
        iFindHandle = HCNetSDK.getInstance().NET_DVR_FindFile_V30(iUserID, lpSearchInfo);
        if (iFindHandle == -1)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_FindFile_V30 failed,Error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret;
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
        }
        int findNext = 0;
        NET_DVR_FINDDATA_V30 struFindData = new NET_DVR_FINDDATA_V30();
        while (findNext != -1)
        {
            findNext = HCNetSDK.getInstance().NET_DVR_FindNextFile_V30(iFindHandle, struFindData);
            if (findNext == HCNetSDK.NET_DVR_FILE_SUCCESS)
            {
                System.out.println("~~~~~Find File" + CommonMethod.toValidString(new String(struFindData.sFileName)));
                System.out.println("~~~~~File Size" + struFindData.dwFileSize);
                System.out.println("~~~~~File Time,from" + struFindData.struStartTime.ToString());
                System.out.println("~~~~~File Time,to" + struFindData.struStopTime.ToString());
                continue;
            }
            else if (HCNetSDK.NET_DVR_FILE_NOFIND == findNext)
            {
                System.out.println("No file found");
                break;
            }
            else if (HCNetSDK.NET_DVR_NOMOREFILE == findNext)
            {
                System.out.println("All files are listed");
                break;
            }
            else if (HCNetSDK.NET_DVR_FILE_EXCEPTION == findNext)
            {
                System.out.println("Exception in searching");
                break;
            }
            else if (HCNetSDK.NET_DVR_ISFINDING == findNext)
            {
                System.out.println("NET_DVR_ISFINDING");
            }
        }
        HCNetSDK.getInstance().NET_DVR_FindClose_V30(iFindHandle);
        return ret;
    }

    /*case 1*/
    public CGReturn Test_FindFileByEvent_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_SEARCH_EVENT_PARAM struParam = new NET_DVR_SEARCH_EVENT_PARAM();
        struParam.wMajorType = MAIN_EVENT_TYPE.EVENT_MOT_DET;
        struParam.wMinorType = (short)0xffff;
        struParam.struStartTime.dwYear = 2017;
        struParam.struStartTime.dwMonth = 9;
        struParam.struStartTime.dwDay = 1;
        struParam.struEndTime.dwYear = 2017;
        struParam.struEndTime.dwMonth = 9;
        struParam.struEndTime.dwDay = 15;
        struParam.wMotDetChanNo[0] = 33;
        struParam.wMotDetChanNo[1] = (short)0xffff;

        int iFindHandle = HCNetSDK.getInstance().NET_DVR_FindFileByEvent(iUserID, struParam);
        NET_DVR_SEARCH_EVENT_RET struRet = new NET_DVR_SEARCH_EVENT_RET();
        int iRet = -1;
        if(iFindHandle >= 0)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            while(true)
            {
                iRet = HCNetSDK.getInstance().NET_DVR_FindNextEvent(iFindHandle, struRet);
                if(iRet != HCNetSDK.NET_DVR_FILE_SUCCESS && iRet != HCNetSDK.NET_DVR_ISFINDING)
                {
                    System.out.println("find next event exit with: " + iRet);
                    break;
                }
                else if(iRet == HCNetSDK.NET_DVR_FILE_SUCCESS)
                {
                    System.out.println("event type:" + struRet.wMajorType + ",starttime:" + struRet.struStartTime.dwYear +
                            "-" + struRet.struStartTime.dwMonth + "-" + struRet.struStartTime.dwDay + " " + struRet.struStartTime.dwHour +
                            ":" + struRet.struStartTime.dwMinute + ":" + struRet.struStartTime.dwSecond + ",endtime:" +
                            struRet.struEndTime.dwYear + "-" + struRet.struEndTime.dwMonth + "-" + struRet.struEndTime.dwDay +
                            " " + struRet.struEndTime.dwHour + ":" + struRet.struEndTime.dwMinute + ":" + struRet.struEndTime.dwSecond);
                }
            }
            HCNetSDK.getInstance().NET_DVR_FindClose_V30(iFindHandle);
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_FindFileByEvent failed: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 2*/
    private static boolean IsDownlaod = false;
    private static int lDownloadHandle = -1;
    public CGReturn Test_GetFileDownload_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        if(!IsDownlaod)
        {
            lDownloadHandle = HCNetSDK.getInstance().NET_DVR_GetFileByName(iUserID, new String("ch0001_02000000670000100"), "/sdcard/download.mp4");
            if(lDownloadHandle < 0)
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_GetFileByName failed: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
                return ret;
            }else{
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_GetFileByName succ!");

                byte[] lpInBuf = new byte[60];
                lpInBuf[0] = 5;

                if(!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(lDownloadHandle, PlaybackControlCommand.NET_DVR_SET_TRANS_TYPE, lpInBuf, 4, null))
                {
                    ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    System.out.println("NET_DVR_PlayBackControl_V40 fail");
                    return ret;
                }else{
                    ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    System.out.println("NET_DVR_SET_TRANS_TYPE succ!");
                }

                if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(lDownloadHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null))
                {
                    ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    Log.e(TAG, "NET_DVR_PlayBackControl_V40 failed!");
                    return ret;
                }else{
                    ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
                    System.out.println("NET_DVR_PLAYSTART succ!");
                }
                IsDownlaod = true;
            }
        }else{
            HCNetSDK.getInstance().NET_DVR_StopGetFile(lDownloadHandle);
        }
        return ret;
    }

    /*case 3*/
    public CGReturn Test_PlayBackConvert_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_TIME timeStart = new NET_DVR_TIME();
        NET_DVR_TIME timeStop = new NET_DVR_TIME();

        timeStart.dwYear = 2015;
        timeStart.dwMonth = 6;
        timeStart.dwDay = 30;
        timeStop.dwYear = 2015;
        timeStop.dwMonth = 7;
        timeStop.dwDay = 1;

        int nHandle = HCNetSDK.getInstance().NET_DVR_PlayBackByTime(iUserID, 1, timeStart, timeStop);
        if (-1 == nHandle)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_PlayBackByTime failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret;
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
        }
        NET_DVR_COMPRESSION_INFO_V30 compression = new NET_DVR_COMPRESSION_INFO_V30();
        compression.byResolution = 1;
        compression.dwVideoBitrate = 7;
//        HCNetSDK.getInstance().NET_DVR_PlayBackControl_V50(nHandle, PlaybackControlCommand.NET_DVR_PLAY_CONVERT, compression, null);
        HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null);
        int nProgress = -1;
        while(true)
        {
            nProgress = HCNetSDK.getInstance().NET_DVR_GetPlayBackPos(nHandle);
            System.out.println("NET_DVR_GetPlayBackPos:" + nProgress);
            if(nProgress < 0 || nProgress >= 100)
            {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        HCNetSDK.getInstance().NET_DVR_StopPlayBack(nHandle);
        return ret;
    }

    /*case 4*/
    public CGReturn Test_GetFileByTime_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_TIME timeStart = new NET_DVR_TIME();
        NET_DVR_TIME timeStop = new NET_DVR_TIME();

        timeStart.dwYear = 2017;
        timeStart.dwMonth = 9;
        timeStart.dwDay = 11;
        timeStart.dwHour = 10;
        timeStart.dwMinute = 1;
        timeStop.dwYear = 2017;
        timeStop.dwMonth = 9;
        timeStop.dwDay = 11;
        timeStop.dwHour = 10;
        timeStop.dwMinute = 5;

        int nDownloadHandle = HCNetSDK.getInstance().NET_DVR_GetFileByTime(iUserID,33, timeStart, timeStop, new String("/sdcard/RecordFile"));
        if (-1 == nDownloadHandle)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetFileByTime failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret;
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
        }
        HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nDownloadHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null);
        int nProgress = -1;
        while(true)
        {
            nProgress = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(nDownloadHandle);
            System.out.println("NET_DVR_GetDownloadPos:" + nProgress);
            if(nProgress < 0 || nProgress >= 100)
            {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        HCNetSDK.getInstance().NET_DVR_StopGetFile(nDownloadHandle);
        return ret;
    }

    /*case 5*/
    public CGReturn Test_GetFileByName_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        int nDownloadHandle = HCNetSDK.getInstance().NET_DVR_GetFileByName(iUserID, new String("ch0001_01000000080001900"), new String("/sdcard/RecordFile"));
        if (-1 == nDownloadHandle)
        {
            System.out.println("NET_DVR_GetFileByName failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return ret;
        }
        HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nDownloadHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null);
        int nProgress = -1;
        while(true)
        {
            nProgress = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(nDownloadHandle);
            System.out.println("NET_DVR_GetDownloadPos:" + nProgress);
            if(nProgress < 0 || nProgress >= 100)
            {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        HCNetSDK.getInstance().NET_DVR_StopGetFile(nDownloadHandle);
        return ret;
    }

    /*case 6*/
    public CGReturn Test_UpdateRecordIndex_jni(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_UpdateRecordIndex(iUserID, iChan))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_UpdateRecordIndex failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_UpdateRecordIndex succ!");
        }
        return ret;
    }

    /*case 7*/
    public CGReturn Test_CaptureJpegPicture_jni(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_JPEGPARA strJpeg = new  NET_DVR_JPEGPARA();
        strJpeg.wPicQuality = 1;
        strJpeg.wPicSize = 2;
        if(!HCNetSDK.getInstance().NET_DVR_CaptureJPEGPicture(iUserID, iChan, strJpeg, new String("/sdcard/cap.jpg")))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_CaptureJPEGPicture!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_CaptureJPEGPicture! succeed");
        }
        return ret;
    }

    /*case 8*/
    public CGReturn Test_CaptureJpegPicture_new_jni(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_JPEGPARA strJpeg = new  NET_DVR_JPEGPARA();
        strJpeg.wPicQuality = 1;
        strJpeg.wPicSize = 2;
        int iBufferSize = 1024*1024;
        byte[] sbuffer = new byte[iBufferSize];
        INT_PTR bytesRerned = new INT_PTR();
        if(!HCNetSDK.getInstance().NET_DVR_CaptureJPEGPicture_NEW(iUserID, iChan, strJpeg, sbuffer, iBufferSize, bytesRerned))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_CaptureJPEGPicture_NEW!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_CaptureJPEGPicture_NEW size!" + bytesRerned.iValue);
        }
        return ret;
    }

    /*case 9*/
    public CGReturn Test_DVRRecord_jni(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_StartDVRRecord(iUserID, 1, 0))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_StartDVRRecord err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_StartDVRRecord succ!");
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!HCNetSDK.getInstance().NET_DVR_StopDVRRecord(iUserID, 1))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_StopDVRRecord err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_StopDVRRecord succ!");
        }
        return ret;
    }

    private static void processSerialData(int lSerialHandle, byte[] pDataBuffer, int iDataSize)
    {
        System.out.println("lSerialHandle " + lSerialHandle + " iDataSize " + iDataSize);
    }
    /*case 10*/
    public CGReturn Test_TransChannel_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        if(SerailDataCbfV40 == null)
        {
            SerailDataCbfV40 = new SerialDataCallBackV40()
            {
                public void fSerialDataCallBackV40(int lSerialHandle, int lChannel, byte[] pDataBuffer, int iDataSize)
                {
                    processSerialData(lSerialHandle, pDataBuffer, iDataSize);
                }
            };
        }
        NET_DVR_SERIALSTART_V40 struSerialStart = new NET_DVR_SERIALSTART_V40();
        struSerialStart.dwSerialPort = 2;
        struSerialStart.wPort = 0;
        int lSerialHandle = HCNetSDK.getInstance().NET_DVR_SerialStart_V40(iUserID, struSerialStart, SerailDataCbfV40);
        if(lSerialHandle < 0)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SerialStart failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SerialStart succ");
        }
        byte[] bytes = {0x5A,0x5A,0x5A,0x7E,0x0F,0x04,0x07,0x00,0x00,0x01,0x1B,0x00,0x00};
        int length = bytes.length;
        if(!HCNetSDK.getInstance().NET_DVR_SerialSend(lSerialHandle, 0, bytes, length))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SerialSend failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SerialStart succ");
        }
        if(!HCNetSDK.getInstance().NET_DVR_SerialStop(lSerialHandle))
        {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SerialStop failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else{
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SerialStart succ");
        }
        return ret;
    }

    /*case 11*/
    public CGReturn Test_Serial_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_SendToSerialPort(iUserID, 1, 1, "12345".getBytes(), 5))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SendToSerialPort failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SendToSerialPort succ!");
        }

        if(!HCNetSDK.getInstance().NET_DVR_SendTo232Port(iUserID, "12345".getBytes(), 5))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SendTo232Port failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SendTo232Port succ!");
        }
        return ret;
    }

    private static RealPlayCallBack cbf = null;
    private static void processRealData(int lRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize)
    {
        System.out.println("recv real stream ,dataType:"+iDataType+", size:" + iDataSize);
    }

    /*case 12*/
    public CGReturn Test_ZeroChanPreview_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        if (cbf==null){
            cbf = new RealPlayCallBack(){
                public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize){
                    processRealData(iRealHandle, iDataType, pDataBuffer, iDataSize);
                }
            };
        }
        NET_DVR_CLIENTINFO ClientInfo = new NET_DVR_CLIENTINFO();
        ClientInfo.lChannel = 1;
        ClientInfo.lLinkMode = 0;
        int iZeroPreviewHandle = HCNetSDK.getInstance().NET_DVR_ZeroStartPlay(iUserID, ClientInfo, cbf, true);
        if(iZeroPreviewHandle < 0)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_ZeroStartPlay failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_ZeroStartPlay succ");
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(!HCNetSDK.getInstance().NET_DVR_ZeroStopPlay(iZeroPreviewHandle))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_ZeroStopPlay failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_ZeroStopPlay succ");
        }
        return ret;
    }

    /*case 13*/
    public CGReturn Test_Hikonline_jni()
    {
        CGReturn ret=new CGReturn();
        NET_DVR_QUERY_COUNTRYID_COND	struCountryIDCond = new NET_DVR_QUERY_COUNTRYID_COND();
        NET_DVR_QUERY_COUNTRYID_RET		struCountryIDRet = new NET_DVR_QUERY_COUNTRYID_RET();
        struCountryIDCond.wCountryID = 248; //248 is for china,other country's ID please see the interface document
        System.arraycopy("www.hik-online.com".getBytes(), 0, struCountryIDCond.szSvrAddr, 0, "www.hik-online.com".getBytes().length);
        System.arraycopy("Android NetSDK Demo".getBytes(), 0, struCountryIDCond.szClientVersion, 0, "Android NetSDK Demo".getBytes().length);
        //first you need get the resolve area server address form www.hik-online.com by country ID
        //and then get you dvr/ipc address from the area resolve server
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYSVR_BY_COUNTRYID, struCountryIDCond, struCountryIDRet))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYSVR_BY_COUNTRYID succ,resolve:" + CommonMethod.toValidString(new String(struCountryIDRet.szResolveSvrAddr)));
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYSVR_BY_COUNTRYID failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        //follow code show how to get dvr/ipc address from the area resolve server by nickname or serial no.
        NET_DVR_QUERY_DDNS_COND	struDDNSCond = new NET_DVR_QUERY_DDNS_COND();
        NET_DVR_QUERY_DDNS_RET	struDDNSQueryRet = new NET_DVR_QUERY_DDNS_RET();
        NET_DVR_CHECK_DDNS_RET	struDDNSCheckRet = new NET_DVR_CHECK_DDNS_RET();
        System.arraycopy("Android NetSDK Demo".getBytes(), 0, struDDNSCond.szClientVersion, 0, "Android NetSDK Demo".getBytes().length);
        System.arraycopy(struCountryIDRet.szResolveSvrAddr, 0, struDDNSCond.szResolveSvrAddr, 0, struCountryIDRet.szResolveSvrAddr.length);
        System.arraycopy("nickname".getBytes(), 0, struDDNSCond.szDevNickName, 0, "nickname".getBytes().length);//your dvr/ipc nickname
        System.arraycopy("serial no.".getBytes(), 0, struDDNSCond.szDevSerial, 0, "serial no.".getBytes().length);//your dvr/ipc serial no.
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_NICKNAME_DDNS, struDDNSCond, struDDNSQueryRet))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_NICKNAME_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSQueryRet.szDevIP)) + ", SDK port:" + struDDNSQueryRet.wCmdPort + ", http port" + struDDNSQueryRet.wHttpPort);
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_NICKNAME_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_SERIAL_DDNS, struDDNSCond, struDDNSQueryRet))
        {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_SERIAL_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSQueryRet.szDevIP)) + ", SDK port:" + struDDNSQueryRet.wCmdPort + ", http port" + struDDNSQueryRet.wHttpPort);
        }
        else
        {
            ret.status_3=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_SERIAL_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        //if you get the dvr/ipc address failed from the area reolve server,you can check the reason show as follow
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.CHECKDEV_BY_NICKNAME_DDNS, struDDNSCond, struDDNSCheckRet))
        {
            System.out.println("CHECKDEV_BY_NICKNAME_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSCheckRet.struQueryRet.szDevIP)) + ", SDK port:" + struDDNSCheckRet.struQueryRet.wCmdPort + ", http port" + struDDNSCheckRet.struQueryRet.wHttpPort + ",region:" + struDDNSCheckRet.wRegionID + ",status:" + struDDNSCheckRet.byDevStatus);
        }
        else
        {
            System.out.println("CHECKDEV_BY_NICKNAME_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.CHECKDEV_BY_SERIAL_DDNS, struDDNSCond, struDDNSCheckRet))
        {
            System.out.println("CHECKDEV_BY_SERIAL_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSCheckRet.struQueryRet.szDevIP)) + ", SDK port:" + struDDNSCheckRet.struQueryRet.wCmdPort + ", http port" + struDDNSCheckRet.struQueryRet.wHttpPort + ",region:" + struDDNSCheckRet.wRegionID + ",status:" + struDDNSCheckRet.byDevStatus);
        }
        else
        {
            System.out.println("CHECKDEV_BY_SERIAL_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 14*/
    public CGReturn Test_IPServer_jni()
    {
        CGReturn ret=new CGReturn();
        NET_DVR_QUERY_IPSERVER_COND	struIPServerCond = new NET_DVR_QUERY_IPSERVER_COND();
        NET_DVR_QUERY_IPSERVER_RET	struIPServerRet = new NET_DVR_QUERY_IPSERVER_RET();
        struIPServerCond.wResolveSvrPort = 7071;
        System.arraycopy("10.10.34.21".getBytes(), 0, struIPServerCond.szResolveSvrAddr, 0, "10.10.34.21".getBytes().length);//your ipserver ip
        System.arraycopy("nickname".getBytes(), 0, struIPServerCond.szDevNickName, 0, "nickname".getBytes().length);//your dvr/ipc nickname on ipserver
        //search by nickname
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_NICKNAME_IPSERVER, struIPServerCond, struIPServerRet))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_NICKNAME_IPSERVER succ,ip:" + CommonMethod.toValidString(new String(struIPServerRet.szDevIP)) + ", SDK port:" + struIPServerRet.wCmdPort);
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_NICKNAME_IPSERVER failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }

        System.arraycopy("serial no.".getBytes(), 0, struIPServerCond.szDevSerial, 0, "serial no.".getBytes().length);//your dvr/ipc serial no.
        //search bu serial no.
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_SERIAL_IPSERVER, struIPServerCond, struIPServerRet))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_SERIAL_IPSERVER succ,ip:" + CommonMethod.toValidString(new String(struIPServerRet.szDevIP)) + ", SDK port:" + struIPServerRet.wCmdPort);
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_SERIAL_IPSERVER failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 15*/
    public CGReturn Test_DVRSetConnectTime_jni()
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_SetConnectTime(3000))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetConnectTime err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetConnectTime succ!");
        }
        return ret;
    }

    /*case 16*/
    public CGReturn Test_DVRSetReConnect_jni()
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_SetReconnect(3000,true))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetReconnect err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetReconnect succ!");
        }
        return ret;
    }

    /*case 17*/
    public CGReturn Test_SDKLOCAL_CFG_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_SDKLOCAL_CFG SdkLocalCfg = new NET_DVR_SDKLOCAL_CFG();
        if (!HCNetSDK.getInstance().NET_DVR_GetSDKLocalConfig(SdkLocalCfg))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetSDKLocalConfig faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetSDKLocalConfig succ!" + "EnableAbilityParse: " + SdkLocalCfg.byEnableAbilityParse );
        }
        if (!HCNetSDK.getInstance().NET_DVR_SetSDKLocalConfig(SdkLocalCfg))
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetSDKLocalConfig faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetSDKLocalConfig succ!" + "EnableAbilityParse: " + SdkLocalCfg.byEnableAbilityParse );
        }
        return ret;
    }

    /*case 18*/
    public CGReturn Test_GetSDKVersion_jni()
    {
        CGReturn ret=new CGReturn();
        long SDKVersion = -1;
        long SDKBuildVersion = -1;
        SDKVersion = HCNetSDK.getInstance().NET_DVR_GetSDKVersion();
        if( SDKVersion < 0)
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetSDKVersion err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetSDKVersion succ!" + SDKVersion);
        }
        SDKBuildVersion = HCNetSDK.getInstance().NET_DVR_GetSDKBuildVersion();
        if( SDKBuildVersion < 0)
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetSDKVersion_GetSDKBuildVersion err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_2=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetSDKVersion_GetSDKBuildVersion succ!" + SDKBuildVersion );
        }
        return ret;
    }

    /*case 19*/
    public CGReturn Test_DVRMakeKeyFrame_jni(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_MakeKeyFrame( iUserID, iChan))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_MakeKeyFrame err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_MakeKeyFrame succ!");
        }
        return ret;
    }

    /*case 20*/
    public CGReturn Test_DVRMakeKeyFrameSub_jni(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_MakeKeyFrameSub( iUserID, iChan))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_MakeKeyFrameSub err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_MakeKeyFrameSub succ!");
        }
        return ret;
    }

    /*case 21*/
    public CGReturn Test_SetRecvTimeOut_jni()
    {
        CGReturn ret=new CGReturn();
        if(!HCNetSDK.getInstance().NET_DVR_SetRecvTimeOut( 5000 ))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetRecvTimeOut err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_SetRecvTimeOut succ!");
        }
        return ret;
    }

    /*case 22*/
    /*
    public CGReturn Test_RecycleGetStream(int iUserID, int iChan)
    {
        CGReturn ret=new CGReturn();
        int i = 0;
        while(true)
        {
            for(i = 0; i < 16; i++)
            {
                Thread thread = new PreviewGetStreamThread(iUserID, (iChan + i));
                thread.start();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ret;
    }
    */

    /*case 23*/
    public CGReturn Test_GetCurrentAudioCompress_V50(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_AUDIO_CHANNEL	audioChannel = new NET_DVR_AUDIO_CHANNEL();
        audioChannel.dwChannelNum = 3;
        NET_DVR_COMPRESSION_AUDIO	audioCompression = new NET_DVR_COMPRESSION_AUDIO();
        if(HCNetSDK.getInstance().NET_DVR_GetCurrentAudioCompress_V50(iUserID, audioChannel, audioCompression))
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetCurrentAudioCompress_V50 succ, type: " + audioCompression.byAudioEncType);
        }
        else
        {
            ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("NET_DVR_GetCurrentAudioCompress_V50 failed: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }
        return ret;
    }

    /*case 24*/
    /*
    public CGReturn Test_MultiThreadLogin()
    {
        CGReturn ret=new CGReturn();
        Thread thread = new Thread()
        {
            public void run()
            {
                //	while(true)
                {
                    int i = 0;
                    for(i = 0; i < 100; i++)
                    {
                        Thread loginThread = new LoginMultiThread();
                        loginThread.start();
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        return ret;
    }
    */

    /*case 25*/
    public CGReturn Test_EzvizConfig_jni(int iUserID)
    {
        CGReturn ret=new CGReturn();
        NET_DVR_MULTI_ALARMIN_COND struCond = new NET_DVR_MULTI_ALARMIN_COND();
        NET_DVR_ALARMIN_PARAM_LIST struList = new NET_DVR_ALARMIN_PARAM_LIST();

        int i = 0;

        for (i = 0; i < 64; i++)
        {
            struCond.iZoneNo[i] = -1;
        }

        for(i = 0; i < 8; i++)
        {
            struCond.iZoneNo[i] = i;
        }

        for(i = 0; i < 400; i++)
        {
            if (!HCNetSDK.getInstance().NET_DVR_GetSTDConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMIN_PARAM_LIST, struCond, null, struList))
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_GetSTDConfig" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
            else
            {
                ret.status_1=HCNetSDK.getInstance().NET_DVR_GetLastError();
                System.out.println("NET_DVR_GetSTDConfig success");
            }
        }
        return ret;
    }

}


