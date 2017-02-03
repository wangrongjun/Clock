package com.wang.clock.entity;

import com.wang.db.v2.Constraint;
import com.wang.db.v2.ConstraintAnno;
import com.wang.db.v2.Type;
import com.wang.db.v2.TypeAnno;

/**
 * by wangrongjun on 2017/1/23.
 */
public class Clock {

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public enum State {
        CLOSE,
        OPEN
    }

    public enum RepeatMode {
        ONCE,
        EVERY_DAY
    }

    @TypeAnno(type = Type.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int clockId;

    @TypeAnno(type = Type.TINYINT)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private int hour;//24小时制

    @TypeAnno(type = Type.TINYINT)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private int minute;

    @TypeAnno(type = Type.TEXT)
    private String musicPath;

    @TypeAnno(type = Type.TEXT)
    private String content;

    @TypeAnno(type = Type.TINYINT)
    @ConstraintAnno(constraint = Constraint.DEFAULT, defaultValue = "0")
    private int state;//0为关闭，1为开启

    @TypeAnno(type = Type.TINYINT)
    @ConstraintAnno(constraint = Constraint.DEFAULT, defaultValue = "0")
    private int repeatMode;//0为一次，1为每天

    public Clock() {
    }

    public Clock(int hour, int minute, String musicPath, String content, State state,
                 RepeatMode repeatMode) {
        this.hour = hour;
        this.minute = minute;
        this.musicPath = musicPath;
        this.content = content;
        setState(state);
        setRepeatMode(repeatMode);
    }

    public int getClockId() {
        return clockId;
    }

    public void setClockId(int clockId) {
        this.clockId = clockId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public State getState() {
        switch (this.state) {
            case 0:
                return State.CLOSE;
            case 1:
                return State.OPEN;
        }
        return State.CLOSE;
    }

    public static State toState(int state) {
        switch (state) {
            case 0:
                return State.CLOSE;
            case 1:
                return State.OPEN;
        }
        return State.CLOSE;
    }

    public void setState(State state) {
        switch (state) {
            case CLOSE:
                this.state = 0;
                break;
            case OPEN:
                this.state = 1;
                break;
        }
    }

    public RepeatMode getRepeatMode() {
        switch (this.repeatMode) {
            case 0:
                return RepeatMode.ONCE;
            case 1:
                return RepeatMode.EVERY_DAY;
        }
        return RepeatMode.ONCE;
    }

    public static RepeatMode toRepeatMode(int repeatMode) {
        switch (repeatMode) {
            case 0:
                return RepeatMode.ONCE;
            case 1:
                return RepeatMode.EVERY_DAY;
        }
        return RepeatMode.ONCE;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        switch (repeatMode) {
            case ONCE:
                this.repeatMode = 0;
                break;
            case EVERY_DAY:
                this.repeatMode = 1;
                break;
        }
    }

}
