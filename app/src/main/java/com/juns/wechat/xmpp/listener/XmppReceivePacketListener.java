package com.juns.wechat.xmpp.listener;

import android.content.Context;


import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：接受到的xmpp包监听事件
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public class XmppReceivePacketListener implements StanzaListener {
    //private MessageDao messageDao;
    private Context mContext;
    //private ConcurrentMap<Integer, MessageProcess> processMap;
    private Object lock = new Object();

    public XmppReceivePacketListener(){
       // messageDao = MessageDao.getInstance();
       // mContext = YunPianApplication.getApplication();
      //  processMap = new ConcurrentHashMap<>();
    }

    /**
     * 处理包：stanaza有3种子类型：Message(消息），Presence(状态),IQ（信息查询）
     * @param packet
     * @throws SmackException.NotConnectedException
     */
    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        if(packet instanceof Message){
            Message message = (Message) packet;
            handleMessage(message);
        }else if(packet instanceof Presence){
            Presence presence = (Presence) packet;
            handlePresence(presence);
        }
    }

    /**
     * 处理消息，Type为normal:发送向服务器发送一条消息时，服务器的回执消息，表明消息已收到
     * Type为chat,别人发给我的聊天消息，Type为error，消息未发送成功
     * @param message
     */
    private void handleMessage(Message message) {
        if(Message.Type.normal == message.getType()){
            //updateExistMessage(message, ChatTable.STATE_SEND_SUCC);
        }else if(Message.Type.chat == message.getType()){
            handleChatMessageByType(message);
        }else if(Message.Type.error == message.getType()){
           // LogUtil.i("type : error!");
          //  updateExistMessage(message, ChatTable.STATE_SEND_FAIL);
        }
    }

    /**
     * 处理Presence
     * @param presence
     */
    private void handlePresence(Presence presence){

    }

    /**
     * 更新数据库已存在的消息数据的状态
     * @param message
     */
    private void updateExistMessage(Message message, int state){
        //MessageEntity messageEntity = new MessageEntity();
        //messageEntity.setPacketId(message.getStanzaId());
        //messageEntity.setState(state);
        //messageDao.updateMessageEntity(messageEntity);
    }

    /**
     * 处理聊天消息
     * 1将{类型
     * 2 根据转换后的实体类得到的type来调用不同的子类具体处理
     * @param message
     */
    private void handleChatMessageByType(Message message){
      /*  MessageEntity messageEntity = MessageEntity.convertToEntity(message);
        int type = messageEntity.getType();
        MessageProcess messageProcess = processMap.get(type);
        if(messageProcess == null){
            synchronized (lock){
                switch (type){
                    case MsgType.CHAT_MSG_TYPE_TEXT:
                    case MsgType.MSG_TYPE_TEXT:
                        messageProcess = new TextMessageProcess(mContext);
                        break;
                    case MsgType.CHAT_MSG_TYPE_VOICE:
                    case MsgType.MSG_TYPE_VOICE:
                        messageProcess = new VoiceMessageProcess(mContext);
                        break;
                    case MsgType.MSG_TYPE_PICTURE:
                        messageProcess = new PictureMessageProcess(mContext);
                        break;
                    case MsgType.MSG_TYPE_ACTIVE_INTRODUCE:
                    case MsgType.MSG_TYPE_RESP_ACTIVE_INTRODUCE:
                        messageProcess = new ActiveIntroMessageProcess(mContext);
                        break;
                    //不支持的消息类型就应该丢弃而不去处理
                    default:
                        return;
                }
            }
        }
        processMap.put(type, messageProcess);
        messageProcess.processedMessage(messageEntity);*/
    }
}
