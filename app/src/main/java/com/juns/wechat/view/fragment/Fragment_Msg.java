package com.juns.wechat.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.adpter.NewMsgAdpter;
import com.juns.wechat.bean.PublicMsgInfo;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.common.NetUtil;
import com.juns.wechat.view.activity.PublishMsgListActivity;

//消息
public class Fragment_Msg extends Fragment implements OnClickListener,
		OnItemClickListener {
	private Activity ctx;
	private View layout, layout_head;;
	public RelativeLayout errorItem;
	public TextView errorText;
	private ListView lvContact;
	private NewMsgAdpter adpter;
	private List conversationList = new ArrayList<>();
	private MainActivity parentActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			parentActivity = (MainActivity) getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_msg,
					null);
			lvContact = (ListView) layout.findViewById(R.id.lvNewFriends);
			errorItem = (RelativeLayout) layout
					.findViewById(R.id.rl_error_item);
			errorText = (TextView) errorItem
					.findViewById(R.id.tv_connect_errormsg);
			setOnListener();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
		//conversationList.clear();
		//initViews();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		initViews();
	}

	private void initViews() {
		conversationList.addAll(loadConversationsWithRecentChat());
		if (conversationList != null && conversationList.size() > 0) {
			layout.findViewById(R.id.txt_nochat).setVisibility(View.GONE);
			adpter = new NewMsgAdpter(getActivity(), conversationList);
			// TODO 加载订阅号信息 ，增加一个Item
			// if (GloableParams.isHasPulicMsg) {
			/*EMConversation nee = new EMConversation("100000");
			conversationList.insertOrUpdate(0, nee);*/
			String time = "";
			String content = "";
			time = "下午 02:45";
			content = "[腾讯娱乐] 赵薇炒股日赚74亿";
			PublicMsgInfo msgInfo = new PublicMsgInfo();
			msgInfo.setContent(content);
			msgInfo.setMsg_ID("12");
			msgInfo.setTime(time);
			adpter.setPublicMsg(msgInfo);
			// }
			lvContact.setAdapter(adpter);
		} else {
			layout.findViewById(R.id.txt_nochat).setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获取所有会话
	 *
	 * @return +
	 */
	private List loadConversationsWithRecentChat() {
		/*// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getDbManager().getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.insertOrUpdate(conversation);
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;*/
		return null;
	}

	/**
	 * 根据最后一条消息的时间排序
	 *
	 */
/*	private void sortConversationByLastChatTime(
			List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1,
					final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}*/

	private void setOnListener() {
		lvContact.setOnItemClickListener(this);
		errorItem.setOnClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adpter.PublicMsg != null && position == 0) {
			// 打开订阅号列表页面
			CommonUtil.startActivity(getActivity(), PublishMsgListActivity.class);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_error_item:
			NetUtil.openSetNetWork(getActivity());
			break;
		default:
			break;
		}
	}

}
