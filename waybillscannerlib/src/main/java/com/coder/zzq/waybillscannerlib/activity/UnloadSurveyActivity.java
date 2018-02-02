package com.coder.zzq.waybillscannerlib.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.coder.zzq.smartshow.snackbar.SmartSnackbar;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.coder.zzq.waybillscannerlib.R;
import com.coder.zzq.waybillscannerlib.adpter.EasyAdapter;
import com.coder.zzq.waybillscannerlib.adpter.LoadAdapter;
import com.coder.zzq.waybillscannerlib.bean.RequestParams;
import com.coder.zzq.waybillscannerlib.bean.ToUnloadTripNo;
import com.coder.zzq.waybillscannerlib.bean.UnloadSummary;
import com.coder.zzq.waybillscannerlib.http.ApiService;
import com.coder.zzq.waybillscannerlib.http.BaseResponse;
import com.coder.zzq.waybillscannerlib.utils.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.coder.zzq.waybillscannerlib.R.id.driver;
import static com.coder.zzq.waybillscannerlib.R.id.last_station;
import static com.coder.zzq.waybillscannerlib.R.id.textView10;
import static com.coder.zzq.waybillscannerlib.R.id.textView11;
import static com.coder.zzq.waybillscannerlib.R.id.textView12;
import static com.coder.zzq.waybillscannerlib.R.id.textView7;
import static com.coder.zzq.waybillscannerlib.R.id.textView8;
import static com.coder.zzq.waybillscannerlib.R.id.textView9;

public class UnloadSurveyActivity extends AppCompatActivity {
    TextView txtvTitle;


    ListView lvheader;

    ListView lvbody;

    TextView mTotalPieces;

    TextView mRealPieces;

    TextView mNotPieces;

    LinearLayout mTotalLine;

    TextView mTopTruck;

    TextView mTopMark;

    TextView mTopPos;

    LinearLayout mTopLine;
    private int flagActivity = 0;
    private String strTruck;
    private LoadAdapter loadAdapter = null;

    private String wayBill;

    private TextView mChoose;
    protected Map<String, Disposable> mDisposables = new HashMap<>();
    private Toolbar mToolbar;

