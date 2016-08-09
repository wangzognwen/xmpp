package com.juns.wechat.fragment.msg;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.activity.ChatActivity;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.net.callback.QueryUserCallBack;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.ImageUtil;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ChatMsgItemShow extends MsgItemShow {
    private FriendBean friendBean;
    private UserBean stranger; //陌生人

    public ChatMsgItemShow(Context context, MsgItem msgItem){
        super(context, msgItem);
    }

    @Override
    public void showTitle(final TextView textView) {
        friendBean = FriendDao.getInstance().findByOwnerAndContactName(myselfName, msgItem.userName);
        if(friendBean != null){
            textView.setText(friendBean.getShowName());
        }else {  //陌生人
            UserRequest.queryUserData(msgItem.userName, new QueryUserCallBack() {
                @Override
                protected void handleSuccess(BaseResponse.QueryUserResponse result) {
                    stranger = result.userBean;
                    textView.setText(stranger.getShowName());
                }
            });
        }
    }

    @Override
    public void loadUrl(final ImageView iv) {
        if(friendBean == null){
            if(stranger != null){
                ImageUtil.loadImage(iv, stranger.getHeadUrl());
            }else {
                UserRequest.queryUserData(msgItem.userName, new QueryUserCallBack() {
                    @Override
                    protected void handleSuccess(BaseResponse.QueryUserResponse result) {
                        stranger = result.userBean;
                        ImageUtil.loadImage(iv, stranger.getHeadUrl());
                    }
                });
            }
        }else {
            ImageUtil.loadImage(iv, friendBean.getHeadUrl());
        }

    }

    @Override
    public void onItemClick() {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(ChatActivity.ARG_USER_NAME, msgItem.userName);
        mContext.startActivity(intent);
    }

}