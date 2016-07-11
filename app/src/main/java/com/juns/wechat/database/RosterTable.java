package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 王宗文 on 2016/6/14.
 */
public class RosterTable {
    public static final String TABLE_NAME = "wcroster";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OWNER_NAME = "ownerName";
    public static final String COLUMN_CONTACT_NAME = "contactName";
    public static final String COLUMN_SUB_TYPE = "subtype";
    public static final String COLUMN_REMARK = "remark";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "ownerName TEXT NOT NULL, contactName TEXT NOT NULL,"
            + "subtype TEXT, remark TEXT);";

    public static final String CREATE_INDEX = "create unique index index_rosters on wcroster(ownerName, contactName)";
    public static final String DELETE_INDEX = "drop index index_rosters";


    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        if (oldVersion != newVersion){
            database.execSQL(DELETE_INDEX);
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }
    }
    
}
