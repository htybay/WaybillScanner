package com.coder.zzq.waybillscannerlib.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.coder.zzq.smartshow.snackbar.SmartSnackbar;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.coder.zzq.waybillscannerlib.R;
import com.coder.zzq.waybillscannerlib.adpter.BillInfoAdapter;
import com.coder.zzq.waybillscannerlib.adpter.EasyAdapter;
import com.coder.zzq.waybillscannerlib.bean.BillEntity;
import com.coder.zzq.waybillscannerlib.bean.LoadResult;
import com.coder.zzq.waybillscannerlib.bean.RequestParams;
import com.coder.zzq.waybillscannerlib.bean.TripNoToDepart;
import com.coder.zzq.waybillscannerlib.http.ApiService;
import com.coder.zzq.waybillscannerlib.http.BaseResponse;
import com.coder.zzq.waybillscannerlib.utils.CustomDialog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.value;

public class TrunckLoadActivity extends BaseScanActivity {
    private BillInfoAdapter billAdapter;

    private TextView mTrukAndBranch;
    private TextView mTruckNo;
    private RecyclerView revResult;
    private Toolbar mToolbar;
    private int mLastPos;
    ;

    @Override
    protected int contentLayout() {
        return R.layout.activity_trunck_load;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrukAndBranch = (TextView) findViewById(R.id.truk_and_branch);
        mTruckNo = (TextView) findViewById(R.id.truck_no);
        revResult = (RecyclerView) findViewById(R.id.rev_result);
        mTrukAndBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTruckAndBranchChoose(view);
            }
        });
        billAdapter = new BillInfoAdapter(new ArrayList<BillEntity>());
        revResult.setLayoutManager(new LinearLayoutManager(this));
        revResult.setAdapter(billAdapter);
        ApiService.get()
                .getDepartTruckList(RequestParams.getDepartTruckListParam(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ArrayList<TripNoToDepart>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (Utils.networkCheck(TrunckLoadActivity.this,d)){
                            mDisposables.put("list",d);
                            CustomDialog.showProgressDialog(TrunckLoadActivity.this);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<ArrayList<TripNoToDepart>> value) {
                        mDisposables.remove("list");
                        truckList = value.getReturnData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposables.remove("list");
                        CustomDialog.dissProgressDialog();
                        if (e.getClass() == SocketTimeoutException.class){

                        }
                        SmartSnackbar.get(TrunckLoadActivity.this).showIndefinite(e.toString(),"知道了");
                    }

                    @Override
                    public void onComplete() {
                        mDisposables.remove("list");
                        CustomDialog.dissProgressDialog();
                    }
                });

        mSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.dismiss();
                Intent intent = new Intent(TrunckLoadActivity.this, LoadSurveyActivity.class);
                intent.putExtra("tripNo", mSelectedTripNo.getTripNo());
                intent.putExtra("flagActivity", 0);//装车
                startActivity(intent);
            }
        });
    }

    public static void startActivity(Context context, int deviceType, int operateType) {
        Intent intent = new Intent(context, TrunckLoadActivity.class);
        intent.putExtra(EXTRA_DEVICE_TYPE, deviceType)
                .putExtra(EXTRA_OPERATE_TYPE, operateType);
        context.startActivity(intent);
    }

    private TripNoToDepart mSelectedTripNo;

    private Map<String, BillEntity> mScanData = new HashMap<>();


    @Override
    protected boolean onPhoneScanTipClick() {
        super.onPhoneScanTipClick();
        if (mTrukAndBranch.getText().equals("请选择车辆信息")) {
            SmartToast.showAtTop("请选择车辆信息！");
            playErrorSound();
            return false;
        }

        if (TextUtils.isEmpty(mSelectedTripNo.getVehicleNo())){
            SmartToast.showAtTop("尚未配置车辆信息，请在PC端进行设置！");
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
                    String orderStr = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.d("scan", "on scan str :" + orderStr);
                    onReceiveScanData(orderStr);
                    break;
                case CodeUtils.RESULT_FAILED:
                    SmartSnackbar.get(this).show("扫面失败");
                    break;
            }
        }
    }


    protected void onReceiveScanData(String data) {

        Log.d("fuck",data);

        if (Utils.trimOrder(data).isEmpty()) {
            SmartSnackbar.get(this).showIndefinite("未扫描到有效内容！","知道了");
            playErrorSound();
            return;
        }

        if (!data.matches(WAYBILL_REG_EX)){

            SmartSnackbar.get(this).showIndefinite("数据解析失败:" + data + "\n请对准货物标签重试！","知道了");

            playErrorSound();
            return;
        }


        if (mTrukAndBranch.getText().equals("请选择车辆信息")) {
            SmartToast.showInCenter("请选择车辆信息！");
            playErrorSound();
            return;
        }

        BillEntity billEntity = new BillEntity();

        int totalIndex = data.indexOf("-");
        int orderIndex = data.lastIndexOf("-");
        billEntity.setBillNo(data);
        billEntity.setGoodsTotal(data.substring(totalIndex + 1, orderIndex));
        billEntity.setGoodsOrder(data.substring(orderIndex + 1));
        billEntity.setDestinationBranchName(mSelectedTripNo.getArrivalBranchName());

        mScanData.put(data, billEntity);

        load(data);

    }

    private void load(String waybill) {
        ApiService.get()
                .loadGoods(RequestParams.getLoadGoodsParams(this,mSelectedTripNo.getTripNo(), waybill))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<LoadResult>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (Utils.networkCheck(TrunckLoadActivity.this,d)){
                            mDisposables.put("loadGoods",d);
                            CustomDialog.showProgressDialog(TrunckLoadActivity.this);
                        }

                    }

                    @Override
                    public void onNext(BaseResponse<LoadResult> value) {
                        mDisposables.remove("loadGoods");
                        LoadResult.WaybillResultBean waybillResultBean = value.getReturnData().getWaybillResult().get(0);
                        BillEntity billEntity;
                        if (value.getReturnCode() == 1) {

                            if (billAdapter.getItemCount() > 100) {

                                billAdapter.remove(billAdapter.getItemCount() - 1);
                            }
                            if (waybillResultBean.isResult()) {
                                SmartToast.show("装车成功|" + waybillResultBean.getWaybillNo());
                                billEntity = mScanData.remove(waybillResultBean.getWaybillNo());
                                billAdapter.getData().add(0, billEntity);
                                billAdapter.notifyDataSetChanged();
                                mEmptyView.setVisibility(View.GONE);
                                playNormalSound();
                            } else {
                                playErrorSound();
                                SmartSnackbar.get(TrunckLoadActivity.this).showIndefinite(waybillResultBean.getWaybillNo() + "|" + waybillResultBean.getMessage(), "好的");

                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposables.remove("loadGoods");
                        CustomDialog.dissProgressDialog();
                        SmartSnackbar.get(TrunckLoadActivity.this).showIndefinite(e.toString(),"知道了");
                    }

                    @Override
                    public void onComplete() {
                        mDisposables.remove("loadGoods");
                        CustomDialog.dissProgressDialog();;
                    }
                });

    }

    EasyAdapter<TripNoToDepart> adapterTruck;
    private List<TripNoToDepart> truckList;

    private String strTruck;
    private PopupWindow mTripNoSelector;
    private View mTripNoSelectorContent;
    ListView mTruckListView;

    private PopupWindow getTripNoSelector() {

        if (mTripNoSelector == null) {
            mTripNoSelectorContent = LayoutInflater.from(this).inflate(R.layout.trip_no_selector_copy, null);
            mTruckListView = (ListView) mTripNoSelectorContent.findViewById(R.id.truck_list);
            mTripNoSelector = new PopupWindow(mTripNoSelectorContent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mTripNoSelector.setFocusable(true);

            View shadow = mTripNoSelectorContent.findViewById(R.id.shadow);
            shadow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTripNoSelector.dismiss();
                }
            });
            adapterTruck = new EasyAdapter<TripNoToDepart>(R.layout.item) {
                @Override
                protected void processView(View convertView, TripNoToDepart o) {
                    String str = o.getVehicleNo() + "/" + o.getArrivalBranchName() + "\n" + o.getTripNo();
                    SpannableString spanStr = new SpannableString(str);
                    int index = str.indexOf("\n");
                    ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#666666"));
                    spanStr.setSpan(span, index + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((CheckedTextView) convertView).setText(spanStr);
                }
            };

            mTruckListView.setAdapter(adapterTruck);

            mTruckListView.setTextFilterEnabled(true);

            final EditText editText = (EditText) mTripNoSelectorContent.findViewById(R.id.search_edt);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    List<TripNoToDepart> items = new ArrayList<TripNoToDepart>();

                    for (int i = 0; i < truckList.size(); i++) {
                        TripNoToDepart tripNoToDepart = truckList.get(i);
                        if (tripNoToDepart.toString().contains(s)) {
                            items.add(tripNoToDepart);
                        }
                    }

                    ((EasyAdapter) mTruckListView.getAdapter()).clear();
                    ((EasyAdapter) mTruckListView.getAdapter()).addAll(items);
                    ((EasyAdapter) mTruckListView.getAdapter()).notifyDataSetChanged();
                }
            });

            mTruckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                    mSelectedTripNo = (TripNoToDepart) parent.getItemAtPosition(position);

                    mSurvey.setVisibility(View.VISIBLE);

                    mTrukAndBranch.setText(mSelectedTripNo.getVehicleNo() + "  " + mSelectedTripNo.getArrivalBranchName());

                    mTruckNo.setText(mSelectedTripNo.getTripNo());

                    mTruckNo.setTextColor(Color.parseColor("#333333"));
                    if (position != mLastPos) {
                        ((BillInfoAdapter) revResult.getAdapter()).getData().clear();
                        revResult.getAdapter().notifyDataSetChanged();
                    }

                    View dialogView = LayoutInflater.from(TrunckLoadActivity.this).inflate(R.layout.item_truck, null);

                    //下拉弹出确认框
                    TextView textView7 = (TextView) dialogView.findViewById(R.id.textView7);//当前网点
                    TextView textView8 = (TextView) dialogView.findViewById(R.id.textView8);//到达网点
                    TextView textView9 = (TextView) dialogView.findViewById(R.id.textView9);//趟次号
                    TextView textView10 = (TextView) dialogView.findViewById(R.id.textView10);//车牌
                    TextView textView11 = (TextView) dialogView.findViewById(R.id.textView11);//司机/手机
                    TextView textView12 = (TextView) dialogView.findViewById(R.id.textView12);//已装货量


                    textView7.setText(mSelectedTripNo.getBranchName());

                    StringBuilder strBranch = new StringBuilder();
                    if (!TextUtils.isEmpty(mSelectedTripNo.getArrivalBranchName())) {
                        strBranch.append(mSelectedTripNo.getArrivalBranchName());
                    }

                    textView8.setText(strBranch);
                    textView9.setText(mSelectedTripNo.getTripNo());
                    textView10.setText(mSelectedTripNo.getVehicleNo());
                    //textView11.setText(truckEntity.getDriverName1() + "/" + truckEntity.getDriverContactTel1());
                    textView11.setText(mSelectedTripNo.getDriverName() + "/" + mSelectedTripNo.getDriverContactTel());
                    textView12.setText(mSelectedTripNo.getTotalPieces().toString());
                    //未卸货
                    TextView textView = (TextView) dialogView.findViewById(R.id.textView);
                    TextView textView14 = (TextView) dialogView.findViewById(R.id.textView14);
                    TextView textView15 = (TextView) dialogView.findViewById(R.id.textView15);
                    textView.setVisibility(View.GONE);
                    textView14.setVisibility(View.GONE);
                    textView15.setVisibility(View.GONE);
                    textView12.setVisibility(View.GONE);

                    mTripNoSelector.dismiss();

                    CustomDialog.showDialog(TrunckLoadActivity.this, "请核对选择的交接单", dialogView, "确认", "取消"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    strTruck = mSelectedTripNo.getTripNo();//取得趟次号
                                    dialog.dismiss();
                                    if (mSelectedTripNo.getVehicleNo().isEmpty()) {
                                        SmartToast.showAtTop("尚未配置车辆信息，请在PC端进行设置！");
                                    }

                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    strTruck = mSelectedTripNo.getTripNo();//取得趟次号
                                    dialog.dismiss();
                                }
                            }
                    );


                }
            });


        }

        ((EditText) mTripNoSelectorContent.findViewById(R.id.search_edt)).setText("");
        ((EasyAdapter<TripNoToDepart>) mTruckListView.getAdapter()).clear();
        ((EasyAdapter<TripNoToDepart>) mTruckListView.getAdapter()).addAll(truckList);

        return mTripNoSelector;
    }


    public void onTruckAndBranchChoose(View view) {
        if (truckList == null || truckList.isEmpty()) {
            SmartToast.showAtTop("暂无车辆和目的网点可选！");
            return;
        }
        getTripNoSelector().showAtLocation(mTrukAndBranch, Gravity.TOP, 0, (int) Utils.dp2px(this,100));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
