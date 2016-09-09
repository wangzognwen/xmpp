package com.juns.wechat.xmpp.process;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.juns.wechat.R;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.activity.ChatActivity;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.xmpp.prompt.NoticePrompt;
import com.juns.wechat.xmpp.prompt.SoundPrompt;

/*******************************************************
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
     * @param messageBean
     * @return
     */
    public void processMessage(MessageBean messageBean){
        if(isMsgExist(messageBean.getPacketId())){
            return;
        }
        saveMessageToDB(messageBean);
        //ringDone();
        noticeShow(messageBean);
    }

    protected final boolean isMsgExist(String packetId){
        String myselfName = AccountManager.getInstance().getUserName();
        boolean isMsgExist = messageDao.findByPacketId(myselfName, packetId) != null;
        LogUtil.i("isMsgExist:"  + isMsgExist);
        return isMsgExist;
    }

    protected void saveMessageToDB(MessageBean messageBean){
        messageDao.save(messageBean);
    }

    public void noticeShow(MessageBean entity){
        noticeShow(entity, null);
    }

    public void noticeShow(MessageBean entity, String notice){
        mNoticePrompt = new NoticePrompt(mContext);

        if (TextUtils.isEmpty(notice)){
            notice = entity.getTypeDesc();
        }

        Intent noticeIntent = new Intent(mContext, ChatActivity.class);
        mNoticePrompt.notifyClient(entity.getOtherName(), notice, noticeIntent);
    }
}
