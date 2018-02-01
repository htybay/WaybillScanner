package com.coder.zzq.waybillscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.coder.zzq.waybillscannerlib.WaybillScanner;
import com.coder.zzq.waybillscannerlib.bean.UserInfo;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onTestClick(View view) {
        WaybillScanner.get(this)
                .runningEnvironment(1)
                .deviceType(WaybillScanner.DEVICE_PHONE)
                .operateType(WaybillScanner.OPT_SCAN_UNLOAD)
                .userInfo(UserInfo.create().userId("58").branchCode("2001").companyCode("002"))
                .toScan();
    }
}
