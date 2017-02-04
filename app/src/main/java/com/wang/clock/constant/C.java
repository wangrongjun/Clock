package com.wang.clock.constant;

import android.os.Environment;

import java.io.File;

/**
 * by wangrongjun on 2017/2/3.
 */
public class C {

    public static final String musicDir = Environment.getExternalStorageDirectory() +
            File.separator + "clockDir" + File.separator;

    public static final String defaulrMusicName = "clock.mid";

}
