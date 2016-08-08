package com.juns.wechat.manager;


import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.util.ThreadPoolUtil;

import org.simple.eventbus.EventBus;

import java.util.List;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：聊天消息的服务类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/26
 *******************************************************/
public class MessageManager {
    private static final int SIZE = 10;  //一次最多查询10条数据

    private String myselfName;
    private MessageDao messageDao;
    private String otherName;
    private int mQueryIndex; //从该位置开始查询


    public MessageManager(String otherName){
        myselfName = AccountManager.getInstance().getUserName();
        this.otherName = otherName;
        messageDao = MessageDao.getInstance();
    }

    private void init(){
        initQueryIndex(myselfName, otherName);
        markAsRead(myselfName, otherName);
    }

    private void initQueryIndex(String myselfName, String otherName){
        int size = messageDao.getMessageCount(myselfName, otherName);
        mQueryIndex = size > SIZE ? size - SIZE : 0;
    }

    public List<MessageBean> getMessagesByIndexAndSize(){
        return messageDao.getMessagesByIndexAndSize(myselfName, otherName, mQueryIndex, SIZE);
    }

    /**
     * 通过_id和两个用户Id获取数据
     * @see MessageDao#getMessageByIdAndUserId(int, int, int)
     * @param _id
     * @param otherUserId
     */
    public synchronized void getMessageById(final int _id, final int otherUserId){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                GetOneMessageEvent event = new GetOneMessageEvent();
                MessageEntity entity = messageDao.getMessageByIdAndUserId(_id, mySelfUserId, otherUserId);
                event.messageEntity = entity;
                EventBus.getDefault().post(event);
            }
        });
    }

    /**
     * 通过两个用户ID获取他们聊天总记录大小
     * @see MessageDao#getMessagesSizeByAB(int, int)
     * @param otherUserId
     * @return
     */
    public int getMessagesSize(int otherUserId){
        return messageDao.getMessagesSizeByAB(mySelfUserId, otherUserId);
    }

    /**
     * 将未读消息状态置为已读状态
     * @param otherName
     */
    public void markAsRead(final String myselfName, final String otherName){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                messageDao.markAsRead(myselfName, otherName);
            }
        });
    }

    public int getQueryIndex() {
        return mQueryIndex;
    }

    public void setQueryIndex(int mQueryIndex) {
        this.mQueryIndex = mQueryIndex;
    }
}
