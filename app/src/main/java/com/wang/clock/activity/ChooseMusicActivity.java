package com.wang.clock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wang.android_lib.view.ToolBarView;
import com.wang.clock.R;
import com.wang.clock.adapter.MusicListAdapter;
import com.wang.clock.constant.C;
import com.wang.clock.util.MusicPlayer;
import com.wang.java_util.TextUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2017/2/4.
 */
public class ChooseMusicActivity extends Activity {
    /**
     * 返回结果到上一层activity的键名
     */
    public static final String RESULT_KEY = "selectedMusicPath";

    @Bind(R.id.lv_music)
    ListView lvMusic;
    @Bind(R.id.tv_music_dir)
    TextView tvMusicDir;
    @Bind(R.id.tool_bar)
    ToolBarView toolBar;

    private String selectedMusicPath;
    private MusicListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_music);
        ButterKnife.bind(this);
        initView();
        showMusicList();
    }

    private void initView() {
        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtil.isEmpty(selectedMusicPath)) {
                    Intent data = new Intent();
                    data.putExtra(RESULT_KEY, selectedMusicPath);
                    setResult(MainActivity.RESULT_CODE_Choose_Activity, data);
                }
                MusicPlayer.stop();
                finish();
            }
        });

        tvMusicDir.append(C.musicDir);

        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMusicPath = adapter.getMusicPathList().get(position);
                MusicPlayer.play(selectedMusicPath);
                tvMusicDir.setText("已选择：" + TextUtil.getTextAfterLastSlash(selectedMusicPath));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayer.stop();
    }

    private void showMusicList() {
        File dir = new File(C.musicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                String suffix = TextUtil.getTextAfterLastPoint(filename);
                return suffix.equals("mp3") || suffix.equals("wav") || suffix.equals("mid");
            }
        });

        List<String> musicPathList = new ArrayList<>();
        for (File file : files) {
            musicPathList.add(file.getAbsolutePath());
        }

        adapter = new MusicListAdapter(this, musicPathList);
        lvMusic.setAdapter(adapter);
    }

}
