package com.example.cameratest1_17.Control;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_FILECOND;
import com.hikvision.netsdk.NET_DVR_FINDDATA_V30;
import com.hikvision.netsdk.NET_DVR_PLAYBACK_INFO;
import com.hikvision.netsdk.NET_DVR_PLAYCOND;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.NET_DVR_VOD_PARA;
import com.hikvision.netsdk.PlaybackControlCommand;

import java.util.Calendar;

/**
 * brief：device guider
 * author：hubinglun
 * date：2019/05/13
 */

public class DevPlayBackGuider {
    //Time Conversion Interface
    public boolean ConvertToTime(NET_DVR_TIME time,Calendar cal){
        if (time == null || cal == null){
            return false;
        }
        time.dwYear = cal.get(Calendar.YEAR);
        time.dwMonth = cal.get(Calendar.MONTH)+1;
        time.dwDay = cal.get(Calendar.DAY_OF_MONTH);
        time.dwHour = cal.get(Calendar.HOUR_OF_DAY);
        time.dwMinute = cal.get(Calendar.MINUTE);
        time.dwSecond = cal.get(Calendar.SECOND);
        return true;
    }

    //Playback control interface
    public boolean PlayBackControl_V40_jni(int lPlayHandle, int dwControlCode, byte[] lpInBuffer, int dwInLen, NET_DVR_PLAYBACK_INFO lpOutBuffer){
        if (lPlayHandle < 0) {
            Log.e("SimpleDemo", "PlayBackControl_V40_jni failed with error param");
            return false;
        }
        return  HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(lPlayHandle, dwControlCode, lpInBuffer, dwInLen, lpOutBuffer);
    }
    //Playback by filename
    public int PlayBackByName_jni(int iLogID, String strFileName, Surface surface){
        if (iLogID < 0 || strFileName.isEmpty() || surface == null) {
            Log.e("SimpleDemo", "PlayBackByName_jni failed with error param");
            return -1;
        }
        int iPlaybackID =  HCNetSDK.getInstance().NET_DVR_PlayBackByName(iLogID,strFileName, surface);

        if (iPlaybackID < 0) {
            Log.e("SimpleDemo", "NET_DVR_PlayBackByName is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }

        if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null))
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopPlayBack(iPlaybackID);
            return -1;
        }

