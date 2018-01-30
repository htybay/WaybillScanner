package com.coder.zzq.waybillscannerlib.bean;

import java.util.List;

/**
 * Created by pig on 2017/12/26.
 */

public class LoadResult {

    private List<WaybillResultBean> waybillResult;

    public List<WaybillResultBean> getWaybillResult() {
        return waybillResult;
    }

    public void setWaybillResult(List<WaybillResultBean> waybillResult) {
        this.waybillResult = waybillResult;
    }

    public static class WaybillResultBean {
        /**
         * WaybillNo : 02200135600054-2-1
         * Message :
         * Result : true
         */

        private String WaybillNo;
        private String Message;
        private boolean Result;

        public String getWaybillNo() {
            return WaybillNo;
        }

        public void setWaybillNo(String WaybillNo) {
            this.WaybillNo = WaybillNo;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public boolean isResult() {
            return Result;
        }

        public void setResult(boolean Result) {
            this.Result = Result;
        }
    }

}
