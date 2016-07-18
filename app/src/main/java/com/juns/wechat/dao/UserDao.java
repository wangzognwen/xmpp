package com.juns.wechat.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.database.DbUtil;
import com.juns.wechat.database.UserTable;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class UserDao extends BaseDao<UserBean>{
    private static final String GET_LAST_MODIFY_DATE =
            "SELECT max(t.modifyDate) as lastModifyDate FROM ( " +
                    "SELECT t1.* FROM (select u.* from wcUser u, wcFriend r where " +
                    "(r.ownerName = ? and u.userName = r.contactName) and (r.subType = 'both' or r.subType = 'from')) t1" +
                    " UNION SELECT u.* from wcUser u WHERE u.userName = ?) t";

    private static UserDao mInstance;

    public static UserDao getInstance(){
        if(mInstance == null){
            mInstance = new UserDao();
        }
        return mInstance;
    }

    public long getLastModifyDate(String userName){
        long lastModifyDate = 0;

        SqlInfo sqlInfo = new SqlInfo(GET_LAST_MODIFY_DATE);
        List<KeyValue> keyValues = new ArrayList<>();
        KeyValue keyValue1 = new KeyValue("key1", userName);
        KeyValue keyValue2 = new KeyValue("key2", userName);
        keyValues.add(keyValue1);
        keyValues.add(keyValue2);
        sqlInfo.addBindArgs(keyValues);

        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            if(cursor != null && cursor.moveToNext()){
               lastModifyDate =  cursor.getLong(cursor.getColumnIndex("lastModifyDate"));
            }
            closeCursor(cursor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return lastModifyDate;
    }
}
