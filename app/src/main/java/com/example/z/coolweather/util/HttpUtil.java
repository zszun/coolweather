package com.example.z.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by z少尊 on 2017/11/8.
 * 与服务器交互
 */

public class HttpUtil {

    //传入请求地址，并注册一个回调服务器响应
    public static void sendOkHttpRequest(String address ,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
        //OkHttp的enqueue的方法是异步方法
        //传入的是：注册了一个回调来处理服务器响应
    }
}
