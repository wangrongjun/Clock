package com.wang.clock.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * by wangrongjun on 2017/1/23.
 */
public class P {

    private static final String prefName = "Clock";
    public static Context context;

    public static String getDefaultMusicPath() {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getString("defaultMusicPath", "");
    }

    public static void setDefaultMusicPath(String defaultMusicPath) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        pref.edit().putString("defaultMusicPath", defaultMusicPath).apply();
    }

}
