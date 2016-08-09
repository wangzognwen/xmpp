package com.juns.wechat.fragment.msg;

import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ImageUtil;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ChatMsgItemShow extends MsgItemShow {
    private FriendBean friendBean;

    public ChatMsgItemShow(MsgItem msgItem){
        super(msgItem);
        String ownerName = AccountManager.getInstance().getUserName();
        friendBean = FriendDao.getInstance().findByOwnerAndContactName(ownerName, msgItem.userName);
    }

    @Override
    public void showTitle(TextView textView) {

        if(friendBean != null){
            textView.setText(friendBean.getShowName());
        }
    }

    @Override
    public void loadUrl(ImageView iv) {
        UserBean userBean = UserDao.getInstance().findByName(msgItem.userName);
        ImageUtil.loadImage(iv, userBean.getHeadUrl());
    }

}
