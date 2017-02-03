package com.wang.clock.main;

import android.app.Application;

import com.wang.clock.util.P;

/**
 * by wangrongjun on 2017/1/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        P.context = getApplicationContext();
    }
}
