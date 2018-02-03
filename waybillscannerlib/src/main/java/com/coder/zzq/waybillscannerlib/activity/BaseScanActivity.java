package com.coder.zzq.waybillscannerlib.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.coder.zzq.smartshow.snackbar.SmartSnackbar;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.coder.zzq.waybillscannerlib.R;
import com.coder.zzq.waybillscannerlib.WaybillScanner;
import com.coder.zzq.waybillscannerlib.receiver.Broadcast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import io.reactivex.disposables.Disposable;

/**
 * Created by pig on 2018/1/29.
 */

public abstract class BaseScanActivity extends AppCompatActivity {

    public static final int REQ_CODE_TO_SCAN = 0x001;

    public static final String EXTRA_DEVICE_TYPE = "device_type";
    public static final String EXTRA_OPERATE_TYPE = "operate_type";

    public static final int SOUND_TIP_NORMAL = 4;

    public static final int SOUND_TIP_ERROR = 5;

    protected int mDeviceType;
    protected int mOperateType;

    protected TextView mSurvey;

    protected TextView mToolbarTitle;

    protected View mScanTip;

    protected View mEmptyView;

    protected Map<String, Disposable> mDisposables = new HashMap<>();


    //凯立 K7 PAD 逻辑 start
    private Broadcast bc;
    private EditText prefix, suffix, interval, pda_sn;
    private int offset = 0;
    private BroadcastReceiver mBrReceiver;
    private boolean statusbar = true, apk_install = true;
    private String sn;
    //凯立 K7 PAD  逻辑 end

    public static final String WAYBILL_REG_EX = "[0-9]{14,}-[0-9]{1,}-[0-9]{1,}";


    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_base_layout);
        LinearLayout root = (LinearLayout) findViewById(R.id.root_container);
        LayoutInflater.from(this).inflate(contentLayout(), root, true);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mSurvey = (TextView) findViewById(R.id.survey);
        mScanTip = findViewById(R.id.scan_line);
        mEmptyView = findViewById(R.id.imgv_empty);
        mDeviceType = getIntent().getIntExtra(EXTRA_DEVICE_TYPE, WaybillScanner.DEVICE_PDA);
        mOperateType = getIntent().getIntExtra(EXTRA_OPERATE_TYPE, WaybillScanner.DEVICE_PHONE);
        switch (mDeviceType) {
            case WaybillScanner.DEVICE_PDA:

                break;
            case WaybillScanner.DEVICE_PHONE:

                mScanTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(BaseScanActivity.this, CaptureActivity.class);
                        if (onPhoneScanTipClick()) {
                            SmartToast.dismiss();
                            startActivityForResult(intent, REQ_CODE_TO_SCAN);
                        }


                    }
                });
                break;
        }

        switch (mOperateType) {
            case WaybillScanner.OPT_SCAN_LOAD:
                mToolbarTitle.setText(R.string.load_title);
                mSurvey.setText(R.string.load_survey_desc);
                break;
            case WaybillScanner.OPT_SCAN_UNLOAD:
                mToolbarTitle.setText(R.string.unload_title);
                mSurvey.setText(R.string.unload_survey_desc);
                mSurvey.setVisibility(View.VISIBLE);
                break;
        }


    }

    protected boolean onPhoneScanTipClick() {

        return false;
    }

    protected abstract int contentLayout();

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        if (mDeviceType == WaybillScanner.DEVICE_PDA) {
            //PDA 逻辑start
            scanSetting();
            //PDA 逻辑 end
        }

    }

    public void scanSetting() {
        bc = Broadcast.getInstance(this);
        getsystemscandata();//注册接收数据广播，以TOAST形式显示，退出界面也可以查看数据

        Intent intent = new Intent("com.android.service_settings");
        intent.putExtra("pda_sn", "string");//机器码设置成“string”
        intent.putExtra("scanner_sound_play", false);
        this.sendBroadcast(intent);
    }


    public void getsystemscandata() {
        final String getstr = "com.android.receive_scan_action";

        mBrReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(getstr)) {
                    String data = intent.getStringExtra("data");
//                    onReceiveScanData(data);
                }
            }
        };
        IntentFilter filter = new IntentFilter(getstr);
        registerReceiver(mBrReceiver, filter);

    }


    private MediaPlayer mMediaPlayerNormal;
    private MediaPlayer mMediaPlayerError;

    private void soundTip(int type) {


        switch (type) {
            case SOUND_TIP_NORMAL:
                getNormalPlayer().start();
                break;
            case SOUND_TIP_ERROR:
                getErrorPlayer().start();
                break;
        }

    }

    private MediaPlayer getErrorPlayer() {
        if (mMediaPlayerError == null){
            mMediaPlayerError = new MediaPlayer();
            try {
                AssetFileDescriptor assetFileDescriptor = getAssets().openFd("001.wav");
                mMediaPlayerError.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mMediaPlayerError.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mMediaPlayerError;
    }

    private MediaPlayer getNormalPlayer() {
        if (mMediaPlayerNormal == null){
            mMediaPlayerNormal = new MediaPlayer();
            try {
                AssetFileDescriptor assetFileDescriptor = getAssets().openFd("003.wav");
                mMediaPlayerNormal.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mMediaPlayerNormal.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mMediaPlayerNormal;
    }

    protected void playNormalSound() {
        soundTip(SOUND_TIP_NORMAL);
    }

    protected void playErrorSound() {
        soundTip(SOUND_TIP_ERROR);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseMediaplayer(mMediaPlayerError);
        releaseMediaplayer(mMediaPlayerNormal);
        mMediaPlayerNormal = null;
        mMediaPlayerError = null;

        if (mDisposables != null && !mDisposables.isEmpty()) {
            Set<String> list = mDisposables.keySet();
            for (String s : list) {
                if (!mDisposables.get(s).isDisposed()) {
                    mDisposables.get(s).dispose();

                }
                mDisposables.remove(s);
            }
        }

        SmartSnackbar.destroy(this);
    }

    private void releaseMediaplayer(MediaPlayer mediaPlayerError) {
        if (mediaPlayerError != null){
            mediaPlayerError.stop();
            mediaPlayerError.release();
        }
    }
}
