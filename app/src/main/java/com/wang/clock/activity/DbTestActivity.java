package com.wang.clock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.android_lib.util.DialogUtil;
import com.wang.clock.R;
import com.wang.clock.entity.Clock;
import com.wang.clock.util.ClockDb;
import com.wang.db.exception.FieldNotFoundException;
import com.wang.db.exception.PrimaryKeyNotFoundException;
import com.wang.java_util.GsonUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2017/2/2.
 */
public class DbTestActivity extends Activity {

    @Bind(R.id.tv_clock_list)
    TextView tvClockList;
    private ClockDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);
        ButterKnife.bind(this);
        db = new ClockDb(this);
    }

    @OnClick({R.id.btn_show, R.id.btn_add, R.id.btn_delete, R.id.btn_delete_all, R.id.btn_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
                break;
            case R.id.btn_add:
                Clock clock = new Clock(
                        12,
                        0,
                        "/sdcard/a.mp3",
                        "content",
                        Clock.State.OPEN,
                        Clock.RepeatMode.EVERY_DAY
                );
                db.addClock(clock);
                show();
                break;
            case R.id.btn_delete:
                DialogUtil.showInputDialog(this, "clockId", "", new DialogUtil.OnInputFinishListener() {
                    @Override
                    public void onInputFinish(String text) {
                        try {
                            int clockId = Integer.parseInt(text);
                            db.deleteClock(clockId);
                            show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(DbTestActivity.this, "not number", Toast.LENGTH_SHORT).show();
                        } catch (PrimaryKeyNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.btn_delete_all:
                try {
                    db.deleteAllClock();
                } catch (FieldNotFoundException e) {
                    e.printStackTrace();
                }
                show();
                break;
            case R.id.btn_update:
                clock = db.getClockList().get(0);
                if (clock != null) {
                    clock.setHour(1);
                    clock.setMinute(10);
                    db.updateClock(clock);
                } else {
                    Toast.makeText(DbTestActivity.this, "list is null", Toast.LENGTH_SHORT).show();
                }
                show();
                break;
        }
    }

    private void show() {
        List<Clock> clockList = db.getClockList();
        tvClockList.setText(GsonUtil.formatJson(clockList));
    }
}
