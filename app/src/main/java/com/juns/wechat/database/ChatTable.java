package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：${TODO}<描述这个类是干什么的>
 *
 * @since ${TODO}<创建这个类时的版本日期>
 * Created by aa on 2015/11/24
 *******************************************************/
public class ChatTable {
    public static final String TABLE_NAME = "chats";
    public static final String DEFAULT_SORT_ORDER   = "_id ASC"; // sort by

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE         = "date";
    public static final String COLUMN_DIRECTION = "direction"; //消息方向，是发出去还是接受到的
    public static final String COLUMN_MYSELF_NAME = "myselfName";
    public static final String COLUMN_OTHER_NAME = "otherName";
    public static final String COLUMN_VALID = "valid";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_STATE = "state"; // SQLite can not
    public static final String COLUMN_PACKET_ID = "packetId";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TYPE_DESC = "typeDesc";

    // boolean mappings
    public static final int INCOMING = 0;
    public static final int OUTGOING = 1;//自己的消息
    public static final int STATE_NEW = 0; // <
    public static final int STATE_SEND_SUCC = 1; // < 消息已发送
    public static final int STATE_SEND_FAIL = 2; //消息发送失败
    public static final int STATE_READ = 3; // < 消息已读
    public static final int MSG_VALID   = 1;
    public static final int MSG_INVALID = 0;
    //代表该消息已被对方阅读，实际本项目并未用上此状态，有点小问题
    public static final int DS_ACKED = 2; // < this message was XEP-0184
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DATE + " LONG,"
            + COLUMN_DIRECTION + " INTEGER,"
            + COLUMN_MYSELF_NAME + " INTEGER,"
            + COLUMN_OTHER_NAME + " INTEGER,"
            + COLUMN_CONTENT + " TEXT,"
            + COLUMN_STATE + " INTEGER DEFAULT 0,"
            + COLUMN_VALID + " INTEGER DEFAULT 1,"
            + COLUMN_TYPE + " INTEGER,"
            + COLUMN_TYPE_DESC + " TEXT,"
            + COLUMN_PACKET_ID + " TEXT);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
        database.execSQL("CREATE INDEX chat_index ON "+TABLE_NAME+" ("
                + COLUMN_OTHER_NAME +", "
                + COLUMN_MYSELF_NAME +", "
                + COLUMN_VALID +");");
    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
            db.execSQL("DROP INDEX IF EXISTS chat_index;");
            db.execSQL("CREATE INDEX chat_index ON " + TABLE_NAME + " ("
                    + COLUMN_OTHER_NAME + ", "
                    + COLUMN_MYSELF_NAME + ", "
                    + COLUMN_VALID + ");");
        }
    }
}
