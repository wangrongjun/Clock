package com.wang.clock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.clock.R;
import com.wang.clock.entity.Clock;
import com.wang.clock.util.ClockDb;
import com.wang.clock.util.ClockManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2017/2/3.
 */
public class ClockListAdapter extends BaseAdapter {

    private Context context;
    private List<Clock> clockList;

    public ClockListAdapter(Context context, List<Clock> clockList) {
        this.context = context;
        this.clockList = clockList;
    }

    public List<Clock> getClockList() {
        return clockList;
    }

    @Override
    public int getCount() {
        return clockList.size();
    }

    @Override
    public Object getItem(int position) {
        return clockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_clock, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(final ViewHolder holder, int position) {
        final Clock clock = clockList.get(position);

        int hour = clock.getHour();
        int minute = clock.getMinute();
        String hourStr = hour > 9 ? (hour + "") : ("0" + hour);
        String minuteStr = minute > 9 ? (minute + "") : ("0" + minute);
        holder.tvTime.setText(hourStr + ":" + minuteStr);
        holder.tvRepeatMode.setText(clock.getRepeatMode() == Clock.RepeatMode.ONCE ? "一次" : "每天");
        holder.tvContent.setText(clock.getContent());
        boolean isOpen = clock.getState() == Clock.State.OPEN;
        int resId = isOpen ? R.mipmap.ic_clock_open : R.mipmap.ic_clock_close;
        holder.btnClockState.setImageResource(resId);

        holder.btnClockState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOpen = clock.getState() == Clock.State.OPEN;
                ClockDb db = new ClockDb(context);
                ClockManager manager = new ClockManager();
                if (isOpen) {
                    clock.setState(Clock.State.CLOSE);
                    manager.cancelAlarm(context, clock.getClockId());
                } else {
                    clock.setState(Clock.State.OPEN);
                    manager.setAlarm(context, clock.getClockId(), clock.getHour(),
                            clock.getMinute(), clock.getRepeatMode() == Clock.RepeatMode.EVERY_DAY);
                }
                db.updateClock(clock);
                db.close();
                notifyDataSetChanged();
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_repeat_mode)
        TextView tvRepeatMode;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.btn_clock_state)
        ImageView btnClockState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
