package com.coder.zzq.waybillscannerlib.bean;

/**
 * Created by pig on 2018/1/31.
 */

public class UnloadGoodsParams {

    /**
     * TripNo : string
     * WaybillList : string
     * CompanyCode : string
     * BranchCode : string
     * UserId : 0
     * Token : string
     * Sign : string
     */

    private String TripNo;
    private String WaybillList;
    private String CompanyCode;
    private String BranchCode;
    private int UserId;
    private String Token;
    private String Sign;

    public String getTripNo() {
        return TripNo;
    }

    public void setTripNo(String TripNo) {
        this.TripNo = TripNo;
    }

    public String getWaybillList() {
        return WaybillList;
    }

    public void setWaybillList(String WaybillList) {
        this.WaybillList = WaybillList;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String CompanyCode) {
        this.CompanyCode = CompanyCode;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String BranchCode) {
        this.BranchCode = BranchCode;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String Sign) {
        this.Sign = Sign;
    }
}
