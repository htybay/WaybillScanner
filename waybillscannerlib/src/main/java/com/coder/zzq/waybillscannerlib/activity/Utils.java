package com.coder.zzq.waybillscannerlib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;

import com.coder.zzq.smartshow.snackbar.SmartSnackbar;
import com.coder.zzq.waybillscannerlib.R;

import io.reactivex.disposables.Disposable;


/**
 * Created by pig on 2018/1/29.
 */

public class Utils {
    public static String trimOrder(String string) {
        return string == null ? "" : string.trim();
    }


    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int screenWidth(Resources resources) {
        return resources.getDisplayMetrics().widthPixels;
    }

    public static boolean isNetworkConnect(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;

    }

    public static boolean networkCheck(final Context context, Disposable d) {
        if (!isNetworkConnect(context)) {
            d.dispose();
            SmartSnackbar.get((Activity) context).showIndefinite("网络不给力，请检查网络设置", "设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    context.startActivity(intent);
                }
            });
            return false;
        }

        return true;
    }
}
