package com.example.loof.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.loof.coolweather.db.CoolWeatherDB;
import com.example.loof.coolweather.model.City;
import com.example.loof.coolweather.model.County;
import com.example.loof.coolweather.model.Province;
import com.example.loof.coolweather.model.WeatherInfo;

import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by loof on 2016/3/12.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvince = response.split(",");
            if (allProvince != null && allProvince.length > 0) {
                for (String p : allProvince) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountisResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject retData = jsonObject.getJSONObject("retData");
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.setCityName(URLDecoder.decode(retData.getString("city"), "UTF-8"));
            weatherInfo.setCityPinyin(retData.getString("pinyin"));
            weatherInfo.setCityCode(retData.getString("citycode"));
            weatherInfo.setPublishDate(retData.getString("date"));
            weatherInfo.setPublishTime(retData.getString("time"));
            weatherInfo.setWeatherDesp(URLDecoder.decode(retData.getString("weather"), "UTF-8"));
            weatherInfo.setCurrentTemp(retData.getString("temp"));
            weatherInfo.setHighestTemp(retData.getString("h_tmp"));
            weatherInfo.setLowestTemp(retData.getString("l_tmp"));
            weatherInfo.setWindDirection(URLDecoder.decode(retData.getString("WD"), "UTF-8"));
            weatherInfo.setWindSpeed(URLDecoder.decode(retData.getString("WS"), "UTF-8"));
            weatherInfo.setSunRise(retData.getString("sunrise"));
            weatherInfo.setSunSet(retData.getString("sunset"));
            saveWeatherInfo(context, weatherInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, final WeatherInfo weatherInfo) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", weatherInfo.getCityName());
        editor.putString("city_pin_yin", weatherInfo.getCityPinyin());
        editor.putString("city_code", weatherInfo.getCityCode());
        editor.putString("weather_code", weatherInfo.getCityCode());
        editor.putString("publish_date", weatherInfo.getPublishDate());
        editor.putString("publish_time", weatherInfo.getPublishTime());
        editor.putString("weather_desp", weatherInfo.getWeatherDesp());
        editor.putString("current_temp", weatherInfo.getCurrentTemp());
        editor.putString("highest_temp", weatherInfo.getHighestTemp());
        editor.putString("lowest_temp", weatherInfo.getLowestTemp());
        editor.putString("wind_direction", weatherInfo.getWindDirection());
        editor.putString("wind_speed", weatherInfo.getWindSpeed());
        editor.putString("sun_rise", weatherInfo.getSunRise());
        editor.putString("sun_set", weatherInfo.getSunSet());
        editor.commit();
    }
}
