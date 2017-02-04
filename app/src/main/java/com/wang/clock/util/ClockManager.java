package com.wang.clock.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wang.clock.receiver.ClockReceiver;

import java.util.Calendar;

/**
 * by wangrongjun on 2017/1/23.
 */
public class ClockManager {

    private static final long INTERVAL = 24 * 60 * 60 * 1000;

    public void setAlarm(Context context, int clockId, int hour, int minute,
                         boolean repeat) {

        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ClockReceiver.class);
        intent.setAction(ClockReceiver.ACTION);
        intent.putExtra("clockId", clockId);
        //这里的clockId用来区别不同的闹钟
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, clockId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long clockTime = calendar.getTimeInMillis();
        long currTime = System.currentTimeMillis();
        while (clockTime <= currTime) {
            clockTime += INTERVAL;
        }

        if (repeat) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, clockTime, INTERVAL,
                    pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, clockTime, pendingIntent);
        }
    }

    public void cancelAlarm(Context context, int clockId) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ClockReceiver.class);
        intent.setAction(ClockReceiver.ACTION);
        //这里的clockId用来区别不同的闹钟
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, clockId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }

}
