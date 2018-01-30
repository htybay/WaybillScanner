package com.coder.zzq.waybillscannerlib.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pig on 2018/1/30.
 */

public class HttpParams extends HashMap<String, String> {

    private HttpParams(){

    }

    public static HttpParams get(){
        return new HttpParams();
    }

    public HttpParams addParam(String paramName, String paramValue) {
        put(paramName, paramValue);
        return this;
    }

    public HttpParams addParam(String paramName, int paramValue) {
        put(paramName, String.valueOf(paramName));
        return this;
    }


    public Map<String, String> toMap() {
        return this;
    }


}
