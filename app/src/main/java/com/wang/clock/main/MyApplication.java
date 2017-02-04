package com.wang.clock.main;

import android.app.Application;

import com.wang.clock.constant.C;
import com.wang.clock.util.P;
import com.wang.java_util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * by wangrongjun on 2017/1/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        P.context = getApplicationContext();
        FileUtil.mkdirs(C.musicDir);
        startCopyDefaultMusic();
    }

    private void startCopyDefaultMusic() {

        if (new File(C.musicDir + C.defaulrMusicName).exists()) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getAssets().open(C.defaulrMusicName);
                    FileOutputStream fos = new FileOutputStream(C.musicDir + C.defaulrMusicName);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
