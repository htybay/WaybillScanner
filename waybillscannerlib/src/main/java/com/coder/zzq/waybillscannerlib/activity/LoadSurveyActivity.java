package com.coder.zzq.waybillscannerlib.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.coder.zzq.waybillscannerlib.bean.LoadAndUnLoadEntity;
import com.coder.zzq.waybillscannerlib.bean.LoadResult;
import com.coder.zzq.waybillscannerlib.bean.LoadSummary;
import com.coder.zzq.waybillscannerlib.bean.RequestParams;
import com.coder.zzq.waybillscannerlib.bean.TripNoToDepart;
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

import static com.coder.zzq.waybillscannerlib.R.id.survey;

/**
 * Created by pig on 2018/1/30.
 */

public class LoadSurveyActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_survey);
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

        Intent intent = getIntent();
        strTruck = intent.getStringExtra("tripNo");
        flagActivity = intent.getIntExtra("flagActivity", 0);

        txtvTitle.setText("交接单号: " + strTruck);
//        HttpTools.getDepartTripNoList(LoadSurveyActivity.this, LoadSurveyActivity.this);


        truckList = new ArrayList<>();


        ApiService.get()
                .getDepartTruckList(RequestParams.getDepartTruckListParam())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<TripNoToDepart>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.put("list", d);
                        CustomDialog.showProgressDialog(LoadSurveyActivity.this);
                    }

                    @Override
                    public void onNext(BaseResponse<List<TripNoToDepart>> value) {
                        mDisposables.remove("list");
                        truckList = value.getReturnData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposables.remove("list");
                        CustomDialog.dissProgressDialog();
                        SmartSnackbar.get(LoadSurveyActivity.this).showIndefinite(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mDisposables.remove("list");
                        CustomDialog.dissProgressDialog();
                    }
                });

        ApiService.get()
                .loadSurvey(RequestParams.getLoadSurveyParams(strTruck))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<LoadSummary>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.put("survey", d);
                    }

                    @Override
                    public void onNext(BaseResponse<LoadSummary> value) {
                        mDisposables.remove("survey");
                        txtvTitle.setText("交接单号: " + strTruck);
                        txtvTitle.setVisibility(View.VISIBLE);
                        mLoadSummary = value.getReturnData();
                        setLoadList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposables.remove("survey");
                    }

                    @Override
                    public void onComplete() {
                        mDisposables.remove("survey");
                    }
                });

        mChoose.setVisibility(View.VISIBLE);
        mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (truckList == null || truckList.isEmpty()) {
                    SmartToast.showAtTop("暂无车辆和目的网点可选！");
                    return;
                }
                getTripNoSelector().showAtLocation(mTopLine, Gravity.TOP, 0, Utils.dp2px(100));
            }
        });
    }


    private LoadSummary mLoadSummary;

    private void setLoadList() {

        List<LoadAndUnLoadEntity> load = mLoadSummary.getReturnData();

        mTotalLine.setVisibility(View.VISIBLE);

        mTopLine.setVisibility(View.VISIBLE);

        mTopTruck.setText(mLoadSummary.getTotalInfo().getVehicleNo());

        mTopPos.setText(mLoadSummary.getTotalInfo().getArrivalBranchName());

        int screenWidth = Utils.screenWidth(getResources());

        int width = Utils.dp2px(50);

        int height = Utils.dp2px(30);

        ArrayList<LoadAdapter.TableRow> th = new ArrayList<LoadAdapter.TableRow>();
        ArrayList<LoadAdapter.TableRow> tb = new ArrayList<LoadAdapter.TableRow>();
        LoadAdapter.TableCell[] titles = new LoadAdapter.TableCell[5];// 每行5个单元

        // 定义标题

        titles[0] = new LoadAdapter.TableCell("运单号", screenWidth - 4 * width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[1] = new LoadAdapter.TableCell("件数", width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[2] = new LoadAdapter.TableCell("实装", width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[3] = new LoadAdapter.TableCell("分装", width, height, LoadAdapter.TableCell.HEADER, 0);
        titles[4] = new LoadAdapter.TableCell("未装", width, height, LoadAdapter.TableCell.HEADER, 0);

        th.add(new LoadAdapter.TableRow(titles));

        loadAdapter = new LoadAdapter(this, th);

        lvheader.setDivider(null);

        lvheader.setAdapter(loadAdapter);

        // 每行的数据
        for (int i = 0; i < load.size(); i++) {
            LoadAdapter.TableCell[] cells = new LoadAdapter.TableCell[5];// 每行5个单元
            int isZero = load.get(i).getTotalPieces() - load.get(i).getActualDeliveryPackage() - load.get(i).getOtherLoaded();
            cells[0] = new LoadAdapter.TableCell(load.get(i).getWaybillNo(), screenWidth - 4 * width, height, LoadAdapter.TableCell.BODY, isZero);
            cells[1] = new LoadAdapter.TableCell(load.get(i).getTotalPieces(), width, height, LoadAdapter.TableCell.BODY, isZero);
            cells[2] = new LoadAdapter.TableCell(load.get(i).getActualDeliveryPackage(), width, height, LoadAdapter.TableCell.BODY, isZero);
            cells[3] = new LoadAdapter.TableCell(load.get(i).getOtherLoaded(), width, height, LoadAdapter.TableCell.BODY, isZero);
            cells[4] = new LoadAdapter.TableCell(isZero, width, height, LoadAdapter.TableCell.BODY, isZero);
            tb.add(new LoadAdapter.TableRow(cells));
        }

        loadAdapter = new LoadAdapter(this, tb);
        lvbody.setDivider(null);
        lvbody.setAdapter(loadAdapter);
        mTotalPieces.setText("共 " + mLoadSummary.getTotalInfo().getSumTotal() + " 件");
        mRealPieces.setText("实装 " + mLoadSummary.getTotalInfo().getSumActualDeliveryPackage() + " 件");
        mNotPieces.setText("未装 " + mLoadSummary.getTotalInfo().getSumUnloaded() + " 件");
    }


    private PopupWindow mTripNoSelector;
    private View mTripNoSelectorContent;
    private ListView mTruckListView;
    private List<TripNoToDepart> truckList;

    private TripNoToDepart mSelectedTripNo;


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
            EasyAdapter<TripNoToDepart> adapter = new EasyAdapter<TripNoToDepart>(R.layout.item) {
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


                    View dialogView = LayoutInflater.from(LoadSurveyActivity.this).inflate(R.layout.item_truck, null);

                    //下拉弹出确认框
                    TextView textView7 = (TextView) dialogView.findViewById(R.id.textView7);//当前网点
                    TextView textView8 = (TextView) dialogView.findViewById(R.id.textView8);//到达网点
                    TextView textView9 = (TextView) dialogView.findViewById(R.id.textView9);//趟次号
                    TextView textView10 = (TextView) dialogView.findViewById(R.id.textView10);//车牌
                    TextView textView11 = (TextView) dialogView.findViewById(R.id.textView11);//司机/手机
                    TextView textView12 = (TextView) dialogView.findViewById(R.id.textView12);//已装货量


                    textView7.setText(mSelectedTripNo.getBranchName());

                    final StringBuilder strBranch = new StringBuilder();
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

                    CustomDialog.showDialog(LoadSurveyActivity.this, "请核对选择的交接单", dialogView, "确认", "取消"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    strTruck = mSelectedTripNo.getTripNo();//取得趟次号
                                    dialog.dismiss();
                                    if (mSelectedTripNo.getVehicleNo().isEmpty()) {
                                        SmartToast.showAtTop("尚未配置车辆信息，请在PC端进行设置！");
                                        return;
                                    }
                                    ApiService.get()
                                            .loadSurvey(RequestParams.getLoadSurveyParams(strTruck))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<BaseResponse<LoadSummary>>() {
                                                @Override
                                                public void onSubscribe(Disposable d) {
                                                    mDisposables.put("survey", d);
                                                }

                                                @Override
                                                public void onNext(BaseResponse<LoadSummary> value) {
                                                    mDisposables.remove("survey");
                                                    txtvTitle.setText("交接单号: " + strTruck);
                                                    txtvTitle.setVisibility(View.VISIBLE);
                                                    mLoadSummary = value.getReturnData();
                                                    setLoadList();
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    mDisposables.remove("survey");
                                                }

                                                @Override
                                                public void onComplete() {
                                                    mDisposables.remove("survey");
                                                }
                                            });
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
