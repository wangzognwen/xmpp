package com.juns.wechat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.App;
import com.juns.wechat.config.ConfigUtil;

import net.tsz.afinal.FinalDb;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by 王宗文 on 2016/5/25
 */
public class DbUtil {
    private static final String DATABASE_NAME = "weixin.db";
    private static final int DATABASE_VERSION = 11;

    public static DbManager getDbManager() {
       return x.getDb(dbConfig);
    }

    private static DbManager.DaoConfig dbConfig = new DbManager.DaoConfig()
            .setDbName(DATABASE_NAME).setDbVersion(DATABASE_VERSION)
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    SQLiteDatabase database = db.getDatabase();
                    RosterTable.onUpgrade(database, oldVersion, newVersion);
                    UserTable.onUpgrade(database, oldVersion, newVersion);
                }
            });

}
