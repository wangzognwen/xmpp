package com.juns.wechat;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.activity.AddFriendActivity;
import com.juns.wechat.adpter.MainAdapter;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.common.BaseActivity;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.dialog.WarnTipDialog;
import com.juns.wechat.dialog.TitleMenu.ActionItem;
import com.juns.wechat.dialog.TitleMenu.TitlePopup;
import com.juns.wechat.dialog.TitleMenu.TitlePopup.OnItemOnClickListener;
import com.juns.wechat.service.XmppService;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.view.activity.AddGroupChatActivity;
import com.juns.wechat.view.activity.GetMoneyActivity;
import com.juns.wechat.view.activity.LoginActivity;
import com.juns.wechat.zxing.CaptureActivity;

@Content(R.layout.activity_main)
public class MainActivity extends BaseActivity{
	private TextView txt_title;
	private ImageView img_right;
	private WarnTipDialog Tipdialog;
	protected static final String TAG = "MainActivity";
    private ViewPager vpMainContent;
	private TitlePopup titlePopup;
	private TextView unreaMsgdLabel;// 未读消息textview
	private TextView unreadAddressLable;// 未读通讯录textview
	private TextView unreadFindLable;// 发现
	private ImageView[] imagebuttons;
	private TextView[] textviews;
	private int index;
    private static MainActivity mInstance;

	private MainAdapter mainAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mInstance = this;
		initView();
		setOnClickListener();
		initPopWindow();
        XmppService.login(this);
	}

	private void initView() {
		vpMainContent = (ViewPager) findViewById(R.id.vp_main_content);
		mainAdapter = new MainAdapter(getSupportFragmentManager());
		vpMainContent.setAdapter(mainAdapter);
        vpMainContent.setOnPageChangeListener(pageChangeListener);

		txt_title = (TextView) findViewById(R.id.txt_title);
		img_right = (ImageView) findViewById(R.id.img_right);
		img_right.setVisibility(View.VISIBLE);
		img_right.setImageResource(R.drawable.icon_add);

		unreaMsgdLabel = (TextView) findViewById(R.id.tv_unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_contact_number);
		unreadFindLable = (TextView) findViewById(R.id.unread_find_number);

		imagebuttons = new ImageView[4];
		imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
		imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
		imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);
		imagebuttons[3] = (ImageView) findViewById(R.id.ib_profile);

		textviews = new TextView[4];
		textviews[0] = (TextView) findViewById(R.id.tv_weixin);
		textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
		textviews[2] = (TextView) findViewById(R.id.tv_find);
		textviews[3] = (TextView) findViewById(R.id.tv_profile);

        setSelectedIndex(0);
	}

	public void onTabClicked(View view) {
		img_right.setVisibility(View.GONE);
		switch (view.getId()) {
            case R.id.re_weixin:
                index = 0;
                break;
            case R.id.re_contact_list:
                index = 1;
                break;
            case R.id.re_find:
                index = 2;
                break;
            case R.id.re_profile:
                index = 3;
                break;
		}
		setSelectedIndex(index);
        vpMainContent.setCurrentItem(index, false);
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onItemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat,
                R.drawable.icon_menu_group));
		titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend,
                R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
                R.drawable.icon_menu_sao));
		titlePopup.addAction(new ActionItem(this, R.string.menu_money,
				R.drawable.abv));
	}

    private void setSelectedIndex(int index){
        for(int i = 0 ; i < textviews.length ; i++){
            if(i != index){
                textviews[i].setTextColor(getResources().getColor(R.color.app_color_gray));
                imagebuttons[i].setSelected(false);
            }else {
                textviews[i].setTextColor(getResources().getColor(R.color.app_color_green));
                imagebuttons[i].setSelected(true);
            }
        }

        img_right.setVisibility(View.GONE);
        switch (index) {
            case 0:
                img_right.setVisibility(View.VISIBLE);
                txt_title.setText(R.string.app_name);
                img_right.setImageResource(R.drawable.icon_add);
                break;
            case 1:
                txt_title.setText(R.string.contacts);
                img_right.setVisibility(View.VISIBLE);
                img_right.setImageResource(R.drawable.icon_titleaddfriend);
                break;
            case 2:
                txt_title.setText(R.string.discover);
                break;
            case 3:
                txt_title.setText(R.string.me);
                break;
        }
    }

    public static void logout(){
        Intent service = new Intent(mInstance, XmppService.class);
        mInstance.stopService(service);

        Intent intent  = new Intent(mInstance, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mInstance.startActivity(intent);
        mInstance.finish();
    }

    public void setUnreadMsgLabel(int viewId, int unreadNum){
        TextView textView;
        if(viewId == unreaMsgdLabel.getId()){
            textView = unreaMsgdLabel;
        }else if(viewId == unreadAddressLable.getId()){
            textView = unreadAddressLable;
        }else if(viewId == unreadFindLable.getId()){
            textView = unreadFindLable;
        }else {
            return;
        }

        if(unreadNum > 0){
            textView.setText(unreadNum + "");
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.GONE);
        }
    }

	private OnItemOnClickListener onItemClick = new OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 发起群聊
				CommonUtil.startActivity(MainActivity.this,
						AddGroupChatActivity.class);
				break;
			case 1:// 添加朋友
				CommonUtil.startActivity(MainActivity.this, AddFriendActivity.class);
				break;
			case 2:// 扫一扫
				CommonUtil.startActivity(MainActivity.this, CaptureActivity.class);
				break;
			case 3:// 收钱
				CommonUtil.startActivity(MainActivity.this, GetMoneyActivity.class);
				break;
			default:
				break;
			}
		}
	};

	private void setOnClickListener() {
		img_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				titlePopup.show(findViewById(R.id.layout_bar));
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
		}
		return super.onKeyDown(keyCode, event);
	}

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setSelectedIndex(position);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstance = null;
    }
}