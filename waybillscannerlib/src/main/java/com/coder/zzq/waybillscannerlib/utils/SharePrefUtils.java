package com.coder.zzq.waybillscannerlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static void storeUserInfo(Context context,UserInfo userInfo){
        userInfoPref(context).edit().putString(USER_INFO_JSON,userInfo == null ? null : JsonUtils.jsonStr(userInfo)).commit();
    }

    private static SharedPreferences userInfoPref(Context context) {
        return context.getSharedPreferences(USER_IFNO_PREF, Context.MODE_PRIVATE);
    }

    public static UserInfo getUserInfo(Context context){
        String jsonStr = userInfoPref(context).getString(USER_INFO_JSON,null);
        return jsonStr == null ? null : JsonUtils.json2obj(jsonStr,UserInfo.class);
    }






}
