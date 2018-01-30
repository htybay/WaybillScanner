package com.coder.zzq.waybillscannerlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.coder.zzq.waybillscannerlib.MyApplication;
import com.coder.zzq.waybillscannerlib.bean.UserInfo;


/**
 * Created by Administrator on 2017/3/15.
 */
public class SharePrefUtils {
    public static final String USER_IFNO_PREF = "user_info";
    public static final String USER_INFO_JSON = "json";
    private static final String USER_IFNO_JPUSH_CLIENT_ALIAS = "alias";
    private static final String PRINT_TYPE = "print_type";
    public static final int PRINT_START = 0;
    public static final int PRINT_ORDER = 1;
    public static final String START_SEQUENCE = "start_sequence";
    private static final String END_SEQUENCE = "end_sequence";

    public static final String ORDER_SIGN_REMARKS_PREF = "order_sign_remarks";

    public static void storeUserInfo(UserInfo userInfo){
        userInfoPref().edit().putString(USER_INFO_JSON,userInfo == null ? null : JsonUtils.jsonStr(userInfo)).commit();
    }

    private static SharedPreferences userInfoPref() {
        return MyApplication.sContext.getSharedPreferences(USER_IFNO_PREF, Context.MODE_PRIVATE);
    }

    public static UserInfo getUserInfo(){
        String jsonStr = userInfoPref().getString(USER_INFO_JSON,null);
        return jsonStr == null ? null : JsonUtils.json2obj(jsonStr,UserInfo.class);
    }

    public static void storeJPushClientAlias(String alias){

        userInfoPref().edit().putString(USER_IFNO_JPUSH_CLIENT_ALIAS,alias).commit();
    }

    public static String fetchJPushClientAlias(){
        return userInfoPref().getString(USER_IFNO_JPUSH_CLIENT_ALIAS,null);
    }

    public static final String PRINT_SETTING_PREF = "print_setting_pref";
    public static final String SAVED_DEVICE_ADDRESS = "device_address";
    public static final String SAVED_DEVICE_NAME = "device_name";
    public static final String PRINT_DIRECTLY = "print";
    public static final String CANCEL_CONNECT = "cancel_connect";

    public static final String JIABO_PIRNT_MODE = "print_mode";

    public static final String GEO_LOCATION_PREF = "geo_location_pref";

    public static final String MODE_TSC = "tsc";
    public static final String MODE_ESC = "esc";



    private static SharedPreferences geoLocationPref(){
        return MyApplication.sContext.getSharedPreferences(GEO_LOCATION_PREF, Context.MODE_PRIVATE);
    }

    public static void storeLocation(String address){
        geoLocationPref().edit().putString("location",address).commit();
    }

    private static SharedPreferences printSettingPref(){
        return MyApplication.sContext.getSharedPreferences(PRINT_SETTING_PREF, Context.MODE_PRIVATE);
    }

    public static void storeJiaboPrintMode(String mode){
        printSettingPref().edit().putString(JIABO_PIRNT_MODE,mode).commit();
    }

    public static String getJiaboPrintMode(){
        return printSettingPref().getString(JIABO_PIRNT_MODE,"");
    }


    public static void setCancelConnect(boolean cancel){
        printSettingPref().edit().putBoolean(CANCEL_CONNECT,cancel).commit();
    }

    public static boolean getCancelConnect(){
        return printSettingPref().getBoolean(CANCEL_CONNECT,false);
    }

    public static String getSavedDeviceAddress(){
        return printSettingPref().getString(SAVED_DEVICE_ADDRESS,"");
    }

    public static void storeDeviceAddress(String deviceAddress){
        printSettingPref().edit().putString(SAVED_DEVICE_ADDRESS,deviceAddress).commit();
    }

    public static String getSavedDeviceName(){
        return printSettingPref().getString(SAVED_DEVICE_NAME,"");
    }

    public static void storeDeviceName(String deviceName){
        printSettingPref().edit().putString(SAVED_DEVICE_NAME,deviceName).commit();
    }


    public static boolean canPrintDirectly(){
        return printSettingPref().getBoolean(PRINT_DIRECTLY,false);
    }

    public static void setPrintDirectly(boolean can){
        printSettingPref().edit().putBoolean(PRINT_DIRECTLY,can).commit();
    }


    public static final String COM_NAME = "com_name";
    public static final String COM_TEL = "com_tel";
    public static String getCompanyName() {
        return userInfoPref().getString(COM_NAME,"");
    }

    public static String getCompanyTel() {
        return userInfoPref().getString(COM_TEL,"");
    }
    public static void setCompanyName(String companyName) {
        userInfoPref().edit().putString(COM_NAME,companyName).commit();
    }

    public static void setCompanyTel(String companyTel) {
        userInfoPref().edit().putString(COM_TEL,companyTel).commit();
    }


    public static int getPrintType() {
        return printSettingPref().getInt(PRINT_TYPE,PRINT_START);
    }

    public static void setPrintType(int printType){
        printSettingPref().edit().putInt(PRINT_TYPE,printType).commit();
    }

    public static void setStartSequence(int startSequence){
        printSettingPref().edit().putInt(START_SEQUENCE,startSequence).commit();
    }

    public static void setEndSequence(int endSequence){
        printSettingPref().edit().putInt(END_SEQUENCE,endSequence).commit();
    }

    public static int getStartSequence(){
        return printSettingPref().getInt(START_SEQUENCE,0);
    }

    public static int getEndSequence(){
        return printSettingPref().getInt(END_SEQUENCE,0);
    }


    private static SharedPreferences getOrderSignRemarksPref(){
        return MyApplication.sContext.getSharedPreferences(ORDER_SIGN_REMARKS_PREF, Context.MODE_PRIVATE);
    }


    public static void storeSignRemarks(String order, String remarks){
        getOrderSignRemarksPref().edit().putString(order,remarks).commit();
    }

    public static String getOrderSignRemarks(String order){
       return getOrderSignRemarksPref().getString(order,"");
    }

    public static void clearSignRemarks(String order){
        getOrderSignRemarksPref().edit().remove(order).commit();
    }

    public static void storeOrderId(String order, String id){
        getOrderSignRemarksPref().edit().putString(order + "_id",id).commit();
    }

    public static String getOrderId(String order){
        return getOrderSignRemarksPref().getString(order + "_id","");
    }

    public static void clearOrderId(String order){
        getOrderSignRemarksPref().edit().remove(order + "_id").commit();
    }



}
