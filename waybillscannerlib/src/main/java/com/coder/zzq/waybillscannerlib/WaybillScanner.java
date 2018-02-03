package com.coder.zzq.waybillscannerlib;

import android.content.Context;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.coder.zzq.waybillscannerlib.activity.TrunckLoadActivity;
import com.coder.zzq.waybillscannerlib.activity.UnloadActivity;
import com.coder.zzq.waybillscannerlib.bean.UserInfo;
import com.coder.zzq.waybillscannerlib.http.ApiService;
import com.coder.zzq.waybillscannerlib.utils.SharePrefUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by pig on 2018/1/29.
 */

public class WaybillScanner {

    public static final int DEVICE_PDA = 0;
    public static final int DEVICE_PHONE = 1;

    public static final int OPT_SCAN_LOAD = 2;
    public static final int OPT_SCAN_UNLOAD = 3;

    public static final int ENV_TEST = 1;
    public static final int ENV_PRE_RELEASE = 2;
    public static final int ENV_REAL = 3;

    private int mDeviceType;
    private int mOperateType;


    private Context mContext;


    private WaybillScanner(Context context) {
        mContext = context;
        SmartToast.plainToast(mContext.getApplicationContext());
        ZXingLibrary.initDisplayOpinion(mContext.getApplicationContext());
    }


    public static WaybillScanner get(Context context) {

        return new WaybillScanner(context);
    }

    public WaybillScanner runningEnvironment(int env) {
        switch (env) {
            case ENV_TEST:
                ApiService.mApiBaseUril = ApiService.DEBUG_API_BASE_URL;
                break;
            case ENV_PRE_RELEASE:
                ApiService.mApiBaseUril = ApiService.PRERELEASE_API_BASE_URL;
                break;
            case ENV_REAL:
                ApiService.mApiBaseUril = ApiService.REAL_API_BASE_URL;
                break;
        }

        return this;
    }

    public WaybillScanner deviceType(int deviceType) {
        mDeviceType = deviceType;
        return this;
    }

    public WaybillScanner operateType(int operateType) {
        mOperateType = operateType;
        return this;
    }

    public WaybillScanner userInfo(UserInfo userInfo) {
        SharePrefUtils.storeUserInfo(mContext, userInfo);
        return this;
    }


    public void toScan() {
        switch (mOperateType) {
            case OPT_SCAN_LOAD:
                TrunckLoadActivity.startActivity(mContext, mDeviceType, mOperateType);
                break;
            case OPT_SCAN_UNLOAD:
                UnloadActivity.startActivity(mContext, mDeviceType, mOperateType);
                break;
        }

        mContext = null;
    }

}
