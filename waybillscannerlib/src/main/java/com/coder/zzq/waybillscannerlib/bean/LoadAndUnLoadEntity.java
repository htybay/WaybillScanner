package com.coder.zzq.waybillscannerlib.bean;

import java.io.Serializable;

/**
 * Created by jetict on 2017/5/10.
 */
public class LoadAndUnLoadEntity implements Serializable {

    private String TripNo;
    private String WaybillNo;
    private int TotalPieces;
    private int ActualDeliveryPackage;
    private int OtherLoaded;
    private int ActualUnloaded;
    private int OtherUnloaded;

    private int SumActualUnloaded;

    private int SumUnScaned;

    public int getSumActualUnloaded() {
        return SumActualUnloaded;
    }

    public void setSumActualUnloaded(int sumActualUnloaded) {
        SumActualUnloaded = sumActualUnloaded;
    }

    public int getSumUnScaned() {
        return SumUnScaned;
    }

    public void setSumUnScaned(int sumUnScaned) {
        SumUnScaned = sumUnScaned;
    }

    public String getTripNo() {
        return TripNo;
    }

    public void setTripNo(String tripNo) {
        TripNo = tripNo;
    }

    public String getWaybillNo() {
        return WaybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        WaybillNo = waybillNo;
    }

    public int getTotalPieces() {
        return TotalPieces;
    }

    public void setTotalPieces(int totalPieces) {
        TotalPieces = totalPieces;
    }

    public int getActualDeliveryPackage() {
        return ActualDeliveryPackage;
    }

    public void setActualDeliveryPackage(int actualDeliveryPackage) {
        ActualDeliveryPackage = actualDeliveryPackage;
    }

    public int getOtherLoaded() {
        return OtherLoaded;
    }

    public void setOtherLoaded(int otherLoaded) {
        OtherLoaded = otherLoaded;
    }

    public int getOtherUnloaded() {
        return OtherUnloaded;
    }

    public void setOtherUnloaded(int otherUnloaded) {
        OtherUnloaded = otherUnloaded;
    }

    public int getActualUnloaded() {
        return ActualUnloaded;
    }

    public void setActualUnloaded(int actualUnloaded) {
        ActualUnloaded = actualUnloaded;
    }
}
