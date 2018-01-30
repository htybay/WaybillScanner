package com.coder.zzq.waybillscannerlib.bean;

import android.text.SpannableString;

/**
 * Created by pig on 2017/12/22.
 */

public class TripNoToDepart {

    /**
     * BranchCode : 2001
     * BranchName : 合肥
     * CompanyCode : 002
     * DeliveryTruckDate : 2017-12-01 00:00:00
     * DriverName : 包溪
     * DriverContactTel : 15645614566
     * ArrivalBranchCode : 2001
     * ArrivalBranchName : 合肥
     * Status : 1
     * TotalPieces : 0
     * RemainPieces : 0
     * TripNo : CT00217120100013
     * VehicleNo : 皖A123456
     */

    private String BranchCode;
    private String BranchName;
    private String CompanyCode;
    private String DeliveryTruckDate;
    private String DriverName;
    private String DriverContactTel;
    private String ArrivalBranchCode;
    private String ArrivalBranchName;
    private int Status;
    private String TotalPieces;
    private String RemainPieces;
    private String TripNo;
    private String VehicleNo;

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String BranchCode) {
        this.BranchCode = BranchCode;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String BranchName) {
        this.BranchName = BranchName;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String CompanyCode) {
        this.CompanyCode = CompanyCode;
    }

    public String getDeliveryTruckDate() {
        return DeliveryTruckDate;
    }

    public void setDeliveryTruckDate(String DeliveryTruckDate) {
        this.DeliveryTruckDate = DeliveryTruckDate;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String DriverName) {
        this.DriverName = DriverName;
    }

    public String getDriverContactTel() {
        return DriverContactTel;
    }

    public void setDriverContactTel(String DriverContactTel) {
        this.DriverContactTel = DriverContactTel;
    }

    public String getArrivalBranchCode() {
        return ArrivalBranchCode;
    }

    public void setArrivalBranchCode(String ArrivalBranchCode) {
        this.ArrivalBranchCode = ArrivalBranchCode;
    }

    public String getArrivalBranchName() {
        return ArrivalBranchName;
    }

    public void setArrivalBranchName(String ArrivalBranchName) {
        this.ArrivalBranchName = ArrivalBranchName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getTotalPieces() {
        return TotalPieces;
    }

    public void setTotalPieces(String TotalPieces) {
        this.TotalPieces = TotalPieces;
    }

    public String getRemainPieces() {
        return RemainPieces;
    }

    public void setRemainPieces(String RemainPieces) {
        this.RemainPieces = RemainPieces;
    }

    public String getTripNo() {
        return TripNo;
    }

    public void setTripNo(String TripNo) {
        this.TripNo = TripNo;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }

    @Override
    public String toString() {
        SpannableString spannableString;
        return VehicleNo + "/" + ArrivalBranchName + "\n" + TripNo;
    }
}
