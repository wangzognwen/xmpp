package com.juns.wechat.dao;

import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.Msg;
import com.juns.wechat.config.MsgType;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

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

    public MessageBean findByPacketId(String packetId){
        Map<String, Object> params = new HashMap<>();
        params.put(MessageBean.PACKET_ID, packetId);
        return findByParams(params);
    }

    public List<MessageBean> getMyReceivedInviteMessages(String myselfName){
        Map<String, Object> params = new HashMap<>();
        params.put(MessageBean.MYSELF_NAME, myselfName);
        params.put(MessageBean.DIRECTION, MessageBean.Direction.INCOMING.value);
        params.put(MessageBean.TYPE, MsgType.MSG_TYPE_INVITE);
        List<MessageBean> messageBeen =  findAllByParams(params);

        if(messageBeen != null && !messageBeen.isEmpty()){
            for(MessageBean messageBean : messageBeen){
                messageBean.setMsgObj(Msg.fromJson(messageBean.getMsg(),  MsgType.MSG_TYPE_INVITE));
            }
        }
        return messageBeen;
    }

}
