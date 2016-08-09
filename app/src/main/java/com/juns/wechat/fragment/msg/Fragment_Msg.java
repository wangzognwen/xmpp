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
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.common.BaseFragment;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;

import org.simple.eventbus.Subscriber;

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
    private ConversationAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			parentActivity = (MainActivity) getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_msg, null);
			lvContact = (ListView) layout.findViewById(R.id.lvNewFriends);
            mAdapter = new ConversationAdapter(getActivity());
            lvContact.setAdapter(mAdapter);
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
        }else {
            msgItemShowList.clear();
        }
        for(MsgItem msgItem : msgItems){
            MsgItemShow msgItemShow = new ChatMsgItemShow(getActivity(), msgItem);
            msgItemShowList.add(msgItemShow);
        }
        mAdapter.setData(msgItemShowList);
    }

    @Subscriber
    private void onMessageDataChanged(DbDataEvent<MessageBean> event){
        initData(); //重新加载数据
    }

    @Subscriber
    private void onUserDataChanged(DbDataEvent<MessageBean> event){
        if(event.action >= DbDataEvent.UPDATE_ONE && event.action <= DbDataEvent.REPLACE_MANY){
            initData(); //重新加载数据
        }
    }

    @Override
    protected boolean registerEventBus() {
        return true;
    }
}