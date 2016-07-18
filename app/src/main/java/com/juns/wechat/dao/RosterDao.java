package com.juns.wechat.dao;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.database.FriendTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class RosterDao extends BaseDao<FriendBean>{
    private static RosterDao mInstance;

    public static RosterDao getInstance(){
        if(mInstance == null){
            mInstance = new RosterDao();
        }
        return mInstance;
    }

    public List<FriendBean> queryAllByOwner(String ownerName){
        Map<String, Object> params = new HashMap<>();
        params.put(FriendTable.COLUMN_OWNER_NAME, ownerName);
        return findAllByParams(params);
    }
}
