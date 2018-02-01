package com.coder.zzq.waybillscannerlib.bean;

import java.util.List;

/**
 * Created by pig on 2017/12/31.
 */

public class UnloadSummary {


    /**
     * returnData : [{"TripNo":"CT00217122500030","WaybillNo":"02200229200085","TotalPieces":1,"ActualDeliveryPackage":1,"OtherLoaded":0,"ActualUnloaded":0,"OtherUnloaded":0},{"TripNo":"CT00217122500030","WaybillNo":"02200229200146","TotalPieces":1,"ActualDeliveryPackage":1,"OtherLoaded":0,"ActualUnloaded":0,"OtherUnloaded":0}]
     * TotalInfo : {"SumTotal":2,"SumActualDeliveryPackage":2,"SuOtherLoaded":0,"SumActualUnloaded":0,"SumUnScaned":0,"BranchName":"芜湖","ArrivalBranchName":"合肥","VehicleNo":"皖Q333333"}
     */

    private TotalInfoBean TotalInfo;
    private List<ReturnDataBean> returnData;

    public TotalInfoBean getTotalInfo() {
        return TotalInfo;
    }

    public void setTotalInfo(TotalInfoBean TotalInfo) {
        this.TotalInfo = TotalInfo;
    }

    public List<ReturnDataBean> getReturnData() {
        return returnData;
    }

    public void setReturnData(List<ReturnDataBean> returnData) {
        this.returnData = returnData;
    }

    public static class TotalInfoBean {
        /**
         * SumTotal : 2
         * SumActualDeliveryPackage : 2
         * SuOtherLoaded : 0
         * SumActualUnloaded : 0
         * SumUnScaned : 0
         * BranchName : 芜湖
         * ArrivalBranchName : 合肥
         * VehicleNo : 皖Q333333
         */

        private int SumTotal;
        private int SumActualDeliveryPackage;
        private int SuOtherLoaded;
        private int SumActualUnloaded;
        private int SumUnScaned;
        private String BranchName;
        private String ArrivalBranchName;
        private String VehicleNo;

        public int getSumTotal() {
            return SumTotal;
        }

        public void setSumTotal(int SumTotal) {
            this.SumTotal = SumTotal;
        }

        public int getSumActualDeliveryPackage() {
            return SumActualDeliveryPackage;
        }

        public void setSumActualDeliveryPackage(int SumActualDeliveryPackage) {
            this.SumActualDeliveryPackage = SumActualDeliveryPackage;
        }

        public int getSuOtherLoaded() {
            return SuOtherLoaded;
        }

        public void setSuOtherLoaded(int SuOtherLoaded) {
            this.SuOtherLoaded = SuOtherLoaded;
        }

        public int getSumActualUnloaded() {
            return SumActualUnloaded;
        }

        public void setSumActualUnloaded(int SumActualUnloaded) {
            this.SumActualUnloaded = SumActualUnloaded;
        }

        public int getSumUnScaned() {
            return SumUnScaned;
        }

        public void setSumUnScaned(int SumUnScaned) {
            this.SumUnScaned = SumUnScaned;
        }

        public String getBranchName() {
            return BranchName;
        }

        public void setBranchName(String BranchName) {
            this.BranchName = BranchName;
        }

        public String getArrivalBranchName() {
            return ArrivalBranchName;
        }

        public void setArrivalBranchName(String ArrivalBranchName) {
            this.ArrivalBranchName = ArrivalBranchName;
        }

        public String getVehicleNo() {
            return VehicleNo;
        }

        public void setVehicleNo(String VehicleNo) {
            this.VehicleNo = VehicleNo;
        }
    }

    public static class ReturnDataBean {
        /**
         * TripNo : CT00217122500030
         * WaybillNo : 02200229200085
         * TotalPieces : 1
         * ActualDeliveryPackage : 1
         * OtherLoaded : 0
         * ActualUnloaded : 0
         * OtherUnloaded : 0
         */

        private String TripNo;
        private String WaybillNo;
        private int TotalPieces;
        private int ActualDeliveryPackage;
        private int OtherLoaded;
        private int ActualUnloaded;
        private int OtherUnloaded;

        public String getTripNo() {
            return TripNo;
        }

        public void setTripNo(String TripNo) {
            this.TripNo = TripNo;
        }

        public String getWaybillNo() {
            return WaybillNo;
        }

        public void setWaybillNo(String WaybillNo) {
            this.WaybillNo = WaybillNo;
        }

        public int getTotalPieces() {
            return TotalPieces;
        }

        public void setTotalPieces(int TotalPieces) {
            this.TotalPieces = TotalPieces;
        }

        public int getActualDeliveryPackage() {
            return ActualDeliveryPackage;
        }

        public void setActualDeliveryPackage(int ActualDeliveryPackage) {
            this.ActualDeliveryPackage = ActualDeliveryPackage;
        }

        public int getOtherLoaded() {
            return OtherLoaded;
        }

        public void setOtherLoaded(int OtherLoaded) {
            this.OtherLoaded = OtherLoaded;
        }

        public int getActualUnloaded() {
            return ActualUnloaded;
        }

        public void setActualUnloaded(int ActualUnloaded) {
            this.ActualUnloaded = ActualUnloaded;
        }

        public int getOtherUnloaded() {
            return OtherUnloaded;
        }

        public void setOtherUnloaded(int OtherUnloaded) {
            this.OtherUnloaded = OtherUnloaded;
        }
    }
}
