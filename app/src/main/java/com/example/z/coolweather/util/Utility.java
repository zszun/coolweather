package com.example.z.coolweather.util;

import android.text.TextUtils;

import com.example.z.coolweather.db.City;
import com.example.z.coolweather.db.County;
import com.example.z.coolweather.db.Province;
import com.example.z.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by z少尊 on 2017/11/8.
 * 工具类
 * 此工具类用来解析和处理服务器返回的省市县的JSON数据
 * 这里JSON的数据结构比较简单，就不使用GSON来进行解析了
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */

    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response); //将服务器返回的数据传入一个JSON数组对象中
                for (int i = 0; i < allProvinces.length(); i++) {  //遍历数据，然后储存到数据库中
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();  //储存到数据库
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provindeId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceID(provindeId);
                    city.save();   //储存到数据库
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherID(countyObject.getString("weather_id"));
                    county.setCityID(cityId);
                    county.save();  //储存到数据库
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");

            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);  //这里用GSON解析

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析和处理必应返回的数据
     * @param response
     * @return
     */
    public static String handleBingPic(String response){

        try {
            JSONObject object = new JSONObject(response);
            JSONArray jsonArray = object.getJSONArray("images");
            JSONObject jsonObject = jsonArray.getJSONObject(0);  //数组的第一组数据有链接
            String bingPic = jsonObject.getString("url"); //获取后缀地址
            String bingUrl = "http://s.cn.bing.net" + bingPic;

            return bingUrl;  //返回必应图片链接

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }


}
