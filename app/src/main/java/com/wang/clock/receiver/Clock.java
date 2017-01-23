package com.wang.clock.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Clock extends BroadcastReceiver {

    private static final String ACTION = "com.wang.intent.ClockReceiver";
    private static final long INTERVAL = 24 * 60 * 60 * 1000;

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

    public static void setAlarm(Context context, int hour, int minute,
                                boolean repeat) {

        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Clock.class);
        intent.setAction(Clock.ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 00);
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

        Clock.repeat = repeat;
        isOpen = true;
        clockHour = hour;
        clockMinute = minute;
    }

    public static void cancel(Context context) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Clock.class);
        intent.setAction(Clock.ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        am.cancel(pendingIntent);
        isOpen = false;
    }

    public static boolean alarm() {
        if (mediaPlayer != null) {
            return true;
        }
        String musicPath = Environment.getExternalStorageDirectory()
                + "/clock.mp3";
        boolean isExist = new File(musicPath).exists();
        if (!isExist) {
            return false;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(musicPath);
            mediaPlayer.prepare();
            mediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    });
            mediaPlayer.start();
            return true;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println(action);
        if (action.equals(ACTION)) {
            Clock.alarm();
            if (!repeat) {
                isOpen = false;
            }
        }

    }
}
