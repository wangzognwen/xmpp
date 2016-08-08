package com.juns.wechat.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.adpter.ContactAdapter;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.activity.UserInfoActivity;
import com.juns.wechat.view.activity.GroupListActivity;
import com.juns.wechat.view.activity.NewFriendsListActivity;
import com.juns.wechat.view.activity.PublishUserListActivity;
import com.juns.wechat.widget.SideBar;

import java.util.List;

//通讯录

public class Fragment_Friends extends Fragment implements OnClickListener,
		OnItemClickListener {
	private View layout, layout_head;
	private ListView lvContact;
	private SideBar indexBar;
	private TextView tvDialog;
    private FriendDao rosterDao = FriendDao.getInstance();
    private List<FriendBean> rosterBeans;
    private ContactAdapter contactAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_friends, container, false);
        initViews();
        initData();
        setOnListener();
		return layout;
	}

	private void initViews() {
		lvContact = (ListView) layout.findViewById(R.id.lvContact);
        contactAdapter = new ContactAdapter(getActivity());
        lvContact.setAdapter(contactAdapter);

		tvDialog = (TextView) layout.findViewById(R.id.tvDialog);
		indexBar = (SideBar) layout.findViewById(R.id.sideBar);
		indexBar.setListView(lvContact);
		indexBar.setTextView(tvDialog);

		layout_head = getActivity().getLayoutInflater().inflate(
				R.layout.layout_head_friend, null);
		lvContact.addHeaderView(layout_head);
	}

	private void initData() {
        String ownerName = AccountManager.getInstance().getUser().getUserName();
	    rosterBeans = rosterDao.queryAllByOwner(ownerName);
        contactAdapter.setData(rosterBeans);
	}

	private void setOnListener() {
		lvContact.setOnItemClickListener(this);
		layout_head.findViewById(R.id.re_newfriends)
				.setOnClickListener(this);
		layout_head.findViewById(R.id.re_chatroom).setOnClickListener(this);
		layout_head.findViewById(R.id.re_public).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_newfriends:// 添加好友
			CommonUtil.startActivity(getActivity(), NewFriendsListActivity.class);
			break;
		case R.id.re_chatroom:// 群聊
			CommonUtil.startActivity(getActivity(), GroupListActivity.class);
			break;
		case R.id.re_public:// 公众号
			CommonUtil.startActivity(getActivity(), PublishUserListActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		FriendBean rosterBean = rosterBeans.get(position - 1);

        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.ARG_USER_NAME, rosterBean.getContactName());

        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);

	}
}
