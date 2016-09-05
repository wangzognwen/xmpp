package com.juns.wechat.bean.chat.viewmodel;

import android.content.Context;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.TextMsg;
import com.juns.wechat.chat.utils.SmileUtils;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.util.TimeUtil;


/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：文本消息的viewModel
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/30
 *******************************************************/
public class VoiceMsgViewModel extends MsgViewModel implements View.OnClickListener, View.OnLongClickListener {
    private final Integer[] mResIds = {R.layout.row_received_voice, R.layout.row_sent_voice};

    private TextMsg textMsg;

    private long beginMicroSec;

    public VoiceMsgViewModel(Context context, MessageBean messageBean) {
        super(context, messageBean);
        textMsg = (TextMsg) messageBean.getMsgObj();
    }

    @Override
    public View fillView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            int resId = !isShowMyself() ? mResIds[0] : mResIds[1];
            convertView =  mInflater.inflate(resId, viewGroup, false);
        }

        TextView tvDate = ViewHolder.get(convertView, R.id.tv_date);
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);

        if(isShowTime()){
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(TimeUtil.getRecentTime(messageBean.getDate()));
        }else {
            tvDate.setVisibility(View.GONE);
        }

        if(isShowMyself()){
            loadUrl(ivAvatar, myselfAvatar);
            ImageView ivSendState = ViewHolder.get(convertView, R.id.iv_send_failed);
            ProgressBar sendingProgress = ViewHolder.get(convertView, R.id.pb_sending);
            if(messageBean.getState() == MessageBean.State.SEND_FAILED.value){
                ivSendState.setVisibility(View.VISIBLE);
                sendingProgress.setVisibility(View.GONE);
            }else if(messageBean.getState() == MessageBean.State.SEND_SUCCESS.value){
                ivSendState.setVisibility(View.GONE);
                sendingProgress.setVisibility(View.GONE);
            } else {
                ivSendState.setVisibility(View.GONE);
                sendingProgress.setVisibility(View.VISIBLE);
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
        return MsgType.MSG_TYPE_TEXT;
    }
}
