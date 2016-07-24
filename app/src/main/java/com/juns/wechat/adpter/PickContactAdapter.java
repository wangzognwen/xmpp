package com.juns.wechat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.juns.wechat.bean.UserBean;

import java.util.List;

/**
 * Created by 王者 on 2016/7/24.
 */
public class PickContactAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private boolean[] isCheckedArray;

    private List<UserBean> userBeen;

    public PickContactAdapter(Context context, List<UserBean> userBeen) {
        layoutInflater = LayoutInflater.from(context);
        this.userBeen = userBeen;
        isCheckedArray = new boolean[userBeen.size()];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        convertView = layoutInflater.inflate(res, null);

        ImageView iv_avatar = (ImageView) convertView
                .findViewById(R.id.iv_avatar);
        TextView tv_name = (TextView) convertView
                .findViewById(R.id.tv_name);
        TextView tvHeader = (TextView) convertView
                .findViewById(R.id.header);
        final User user = list.get(position);

        final String avater = user.getAvatar();
        String name = user.getNick();
        String header = user.getHeader();
        final String username = user.getUsername();
        tv_name.setText(name);
        iv_avatar.setImageResource(R.drawable.default_useravatar);
        iv_avatar.setTag(avater);
        Bitmap bitmap = null;
        if (avater != null && !avater.equals("")) {
            bitmap = avatarLoader.loadImage(iv_avatar, avater,
                    new ImageDownloadedCallBack() {

                        @Override
                        public void onImageDownloaded(ImageView imageView,
                                                      Bitmap bitmap) {
                            if (imageView.getTag() == avater) {
                                imageView.setImageBitmap(bitmap);

                            }
                        }

                    });

            if (bitmap != null) {

                iv_avatar.setImageBitmap(bitmap);

            }

            bitmaps[position] = bitmap;

        }
        if (position == 0 || header != null
                && !header.equals(getItem(position - 1))) {
            if ("".equals(header)) {
                tvHeader.setVisibility(View.GONE);
            } else {
                tvHeader.setVisibility(View.VISIBLE);
                tvHeader.setText(header);
            }
        } else {
            tvHeader.setVisibility(View.GONE);
        }

        // 选择框checkbox
        final CheckBox checkBox = (CheckBox) convertView
                .findViewById(R.id.checkbox);

        if (exitingMembers != null && exitingMembers.contains(username)) {
            checkBox.setButtonDrawable(R.drawable.btn_check);
        } else {
            checkBox.setButtonDrawable(R.drawable.check_blue);
        }

        if (addList != null && addList.contains(username)) {
            checkBox.setChecked(true);
            isCheckedArray[position] = true;
        }
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // 群组中原来的成员一直设为选中状态
                    if (exitingMembers.contains(username)) {
                        isChecked = true;
                        checkBox.setChecked(true);
                    }
                    isCheckedArray[position] = isChecked;
                    // 如果是单选模式
                    if (isSignleChecked && isChecked) {
                        for (int i = 0; i < isCheckedArray.length; i++) {
                            if (i != position) {
                                isCheckedArray[i] = false;
                            }
                        }
                        contactAdapter.notifyDataSetChanged();
                    }

                    if (isChecked) {
                        // 选中用户显示在滑动栏显示
                        showCheckImage(contactAdapter.getBitmap(position),
                                list.get(position));

                    } else {
                        // 用户显示在滑动栏删除
                        deleteImage(list.get(position));

                    }

                }
            });
            // 群组中原来的成员一直设为选中状态
            if (exitingMembers.contains(username)) {
                checkBox.setChecked(true);
                isCheckedArray[position] = true;
            } else {
                checkBox.setChecked(isCheckedArray[position]);
            }

        }
        return convertView;
    }

    @Override
    public int getCount() {
        if(userBeen == null)
            return 0;
        return userBeen.size();
    }

    @Override
    public Object getItem(int position) {
        if(userBeen == null)
            return 0;

        String header = list.get(position).getHeader();

        return header;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
