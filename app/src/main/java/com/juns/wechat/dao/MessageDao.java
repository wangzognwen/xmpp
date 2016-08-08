package com.juns.wechat.dao;

import android.database.Cursor;

import com.juns.wechat.bean.Flag;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.Msg;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.database.CursorUtil;
import com.juns.wechat.database.IdGenerator;

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

    private static final String SELECT_MESSAGES_BY_PAGING =
            "select * from wcMessage where myselfName = ? and otherName = ? and flag != -1 limit ? offset ?";
    private static final String SELECT_MESSAGE_COUNT_BETWEEN_TWO_USER =
            "select count(id) as count from wcMessage where myselfName = ? and otherName = ? and flag != -1";

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

    public MessageBean findByPacketId(String myselfName, String packetId){
        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.PACKET_ID, "=", packetId);
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        return findByParams(whereBuilder);
    }

    public List<MessageBean> getMyReceivedInviteMessages(String myselfName){
        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.TYPE, "=", MsgType.MSG_TYPE_INVITE);
        List<MessageBean> messageBeen =  findAllByParams(whereBuilder);

        if(messageBeen != null && !messageBeen.isEmpty()){
            for(MessageBean messageBean : messageBeen){
                messageBean.setMsgObj(Msg.fromJson(messageBean.getMsg(),  MsgType.MSG_TYPE_INVITE));
            }
        }
        return messageBeen;
    }

    public List<MessageBean> getMessagesByIndexAndSize(String myselfName, String otherName, int index, int size){
        SqlInfo sqlInfo = new SqlInfo(SELECT_MESSAGES_BY_PAGING);
        sqlInfo.addBindArg(new KeyValue("key1", myselfName));
        sqlInfo.addBindArg(new KeyValue("key2", otherName));
        sqlInfo.addBindArg(new KeyValue("key3", size));
        sqlInfo.addBindArg(new KeyValue("key4", index));

        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            List<MessageBean> messageBeanList = new ArrayList<>();
            while (cursor.moveToNext()){
                MessageBean messageBean = CursorUtil.fromCursor(cursor, MessageBean.class);
                messageBeanList.add(messageBean);
            }
            return messageBeanList;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getMessageCount(String myselfName, String otherName){
        SqlInfo sqlInfo = new SqlInfo(SELECT_MESSAGE_COUNT_BETWEEN_TWO_USER);
        sqlInfo.addBindArg(new KeyValue("key1", myselfName));
        sqlInfo.addBindArg(new KeyValue("key2", otherName));
        int count = 0;
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            if(cursor.moveToNext()){
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void markAsRead(String myselfName, String otherName){
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.OTHER_NAME, "=", otherName);
        whereBuilder.and(MessageBean.FLAG, "!=", Flag.INVALID.value());
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);

        KeyValue keyValue = new KeyValue(MessageBean.STATE, MessageBean.State.READ.value);
        update(whereBuilder, keyValue);
    }

    @Override
    protected void addIdIfNeeded(MessageBean messageBean) {
        if(messageBean.getId() == 0){
            messageBean.setId(IdGenerator.nextId(ChatTable.TABLE_NAME));
        }
    }
}
