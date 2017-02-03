package com.wang.clock.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.util.DialogUtil;
import com.wang.clock.R;
import com.wang.clock.adapter.ClockListAdapter;
import com.wang.clock.entity.Clock;
import com.wang.clock.util.ClockDb;
import com.wang.db.exception.FieldNotFoundException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2017/2/2.
 */
public class MainActivity extends Activity {

    @Bind(R.id.lv_clock)
    ListView lvClock;
    @Bind(R.id.btn_more)
    ImageView btnMore;

    private PopupMenu popupMenu;

    private ClockDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        db = new ClockDb(this);
        initMenu();
        showClockList();
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
                        chooseDefaultMusic();
                        startActivity(new Intent(MainActivity.this, DbTestActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    private void deleteAll() {
        DialogUtil.showConfirmDialog(this, "确认删除", "确实要取消所有闹钟？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.deleteAllClock();
                            showClockList();
                        } catch (FieldNotFoundException e) {
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void chooseDefaultMusic() {

    }

    @OnClick({R.id.btn_add_clock, R.id.btn_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_clock:
                ClockActivity.start(this, null);
                break;
            case R.id.btn_more:
                popupMenu.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showClockList();
    }

    private void showClockList() {
        ClockDb db = new ClockDb(this);
        List<Clock> clockList = db.getClockList();
        if (clockList != null && clockList.size() > 0) {
            ClockListAdapter adapter = new ClockListAdapter(this, clockList);
            lvClock.setAdapter(adapter);
        } else {
            lvClock.setAdapter(new NullAdapter(this, "暂无闹钟"));
        }
    }

}
