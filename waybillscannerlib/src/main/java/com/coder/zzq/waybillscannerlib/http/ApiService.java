package com.coder.zzq.waybillscannerlib.http;

import com.coder.zzq.waybillscannerlib.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pig on 2018/1/30.
 */

public class ApiService {

    public static final String DEBUG_API_BASE_URL = "http://testapi.liinji.cn";

    public static final String PRERELEASE_API_BASE_URL = "http://cache.api.liinji.com";

    public static final String REAL_API_BASE_URL = "https://api.liinji.com";

    public static String mApiBaseUril = DEBUG_API_BASE_URL;

    private static IApiService sApiService;

    private ApiService() {

    }


    public static IApiService get() {

        if (sApiService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            sApiService = new Retrofit.Builder()
                    .client(httpClient)

                    .baseUrl(mApiBaseUril)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(IApiService.class);
        }

        return sApiService;
    }
}
