package com.wang.clock.entity;

import java.util.Date;

/**
 * by wangrongjun on 2017/1/23.
 */
public class Clock {

    public enum Repeat {
        ONCE,
        EVERY_DAY
    }

    private int clockId;
    private Date date;
    private String musicPath;
    private Repeat repeat;

}
