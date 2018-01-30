package com.coder.zzq.waybillscannerlib.activity;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.coder.zzq.waybillscannerlib.MyApplication;

/**
 * Created by pig on 2018/1/29.
 */

public class Utils {
    public static String trimOrder(String string) {
        return string == null ? "" : string.trim();
    }

    public static int dp2px(float dp){
        return (int) dp2px(MyApplication.sContext,dp);
    }

    public static float dp2px(Context context, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }

    public static int screenWidth(Resources resources) {
        return resources.getDisplayMetrics().widthPixels;
    }
}
