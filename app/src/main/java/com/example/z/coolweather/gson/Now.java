package com.example.z.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by z少尊 on 2017/11/14.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;
    }
}