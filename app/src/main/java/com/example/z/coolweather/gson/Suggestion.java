package com.example.z.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by z少尊 on 2017/11/14.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfore;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {

        @SerializedName("txt")
        public String info;
    }

    public class CarWash {

        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
