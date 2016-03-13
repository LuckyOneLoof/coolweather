package com.example.loof.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loof.coolweather.R;
import com.example.loof.coolweather.service.AutoUpdateService;
import com.example.loof.coolweather.util.HttpCallbackListener;
import com.example.loof.coolweather.util.HttpUtil;
import com.example.loof.coolweather.util.Utility;

/**
 * Created by loof on 2016/3/13.
 */
public class WeatherActivity extends Activity {

    private LinearLayout weatherInfoLayout;

    private TextView cityNameText;
    private TextView cityCodeTExt;
    private TextView weatherDespText;
    private TextView publishTimeText;
    private TextView curTemp;
    private TextView lowTemp;
    private TextView hiTemp;
    private TextView windDirectionText;
    private TextView windSpeedText;
    private TextView sunRiseText;
    private TextView sunSetText;

    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        cityCodeTExt = (TextView) findViewById(R.id.city_code);
        publishTimeText = (TextView) findViewById(R.id.publish_time);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        curTemp = (TextView) findViewById(R.id.cur_temp);
        lowTemp = (TextView) findViewById(R.id.low_temp);
        hiTemp = (TextView) findViewById(R.id.hi_temp);
        windDirectionText = (TextView) findViewById(R.id.wind_direction);
        windSpeedText = (TextView) findViewById(R.id.wind_speed);
        sunRiseText = (TextView) findViewById(R.id.sun_rise);
        sunSetText = (TextView) findViewById(R.id.sun_set);

        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);

        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            publishTimeText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            showWeather();
        }
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
            }
        });

        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTimeText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = prefs.getString("weather_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherInfo(weatherCode);
                }
            }
        });

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void queryWeatherInfo(String weatherCode) {
        String address = "http://apis.baidu.com/apistore/weatherservice/cityid?cityid=" + weatherCode;
        queryFromServer(address, "weatherCode");
    }

    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" +
                countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] array = response.split("\\|");
                            if (array != null && array.length == 2) {
                                String weatherCode = array[1];
                                queryWeatherInfo(weatherCode);
                            }
                        }
                    });
                } else if ("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTimeText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather() {
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(spf.getString("city_name", ""));
//        cityCodeTExt.setText(spf.getString("city_code", ""));
        weatherDespText.setText(spf.getString("weather_desp", ""));
        publishTimeText.setText(spf.getString("publish_date", "") + " " + spf.getString("publish_time", "") + "发布");
        curTemp.setText(spf.getString("current_temp", ""));
        lowTemp.setText(spf.getString("lowest_temp", ""));
        hiTemp.setText(spf.getString("highest_temp", ""));
        windDirectionText.setText(spf.getString("wind_direction", ""));
        windSpeedText.setText(spf.getString("wind_speed", ""));
        sunRiseText.setText(spf.getString("sun_rise", ""));
        sunSetText.setText(spf.getString("sun_set", ""));

        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
