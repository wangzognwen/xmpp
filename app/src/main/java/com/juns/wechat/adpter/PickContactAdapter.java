package com.juns.wechat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.PingYinUtil;
import com.juns.wechat.common.PinyinComparator;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.util.ImageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
public class PickContactAdapter extends BaseAdapter implements SectionIndexer{
    private Context mContext;
    private boolean[] isCheckedArray;
    private List<FriendBean> list = new ArrayList<>();
    private List<FriendBean> selectedUsers;

    public PickContactAdapter(Context mContext, List<FriendBean> friends) {
        this.mContext = mContext;
        this.list = friends;
        if(friends != null){
            isCheckedArray = new boolean[list.size()];
            for(int i = 0 ; i < isCheckedArray.length; i++){
                isCheckedArray[i] = false;
            }
            // 排序(实现了中英文混排)
            Collections.sort(list, new PinyinComparator());
        }
        selectedUsers = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(list == null) return convertView;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
        }

        final FriendBean friendBean = list.get(position);
        ImageView ivAvatar = ViewHolder.get(convertView,
                R.id.contactitem_avatar_iv);
        TextView tvCatalog = ViewHolder.get(convertView,
                R.id.contactitem_catalog);
        TextView tvNick = ViewHolder
                .get(convertView, R.id.contactitem_nick);
        final CheckBox checkBox = ViewHolder
                .get(convertView, R.id.checkbox);
        checkBox.setVisibility(View.VISIBLE);
        String catalog = PingYinUtil.converterToFirstSpell(
                friendBean.getShowName()).substring(0, 1);
        if (position == 0) {
            tvCatalog.setVisibility(View.VISIBLE);
            tvCatalog.setText(catalog);
        } else {
            FriendBean nextFriend = list.get(position - 1);
            String lastCatalog = PingYinUtil.converterToFirstSpell(
                    nextFriend.getShowName()).substring(0, 1);
            if (catalog.equals(lastCatalog)) {
                tvCatalog.setVisibility(View.GONE);
            } else {
                tvCatalog.setVisibility(View.VISIBLE);
                tvCatalog.setText(catalog);
            }
        }
        ImageUtil.loadImage(ivAvatar, friendBean.getContactUser().getHeadUrl());
        tvNick.setText(friendBean.getShowName());

        checkBox.setChecked(isCheckedArray[position]);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedArray[position] = isChecked;
                if(isChecked){
                    selectedUsers.add(friendBean);
                }else {
                    selectedUsers.remove(friendBean.getContactName());
                }
            }
        });

        return convertView;
    }

    public List<FriendBean> getSelectedUsers(){
        return selectedUsers;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < list.size(); i++) {
            FriendBean friendBean = list.get(i);
            String l = PingYinUtil
                    .converterToFirstSpell(friendBean.getShowName()).substring(0,
                            1);
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
