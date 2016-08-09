package com.juns.wechat.fragment.msg;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.TimeUtil;

/**
 * Created by 王者 on 2016/8/9.
 */
public abstract class MsgItemShow {
    protected MsgItem msgItem;
    protected String myselfName = AccountManager.getInstance().getUserName();

    public MsgItemShow(MsgItem msgItem){
        this.msgItem = msgItem;
    }

    public abstract void showTitle(TextView textView);

    public abstract void loadUrl(ImageView iv);

    public void showContent(TextView textView){
        textView.setText(msgItem.content == null ? "" : msgItem.content);
    }

    public  void showUnreadMsgNumber(TextView textView){
        int unreadMsgCount = MessageDao.getInstance().getUnreadMsgNum(myselfName, msgItem.userName);
        if(unreadMsgCount == 0){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(unreadMsgCount + "");
        }
    }

    public final void showTime(TextView textView){
        textView.setText(TimeUtil.getRecentTime(msgItem.date));
    }
}
