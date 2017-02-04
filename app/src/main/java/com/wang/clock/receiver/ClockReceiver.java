package com.wang.clock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wang.clock.entity.Clock;
import com.wang.clock.util.ClockDb;
import com.wang.clock.util.MusicPlayer;
import com.wang.java_util.DebugUtil;

public class ClockReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.wang.intent.ClockReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        DebugUtil.println(action);
        if (action.equals(ACTION)) {
            int clockId = intent.getIntExtra("clockId", 0);
            ClockDb db = new ClockDb(context);
            Clock clock = db.getClockById(clockId);
            MusicPlayer.play(clock.getMusicPath());
            db.closeClock(clockId);
            DebugUtil.printlnEntity(clock);
            db.close();
        }
    }

}
