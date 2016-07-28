package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ImageUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * 用户资料
 */
@Content(R.layout.activity_user_info)
public class UserInfoActivity extends ToolbarActivity implements OnClickListener {
    public static final String ARG_USER_NAME = "user_name";
    @Id
	private TextView tvNickName;
    @Id
    private TextView tvUserName;
    @Extra(name = ARG_USER_NAME)
	private String userName; //好友或者陌生人的userName
    @Id
	private Button btn_sendmsg;
    @Id
    private ImageView ivAvatar;

    private UserBean ownerUser = AccountManager.getInstance().getUser();
    private FriendBean friendBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setToolbarRight(2, R.drawable.icon_more);
        initData();
        setListener();
        EventBus.getDefault().register(this);
	}

	protected void initData() {
		friendBean = FriendDao.getInstance().findByOwnerAndContactName(ownerUser.getUserName(), userName);
        tvUserName.setText(userName);
        if(friendBean != null){
            if(friendBean.getSubType().equals("from") || friendBean.getSubType().equals("both")){
                tvNickName.setText(friendBean.getShowName());
                ImageUtil.loadImage(ivAvatar, friendBean.getHeadUrl());
            }else {
                throw new RuntimeException("该好友存在，但两人关系异常");
            }
        }
	}

    /**
     * 好友信息改变
     * @param event
     */
    @Subscriber
    private void onUserDbDataChanged(DbDataEvent<UserBean> event){
        UserBean userBean = event.data;
        if(userBean.getUserName().equals(userName)){  //如果好友的信息更新了
            initData();
        }
    }

	protected void setListener() {
		btn_sendmsg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(UserInfoActivity.this);
			break;
		case R.id.img_right:

			break;
		case R.id.btn_sendmsg:
			if ("1".equals(v.getTag().toString())) {
				Intent intent = new Intent(this, ChatActivity.class);
				intent.putExtra(Constants.NAME, userName);
				intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {

			}
			break;
		default:
			break;
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
