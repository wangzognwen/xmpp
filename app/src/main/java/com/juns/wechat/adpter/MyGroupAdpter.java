package com.juns.wechat.adpter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.juns.wechat.R;

public class MyGroupAdpter extends BaseAdapter {
	protected Context context;
	private List grouplist;

	public MyGroupAdpter(Context ctx, List grouplist) {
		context = ctx;
		this.grouplist = grouplist;
	}

	@Override
	public int getCount() {
		return grouplist.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_item_mygroup, parent, false);
		}
		/*final EMGroup group = grouplist.get(position);
		ImageView img_avar = ViewHolder.get(convertView, R.id.img_photo);
		TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
		img_avar.setImageResource(R.drawable.defult_group);
		txt_name.setText(group.getGroupName());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra(Constants.NAME, group.getGroupName());
				intent.putExtra(Constants.COLUMN_TYPE, ChatActivity.CHATTYPE_GROUP);
				intent.putExtra(Constants.GROUP_ID, group.getGroupId());
				context.startActivity(intent);
			}
		});*/
		return convertView;
	}
}
