package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.QueryUserCallBack;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.ImageUtil;
import com.juns.wechat.util.ToastUtil;

/**
 * 用户资料
 */
@Content(R.layout.activity_user_info)
public class UserInfoActivity extends ToolbarActivity implements OnClickListener {
    public static final String ARG_USER_NAME = "user_name";

    @Id
    private ImageView ivAvatar;
    @Id
	private TextView tvNickName;
    @Id
    private TextView tvUserName;
    @Id
    private Button btnSendMsg;
    @Extra(name = ARG_USER_NAME)
	private String userName;

    private UserBean account = AccountManager.getInstance().getUser();
    private FriendBean friendBean;
    private UserBean userBean;
    private String subType = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setToolbarRight(2, R.drawable.icon_more);
        initData();
	}

	protected void initData() {
		friendBean = FriendDao.getInstance().findByOwnerAndContactName(account.getUserName(), userName);
        if (friendBean == null){  //不是好友关系
            UserRequest.queryUserData(userName, queryUserCallBack);
        }else {
            subType = friendBean.getSubType();
            userBean = friendBean.getContactUser();
            setData();
        }
	}

    private void setData(){
        tvNickName.setText(userBean.getShowName());
        tvUserName.setText("微信号：" + userBean.getUserName());
        ImageUtil.loadImage(ivAvatar, userBean.getHeadUrl());

        if(subType == null){
            btnSendMsg.setText("加为好友");
        }else {
            btnSendMsg.setText("发送消息");
        }
    }

	private QueryUserCallBack queryUserCallBack = new QueryUserCallBack() {
        @Override
        protected void handleSuccess(BaseResponse.QueryUserResponse result) {
            userBean = result.userBean;
            setData();
        }

        @Override
        protected void handleFailed(BaseResponse.QueryUserResponse result) {
            ToastUtil.showToast("该用户不存在", Toast.LENGTH_SHORT);
        }
    };

	@Click(viewId = R.id.btnSendMsg)
	public void onClick(View v) {
	    if(subType == null){
            Utils.start_Activity(this, AddFriendFinalActivity.class);
        }else {
            Utils.start_Activity(this, ChatActivity.class);
        }
	}

}
