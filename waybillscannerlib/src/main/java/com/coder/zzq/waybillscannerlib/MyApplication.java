package com.coder.zzq.waybillscannerlib;

import android.app.Application;
import android.content.Context;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.coder.zzq.waybillscannerlib.bean.UserInfo;
import com.coder.zzq.waybillscannerlib.utils.SharePrefUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by pig on 2018/1/29.
 */

public class MyApplication extends Application {
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        ZXingLibrary.initDisplayOpinion(this);
        SmartToast.plainToast(this);
//        UserInfo userInfo = new UserInfo();
//        userInfo.setCompanyCode("002");
//        userInfo.setBranchCode("2001");
//        userInfo.setUserId("58");
//        SharePrefUtils.storeUserInfo(userInfo);
    }
}
