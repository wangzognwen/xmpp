package com.juns.wechat.dao;

import android.database.Cursor;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.database.FriendTable;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class FriendDao extends BaseDao<FriendBean>{
    private static final String GET_LAST_MODIFY_DATE =
            "SELECT max(f.modifyDate) as lastModifyDate from wcFriend f where f.ownerName = ? and " +
                    "(f.subType = 'both' or f.subType = 'from')";

    private static final String SELECT_NOT_EXIST_USER_IN_FRIEND =
            "select contactName from wcFriend where ownerName = ? and contactName not in (select userName from wcUser)";

    private static FriendDao mInstance;

    public static FriendDao getInstance(){
        if(mInstance == null){
            mInstance = new FriendDao();
        }
        return mInstance;
    }

    public List<FriendBean> queryAllByOwner(String ownerName){
        Map<String, Object> params = new HashMap<>();
        params.put(FriendBean.OWNER_NAME, ownerName);
        return findAllByParams(params);
    }

    public FriendBean findByOwnerAndContactName(String ownerName, String contactName){
        Map<String, Object> params = new HashMap<>();
        params.put(FriendBean.OWNER_NAME, ownerName);
        params.put(FriendBean.CONTACT_NAME, contactName);
        return findByParams(params);
    }

    public long getLastModifyDate(String userName){
        long lastModifyDate = 0;

        SqlInfo sqlInfo = new SqlInfo(GET_LAST_MODIFY_DATE);
        List<KeyValue> keyValues = new ArrayList<>();
        KeyValue keyValue1 = new KeyValue("key1", userName);
        keyValues.add(keyValue1);
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

    public String[] getNotExistUsersInFriend(String ownerName){
        SqlInfo sqlInfo = new SqlInfo(SELECT_NOT_EXIST_USER_IN_FRIEND);
        List<KeyValue> keyValues = new ArrayList<>();
        KeyValue keyValue1 = new KeyValue("key1", ownerName);
        keyValues.add(keyValue1);
        sqlInfo.addBindArgs(keyValues);

        List<String> userNames = new ArrayList<>();
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            while (cursor.moveToNext()){
                String contactName = cursor.getString(cursor.getColumnIndex("contactName"));
                userNames.add(contactName);
            }
            closeCursor(cursor);
            if(userNames != null && userNames.isEmpty()){
                String[] userNameArray = new String[1];
                return userNames.toArray(userNameArray);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

}
