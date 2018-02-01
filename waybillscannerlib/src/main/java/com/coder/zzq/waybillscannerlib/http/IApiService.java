package com.coder.zzq.waybillscannerlib.http;

import com.coder.zzq.waybillscannerlib.bean.LoadGoodsParams;
import com.coder.zzq.waybillscannerlib.bean.LoadResult;
import com.coder.zzq.waybillscannerlib.bean.LoadSummary;
import com.coder.zzq.waybillscannerlib.bean.ToUnloadTripNo;
import com.coder.zzq.waybillscannerlib.bean.TripNoToDepart;
import com.coder.zzq.waybillscannerlib.bean.UnloadGoodsParams;
import com.coder.zzq.waybillscannerlib.bean.UnloadResult;
import com.coder.zzq.waybillscannerlib.bean.UnloadSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by pig on 2018/1/30.
 */

public interface IApiService {

    @POST("/api/WaybillService/LoadPackage")
    Observable<BaseResponse<LoadResult>> loadGoods(@Body LoadGoodsParams loadGoodsParams);

    @GET("/api/WaybillService/GetStoLocalListByDeliveryBranchCode")
    Observable<BaseResponse<ArrayList<TripNoToDepart>>> getDepartTruckList(@QueryMap Map<String, String> params);

    @GET("/api/WaybillService/GetLoadingSummaryByTripNo")
    Observable<BaseResponse<LoadSummary>> loadSurvey(@QueryMap Map<String, String> params);

    @POST("/api/WaybillService/UnloadBatchPer")
    Observable<BaseResponse<UnloadResult>> unloadGoods(@Body UnloadGoodsParams unloadGoodsParams);

    @GET("/api/WaybillService/GetStoLocalListByReceiveBranchCode")
    Observable<BaseResponse<List<ToUnloadTripNo>>> getReceiveTruckList(@QueryMap Map<String, String> params);

    @GET("/api/WaybillService/GetUnLoadingSummaryByTripNo")
    Observable<BaseResponse<UnloadSummary>> unloadSurvey(@QueryMap Map<String, String> params);

}
