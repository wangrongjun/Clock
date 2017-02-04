package com.wang.clock.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.wang.android_lib.util.SqliteUtil;
import com.wang.clock.entity.Clock;
import com.wang.db.exception.FieldNotFoundException;
import com.wang.db.exception.PrimaryKeyNotFoundException;
import com.wang.db.v2.DaoUtil;
import com.wang.db.v2.DbType;
import com.wang.db.v2.SqlUtil;
import com.wang.java_util.DebugUtil;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/1/23.
 */
public class ClockDb extends SQLiteOpenHelper {

    public ClockDb(Context context) {
        super(context, Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "clock.db", null, 1);
        SqlUtil.dbType = DbType.SQLITE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = DaoUtil.createTableSql(Clock.class);
        db.execSQL(createTableSql);
    }

    public Clock getClockById(int clockId) {
        try {
            String sql = DaoUtil.queryByIdSql(Clock.class, clockId + "");
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            List<Clock> clockList = SqliteUtil.getResult(Clock.class, cursor);
            if (clockList.size() > 0) {
                return clockList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeClock(int clockId) {
        try {
            String sql = DaoUtil.updateSql(Clock.class, clockId + "", "state",
                    Clock.toState(Clock.State.CLOSE) + "");
            execute(sql);
        } catch (PrimaryKeyNotFoundException | FieldNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Clock> getClockList() {
        try {
            String sql = DaoUtil.queryAllSql(Clock.class);
            DebugUtil.println(sql);
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            List<Clock> clockList = SqliteUtil.getResult(Clock.class, cursor);
            cursor.close();
            db.close();
            return clockList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * @return 插入后自增的clockId
     */
    public int addClock(Clock clock) {
        String sql = DaoUtil.insertSql(clock);
        execute(sql);
        SQLiteDatabase db = getReadableDatabase();
        int clockId = 0;
        try {
            clockId = SqliteUtil.getLatestAutoIncrementPrimaryKey(Clock.class, db);
        } catch (PrimaryKeyNotFoundException e) {
            e.printStackTrace();
        }
        db.close();
        return clockId;
    }

    public void deleteClock(int clockId) {
        try {
            String sql = DaoUtil.deleteByIdSql(Clock.class, clockId + "");
            execute(sql);
        } catch (PrimaryKeyNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllClock() throws FieldNotFoundException {
        String sql = DaoUtil.deleteAllSql(Clock.class);
        execute(sql);
    }

    public void updateClock(Clock clock) {
        String sql = DaoUtil.updateByIdSql(clock);
        execute(sql);
    }

    private void execute(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
        DebugUtil.println(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
