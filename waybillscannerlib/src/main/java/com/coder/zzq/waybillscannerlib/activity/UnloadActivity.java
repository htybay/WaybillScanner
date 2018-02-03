package com.coder.zzq.waybillscannerlib.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.zzq.smartshow.snackbar.SmartSnackbar;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.coder.zzq.waybillscannerlib.R;
import com.coder.zzq.waybillscannerlib.adpter.UnloadAdapter;
import com.coder.zzq.waybillscannerlib.bean.BillEntity;
import com.coder.zzq.waybillscannerlib.bean.RequestParams;
import com.coder.zzq.waybillscannerlib.bean.UnloadResult;
import com.coder.zzq.waybillscannerlib.bean.UserInfo;
import com.coder.zzq.waybillscannerlib.http.ApiService;
import com.coder.zzq.waybillscannerlib.http.BaseResponse;
import com.coder.zzq.waybillscannerlib.utils.CustomDialog;
import com.coder.zzq.waybillscannerlib.utils.SharePrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UnloadActivity extends BaseScanActivity {

    private BillEntity billEntity = new BillEntity();
    private List<BillEntity> billList;
    private UnloadAdapter billAdapter;
    private Map<String, BillEntity> mScanData = new HashMap<>();
    private String strTruck;

    private RecyclerView revResult;
    ;

    @Override
    protected int contentLayout() {
        return R.layout.activity_unload;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strTruck = "";
        billList = new ArrayList<>();
        billAdapter = new UnloadAdapter(billList);
        revResult = (RecyclerView) findViewById(R.id.rev_result);
        revResult.setLayoutManager(new LinearLayoutManager(this));
        revResult.setAdapter(billAdapter);
        mSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.dismiss();
                Intent intent = new Intent(UnloadActivity.this, UnloadSurveyActivity.class);
                intent.putExtra("tripNo", strTruck);
                intent.putExtra("flagActivity", 1);//1卸货
                startActivity(intent);
            }
        });
    }

    public static void startActivity(Context context, int deviceType, int operateType) {
        Intent intent = new Intent(context, UnloadActivity.class);
        intent.putExtra(EXTRA_DEVICE_TYPE, deviceType)
                .putExtra(EXTRA_OPERATE_TYPE, operateType);
        context.startActivity(intent);
    }


    @Override
    protected void onReceiveScanData(String data) {
        if (Utils.trimOrder(data).isEmpty()) {
            SmartSnackbar.get(this).showIndefinite("未扫描到有效内容！", "知道了");
            playErrorSound();
        }

        if (!data.matches(WAYBILL_REG_EX)) {
            SmartSnackbar.get(this).showIndefinite("请扫描货物标签：" + data, "知道了");
            playErrorSound();
        }
        final BillEntity billEntity = new BillEntity();


        int totalIndex = data.indexOf("-");
        int orderIndex = data.lastIndexOf("-");
        billEntity.setBillNo(data);
        billEntity.setGoodsTotal(data.substring(totalIndex + 1, orderIndex));
        billEntity.setGoodsOrder(data.substring(orderIndex + 1));

        mScanData.put(data, billEntity);
        ApiService.get().unloadGoods(RequestParams.getUnloadGoodsParams(this, strTruck, billEntity.getBillNo()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<UnloadResult>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (Utils.networkCheck(UnloadActivity.this, d)) {
                            mDisposables.put("unload", d);
                            CustomDialog.showProgressDialog(UnloadActivity.this);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<UnloadResult> value) {
                        UnloadResult.WaybillResultBean waybillResultBean = value.getReturnData().getWaybillResult().get(0);
                        BillEntity billEntity;
                        if (value.getReturnCode() == 1) {

                            if (billAdapter.getItemCount() > 100) {

                                billAdapter.remove(billAdapter.getItemCount() - 1);

                            }
                            if (waybillResultBean.isResult()) {
                                billEntity = mScanData.remove(waybillResultBean.getWaybillNo());

                                SmartToast.show("卸车成功|" + billEntity.getBillNo());
                                billEntity.setDestinationBranchName(waybillResultBean.getFromBranchName());
                                billAdapter.getData().add(0, billEntity);
                                billAdapter.notifyDataSetChanged();
                                mEmptyView.setVisibility(View.GONE);
                                playNormalSound();
                            } else {
                                playErrorSound();
                                SmartSnackbar.get(UnloadActivity.this).showIndefinite(waybillResultBean.getWaybillNo() + "|" + waybillResultBean.getMessage(), "好的");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposables.remove("unload");
                        CustomDialog.dissProgressDialog();
                        SmartSnackbar.get(UnloadActivity.this).showIndefinite(e.toString(), "知道了");
                    }

                    @Override
                    public void onComplete() {
                        mDisposables.remove("unload");
                        CustomDialog.dissProgressDialog();
                    }
                });

    }

    @Override
    protected boolean onPhoneScanTipClick() {
        return true;
    }
}
