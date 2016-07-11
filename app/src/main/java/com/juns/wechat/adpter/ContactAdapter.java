package com.juns.wechat.adpter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.RosterBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.PingYinUtil;
import com.juns.wechat.common.PinyinComparator;
import com.juns.wechat.common.ViewHolder;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private List<RosterBean> userInfos = new ArrayList<>();// 好友信息

    public ContactAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<RosterBean> userInfos){
        this.userInfos = userInfos;
        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
        if(userInfos == null){
            return 0;
        }
		return userInfos.size();
	}

	@Override
	public Object getItem(int position) {
		if(userInfos == null){
            return null;
        }
        return userInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RosterBean rosterBean = userInfos.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.contact_item, parent, false);

		}
		ImageView ivAvatar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView tvCatalog = ViewHolder.get(convertView,
				R.id.contactitem_catalog);
		TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
		String catalog = PingYinUtil.converterToFirstSpell(rosterBean.getContactName())
				.substring(0, 1);
		if (position == 0) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(catalog);
		} else {
			RosterBean prevRosterBean = userInfos.get(position - 1);
			String lastCatalog = PingYinUtil.converterToFirstSpell(
                    prevRosterBean.getContactName()).substring(0, 1);
			if (catalog.equals(lastCatalog)) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			}
		}

		ivAvatar.setImageResource(R.drawable.head);
		tvNick.setText(rosterBean.getContactName());
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < userInfos.size(); i++) {
			RosterBean rosterBean = userInfos.get(i);
			String l = PingYinUtil.converterToFirstSpell(rosterBean.getContactName())
					.substring(0, 1);
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
