package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.util.ToolBarUtil;
import com.juns.wechat.view.BaseActivity;

//好友详情
public class UserInfoActivity extends ToolbarActivity implements OnClickListener {
	private TextView tv_name, tv_accout;
	private String Name, UserId;
	private Button btn_sendmsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendmsg);
        ToolBarUtil.setToolBar(this);
        ToolBarUtil.setToolbarRight(this, 2, R.drawable.icon_more);
	}


	@Override
	protected void initView() {
        btn_sendmsg = (Button) findViewById(R.id.btn_sendmsg);
        btn_sendmsg.setTag("1");
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_accout = (TextView) findViewById(R.id.tv_accout);
	}

	@Override
	protected void initData() {
		Name = getIntent().getStringExtra(Constants.NAME);
        tv_name.setText(Name);
	}

	@Override
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
				intent.putExtra(Constants.NAME, Name);
				intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
				intent.putExtra(Constants.User_ID, UserId);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				// TODO 添加好友
			}
			break;
		default:
			break;
		}
	}

}
