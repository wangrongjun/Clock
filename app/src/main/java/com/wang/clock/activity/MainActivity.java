package com.wang.clock.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.util.DialogUtil;
import com.wang.clock.R;
import com.wang.clock.adapter.ClockListAdapter;
import com.wang.clock.entity.Clock;
import com.wang.clock.util.ClockDb;
import com.wang.clock.util.ClockManager;
import com.wang.clock.util.MusicPlayer;
import com.wang.clock.util.P;
import com.wang.db.exception.FieldNotFoundException;
import com.wang.java_util.DebugUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2017/2/2.
 */
public class MainActivity extends Activity {

    public static final int RESULT_CODE_Clock_Activity = 12;
    public static final int RESULT_CODE_Choose_Activity = 34;

    @Bind(R.id.lv_clock)
    ListView lvClock;
    @Bind(R.id.btn_more)
    ImageView btnMore;
    @Bind(R.id.btn_stop_music)
    TextView btnStopMusic;

    private PopupMenu popupMenu;
    private ClockListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initMenu();
        showClockList();
    }

    private void initView() {
        lvClock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != NullAdapter.NULL_ADAPTER_ID) {
                    Clock clock = adapter.getClockList().get(position);
                    ClockActivity.start(MainActivity.this, clock);
                }
            }
        });

        lvClock.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != NullAdapter.NULL_ADAPTER_ID) {
                    final Clock clock = adapter.getClockList().get(position);
                    DialogUtil.showConfirmDialog(MainActivity.this, "确认删除", "确实要删除吗？",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelClock(clock.getClockId());
                                    showClockList();
                                }
                            });
                }
                return true;
            }
        });

        btnStopMusic.setVisibility(MusicPlayer.isPlaying() ? View.VISIBLE : View.GONE);

    }

    private void cancelClock(int clockId) {
        ClockDb db = new ClockDb(this);
        db.deleteClock(clockId);
        db.close();
        new ClockManager().cancelAlarm(this, clockId);
    }

    private void initMenu() {
        popupMenu = new PopupMenu(this, btnMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_menu_delete_all:
                        deleteAll();
                        break;
                    case R.id.btn_menu_default_music:
                        startActivityForResult(
                                new Intent(MainActivity.this, ChooseMusicActivity.class), 0);
                        break;
                }
                return true;
            }
        });
    }

    @OnClick({R.id.btn_add_clock, R.id.btn_more, R.id.btn_stop_music})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_clock:
                ClockActivity.start(this, null);
                break;
            case R.id.btn_more:
                popupMenu.show();
                break;
            case R.id.btn_stop_music:
                MusicPlayer.stop();
                btnStopMusic.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CODE_Clock_Activity) {
            showClockList();

        } else if (resultCode == RESULT_CODE_Choose_Activity) {
            if (data != null) {
                String musicPath = data.getStringExtra(ChooseMusicActivity.RESULT_KEY);
                P.setDefaultMusicPath(musicPath);
                DebugUtil.println("set default musicPath: " + musicPath);
            }
        }
    }

    private void deleteAll() {
        DialogUtil.showConfirmDialog(this, "确认删除", "确实要删除所有闹钟？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ClockDb db = new ClockDb(MainActivity.this);
                            List<Clock> clockList = db.getClockList();
                            db.deleteAllClock();
                            db.close();
                            ClockManager manager = new ClockManager();
                            for (Clock clock : clockList) {
                                if (clock.getState() == Clock.State.OPEN) {
                                    manager.cancelAlarm(MainActivity.this, clock.getClockId());
                                }
                            }
                            showClockList();
                        } catch (FieldNotFoundException e) {
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showClockList() {
        DebugUtil.println("update clock list");
        ClockDb db = new ClockDb(this);
        List<Clock> clockList = db.getClockList();
        if (clockList != null && clockList.size() > 0) {
            adapter = new ClockListAdapter(this, clockList);
            lvClock.setAdapter(adapter);
        } else {
            lvClock.setAdapter(new NullAdapter(this, "暂无闹钟"));
        }
    }

}
