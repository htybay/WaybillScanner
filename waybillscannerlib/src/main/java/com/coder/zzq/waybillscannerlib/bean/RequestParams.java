package com.coder.zzq.waybillscannerlib.bean;

import android.content.Intent;

import com.coder.zzq.waybillscannerlib.http.HttpParams;
import com.coder.zzq.waybillscannerlib.utils.SharePrefUtils;

import java.util.Map;

/**
 * Created by pig on 2018/1/30.
 */

public class RequestParams {

    public static LoadGoodsParams getLoadGoodsParams(String truckNo, String waybill) {
        UserInfo userInfo = SharePrefUtils.getUserInfo();
        LoadGoodsParams loadGoodsParams = new LoadGoodsParams();
        loadGoodsParams.setBranchCode(userInfo.getBranchCode());
        loadGoodsParams.setCompanyCode(userInfo.getCompanyCode());
        loadGoodsParams.setUserId(userInfo.getUserId().length() == 0 ? 0 : Integer.parseInt(userInfo.getUserId()));
        loadGoodsParams.setWaybillList(waybill);
        loadGoodsParams.setLocalTripNo(truckNo);
        loadGoodsParams.setTrayNo("");
        return loadGoodsParams;
    }

    public static Map<String, String> getDepartTruckListParam() {
        UserInfo userInfo = SharePrefUtils.getUserInfo();

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


    public static Map<String, String> getLoadSurveyParams(String s) {
        UserInfo userInfo = SharePrefUtils.getUserInfo();

        return HttpParams.get()
                .addParam(BRANCH_CODE, userInfo.getBranchCode())
                .addParam(COMPANY_CODE, userInfo.getCompanyCode())
                .addParam(TRIP_NO, s)
                .addParam(USER_ID, userInfo.getUserId())
                .toMap();
    }
}
