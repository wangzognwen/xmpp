package com.juns.wechat.view.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.adpter.PickContactAdapter;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.chat.utils.Constant;
import com.juns.wechat.common.PingYinUtil;
import com.juns.wechat.common.PinyinComparator;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.widget.SideBar;

@Content(R.layout.activity_chatroom)
public class AddGroupChatActivity extends ToolbarActivity{
    @Id
    private TextView tvRightText;
	private ImageView iv_search;
	private TextView tv_header;
	private ListView listView;
	private EditText et_search;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers = new ArrayList<String>();
	private List<FriendBean> myFriends;// 好友列表
	// 可滑动的显示选中用户的View
	private LinearLayout menuLinerLayout;

	// 选中用户总数,右上角显示
	int total = 0;
	private String userId = null;
	private String groupId = null;
	private String groupname;
	// 添加的列表
	private List<String> addList = new ArrayList<String>();
	private String hxid;
	private UserBean user = AccountManager.getInstance().getUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        initControl();
        initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWindowManager.removeView(mDialogText);
	}

	private void initControl() {
        tvRightText.setText("确定");
		menuLinerLayout = (LinearLayout) this
				.findViewById(R.id.linearLayoutMenu);
		et_search = (EditText) this.findViewById(R.id.et_search);
		listView = (ListView) findViewById(R.id.list);
		iv_search = (ImageView) this.findViewById(R.id.iv_search);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(
				R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		indexBar = (SideBar) findViewById(R.id.sideBar);
		indexBar.setListView(listView);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View headerView = layoutInflater.inflate(R.layout.item_chatroom_header,
				null);
		tv_header = (TextView) headerView.findViewById(R.id.tv_header);
		listView.addHeaderView(headerView);
	}

	protected void initData() {
		// 获取好友列表
		myFriends = FriendDao.getInstance().queryAllByOwner(user.getUserName());
		contactAdapter = new PickContactAdapter(AddGroupChatActivity.this,
                myFriends);
		listView.setAdapter(contactAdapter);
	}

	protected void setListener() {
		et_search.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				/*if (s.length() > 0) {
					String str_s = et_search.getText().toString().trim();
					List<FriendBean> users_temp = new ArrayList<>();
					for (FriendBean friendBean : myFriends) {
						String usernick = user.getUserName();
						if (usernick.contains(str_s)) {
							users_temp.add(user);
						}
						contactAdapter = new PickContactAdapter(
								AddGroupChatActivity.this, users_temp);
						listView.setAdapter(contactAdapter);
					}
				} else {
					contactAdapter = new PickContactAdapter(
							AddGroupChatActivity.this, myFriends);
					listView.setAdapter(contactAdapter);
				}*/
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}



	/**
	 * 确认选择的members
	 *
	 */
	public void save() {
		if (addList.size() == 0) {
			Utils.showLongToast(AddGroupChatActivity.this, "请选择用户");
			return;
		}
		// 如果只有一个用户说明只是单聊,并且不是从群组加人
		if (addList.size() == 1 && isCreatingNewGroup) {
			String userId = addList.get(0);
			UserBean user = GloableParams.Users.get(userId);
			Intent intent = new Intent(AddGroupChatActivity.this,
					ChatActivity.class);
			intent.putExtra(Constants.NAME, user.getUserName());
			intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
			intent.putExtra(Constants.User_ID, user.getTelephone());
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		} else {
			if (isCreatingNewGroup) {
				getLoadingDialog("正在创建群聊...").show();
			} else {
				getLoadingDialog("正在加人...").show();
			}
			creatNewGroup(addList);// 创建群组
		}
	}

	// 即时显示被选中用户的头像和昵称。
	private void showCheckImage(Bitmap bitmap, UserBean glufineid) {
		if (exitingMembers.contains(glufineid.getUserName()) && groupId != null) {
			return;
		}
		if (addList.contains(glufineid.getTelephone())) {
			return;
		}
		total++;

		final ImageView imageView = new ImageView(this);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//				DensityUtil.dip2px(this, 40), DensityUtil.dip2px(this, 40));
//		lp.setMargins(0, 0, DensityUtil.dip2px(this, 5), 0);
		//imageView.setLayoutParams(lp);

		// 设置id，方便后面删除
		imageView.setTag(glufineid);
		if (bitmap == null) {
			imageView.setImageResource(R.drawable.head);
		} else {
			imageView.setImageBitmap(bitmap);
		}

		menuLinerLayout.addView(imageView);
		tvRightText.setText("确定(" + total + ")");
		if (total > 0) {
			if (iv_search.getVisibility() == View.VISIBLE) {
				iv_search.setVisibility(View.GONE);
			}
		}
		addList.add(glufineid.getTelephone());
	}

	private void deleteImage(UserBean glufineid) {
		View view = (View) menuLinerLayout.findViewWithTag(glufineid);

		menuLinerLayout.removeView(view);
		total--;
		tvRightText.setText("确定(" + total + ")");
		addList.remove(glufineid.getTelephone());
		if (total < 1) {
			if (iv_search.getVisibility() == View.GONE) {
				iv_search.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 创建新群组
	 * 
	 * @param newmembers
	 */
	String groupName = "";
	String manber = "";

	private void creatNewGroup(final List<String> members) {

	}


}
