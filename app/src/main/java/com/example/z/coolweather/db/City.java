package com.example.z.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by z少尊 on 2017/11/5.
 * 存放市的数据信息
 * 使用LitePal处理
 */

public class City extends DataSupport {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    private int id;

    private String cityName;  //记录市的名字

    private int cityCode;  //记录市的代号

    private int provinceID;  //记录当前市所属省的id值
}
