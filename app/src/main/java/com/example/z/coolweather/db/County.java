package com.example.z.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by z少尊 on 2017/11/5.
 */

public class County extends DataSupport{

    private int id;

    private String countyName;  //记录县的名字

    private String weatherID;  //记录县所对应的天气id

    private int cityID;  //记录当前县所属市的id值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(String weatherID) {
        this.weatherID = weatherID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
