package com.juns.wechat.xmpp.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.InviteMsg;
import com.juns.wechat.bean.chat.TextMsg;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ThreadPoolUtil;
import com.juns.wechat.xmpp.XmppManager;
import com.juns.wechat.xmpp.XmppManagerImpl;

import org.jivesoftware.smack.packet.id.StanzaIdUtil;
import org.xutils.db.sqlite.WhereBuilder;

import java.io.File;
import java.util.Date;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：发送消息的工具类，可以发送不同类型的消息
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public class SendMessage {
    private static MessageDao messageDao = MessageDao.getInstance();

    /**
     * 发送普通的文字消息
     * @param content
     */
    public static void sendTextMsg(final String otherName, final String content){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                TextMsg textMsg = new TextMsg();
                textMsg.content = content;
                try {
                    messageBean.setMsg(textMsg.toSendJson());
                    messageBean.setOtherName(otherName);
                    messageBean.setType(MsgType.MSG_TYPE_TEXT);
                    sendMsg(messageBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 发送添加好友消息
     * @param reason
     */
    public static void sendInviteMsg(final String otherName, final String reason){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                InviteMsg inviteMsg = new InviteMsg();
                inviteMsg.name = AccountManager.getInstance().getUser().getShowName();
                inviteMsg.reason = reason;
                try {
                    messageBean.setMsg(inviteMsg.toSendJson());
                    messageBean.setOtherName(otherName);
                    messageBean.setType(MsgType.MSG_TYPE_SEND_INVITE);
                    sendMsg(messageBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void completeMessageEntityInfo(MessageBean message){
        String myselfName = AccountManager.getInstance().getUserName();
        message.setMyselfName(myselfName);
        String packetId = StanzaIdUtil.newStanzaId();
        message.setPacketId(packetId);
        message.setDate(new Date());
        message.setState(MessageBean.State.NEW.value);
        message.setDirection(MessageBean.Direction.OUTGOING.value);
    }

    /**
     * 发送消息分两步：1.将消息发出去
     * 2.将消息存入本地数据库
     * @param message
     */
    public static void sendMsg(MessageBean message) {
        completeMessageEntityInfo(message);
        addMessageToDB(message);

        boolean send = XmppManagerImpl.getInstance().sendMessage(message);
        if(!send){  //如果未成功发送,
            updateMessageState(message.getPacketId(), MessageBean.State.SEND_FAILED.value);
        }
    }

    /**
     * 将消息存进本地数据库
     * @return
     */
    private static void addMessageToDB(MessageBean messageBean){
       messageDao.save(messageBean);
    }

    /**
     * 更新消息状态，只是在消息未成功发送时调用，因为消息成功发送后，服务器会发送一条回执消息，那时可以将消息状态置为发送成功
     * @param state
     */
    private static void updateMessageState(String packetId, int state){
        messageDao.updateMessageState(packetId, state);
    }

}