    private TextView mTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload_survey);
        ApiService.get().getReceiveTruckList(RequestParams.getReceiveTruckListParams(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<ToUnloadTripNo>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (Utils.networkCheck(UnloadSurveyActivity.this, d)) {
                            mDisposables.put("list", d);
                            CustomDialog.showProgressDialog(UnloadSurveyActivity.this);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<List<ToUnloadTripNo>> value) {
                        truckList = value.getReturnData();

                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposables.remove("list");
                        CustomDialog.dissProgressDialog();
                        SmartSnackbar.get(UnloadSurveyActivity.this).showIndefinite(e.toString(), "知道了");
                    }

                    @Override
                    public void onComplete() {
                        mDisposables.remove("list");
                        CustomDialog.dissProgressDialog();
                    }
                });

        mTopLine = (LinearLayout) findViewById(R.id.top_line);

        mTopPos = (TextView) findViewById(R.id.top_pos);
        mTopMark = (TextView) findViewById(R.id.top_mark);

        mTopTruck = (TextView) findViewById(R.id.top_truck);

        mTotalLine = (LinearLayout) findViewById(R.id.total_line);

        mTotalPieces = (TextView) findViewById(R.id.total_pieces);
        mNotPieces = (TextView) findViewById(R.id.not_pieces);
        mRealPieces = (TextView) findViewById(R.id.real_pieces);
        lvbody = (ListView) findViewById(R.id.lv_body);
        lvheader = (ListView) findViewById(R.id.lv_header);
        txtvTitle = (TextView) findViewById(R.id.txtv_title);
        mChoose = (TextView) findViewById(R.id.txtv_popo);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (truckList == null || truckList.isEmpty()) {
                    SmartToast.showAtTop("暂无车辆和目的网点可选！");
                    return;
                }
                getTripNoSelector().showAtLocation(mToolbar, Gravity.TOP, 0, (int) Utils.dp2px(UnloadSurveyActivity.this, 100));
            }
        });

        mTip = (TextView) findViewById(R.id.tip);

    }

    private PopupWindow mTripNoSelector;
    private View mTripNoSelectorContent;
    private ListView mTruckListView;
    private List<ToUnloadTripNo> truckList;

    private ToUnloadTripNo mSelectedTripNo;


    private PopupWindow getTripNoSelector() {

        if (mTripNoSelector == null) {
            mTripNoSelectorContent = LayoutInflater.from(this).inflate(R.layout.trip_no_selector_copy, null);
            mTruckListView = (ListView) mTripNoSelectorContent.findViewById(R.id.truck_list);
            mTripNoSelector = new PopupWindow(mTripNoSelectorContent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mTripNoSelector.setFocusable(true);
            final View shadow = mTripNoSelectorContent.findViewById(R.id.shadow);
            shadow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTripNoSelector.dismiss();
                }
            });
            EasyAdapter<ToUnloadTripNo> adapter = new EasyAdapter<ToUnloadTripNo>(R.layout.item) {
                @Override
                protected void processView(View convertView, ToUnloadTripNo o) {
                    String str = o.getVehicleNo() + "/" + o.getArrivalBranchName() + "\n" + o.getTripNo();
                    SpannableString spanStr = new SpannableString(str);
                    int index = str.indexOf("\n");
                    ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#666666"));
                    spanStr.setSpan(span, index + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((CheckedTextView) convertView).setText(spanStr);
                }
            };

            mTruckListView.setAdapter(adapter);

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
                    List<ToUnloadTripNo> items = new ArrayList<ToUnloadTripNo>();

                    for (int i = 0; i < truckList.size(); i++) {
                        ToUnloadTripNo toUnloadTripNo = truckList.get(i);
                        if (toUnloadTripNo.toString().contains(s)) {
                            items.add(toUnloadTripNo);
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
                    mSelectedTripNo = (ToUnloadTripNo) parent.getItemAtPosition(position);


                    View dialogView = LayoutInflater.from(UnloadSurveyActivity.this).inflate(R.layout.item_truck_new, null);

                    //下拉弹出确认框
                    TextView lastStationTV = (TextView) dialogView.findViewById(last_station);//上一站
                    TextView tripNoTV = (TextView) dialogView.findViewById(R.id.trip_no);//交接单号
                    TextView truckNoTV = (TextView) dialogView.findViewById(R.id.truck_no);//车牌
                    TextView driverTV = (TextView) dialogView.findViewById(driver);//司机/手机


                    lastStationTV.append(mSelectedTripNo.getBranchName());


                    tripNoTV.append(mSelectedTripNo.getTripNo());
                    truckNoTV.append(mSelectedTripNo.getVehicleNo());
                    //textView11.setText(truckEntity.getDriverName1() + "/" + truckEntity.getDriverContactTel1());
                    driverTV.append(mSelectedTripNo.getDriverName() + "/" + mSelectedTripNo.getDriverContactTel());


                    mTripNoSelector.dismiss();

                    CustomDialog.showDialog(UnloadSurveyActivity.this, "请核对选择的交接单", dialogView, "确认", "取消"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    strTruck = mSelectedTripNo.getTripNo();//取得趟次号
                                    dialog.dismiss();
                                    getList(mSelectedTripNo.getTripNo());
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    strTruck = mSelectedTripNo.getTripNo();//取得趟次号
                                    getList(strTruck);
                                    dialog.dismiss();
                                }
                            }
                    );


                }
            });


        }

        ((EditText) mTripNoSelectorContent.findViewById(R.id.search_edt)).setText("");
        ((EasyAdapter) mTruckListView.getAdapter()).clear();
        ((EasyAdapter) mTruckListView.getAdapter()).addAll(truckList);

        return mTripNoSelector;
    }


    private void getList(String tripNo) {
        //卸车概况
        ApiService.get().unloadSurvey(RequestParams.getUnloadSurveyParams(this, tripNo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<UnloadSummary>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (Utils.networkCheck(UnloadSurveyActivity.this, d)) {
                            mDisposables.put("survey", d);
                            CustomDialog.showProgressDialog(UnloadSurveyActivity.this);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<UnloadSummary> value) {
                        txtvTitle.setVisibility(View.VISIBLE);
                        txtvTitle.setText("交接单号：" + strTruck);
                        mTip.setVisibility(View.GONE);
                        setUnLoadList(value.getReturnData());
                        mTotalLine.setVisibility(View.VISIBLE);
                        mTopLine.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposables.remove("survey");
                        CustomDialog.dissProgressDialog();
                        SmartSnackbar.get(UnloadSurveyActivity.this).showIndefinite(e.toString(),"知道了");
                    }

                    @Override
                    public void onComplete() {
                        mDisposables.remove("survey");
                        CustomDialog.dissProgressDialog();
                    }
                });
    }

    private UnloadSummary mUnloadSummary;
    private void setUnLoadList(UnloadSummary unloadSummary) {
        mUnloadSummary = unloadSummary;
        List<UnloadSummary.ReturnDataBean> unLoad = mUnloadSummary.getReturnData();
        mTopTruck.setText(mUnloadSummary.getTotalInfo().getVehicleNo());
        mTopMark.setText("来自");
        mTopPos.setText(mUnloadSummary.getTotalInfo().getBranchName());
        int screenWidth = Utils.screenWidth(getResources());
        int width = (int) Utils.dp2px(this,50);
        int height = (int) Utils.dp2px(this,30);
        ArrayList<LoadAdapter.TableRow> th = new ArrayList<LoadAdapter.TableRow>();
        ArrayList<LoadAdapter.TableRow> tb = new ArrayList<LoadAdapter.TableRow>();
        LoadAdapter.TableCell[] titles = new LoadAdapter.TableCell[5];// 每行5个单元

        // 定义标题
        titles[0] = new LoadAdapter.TableCell("运单号", screenWidth - 4 * width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[1] = new LoadAdapter.TableCell("实装", width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[2] = new LoadAdapter.TableCell("已卸", width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[3] = new LoadAdapter.TableCell("未卸", width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[4] = new LoadAdapter.TableCell("其它", width, height, LoadAdapter.TableCell.HEADER, 0);
        th.add(new LoadAdapter.TableRow(titles));
        LoadAdapter loadAdapter = new LoadAdapter(this, th);
        lvheader.setAdapter(loadAdapter);

        // 每行的数据
        for (int i = 0; i < unLoad.size(); i++) {
            int isZero = 0;//其它
            int isZero1 = 0;//未卸
            //其它和未卸都大于0
            if (unLoad.get(i).getOtherLoaded() > 0 && unLoad.get(i).getActualDeliveryPackage() - unLoad.get(i).getActualUnloaded() > 0) {
                LoadAdapter.TableCell[] cells = new LoadAdapter.TableCell[5];// 每行5个单元
                isZero = unLoad.get(i).getOtherLoaded() - unLoad.get(i).getOtherUnloaded();
                isZero1 = unLoad.get(i).getActualDeliveryPackage() - unLoad.get(i).getActualUnloaded();
                cells[0] = new LoadAdapter.TableCell(unLoad.get(i).getWaybillNo(), screenWidth - 4 * width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[1] = new LoadAdapter.TableCell(unLoad.get(i).getActualDeliveryPackage() + "/ " + unLoad.get(i).getTotalPieces(), width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[2] = new LoadAdapter.TableCell(unLoad.get(i).getActualUnloaded(), width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[3] = new LoadAdapter.TableCell(isZero1, width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[4] = new LoadAdapter.TableCell(isZero, width, height, LoadAdapter.TableCell.BODY, isZero1);

                tb.add(new LoadAdapter.TableRow(cells));
            } else {
                //求其它
                if (unLoad.get(i).getOtherLoaded() > 0) {

                    LoadAdapter.TableCell[] cells = new LoadAdapter.TableCell[5];// 每行5个单元
                    isZero = unLoad.get(i).getOtherLoaded() - unLoad.get(i).getOtherUnloaded();
                    isZero1 = unLoad.get(i).getActualDeliveryPackage() - unLoad.get(i).getActualUnloaded();
                    cells[0] = new LoadAdapter.TableCell(unLoad.get(i).getWaybillNo(), screenWidth - 4 * width, height, LoadAdapter.TableCell.BODY, isZero);
                    cells[1] = new LoadAdapter.TableCell(unLoad.get(i).getActualDeliveryPackage() + "/ " + unLoad.get(i).getTotalPieces(), width, height, LoadAdapter.TableCell.BODY, isZero);
                    cells[2] = new LoadAdapter.TableCell(unLoad.get(i).getActualUnloaded(), width, height, LoadAdapter.TableCell.BODY, isZero);
                    cells[3] = new LoadAdapter.TableCell(isZero1, width, height, LoadAdapter.TableCell.BODY, isZero);
                    cells[4] = new LoadAdapter.TableCell(isZero, width, height, LoadAdapter.TableCell.BODY, isZero);

                    tb.add(new LoadAdapter.TableRow(cells));
                }
                //未卸

                LoadAdapter.TableCell[] cells = new LoadAdapter.TableCell[5];// 每行5个单元
                isZero = unLoad.get(i).getOtherLoaded() - unLoad.get(i).getOtherUnloaded();
                isZero1 = unLoad.get(i).getActualDeliveryPackage() - unLoad.get(i).getActualUnloaded();

                cells[0] = new LoadAdapter.TableCell(unLoad.get(i).getWaybillNo(), screenWidth - 4 * width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[1] = new LoadAdapter.TableCell(unLoad.get(i).getActualDeliveryPackage() + "/ " + unLoad.get(i).getTotalPieces(), width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[2] = new LoadAdapter.TableCell(unLoad.get(i).getActualUnloaded(), width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[3] = new LoadAdapter.TableCell(isZero1, width, height, LoadAdapter.TableCell.BODY, isZero1);
                cells[4] = new LoadAdapter.TableCell(isZero, width, height, LoadAdapter.TableCell.BODY, isZero1);

                tb.add(new LoadAdapter.TableRow(cells));

            }
        }

        loadAdapter = new LoadAdapter(this, tb);
        lvbody.setDivider(null);
        lvbody.setAdapter(loadAdapter);
        mTotalPieces.setText("共 " + mUnloadSummary.getTotalInfo().getSumTotal() + " 件");
        mRealPieces.setText("已卸 " + mUnloadSummary.getTotalInfo().getSumActualUnloaded() + " 件");
        mNotPieces.setText("未卸 " + mUnloadSummary.getTotalInfo().getSumUnScaned() + " 件");

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
    }
}
