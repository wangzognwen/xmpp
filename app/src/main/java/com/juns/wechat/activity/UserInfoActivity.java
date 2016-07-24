package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.Utils;

/**
 * 用户资料
 */
@Content(R.layout.activity_user_info)
public class UserInfoActivity extends ToolbarActivity implements OnClickListener {
	private TextView tv_name, tv_accout;
	private String Name, UserId;
	private Button btn_sendmsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setToolbarRight(2, R.drawable.icon_more);
        initView();
        initData();
        setListener();
	}

	protected void initView() {
        btn_sendmsg = (Button) findViewById(R.id.btn_sendmsg);
        btn_sendmsg.setTag("1");
        tv_name = (TextView) findViewById(R.id.tvName);
        tv_accout = (TextView) findViewById(R.id.tv_fxid);
	}


	protected void initData() {
		Name = getIntent().getStringExtra(Constants.NAME);
        tv_name.setText(Name);
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
				intent.putExtra(Constants.NAME, Name);
				intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
				intent.putExtra(Constants.User_ID, UserId);
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

}
