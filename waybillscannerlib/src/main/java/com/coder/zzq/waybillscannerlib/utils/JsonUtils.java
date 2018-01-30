package com.coder.zzq.waybillscannerlib.utils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/3/15.
 */
public class JsonUtils {
    public static final Gson sGson = new Gson();

    public static String jsonStr(Object obj){
        return sGson.toJson(obj);
    }

    public static <Data> Data json2obj(String jsonStr, Class<Data> clazz){
        return sGson.fromJson(jsonStr,clazz);
    }
}
