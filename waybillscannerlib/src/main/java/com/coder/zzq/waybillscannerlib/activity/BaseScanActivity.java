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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.zzq.smartshow.snackbar.SmartSnackbar;
import com.coder.zzq.waybillscannerlib.R;
import com.coder.zzq.waybillscannerlib.WaybillScanner;
import com.coder.zzq.waybillscannerlib.receiver.Broadcast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    protected EditText mScanTip;

    protected View mEmptyView;

    protected Map<String,Disposable> mDisposables = new HashMap<>();




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
        mScanTip = (EditText) findViewById(R.id.scan_tip);
        mEmptyView = findViewById(R.id.imgv_empty);
        mDeviceType = getIntent().getIntExtra(EXTRA_DEVICE_TYPE, WaybillScanner.DEVICE_PDA);
        mOperateType = getIntent().getIntExtra(EXTRA_OPERATE_TYPE, WaybillScanner.DEVICE_PHONE);
        switch (mDeviceType) {
            case WaybillScanner.DEVICE_PDA:
                mScanTip.setText(R.string.pda_scan_tip);
                break;
            case WaybillScanner.DEVICE_PHONE:
                mScanTip.setText(R.string.phone_scan_tip);
                mScanTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BaseScanActivity.this, CaptureActivity.class);
                        if(onPhoneScanTipClick()){
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
                    onReceiveScanData(data);
                }
            }
        };
        IntentFilter filter = new IntentFilter(getstr);
        registerReceiver(mBrReceiver, filter);

    }



    private void soundTip(int soundType) {


        if (mDeviceType == WaybillScanner.DEVICE_PHONE){
            return;
        }

        String soundFile = "001.wav";

        try {

            AssetManager assetManager = this.getAssets();
            final MediaPlayer player = new MediaPlayer();

            switch (soundType) {
                case SOUND_TIP_NORMAL:
                    soundFile = "003.wav";
                    break;
                case SOUND_TIP_ERROR:
                    soundFile = "001.wav";
                    break;

            }

            AssetFileDescriptor afd = assetManager.openFd(soundFile);
            player.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());

            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void playNormalSound(){
        soundTip(SOUND_TIP_NORMAL);
    }

    protected void playErrorSound(){
        soundTip(SOUND_TIP_ERROR);
    }



    protected boolean onReceiveScanData(String data) {
        if (Utils.trimOrder(data).isEmpty()) {
            SmartSnackbar.get(this).showIndefinite("未扫描到有效内容！","知道了");
            playErrorSound();
            return false;
        }

        if (!data.matches(WAYBILL_REG_EX)){
            SmartSnackbar.get(this).showIndefinite("扫描到非法运单号：" + data,"知道了");
            playErrorSound();
            return false;
        }



        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_CODE_TO_SCAN && data != null) {
            Bundle bundle = data.getExtras();
            switch (bundle.getInt(CodeUtils.RESULT_TYPE)) {
                case CodeUtils.RESULT_SUCCESS:
                    String orderStr = Utils.trimOrder(bundle.getString(CodeUtils.RESULT_STRING));
                    onReceiveScanData(orderStr);
                    break;
                case CodeUtils.RESULT_FAILED:
                    SmartSnackbar.get(this).show("扫面失败");
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposables != null && !mDisposables.isEmpty()){
            Set<String> list = mDisposables.keySet();
            for (String s : list){
                if (!mDisposables.get(s).isDisposed()){
                    mDisposables.get(s).dispose();

                }
                mDisposables.remove(s);
            }
        }
    }
}
