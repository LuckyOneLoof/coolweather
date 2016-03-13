package com.example.loof.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.loof.coolweather.service.AutoUpdateService;

/**
 * Created by loof on 2016/3/13.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
