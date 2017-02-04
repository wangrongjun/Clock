package com.wang.clock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wang.clock.R;
import com.wang.clock.entity.Clock;
import com.wang.clock.util.ClockDb;
import com.wang.clock.util.ClockManager;
import com.wang.clock.util.P;
import com.wang.java_util.TextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2017/2/3.
 */
public class ClockActivity extends Activity {

    private enum Mode {
        CREATE,
        UPDATE
    }

    @Bind(R.id.time_picker)
    TimePicker timePicker;
    @Bind(R.id.tv_repeat_mode)
    TextView tvRepeatMode;
    @Bind(R.id.tv_music_path)
    TextView tvMusicPath;
    @Bind(R.id.et_content)
    EditText etContent;

    private Mode mode;
    /**
     * 若ClockActivity启动后clock不为空，则为修改闹钟，否则为添加闹钟。
     */
    private Clock clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        ButterKnife.bind(this);
        initData();
        updateView();
    }

    private void initData() {
        try {
            clock = new Gson().fromJson(getIntent().getStringExtra("clockJson"), Clock.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        mode = clock == null ? Mode.CREATE : Mode.UPDATE;

        if (mode == Mode.CREATE) {//添加闹钟
            clock = new Clock(
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute(),
                    P.getDefaultMusicPath(),
                    "",
                    Clock.State.OPEN,
                    Clock.RepeatMode.ONCE
            );
        }
    }

    private void updateView() {
        timePicker.setCurrentHour(clock.getHour());
        timePicker.setCurrentMinute(clock.getMinute());
        tvRepeatMode.setText(clock.getRepeatMode() == Clock.RepeatMode.ONCE ? "一次" : "每天");
        tvMusicPath.setText(TextUtil.getTextAfterLastSlash(clock.getMusicPath()));
        etContent.setText(clock.getContent());
    }

    public static void start(Activity activity, Clock clock) {
        Intent intent = new Intent(activity, ClockActivity.class);
        intent.putExtra("clockJson", new Gson().toJson(clock));
        activity.startActivityForResult(intent, 0);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm, R.id.btn_repeat_mode, R.id.btn_music})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            case R.id.btn_repeat_mode:
                if (clock.getRepeatMode() == Clock.RepeatMode.ONCE) {
                    clock.setRepeatMode(Clock.RepeatMode.EVERY_DAY);
                } else {
                    clock.setRepeatMode(Clock.RepeatMode.ONCE);
                }
                updateView();
                break;
            case R.id.btn_music:
                startActivityForResult(new Intent(this, ChooseMusicActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String musicPath = data.getStringExtra(ChooseMusicActivity.RESULT_KEY);
            tvMusicPath.setText(musicPath);
            clock.setMusicPath(musicPath);
            updateView();
        }
    }

    private void confirm() {
        clock.setContent(etContent.getText().toString());
        clock.setState(Clock.State.OPEN);
        clock.setHour(timePicker.getCurrentHour());
        clock.setMinute(timePicker.getCurrentMinute());

        ClockDb clockDb = new ClockDb(this);
        ClockManager manager = new ClockManager();
        int id;
        if (mode == Mode.CREATE) {
            id = clockDb.addClock(clock);
        } else {
            clockDb.updateClock(clock);
            id = clock.getClockId();
        }
        manager.setAlarm(
                this,
                id,
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute(),
                clock.getRepeatMode() == Clock.RepeatMode.EVERY_DAY
        );

        setResult(MainActivity.RESULT_CODE_Clock_Activity);
        finish();
    }

}
