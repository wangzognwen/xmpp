package com.juns.wechat.activity;

import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.bean.chat.viewmodel.MsgViewModel;
import com.juns.wechat.bean.chat.viewmodel.TextMsgViewModel;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 王者 on 2016/8/8.
 */
public class ChatActivityHelper {
    private ChatActivity chatActivity;

    private static final int SIZE = 10;  //一次最多查询10条数据

    private String myselfName;
    private MessageDao messageDao;
    private String otherName;
    private int mQueryIndex; //从该位置开始查询

    private UserBean account = AccountManager.getInstance().getUser();

    private List<MsgViewModel> msgViewModels = new ArrayList<>();


    public ChatActivityHelper(ChatActivity chatActivity){
        this.chatActivity = chatActivity;
        this.otherName = chatActivity.getContactName();
    }

    public void onCreate(){
        initQueryIndex(myselfName, otherName);
        markAsRead(myselfName, otherName);
    }

    private void initQueryIndex(String myselfName, String otherName){
        int size = messageDao.getMessageCount(myselfName, otherName);
        mQueryIndex = size > SIZE ? size - SIZE : 0;
    }

    private List<MessageBean> getMessagesByIndexAndSize(){
        return messageDao.getMessagesByIndexAndSize(myselfName, otherName, mQueryIndex, SIZE);
    }

    public void loadMessagesFromDb(){
        List<MessageBean> messageBeen = getMessagesByIndexAndSize();
        chatActivity.refreshComplete();
        if(messageBeen == null || messageBeen.isEmpty()) return;
        mQueryIndex = mQueryIndex - messageBeen.size();
        addEntityToViewModel(messageBeen);
    }

    /**
     * 由于消息是从上向下展示，下拉刷新查询出消息应该放在数据链表头部
     * @param messageEntities
     */
    private void addEntityToViewModel(List<MessageBean> messageEntities){
        int size = messageEntities.size();
        for(int i = size - 1; i >= 0 ; i--){
            MessageBean entity = messageEntities.get(i);
            addEntityToViewModel(entity);
        }
        Collections.sort(msgViewModels);  //要将消息重新排一次序
    }

    private void addEntityToViewModel(MessageBean entity){
        int type = entity.getType();
        if(entity == null) return;
        MsgViewModel viewModel = null;
        UserBean contactUser = chatActivity.getContactUser();
        switch (type){
            case MsgType.MSG_TYPE_TEXT:
                viewModel = new TextMsgViewModel(chatActivity, entity);
                viewModel.setInfo(account.getUserName(), account.getHeadUrl(), contactUser.getUserName(), contactUser.getHeadUrl());
                viewModel.markAsRead();
                msgViewModels.add(viewModel);
                break;
            default:
                break;
        }
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

    public void onDestroy(){

    }

    public int getQueryIndex() {
        return mQueryIndex;
    }

    public void setQueryIndex(int mQueryIndex) {
        this.mQueryIndex = mQueryIndex;
    }
}
