package com.juns.wechat.dao;

import android.database.Cursor;

import com.juns.wechat.bean.Flag;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.bean.chat.Msg;
import com.juns.wechat.fragment.msg.MsgItem;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.database.CursorUtil;
import com.juns.wechat.database.IdGenerator;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /**
     * 获取每一个用户与这个用户聊天的最近的一条消息
     * @param userName
     */
    public List<MsgItem> getLastMessageWithEveryFriend(String userName){
        SqlInfo sqlInfo = new SqlInfo("select id, otherName, typeDesc, type, date" +
                " from wcMessage where myselfName = ? and state = ? and flag != -1 group by otherName order by date");
        sqlInfo.addBindArg(new KeyValue(UserBean.USERNAME, userName));
        try {
            List<MsgItem> msgItems = new ArrayList<>();
            Cursor cursor = dbManager.execQuery(sqlInfo);
            while (cursor.moveToNext()){
                MsgItem msgItem = new MsgItem();
                msgItem.itemId = cursor.getInt(cursor.getColumnIndex("id"));
                msgItem.userName = cursor.getString(cursor.getColumnIndex("otherName"));
                msgItem.content = cursor.getString(cursor.getColumnIndex("typeDesc"));
                long time = cursor.getLong(cursor.getColumnIndex("date"));
                msgItem.date = new Date(time);
                msgItems.add(msgItem);
            }
            closeCursor(cursor);
            return msgItems;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUnreadMsgNum(String myselfName, String otherName){
        SqlInfo sqlInfo = new SqlInfo("select count(1) as count from wcFriend where myselfName = ? " +
                "and otherName = ? and direction = ? and state = ?");
        sqlInfo.addBindArg(new KeyValue("key1", myselfName));
        sqlInfo.addBindArg(new KeyValue("key2", otherName));
        sqlInfo.addBindArg((new KeyValue("key3", MessageBean.Direction.INCOMING.value)));
        sqlInfo.addBindArg(new KeyValue("key4", MessageBean.State.NEW));
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            if(cursor.moveToNext()){
                return cursor.getInt(cursor.getColumnIndex("count"));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取收到的请求添加好友的消息
     * @param myselfName
     * @return
     */
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

    /**
     * 分页查询两个用户之间的聊天记录
     * @param myselfName
     * @param otherName
     * @param index
     * @param size
     * @return
     */
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
            closeCursor(cursor);
            return messageBeanList;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取两个用户之间的聊天记录总数
     * @param myselfName
     * @param otherName
     * @return
     */
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
            closeCursor(cursor);
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
