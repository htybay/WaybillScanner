package com.coder.zzq.waybillscannerlib;

import android.content.Context;

import com.coder.zzq.waybillscannerlib.activity.TrunckLoadActivity;
import com.coder.zzq.waybillscannerlib.bean.UserInfo;
import com.coder.zzq.waybillscannerlib.utils.SharePrefUtils;

/**
 * Created by pig on 2018/1/29.
 */

public class WaybillScanner {

    public static final int DEVICE_PDA = 0;
    public static final int DEVICE_PHONE = 1;

    public static final int OPT_SCAN_LOAD = 2;
    public static final int OPT_SCAN_UNLOAD = 3;

    private int mDeviceType;
    private int mOperateType;

    private Context mContext;


    private WaybillScanner(Context context) {
        mContext = context;
    }


    public static WaybillScanner get(Context context) {

        return new WaybillScanner(context);
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
        SharePrefUtils.storeUserInfo(userInfo);
        return this;
    }


    public void toScan() {
        switch (mOperateType) {
            case OPT_SCAN_LOAD:
                TrunckLoadActivity.startActivity(mContext, mDeviceType, mOperateType);
                break;
            case OPT_SCAN_UNLOAD:

                break;
        }

        mContext = null;
    }

}
