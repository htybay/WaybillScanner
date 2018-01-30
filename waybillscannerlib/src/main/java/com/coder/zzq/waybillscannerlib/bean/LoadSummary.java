package com.coder.zzq.waybillscannerlib.bean;



import java.util.List;

/**
 * Created by pig on 2017/12/31.
 */

public class LoadSummary {

    /**
     * returnData : [{"TripNo":"CT00217123100005","WaybillNo":"02200133400410","TotalPieces":1,"ActualDeliveryPackage":1,"OtherLoaded":0},{"TripNo":"CT00217123100005","WaybillNo":"02200136300403","TotalPieces":1,"ActualDeliveryPackage":0,"OtherLoaded":0},{"TripNo":"CT00217123100005","WaybillNo":"02200136300410","TotalPieces":2,"ActualDeliveryPackage":0,"OtherLoaded":0}]
     * TotalInfo : {"SumTotal":4,"SumActualDeliveryPackage":1,"SuOtherLoaded":0,"SumUnloaded":3}
     */

    private TotalInfoBean TotalInfo;
    private List<LoadAndUnLoadEntity> returnData;

    public TotalInfoBean getTotalInfo() {
        return TotalInfo;
    }

    public void setTotalInfo(TotalInfoBean TotalInfo) {
        this.TotalInfo = TotalInfo;
    }

    public List<LoadAndUnLoadEntity> getReturnData() {
        return returnData;
    }

    public void setReturnData(List<LoadAndUnLoadEntity> returnData) {
        this.returnData = returnData;
    }

    public static class TotalInfoBean {

        private String VehicleNo;
        private int SumTotal;
        private int SumActualDeliveryPackage;
        private int SuOtherLoaded;
        private int SumUnloaded;
        private String BranchName = "";
        private String ArrivalBranchName = "";


        public String getVehicleNo() {
            return VehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            VehicleNo = vehicleNo;
        }

        public String getBranchName() {
            return BranchName;
        }

        public void setBranchName(String branchName) {
            BranchName = branchName;
        }

        public String getArrivalBranchName() {
            return ArrivalBranchName;
        }

        public void setArrivalBranchName(String arrivalBranchName) {
            ArrivalBranchName = arrivalBranchName;
        }

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

        public int getSumUnloaded() {
            return SumUnloaded;
        }

        public void setSumUnloaded(int SumUnloaded) {
            this.SumUnloaded = SumUnloaded;
        }
    }

}
