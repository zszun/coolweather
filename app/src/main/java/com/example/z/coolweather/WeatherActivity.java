package com.example.z.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.z.coolweather.gson.Forecast;
import com.example.z.coolweather.gson.Weather;
import com.example.z.coolweather.util.HttpUtil;
import com.example.z.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //透明状态栏
        //判断系统版本号是否大于等于21
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        //初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);

        if (weatherString != null){

            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);

        }else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");//从Intent中取出天气ID
            weatherLayout.setVisibility(View.INVISIBLE); //请求数据时先将ScrollView隐藏
            requestWeather(weatherId);
        }

        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic(); //请求必应背景图
        }

    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";  //返回必应背景图链接
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();  //获取到图片链接
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);  //将图片链接缓存到SP当中
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //加载出图片
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }


    /**
     * 根据ID请求天气信息
     * @param weatherId
     */
    private void requestWeather(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=09b98a795c1d47bbad4ec97f3fbeb004";


        //发出请求，服务器会将相应的天气信息以JSON格式返回
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            //onResponse()回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();  //获取服务器返回的数据
                final Weather weather = Utility.handleWeatherResponse(responseText);//解析数据为Weather对象
                runOnUiThread(new Runnable() {  //将线程切换到主线程，再更新UI元素
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString("weather",responseText);
                            editor.apply();  //向SP中缓存（存储）数据
                            showWeatherInfo(weather);  //从SP中读取数据并展示出来
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        loadBingPic();  //请求必应背景图
    }


    /**
     * 处理并展示Weather实体类中的数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();

        for (Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this)  //动态加载布局
                    .inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);  //把动态加载的布局添加到父容器
        }
        if (weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度："+ weather.suggestion.comfore.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动指数：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
