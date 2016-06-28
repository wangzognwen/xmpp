package com.juns.wechat.chat;

import android.app.NotificationManager;
import android.content.Context;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.juns.wechat.util.ToolBarUtil;


public class BaseActivity extends AppCompatActivity {
	private static final int notifiId = 11;
	protected NotificationManager notificationManager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}


	public void back(View view) {
		finish();
	}

}
