package com.example.cameratest1_17.Control;

import android.util.Log;

import com.example.cameratest1_17.jna.HCNetSDKByJNA;
import com.example.cameratest1_17.jna.HCNetSDKJNAInstance;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

public class DevManageGuider implements Serializable {

    /**
     * @brief 设备状态对象
     */
    public class DeviceState implements Serializable {
        public int m_iLogState = 0; // 0-offline, 1-online, 2-dropoff
        public int m_iAlarmState = 0; // 0-alarmclosed, 1-alarmopen

        public void reset(){
            m_iLogState = 0;
            m_iAlarmState = 0;
        }
    }

    public class DevNetInfo implements Serializable {
        public String m_szIp;
        public String m_szPort;
        public String m_szUserName;
        public String m_szPassword;

        public DevNetInfo(){}

        public DevNetInfo(String szIp, String szPort, String szUserName, String szPassWorld){
            m_szIp = szIp;
            m_szPort = szPort;
            m_szUserName = szUserName;
            m_szPassword = szPassWorld;
        }

        /**
         * 判断IP格式和范围
         */
        public boolean checkIp(){
            if(m_szIp.length() < 7 || m_szIp.length() > 15 || "".equals(m_szIp))
            {
                return false;
            }
            if (m_szIp != null && !m_szIp.isEmpty()) {
                // 定义正则表达式
                String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
                // 判断ip地址是否与正则表达式匹配
                return m_szIp.matches(regex);
            }
            return false;
        }
        public boolean checkPort() {
            Pattern pattern = Pattern.compile("[1-9][0-9]*");
            return pattern.matcher(m_szPort).matches();
        }

        public boolean checkNetInfo(){
            return checkIp()&&checkPort()&&!m_szUserName.isEmpty()&&!m_szPassword.isEmpty();
        }
    }

    /**
     * @brief 设备信息对象
     */
    public class DeviceItem implements Serializable {
        public String m_szUUID;
        public String m_szDevName;
        public int m_lUserID = -1;
        public byte m_byLoginFlag = -1; // 设备登录方式，0-jni, 1-jna
        public DeviceState m_struDevState = new DeviceState();
        public DevNetInfo m_struNetInfo;
        public HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40 m_struDeviceInfoV40_jna;
        public NET_DVR_DEVICEINFO_V30 m_struDeviceInfoV30_jni;

        public DeviceItem() {
            m_szUUID = UUID.randomUUID().toString();
        }
        public DeviceItem(String szUUID) {
            m_szUUID = szUUID;
        }
    }

