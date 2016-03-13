package com.example.loof.coolweather.util;

/**
 * Created by loof on 2016/3/12.
 */
public interface HttpCallbackListener{
    void onFinish(String response);
    void onError(Exception e);
}
