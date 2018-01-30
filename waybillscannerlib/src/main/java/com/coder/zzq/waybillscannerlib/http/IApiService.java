package com.coder.zzq.waybillscannerlib.http;

import com.coder.zzq.waybillscannerlib.bean.LoadGoodsParams;
import com.coder.zzq.waybillscannerlib.bean.LoadResult;
import com.coder.zzq.waybillscannerlib.bean.LoadSummary;
import com.coder.zzq.waybillscannerlib.bean.TripNoToDepart;

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
    Observable<BaseResponse<List<TripNoToDepart>>> getDepartTruckList(@QueryMap Map<String, String> params);

    @GET("/api/WaybillService/GetLoadingSummaryByTripNo")
    Observable<BaseResponse<LoadSummary>> loadSurvey(@QueryMap Map<String,String> params);

}
