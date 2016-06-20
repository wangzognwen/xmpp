package com.juns.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.juns.wechat.common.Utils;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.view.activity.LoginActivity;

public class SplashActivity extends Activity {
	private boolean isLogin; //用户是否已经登录
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		isLogin = UserManager.getInstance().isLogin();
		if (isLogin) {
			pageToHome();
		} else {
			pageToLogin();
		}
	}

	private void pageToHome() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
                finish();
			}
		}, 600);
	}

	private void pageToLogin() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		}, 600);
	}

}
