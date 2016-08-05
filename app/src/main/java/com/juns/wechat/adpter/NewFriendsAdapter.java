package com.juns.wechat.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.activity.UserInfoActivity;

import java.util.List;

public class NewFriendsAdapter extends BaseAdapter {
	protected Context context;
    private List<MessageBean> inviteMessages;

	public NewFriendsAdapter(Context ctx, List<MessageBean> inviteMessages) {
		context = ctx;
        this.inviteMessages = inviteMessages;
	}

	@Override
	public int getCount() {
		if(inviteMessages == null){
            return 0;
        }
        return inviteMessages.size();
	}

	@Override
	public Object getItem(int position) {
		if(inviteMessages == null){
            return null;
        }
        return inviteMessages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_item_newfriend, parent, false);
		}
		ImageView img_avar = ViewHolder.get(convertView, R.id.ivAvatar);
		TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
		final TextView txt_add = ViewHolder.get(convertView, R.id.txt_add);
		final UserBean user = GloableParams.UserInfos.get(position);
		txt_name.setText(user.getUserName());
		txt_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_add.setTextColor(context.getResources().getColor(
						R.color.black1));
				txt_add.setBackgroundResource(R.drawable.btn_bg_gray1);
				txt_add.setText("已添加");
				Utils.showLongToast(context, "添加好友成功，等待对方同意");
			/*	try {
					EMContactManager.getDbManager().addContact(
							user.getTelephone(), "请求添加你为朋友");
				} catch (EaseMobException e) {
					e.printStackTrace();
				}*/
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserInfoActivity.class);
				intent.putExtra(Constants.NAME, user.getUserName());
				intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
				intent.putExtra(Constants.User_ID, user.getTelephone());
				context.startActivity(intent);
			}
		});
		return convertView;
	}
}
