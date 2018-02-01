package com.coder.zzq.waybillscannerlib.bean;

import android.content.Context;
import android.content.Intent;

import com.coder.zzq.waybillscannerlib.http.HttpParams;
import com.coder.zzq.waybillscannerlib.utils.SharePrefUtils;

import java.util.Map;

/**
 * Created by pig on 2018/1/30.
 */

public class RequestParams {

    public static LoadGoodsParams getLoadGoodsParams(Context context, String truckNo, String waybill) {
        UserInfo userInfo = SharePrefUtils.getUserInfo(context);
        LoadGoodsParams loadGoodsParams = new LoadGoodsParams();
        loadGoodsParams.setBranchCode(userInfo.getBranchCode());
        loadGoodsParams.setCompanyCode(userInfo.getCompanyCode());
        loadGoodsParams.setUserId(userInfo.getUserId().length() == 0 ? 0 : Integer.parseInt(userInfo.getUserId()));
        loadGoodsParams.setWaybillList(waybill);
        loadGoodsParams.setLocalTripNo(truckNo);
        loadGoodsParams.setTrayNo("");
        return loadGoodsParams;
    }

    public static Map<String, String> getDepartTruckListParam(Context context) {
        UserInfo userInfo = SharePrefUtils.getUserInfo(context);

        return HttpParams.get()
                .addParam(COMPANY_CODE, userInfo.getCompanyCode())
                .addParam(BRANCH_CODE, userInfo.getBranchCode())
                .addParam("userId", userInfo.getUserId())
                .addParam("status", "1")
                .toMap();
    }

    public static final String COMPANY_CODE = "companyCode";
    public static final String BRANCH_CODE = "branchCode";
    public static final String TRIP_NO = "tripNo";
    public static final String USER_ID = "userId";


    public static Map<String, String> getLoadSurveyParams(Context context, String s) {
        UserInfo userInfo = SharePrefUtils.getUserInfo(context);

        return HttpParams.get()
                .addParam(BRANCH_CODE, userInfo.getBranchCode())
                .addParam(COMPANY_CODE, userInfo.getCompanyCode())
                .addParam(TRIP_NO, s)
                .addParam(USER_ID, userInfo.getUserId())
                .toMap();
    }

    public static UnloadGoodsParams getUnloadGoodsParams(Context context, String tripNo, String bills) {
        UserInfo userInfo = SharePrefUtils.getUserInfo(context);
        UnloadGoodsParams params = new UnloadGoodsParams();
        params.setCompanyCode(userInfo.getCompanyCode());
        params.setBranchCode(userInfo.getBranchCode());
        params.setUserId(Integer.parseInt(userInfo.getUserId()));
        params.setTripNo(tripNo);
        params.setWaybillList(bills);
        return params;
    }

    public static Map<String, String> getReceiveTruckListParams(Context context) {
        UserInfo userInfo = SharePrefUtils.getUserInfo(context);
        return HttpParams.get()
                .addParam(BRANCH_CODE, userInfo.getBranchCode())
                .addParam(COMPANY_CODE, userInfo.getCompanyCode())
                .addParam(USER_ID, Integer.parseInt(userInfo.getUserId()))
                .addParam("status", "3")
                .toMap();
    }

    public static Map<String, String> getUnloadSurveyParams(Context context, String tripNo) {
        UserInfo userInfo = SharePrefUtils.getUserInfo(context);
        return HttpParams.get()
                .addParam("tripNo",tripNo)
                .addParam(BRANCH_CODE,userInfo.getBranchCode())
                .addParam(COMPANY_CODE,userInfo.getCompanyCode())
                .toMap();
    }

}
