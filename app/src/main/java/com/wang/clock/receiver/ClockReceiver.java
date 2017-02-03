package com.wang.clock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class ClockReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.wang.intent.ClockReceiver";

    private static boolean isOpen = false;
    private static boolean repeat = false;
    private static int clockHour;
    private static int clockMinute;

    private static MediaPlayer mediaPlayer = null;

    public static boolean isOpen() {
        return isOpen;
    }

    public static boolean repeat() {
        return repeat;
    }

    public static int clockHour() {
        return clockHour;
    }

    public static int clockMinute() {
        return clockMinute;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println(action);
        if (action.equals(ACTION)) {
//            ClockReceiver.alarm();
            if (!repeat) {
                isOpen = false;
            }
        }

    }
}
