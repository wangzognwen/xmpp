package com.juns.wechat.bean.chat.viewmodel;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.PictureMsg;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.PhotoUtil;
import com.juns.wechat.util.TimeUtil;


/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：文本消息的viewModel
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/30
 *******************************************************/
public class PictureMsgViewModel extends MsgViewModel implements View.OnClickListener, View.OnLongClickListener {
    private final Integer[] mResIds = {R.layout.chat_item_received_picture, R.layout.chat_item_sent_picture};

    private PictureMsg pictureMsg;

    private long beginMicroSec;

    public PictureMsgViewModel(Context context, MessageBean messageBean) {
        super(context, messageBean);
        pictureMsg = (PictureMsg) messageBean.getMsgObj();
    }

    @Override
    public View fillView(View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            int resId = !isShowMyself() ? mResIds[0] : mResIds[1];
            convertView =  mInflater.inflate(resId, viewGroup, false);
        }

        TextView tvDate = ViewHolder.get(convertView, R.id.tv_date);
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
        ImageView ivSendPicture = (ImageView) convertView.findViewById(R.id.iv_sendPicture);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        TextView tvPercent = (TextView) convertView.findViewById(R.id.percentage);


        if(isShowTime()){
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(TimeUtil.getRecentTime(messageBean.getDate()));
        }else {
            tvDate.setVisibility(View.GONE);
        }

        if(isShowMyself()){
            loadUrl(ivAvatar, myselfAvatar);
            ImageLoader.loadLocalImage(ivSendPicture, PhotoUtil.PHOTO_PATH + "/" + pictureMsg.imgName);
            ImageView ivSendState = ViewHolder.get(convertView, R.id.iv_send_failed);
            if(messageBean.getState() == MessageBean.State.SEND_FAILED.value){
                ivSendState.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tvPercent.setVisibility(View.GONE);
            }else if(messageBean.getState() == MessageBean.State.SEND_SUCCESS.value){
                ivSendState.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                tvPercent.setVisibility(View.GONE);
            } else {
                ivSendState.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tvPercent.setText(pictureMsg.progress + "%");
            }

        }else {
            loadUrl(ivAvatar, otherAvatar);
            ivAvatar.setOnClickListener(this);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        if(isShowMyself()){
            super.onUserPhotoClick(myselfName);
        }else {
            super.onUserPhotoClick(otherName);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        popupShow();
        return true;
    }

    /**
     * 触发弹出显示
     */
    private void popupShow(){

    }

    @Override
    public int getType() {
        return MsgType.MSG_TYPE_PICTURE;
    }
}
