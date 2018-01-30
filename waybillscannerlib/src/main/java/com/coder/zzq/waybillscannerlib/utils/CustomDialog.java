package com.coder.zzq.waybillscannerlib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.coder.zzq.waybillscannerlib.R;


import dmax.dialog.SpotsDialog;

/**
 * 自定义dialog
 * Created by YUEYINGSK on 2016/8/15.
 * Change by jetict on 2017/03/28.
 */
public class CustomDialog {

    private static SpotsDialog dialog;

    /**
     * 默认的对话框
     *
     * @param context
     * @param title
     * @param content
     * @param btnOKStr
     * @param btnCancelStr
     * @param onClick
     * @param onCancel
     */
    public static void showDialog(Context context, String title, String content,
                                  String btnOKStr, String btnCancelStr,
                                  DialogInterface.OnClickListener onClick, DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            mBuilder.setTitle(title);
        }
        mBuilder.setMessage(content);

        if (!TextUtils.isEmpty(btnOKStr)) {
            mBuilder.setPositiveButton(btnOKStr, onClick);
        }
        if (!TextUtils.isEmpty(btnCancelStr)) {
            mBuilder.setNegativeButton(btnCancelStr, onCancel);
        }
        mBuilder.setCancelable(false);
        mBuilder.show();
    }

    /**
     * 自定义样式
     *
     * @param context
     * @param title
     * @param view
     * @param btnOKStr
     * @param btnCancelStr
     * @param onClick
     * @param onCancel
     */
    public static void showDialog(Context context, String title, View view,
                                  String btnOKStr, String btnCancelStr,
                                  DialogInterface.OnClickListener onClick, DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setIcon(R.drawable.login_icon_logo);
        if (!TextUtils.isEmpty(title)) {
            mBuilder.setTitle(title);
        }
        mBuilder.setView(view);

        if (!TextUtils.isEmpty(btnOKStr)) {
            mBuilder.setPositiveButton(btnOKStr, onClick);
        }
        if (!TextUtils.isEmpty(btnCancelStr)) {
            mBuilder.setNegativeButton(btnCancelStr, onCancel);
        }
        mBuilder.setCancelable(false);
        mBuilder.show();
    }

    /**
     * 应用重启的dialog
     *
     * @param context
     * @param content
     */
    public static void showRestartAppDialog(final Context context, String content) {
        showDialog(context, "提示", content, "立即重启应用", "暂不重启应用", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //保存数据
                //MobclickAgent.onKillProcess(context);
                final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }, null);
    }

    /**
     * loading对话框
     *
     * @param context
     */
    public static void showProgressDialog(Context context) {
        if (dialog != null) {
            dissProgressDialog();
        }
        dialog = new SpotsDialog(context, R.style.CustomSpotsDialog);
        dialog.show();
    }

    /**
     * loading对话框
     *
     * @param context
     */
    public static void showProgressDialogNoCancel(Context context) {
        if (dialog != null) {
            dissProgressDialog();
        }
        dialog = new SpotsDialog(context, R.style.CustomSpotsDialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 隐藏loading对话框
     */
    public static void dissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
