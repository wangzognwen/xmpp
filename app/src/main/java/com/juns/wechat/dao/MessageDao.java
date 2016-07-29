package com.juns.wechat.dao;

import android.database.Cursor;

import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class MessageDao extends BaseDao<MessageBean>{

    private static MessageDao mInstance;

    public static MessageDao getInstance(){
        if(mInstance == null){
            mInstance = new MessageDao();
        }
        return mInstance;
    }

    public boolean updateMessageState(String packetId, int state){
        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.PACKET_ID, "=", packetId);
        KeyValue keyValue = new KeyValue(MessageBean.STATE, state);
        return update(whereBuilder, keyValue);
    }

}
