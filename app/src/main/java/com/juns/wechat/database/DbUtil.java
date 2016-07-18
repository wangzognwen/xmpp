package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;

import net.tsz.afinal.db.sqlite.SqlBuilder;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.SqlInfoBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
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
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    try {
                        TableEntity<UserBean> userBeanTableEntity = db.getTable(UserBean.class);
                        if(!userBeanTableEntity.tableIsExist()){
                            SqlInfo sqlInfo = SqlInfoBuilder.buildCreateTableSqlInfo(userBeanTableEntity);
                            db.execNonQuery(sqlInfo);
                        }

                        TableEntity<FriendBean> friendBeanTableEntity = db.getTable(FriendBean.class);
                        if(!friendBeanTableEntity.tableIsExist()){
                            SqlInfo sqlInfo = SqlInfoBuilder.buildCreateTableSqlInfo(friendBeanTableEntity);
                            db.execNonQuery(sqlInfo);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    SQLiteDatabase database = db.getDatabase();
                    FriendTable.onUpgrade(database, oldVersion, newVersion);
                    UserTable.onUpgrade(database, oldVersion, newVersion);
                }
            });

}
