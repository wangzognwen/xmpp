package com.juns.wechat.fragment.msg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.adpter.ConversationAdapter;
import com.juns.wechat.adpter.NewMsgAdpter;
import com.juns.wechat.bean.PublicMsgInfo;
import com.juns.wechat.common.BaseFragment;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;

//消息
public class Fragment_Msg extends BaseFragment{
	private Activity ctx;
	private View layout, layout_head;
	public RelativeLayout errorItem;
	public TextView errorText;
	private ListView lvContact;
	private NewMsgAdpter adpter;
	private List conversationList = new ArrayList<>();
	private MainActivity parentActivity;
    private String account = AccountManager.getInstance().getUserName();
    private List<MsgItemShow> msgItemShowList;
    private ConversationAdapter mAapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			parentActivity = (MainActivity) getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_msg,
					null);
			lvContact = (ListView) layout.findViewById(R.id.lvNewFriends);
            mAapter = new ConversationAdapter(getActivity());
            lvContact.setAdapter(mAapter);
			errorItem = (RelativeLayout) layout
					.findViewById(R.id.rl_error_item);
			errorText = (TextView) errorItem
					.findViewById(R.id.tv_connect_errormsg);

            initData();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

    private void initData(){
        List<MsgItem> msgItems = MessageDao.getInstance().getLastMessageWithEveryFriend(account);
        if(msgItems == null || msgItems.isEmpty()){
            layout.findViewById(R.id.txt_nochat).setVisibility(View.VISIBLE);
        }else {
            layout.findViewById(R.id.txt_nochat).setVisibility(View.GONE);
            assembleData(msgItems);
        }
    }

    /**
     * 组装数据
     * @param msgItems
     */
    private void assembleData(List<MsgItem> msgItems){
        if(msgItemShowList == null){
            msgItemShowList = new ArrayList<>();
        }
        for(MsgItem msgItem : msgItems){
            MsgItemShow msgItemShow = new ChatMsgItemShow(msgItem);
            msgItemShowList.add(msgItemShow);
        }
        mAapter.setData(msgItemShowList);
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

}
