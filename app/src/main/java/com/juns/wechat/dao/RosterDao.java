package com.juns.wechat.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.stmt.PreparedStmt;
import com.juns.wechat.bean.RosterBean;
import com.juns.wechat.database.DatabaseHelper;
import com.juns.wechat.database.RosterTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class RosterDao {
    private static RosterDao mInstance;

    public static RosterDao getInstance(){
        if(mInstance == null){
            mInstance = new RosterDao();
        }
        return mInstance;
    }

    public void insertOrUpdate(List<RosterBean> rosterBeans){
        if(rosterBeans == null || rosterBeans.isEmpty()) return;
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance().getWritableDatabase();
        StringBuilder sb = new StringBuilder("replace into rosters(ownerName, contactName, subType, remark) ");

        for(RosterBean rosterBean : rosterBeans){
            sb.append("values('").append(rosterBean.getOwnerName()).append("', '").append(rosterBean.getContactName())
                    .append("', '").append(rosterBean.getSubType()).append("', '").append(rosterBean.getRemark())
                    .append("'), ");
        }

        sb = sb.delete(sb.lastIndexOf(","), sb.length());
        sqLiteDatabase.execSQL(sb.toString());

    }

    public List<RosterBean> queryAllByOwner(String ownerName){
        String sql = "select * from " + RosterTable.TABLE_NAME + " where " + RosterTable.COLUMN_OWNER_NAME + " = ?";
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance().getWritableDatabase();
        String[] bindArgs = {ownerName};
        Cursor cursor = sqLiteDatabase.rawQuery(sql, bindArgs);
        List<RosterBean> rosterBeans = new ArrayList<>();
        while (cursor.moveToNext()){
            RosterBean rosterBean = new RosterBean(cursor);
            rosterBeans.add(rosterBean);
        }
        return rosterBeans;
    }
}
