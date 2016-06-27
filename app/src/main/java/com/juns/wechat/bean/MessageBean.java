package com.juns.wechat.bean;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.juns.wechat.database.ChatTable;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.delay.DelayInformationManager;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：聊天消息所对应的实体类，用泛型是因为用json字符串来存message属性，需要转换
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/16
 *******************************************************/
public class MessageBean implements Comparable<MessageBean> {
    private Integer _Id; //主键，子增长
    private String packetId; //每一条消息在网络上发送时都表现为一个消息包，这个packetId与每条消息的消息Id是一致的
    private Date date; //发送消息时间
    private Integer direction; //
    private Integer myselfUserId; //我自己的Jid;
    private Integer otherUserId; //对方的Jid;
    private String msg; //对应表中msg字段
    private Integer valid; //判断消息是否有效
    private Integer state; //判断消息是否已发送或者已读
    private Integer type; //消息类型

    public MessageBean() { }

    public MessageBean(Cursor cursor){
    /*    _Id = cursor.getInt(cursor.getColumnIndex(ChatTable._ID));
        packetId = cursor.getString(cursor.getColumnIndex(ChatTable.PACKET_ID));
        long time = cursor.getLong(cursor.getColumnIndex(ChatTable.DATE));
        date = new Date(time);
        myselfUserId = cursor.getInt(cursor.getColumnIndex(ChatTable.MYSELF_USER_ID));
        otherUserId = cursor.getInt(cursor.getColumnIndex(ChatTable.OTHER_USER_ID));
        direction = cursor.getInt(cursor.getColumnIndex(ChatTable.DIRECTION));
        msg = cursor.getString(cursor.getColumnIndex(ChatTable.MSG));
        valid = cursor.getInt(cursor.getColumnIndex(ChatTable.VALID));
        state = cursor.getInt(cursor.getColumnIndex(ChatTable.STATE));
        type = cursor.getInt(cursor.getColumnIndex(ChatTable.TYPE));*/
    }

    /**
     * 将Message转换为MessageEntity
     * @param message
     * @return
     */
    public static MessageBean convertToEntity(Message message){
        String body = message.getBody();
        try {
            MessageBean messageEntity = new MessageBean();
            JSONObject jsonObject = new JSONObject(body);

            if(!TextUtils.isEmpty(message.getStanzaId())){
                messageEntity.setPacketId(message.getStanzaId());
            }
           // int fromUserId = Integer.parseInt(ConfigUtil.getUserId(message.getFrom()));
           // int toUserId = Integer.parseInt(ConfigUtil.getUserId(message.getTo()));
          //  messageEntity.setMyselfUserId(toUserId);
          //  messageEntity.setOtherUserId(fromUserId);
            messageEntity.setDirection(ChatTable.INCOMING);
//            int type = messageEntity.getType();


            Date date = new Date();
            DelayInformation delayInformation = DelayInformationManager.getDelayInformation(message);
            if(delayInformation != null){
                date = delayInformation.getStamp();
            }
            messageEntity.setDate(date);
            messageEntity.setValid(ChatTable.MSG_VALID);
            messageEntity.setState(ChatTable.STATE_NEW);
            return messageEntity;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Integer get_Id() {
        return _Id;
    }

    public void set_Id(Integer _Id) {
        this._Id = _Id;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMyselfUserId() {
        return myselfUserId;
    }

    public void setMyselfUserId(Integer myselfUserId) {
        this.myselfUserId = myselfUserId;
    }

    public Integer getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(Integer otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    @Override
    public int compareTo(MessageBean another) {
        int anotherId = another.get_Id();
        return anotherId == _Id ? 0 : (_Id > anotherId ? 1 : - 1);
    }
}
