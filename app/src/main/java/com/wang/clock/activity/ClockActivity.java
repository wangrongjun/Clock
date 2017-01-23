package com.wang.clock.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.wang.clock.R;
import com.wang.clock.receiver.Clock;

public class ClockActivity extends Activity implements OnTimeChangedListener {

    private TimePicker timePicker;
    private TextView tvIsOpen;

    private boolean isTimeChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        initView();
    }

    private void initView() {

        timePicker = (TimePicker) findViewById(R.id.tp);
        if (Clock.isOpen()) {
            timePicker.setCurrentHour(Clock.clockHour());
            timePicker.setCurrentMinute(Clock.clockMinute());
        }
        timePicker.setOnTimeChangedListener(this);

        tvIsOpen = (TextView) findViewById(R.id.tv_isOpen);
        String s = "";
        if (Clock.isOpen()) {
            s = "闹钟已开启   " + (Clock.repeat() ? "每天重复" : "响一次");
        } else {
            s = "闹钟已关闭";
        }
        tvIsOpen.setText(s);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        isTimeChanged = true;
    }

    public void onClickStop(View v) {
        Clock.stop();
    }

    public void onClickTest(View v) {
        String s = Clock.alarm() ? "测试中" : "文件不存在";
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isTimeChanged) {
                    showClockConfirmDialog();
                } else {
                    finish();
                }
                break;

        }
        return false;
    }

    private boolean repeat = false;

    private void showClockConfirmDialog() {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText("重复");
        checkBox.setChecked(Clock.repeat());
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                repeat = isChecked;

            }
        });

        new AlertDialog.Builder(this).setTitle("提示").setMessage("开启闹钟？")
                .setNegativeButton("关闭", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Clock.cancel(getApplicationContext());
                        Toast.makeText(ClockActivity.this, "闹钟已关闭",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setPositiveButton("开启", new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int clockHour = timePicker.getCurrentHour();
                int clockMinute = timePicker.getCurrentMinute();
                Clock.setAlarm(getApplicationContext(), clockHour,
                        clockMinute, repeat);
                System.out.println("repeat: " + repeat);
                Toast.makeText(ClockActivity.this, "闹钟已开启",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }).setView(checkBox).show();
    }

}
