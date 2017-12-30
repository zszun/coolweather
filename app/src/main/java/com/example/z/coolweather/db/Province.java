package com.example.z.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by z少尊 on 2017/11/5.
 *存放省的天气信息
 * 使用LitePal处理
 */

public class Province extends DataSupport{

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    private int id;

    private String provinceName;  //记录省的名字

    private  int provinceCode;  //记录省的代号



}