    /**
     * @fn getExceptiongCb
     * @author zhuzhenlei
     * @brief process exception
     * @param None.
     * @return exception instance
     */
    private ExceptionCallBack getExceptiongCb()
    {
        ExceptionCallBack oExceptionCb = new ExceptionCallBack()
        {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                Log.e("[NetSDKSimpleDemo]", "recv exception, type:" + iType);
                switch (iType) {
                    case 0x8044:
                        getDevByUserID(iUserID).m_struDevState.m_iLogState = 2;
                        break;
                    default:
                }
            }
        };
        return oExceptionCb;
    }

    private ArrayList<DeviceItem> m_alDevList = new ArrayList<DeviceItem>();
    private int m_iCurrSelectDevIndex = -1;

    /**
     * @fn setCurrSelectDevIndex
     * @param [int] iCurrSelectDevIndex
     * @return None.
     * @brief 设置当前选中设备的列表序号.
     */
    public void setCurrSelectDevIndex(int iCurrSelectDevIndex){
        m_iCurrSelectDevIndex = iCurrSelectDevIndex;
    }

    /**
     * @fn getCurrSelectDevIndex
     * @return [int] 设备索引号
     * @brief 获取当前选中的设备在设备列表中的索引号.
     */
    public int getCurrSelectDevIndex(){
        return m_iCurrSelectDevIndex;
    }

    /**
     * @fn getDevList
     * @return [ArrayList<DeviceItem>] 设备列表
     * @brief 获取设备列表
     */
    public ArrayList<DeviceItem> getDevList(){
        return m_alDevList;
    }

    /**
     * @fn getCurrSelectDev
     * @return [DeviceItem] 设备信息对象
     * @brief 获取当前选中设备的信息对象
     */
    public DeviceItem getCurrSelectDev(){
        if(m_iCurrSelectDevIndex < 0 || m_iCurrSelectDevIndex >= m_alDevList.size()){
            return null;
        }
        return m_alDevList.get(m_iCurrSelectDevIndex);
    }

    /**
     * @fn getDevByUserID
     * @param [in] lUserID 设备登陆ID
     * @return [DeviceItem] 设备信息对象
     * @brief 使用登陆id查找设备信息对象
     */
    public DeviceItem getDevByUserID(int lUserID){
        if(lUserID < 0){
            return null;
        }
        for (DeviceItem item: this.m_alDevList) {
            if(item.m_lUserID == lUserID){
                return item;
            }
        }
        return null;
    }

    public void setDevList(ArrayList<DeviceItem> alDevList){
        m_alDevList = alDevList;
    }

    /**
     * @fn login_v40_jna
     * @param [in] szDevName 设备名称
     * @param [in] struDevNetInfo 设备登陆的网络参数
     * @return 登陆成功返回true,否则false
     * @brief jna方式登陆设备
     */
    public boolean login_v40_jna(String szDevName, DevNetInfo struDevNetInfo){
        // 验证参数有效性
        if(!struDevNetInfo.checkIp() || !struDevNetInfo.checkPort() || struDevNetInfo.m_szUserName.isEmpty() || struDevNetInfo.m_szPassword.isEmpty()){
            System.out.println("login_v40_jna failed with error param");
            return false;
        }

        // call hcnetsdk jna login API.
        HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO loginInfo = new HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO();
        System.arraycopy(struDevNetInfo.m_szIp.getBytes(), 0, loginInfo.sDeviceAddress, 0, struDevNetInfo.m_szIp.length());
        System.arraycopy(struDevNetInfo.m_szUserName.getBytes(), 0, loginInfo.sUserName, 0, struDevNetInfo.m_szUserName.length());
        System.arraycopy(struDevNetInfo.m_szPassword.getBytes(), 0, loginInfo.sPassword, 0, struDevNetInfo.m_szPassword.length());
        loginInfo.wPort = (short)Integer.parseInt(struDevNetInfo.m_szPort);
        HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40 deviceInfo = new HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40();
        loginInfo.write();
        int lUserID = HCNetSDKJNAInstance.getInstance().NET_DVR_Login_V40(loginInfo.getPointer(), deviceInfo.getPointer());
        if(lUserID < 0)
        {
            Log.e("[NetSDKSimpleDemo]","NET_DVR_Login_V40 failed with:" + HCNetSDKJNAInstance.getInstance().NET_DVR_GetLastError());
            return false;
        }

        // add a DeviceItem to device list.
        deviceInfo.read();
        DeviceItem devItem = new DeviceItem();
        devItem.m_byLoginFlag = 1;
        devItem.m_lUserID = lUserID;
        if (szDevName.isEmpty()){
            devItem.m_szDevName = struDevNetInfo.m_szIp;
        }
        devItem.m_struDevState.m_iLogState = 1;
        devItem.m_struNetInfo = struDevNetInfo;
        devItem.m_struDeviceInfoV40_jna = deviceInfo;
        m_alDevList.add(devItem);
        Log.i("[NetSDKSimpleDemo]","NET_DVR_Login_V40 succ with:" + lUserID);
        return true;
    }

    /**
     * @fn login_v40_jna_with_index
     * @param [in] iDevIndex 设备列表中的索引号
     * @return 登陆成功返回true,否则false
     * @brief 当设备已经添加到设备列表之后，可以通过在列表中的索引号进行登陆
     */
    public boolean login_v40_jna_with_isapi(int iDevIndex){
        // 验证参数有效性
        if(iDevIndex<0||iDevIndex>=m_alDevList.size()) {
            Log.w("[NetSDKSimpleDemo]", "logout_jna failed with error param");
            return false;
        }
        DeviceItem devItem = m_alDevList.get(iDevIndex);
        if(devItem.m_struDevState.m_iLogState == 1){
            return true;
        }
        // call hcnetsdk jna login API.
        HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO loginInfo = new HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO();
        System.arraycopy(devItem.m_struNetInfo.m_szIp.getBytes(), 0, loginInfo.sDeviceAddress, 0, devItem.m_struNetInfo.m_szIp.length());
        System.arraycopy(devItem.m_struNetInfo.m_szUserName.getBytes(), 0, loginInfo.sUserName, 0, devItem.m_struNetInfo.m_szUserName.length());
        System.arraycopy(devItem.m_struNetInfo.m_szPassword.getBytes(), 0, loginInfo.sPassword, 0, devItem.m_struNetInfo.m_szPassword.length());
        loginInfo.wPort = 80;
        loginInfo.byLoginMode = 1; // isapi login
        HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40 deviceInfo = new HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40();
        loginInfo.write();
        int lUserID = HCNetSDKJNAInstance.getInstance().NET_DVR_Login_V40(loginInfo.getPointer(), deviceInfo.getPointer());
        if(lUserID < 0)
        {
            Log.e("[NetSDKSimpleDemo]","NET_DVR_Login_V40 failed with:" + HCNetSDKJNAInstance.getInstance().NET_DVR_GetLastError());
            return false;
        }

        // add a DeviceItem to device list.
        deviceInfo.read();
        devItem.m_lUserID = lUserID;
        devItem.m_struDevState.m_iLogState = 1;
        devItem.m_struDeviceInfoV40_jna = deviceInfo;
//        m_alDevList.add(devItem);
        Log.i("[NetSDKSimpleDemo]","NET_DVR_Login_V40 succ with:" + lUserID);
        return true;
    }

    /**
     * @fn login_v40_jna_with_index
     * @param [in] iDevIndex 设备列表中的索引号
     * @return 登陆成功返回true,否则false
     * @brief 当设备已经添加到设备列表之后，可以通过在列表中的索引号进行登陆
     */
    public boolean login_v40_jna_with_index(int iDevIndex){
        // 验证参数有效性
        if(iDevIndex<0||iDevIndex>=m_alDevList.size()) {
            Log.w("[NetSDKSimpleDemo]", "logout_jna failed with error param");
            return false;
        }
        DeviceItem devItem = m_alDevList.get(iDevIndex);
        if(devItem.m_struDevState.m_iLogState == 1){
            return true;
        }
        // call hcnetsdk jna login API.
        HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO loginInfo = new HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO();
        System.arraycopy(devItem.m_struNetInfo.m_szIp.getBytes(), 0, loginInfo.sDeviceAddress, 0, devItem.m_struNetInfo.m_szIp.length());
        System.arraycopy(devItem.m_struNetInfo.m_szUserName.getBytes(), 0, loginInfo.sUserName, 0, devItem.m_struNetInfo.m_szUserName.length());
        System.arraycopy(devItem.m_struNetInfo.m_szPassword.getBytes(), 0, loginInfo.sPassword, 0, devItem.m_struNetInfo.m_szPassword.length());
        loginInfo.wPort = (short)Integer.parseInt(devItem.m_struNetInfo.m_szPort);
        HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40 deviceInfo = new HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40();
        loginInfo.write();
        int lUserID = HCNetSDKJNAInstance.getInstance().NET_DVR_Login_V40(loginInfo.getPointer(), deviceInfo.getPointer());
        if(lUserID < 0)
        {
            Log.e("[NetSDKSimpleDemo]","NET_DVR_Login_V40 failed with:" + HCNetSDKJNAInstance.getInstance().NET_DVR_GetLastError());
            return false;
        }

        // add a DeviceItem to device list.
        deviceInfo.read();
        devItem.m_lUserID = lUserID;
        devItem.m_struDevState.m_iLogState = 1;
        devItem.m_struDeviceInfoV40_jna = deviceInfo;
//        m_alDevList.add(devItem);
        Log.i("[NetSDKSimpleDemo]","NET_DVR_Login_V40 succ with:" + lUserID);
        return true;
    }

    /**
     * @fn logout_jna
     * @param [in] iDevIndex 设备索引号
     * @return 注销成功true,否则false
     * @brief 注销设备
     */
    public boolean logout_jna(int iDevIndex){
        if(iDevIndex<0||iDevIndex>=m_alDevList.size()) {
            Log.w("[NetSDKSimpleDemo]", "logout_jna failed with error param");
            return false;
        }
        DeviceItem devItem = m_alDevList.get(iDevIndex);
        boolean ret = HCNetSDKJNAInstance.getInstance().NET_DVR_Logout(devItem.m_lUserID);
        if (!ret){
            Log.e("[NetSDKSimpleDemo]","NET_DVR_Logout failed with:" + HCNetSDKJNAInstance.getInstance().NET_DVR_GetLastError());
        }
//        m_alDevList.remove(iDevIndex);
        devItem.m_struDevState.reset();
        devItem.m_struDeviceInfoV30_jni = null;
        devItem.m_struDeviceInfoV40_jna = null;
        return true;
    }

    public boolean login_v30_jni(String szDevName, DevNetInfo struDevNetInfo){
        if(!struDevNetInfo.checkIp() || !struDevNetInfo.checkPort() || struDevNetInfo.m_szUserName.isEmpty() || struDevNetInfo.m_szPassword.isEmpty()){
            System.out.println("login_v40_jna failed with error param");
            return false;
        }

        NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        String strIP = struDevNetInfo.m_szIp;
        int nPort = Integer.parseInt(struDevNetInfo.m_szPort);
        String strUser = struDevNetInfo.m_szUserName;
        String strPsd = struDevNetInfo.m_szPassword;

        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int lUserID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort, strUser, strPsd, m_oNetDvrDeviceInfoV30);
        if (lUserID < 0)
        {
            Log.e("SimpleDemo", "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return false;
        }

        DeviceItem devItem = new DeviceItem();
        devItem.m_byLoginFlag = 0;
        devItem.m_lUserID = lUserID;
        if (szDevName.isEmpty()){
            devItem.m_szDevName = struDevNetInfo.m_szIp;
        }else {
            devItem.m_szDevName = szDevName;
        }
        devItem.m_struDevState.m_iLogState = 1;
        devItem.m_struNetInfo = struDevNetInfo;
        devItem.m_struDeviceInfoV30_jni = m_oNetDvrDeviceInfoV30;
        m_alDevList.add(devItem);

        // get instance of exception callback and set
        ExceptionCallBack oexceptionCbf = getExceptiongCb();

        if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf))
        {
            Log.e("[NetSDKSimpleDemo]", "NET_DVR_SetExceptionCallBack is failed!");
            return false;
        }
        Log.i("SimpleDemo", "NET_DVR_Login is Successful!");
        return true;
    }

    public boolean login_v30_jni_with_index(int iDevIndex){
        // 验证参数有效性
        if(iDevIndex<0||iDevIndex>=m_alDevList.size()) {
            Log.w("[NetSDKSimpleDemo]", "logout_jna failed with error param");
            return false;
        }
        DeviceItem devItem = m_alDevList.get(iDevIndex);
        if(devItem.m_struDevState.m_iLogState == 1){
            return true;
        }
        NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        String strIP = devItem.m_struNetInfo.m_szIp;
        int nPort = Integer.parseInt(devItem.m_struNetInfo.m_szPort);
        String strUser = devItem.m_struNetInfo.m_szUserName;
        String strPsd = devItem.m_struNetInfo.m_szPassword;

        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int lUserID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort, strUser, strPsd, m_oNetDvrDeviceInfoV30);
        if (lUserID < 0)
        {
            Log.e("SimpleDemo", "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return false;
        }

        devItem.m_byLoginFlag = 0;
        devItem.m_lUserID = lUserID;
        devItem.m_struDevState.m_iLogState = 1;
        devItem.m_struDeviceInfoV30_jni = m_oNetDvrDeviceInfoV30;

        // get instance of exception callback and set
        ExceptionCallBack oexceptionCbf = getExceptiongCb();

        if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf))
        {
            Log.e("[NetSDKSimpleDemo]", "NET_DVR_SetExceptionCallBack is failed!");
            return false;
        }
        Log.i("SimpleDemo", "NET_DVR_Login is Successful!");
        return true;
    }

    public boolean logout_jni(int iDevIndex) {
        if(iDevIndex<0||iDevIndex>=m_alDevList.size()) {
            Log.w("[NetSDKSimpleDemo]", "logout_jna failed with error param");
            return false;
        }
        DeviceItem devItem = m_alDevList.get(iDevIndex);
        boolean ret = HCNetSDK.getInstance().NET_DVR_Logout_V30(devItem.m_lUserID);
        if (!ret){
            Log.e("[NetSDKSimpleDemo]","NET_DVR_Logout failed with:" + HCNetSDKJNAInstance.getInstance().NET_DVR_GetLastError());
        }
//        m_alDevList.remove(iDevIndex);
        devItem.m_struDevState.reset();
        devItem.m_struDeviceInfoV30_jni = null;
        devItem.m_struDeviceInfoV40_jna = null;
        return true;
    }

    public void testFunc() {
        Log.i("[yz]","call DevManageGuider.testFunc()");
    }
}
