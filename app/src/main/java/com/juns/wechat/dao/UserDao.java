package com.juns.wechat.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.database.DatabaseHelper;
import com.juns.wechat.database.UserTable;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class UserDao {
    private static UserDao mInstance;

    public static UserDao getInstance(){
        if(mInstance == null){
            mInstance = new UserDao();
        }
        return mInstance;
    }

    public UserBean getUserByName(String userName){
        String sql = "select * from " + UserTable.TABLE_NAME + " where userName = " + userName;
        SQLiteDatabase database = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        if(cursor.moveToNext()){
            UserBean userBean = new UserBean(cursor);
            closeCursor(cursor);
            return userBean;
        }
        closeCursor(cursor);
        return null;
    }

    private void closeCursor(Cursor cursor){
        if(cursor != null){
            cursor.close();
        }
    }
}
