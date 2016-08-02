package com.juns.wechat.bean;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.juns.wechat.database.ChatTable;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.delay.DelayInformationManager;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

/*******************************************************
 * Created by 王宗文 on 2015/11/16
 *******************************************************/

@Table(name = ChatTable.TABLE_NAME, onCreated = ChatTable.CREATE_INDEX)
public class MessageBean{
    public static final String ID = "id";
    public static final String MYSELF_NAME = "myselfName";
    public static final String OTHER_NAME = "otherName";
    public static final String MSG = "msg";
    public static final String TYPE = "type";
    public static final String TYPE_DESC = "typeDesc";
    public static final String PACKET_ID = "packetId";
    public static final String DATE  = "date";
    public static final String DIRECTION = "direction"; //消息方向，是发出去还是接受到的
    public static final String STATE = "state";
    public static final String FLAG = "flag";

    public static final int INCOMING = 1;
    public static final int OUTGOING = 2;

    @Column(name = ID, isId = true)
    private int id; //主键，子增长
    @Column(name = MYSELF_NAME)
    private String myselfName; //自己;
    @Column(name = OTHER_NAME)
    private String otherName; //对方;
    @Column(name = MSG)
    private String msg; //对应表中msg字段
    @Column(name = TYPE)
    private int type; //消息类型
    @Column(name = TYPE_DESC)
    private String typeDesc;
    @Column(name = PACKET_ID)
    private String packetId; //每一条消息在网络上发送时都表现为一个消息包，这个packetId与每条消息的消息Id是一致的
    @Column(name = DATE)
    private Date date; //发送消息时间
    @Column(name = DIRECTION)
    private int direction; // 发出去还是收到了消息
    @Column(name = STATE)
    private int state; //判断消息是否已发送或者已读
    @Column(name = FLAG)
    private int flag; //判断消息是否有效
    private Object obj; //msg字段对应的字段

    public MessageBean() { }

    public String toSendJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MSG, msg);
            jsonObject.put(TYPE, type);
            jsonObject.put(TYPE_DESC, typeDesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMyselfName() {
        return myselfName;
    }

    public void setMyselfName(String myselfName) {
        this.myselfName = myselfName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
