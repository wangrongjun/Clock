package com.wang.clock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wang.clock.R;
import com.wang.java_util.TextUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2017/2/4.
 */
public class MusicListAdapter extends BaseAdapter {

    private Context context;
    private List<String> musicPathList;

    public MusicListAdapter(Context context, List<String> musicPathList) {
        this.context = context;
        this.musicPathList = musicPathList;
    }

    public List<String> getMusicPathList() {
        return musicPathList;
    }

    @Override
    public int getCount() {
        return musicPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicPathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_music, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(ViewHolder holder, int position) {
        String musicPath = musicPathList.get(position);
        holder.tvMusicName.setText(TextUtil.getTextAfterLastSlash(musicPath));
    }

    static class ViewHolder {
        @Bind(R.id.tv_music_name)
        TextView tvMusicName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