        return iPlaybackID;
    }

    //Find the files
    public int FindFile_V30_jni(int iUserID, NET_DVR_FILECOND lpSearchInfo){
        if (iUserID < 0 || lpSearchInfo == null) {
            Log.e("SimpleDemo", "FindFile_V30_jni failed with error param");
            return -1;
        }
        return  HCNetSDK.getInstance().NET_DVR_FindFile_V30(iUserID, lpSearchInfo);
    }

    public int FindNextFile_V30_jni(int iFindHandle, NET_DVR_FINDDATA_V30 struFindData){
        if (iFindHandle < 0 || struFindData == null) {
            Log.e("SimpleDemo", "FindNextFile_V30_jni failed with error param");
            return -1;
        }
        return  HCNetSDK.getInstance().NET_DVR_FindNextFile_V30(iFindHandle, struFindData);
    }

    public boolean FindClose_V30_jni(int iFindHandle){
        if (iFindHandle < 0) {
            Log.e("SimpleDemo", "FindClose_V30_jni failed with error param");
            return false;
        }
        return  HCNetSDK.getInstance().NET_DVR_FindClose_V30(iFindHandle);
    }

    public int PlayBackSurfaceChanged_jni(int iHandle, int nRegionNum, SurfaceHolder hHwnd){
        if (iHandle < 0 || nRegionNum < 0) {
            Log.e("SimpleDemo", "PlayBackSurfaceChanged_jni failed with error param");
            return -1;
        }
        return  HCNetSDK.getInstance().NET_DVR_PlayBackSurfaceChanged(iHandle, nRegionNum, hHwnd);
    }

    public boolean StopPlayBack_jni(int iPlaybackID){
        if (iPlaybackID < 0) {
            Log.e("SimpleDemo", "StopPlayBack_jni failed with error param");
            return false;
        }
        return  HCNetSDK.getInstance().NET_DVR_StopPlayBack(iPlaybackID);
    }

    // Download by File Name
    public int GetFileByName_jni(int iLogID, String strFileName, String strPath){
        if (iLogID < 0 || strFileName.isEmpty() || strPath.isEmpty()) {
            Log.e("SimpleDemo", "GetFileByName_jni failed with error param");
            return -1;
        }

        int iDownloadID = HCNetSDK.getInstance().NET_DVR_GetFileByName(iLogID,strFileName, strPath);

        if (iDownloadID < 0) {
            Log.e("SimpleDemo", "NET_DVR_GetFileByName is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }

        byte[] byTranstype = new byte[60];
        byTranstype[0] = 5;

        if(!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iDownloadID, PlaybackControlCommand.NET_DVR_SET_TRANS_TYPE,  byTranstype, 4,null))
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40  NET_DVR_SET_TRANS_TYPE is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopGetFile(iDownloadID);
            return -1;
        }

        if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iDownloadID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null)) {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40  NET_DVR_PLAYSTART is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopGetFile(iDownloadID);
            return -1;
        }
        return iDownloadID;
    }


    //Reverse by filename
    public int PlayBackReverseByName_jni(int iLogID, String strFileName, Surface surface){
        if (iLogID < 0 || surface == null || strFileName.isEmpty()) {
            Log.e("SimpleDemo", "PlayBackReverseByName_jni failed with error param");
            return -1;
        }
        int iPlaybackID = HCNetSDK.getInstance().NET_DVR_PlayBackReverseByName(iLogID,strFileName, surface);

        if (iPlaybackID < 0) {
            Log.e("SimpleDemo", "NET_DVR_PlayBackReverseByName is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }

        if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null))
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopPlayBack(iPlaybackID);
            return -1;
        }
        return iPlaybackID;
    }


    //Playback JNI interface by time
    public int GetFileByTime_jni(int iUserID, int iChannel, NET_DVR_TIME timeStart, NET_DVR_TIME timeStop, String fileName){

        if (iUserID < 0 || iChannel < 0 || timeStart == null || timeStop == null || fileName.isEmpty()) {
            Log.e("SimpleDemo", "GetFileByTime_jni failed with error param");
            return -1;
        }

        int iDownloadID = HCNetSDK.getInstance().NET_DVR_GetFileByTime(iUserID,iChannel, timeStart, timeStop, fileName);

        if (iDownloadID < 0) {
            Log.e("SimpleDemo", "NET_DVR_GetFileByTime is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }

        if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iDownloadID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null)) {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopGetFile(iDownloadID);
            return -1;
        }
        return iDownloadID;
    }

    //Get playback progress
    public int GetPlayBackPos_jni(int iPlaybackID){
        return  HCNetSDK.getInstance().NET_DVR_GetPlayBackPos(iPlaybackID);
    }

    //Get the progress of downloading videos
    public int GetDownloadPos_jni(int iDownloadID){
        if (iDownloadID < 0) {
            Log.e("SimpleDemo", "GetDownloadPos_jni failed with error param");
            return -1;
        }
        return  HCNetSDK.getInstance().NET_DVR_GetDownloadPos(iDownloadID);
    }

    //停止下载录像文件
    public boolean StopGetFile_jni(int iDownloadID){
        if (iDownloadID < 0) {
            Log.e("SimpleDemo", "StopGetFile_jni failed with error param");
            return false;
        }
        return  HCNetSDK.getInstance().NET_DVR_StopGetFile(iDownloadID);
    }

    //Playback on time
    public int PlayBackByTime_v40_jni(int iLogID, NET_DVR_VOD_PARA vodParma){
        if(iLogID < 0 || vodParma == null){
            Log.e("SimpleDemo", "PlayBackByTime_v40_jni failed with error param");
            return -1;
        }

        int iPlaybackID = HCNetSDK.getInstance().NET_DVR_PlayBackByTime_V40(iLogID, vodParma);
        if (iPlaybackID < 0)
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackByTime_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }

        if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null))
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopPlayBack(iPlaybackID);
            return -1;
        }

        if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTARTAUDIO, null, 0, null))
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopPlayBack(iPlaybackID);
            return -1;
        }
        return iPlaybackID;
    }

    //Time reversal
    public int PlayBackReverseByTime_V40_jni(int iLogID, Surface surface,NET_DVR_PLAYCOND vodcond){
        if(iLogID < 0 || vodcond == null || surface == null){
            Log.e("SimpleDemo", "PlayBackReverseByTime_V40_jni failed with error param");
            return -1;
        }

        int iPlaybackID = HCNetSDK.getInstance().NET_DVR_PlayBackReverseByTime_V40(iLogID, surface, vodcond);
        if (iPlaybackID < 0)
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackReverseByTime_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }

        if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null))
        {
            Log.e("SimpleDemo", "NET_DVR_PlayBackControl_V40 is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            HCNetSDK.getInstance().NET_DVR_StopPlayBack(iPlaybackID);
            return -1;
        }
        return iPlaybackID;
    }

}
