package com.juns.wechat.xmpp.process;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.juns.wechat.R;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.xmpp.prompt.NoticePrompt;
import com.juns.wechat.xmpp.prompt.SoundPrompt;

import org.json.JSONException;
import org.json.JSONObject;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：不同类型消息处理的抽象基类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/27
 *******************************************************/
public abstract class MessageProcess {
    private MessageDao messageDao;
    /**
     * 声音提示
     */
    private SoundPrompt mSoundPrompt;
    /**
     * 下拉通知提示
     */
    private NoticePrompt mNoticePrompt;
    protected Context mContext;

    public MessageProcess(Context context){
        mContext = context;
        messageDao = MessageDao.getInstance();
        mSoundPrompt = new SoundPrompt(mContext);
        mNoticePrompt = new NoticePrompt(mContext);
    }

    /**
     * 处理消息，大多消息处理遵循以下几步：
     * 1：根据packetId检测消息是否已存在
     * 2：将消息存入数据库
     * 3：发出提示音和显示通知
     * 特定类型的消息需要重写此方法
     * @param entity
     * @return
     */
    public void processedMessage(MessageBean entity){
        if(isMsgExist(entity.getPacketId())){
            return;
        }
        saveMessageToDB(entity);
        ringDone();
        noticeShow(entity);
    }

    protected boolean isMsgExist(String packetId){
        return messageDao.isMsgExist(packetId);
    }

    protected void saveMessageToDB(MessageEntity entity){
        messageDao.addMessageEntity(entity);
    }

    public void ringDone(){
        ringDone(R.raw.office);
    }

    public void ringDone(int resId){
        Context context = YunPianApplication.getApplication().getBaseContext();
        mSoundPrompt = new SoundPrompt(context);
        mSoundPrompt.setRingRes(resId);
        mSoundPrompt.ringDone();
    }

    public void noticeShow(MessageEntity entity){
        noticeShow(entity, null);
    }

    public void noticeShow(MessageEntity entity, String notice){
        Context context = YunPianApplication.getApplication().getBaseContext();
        mNoticePrompt = new NoticePrompt(context);
        String userId = entity.getOtherUserId() + "";
        Card card = Card.getOneCardByOwnerId(userId);
        if (null == card ) {
            return;
        }

        if (TextUtils.isEmpty(notice)){
            notice = getContentOfMsg(entity.getMsg());
        }

        Intent noticeIntent = new Intent(mContext, ChatDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ChatDetailsActivity.PEER_USER_ID, Integer.valueOf(userId));
        noticeIntent.putExtras(bundle);
        mNoticePrompt.notifyClient(userId, card.getName(), notice, noticeIntent);
    }

    private String getContentOfMsg(String msg){
        try {
            JSONObject object = new JSONObject(msg);
            return object.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
