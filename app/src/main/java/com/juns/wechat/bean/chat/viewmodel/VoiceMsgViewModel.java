package com.juns.wechat.bean.chat.viewmodel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.activity.ChatMediaPlayer;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.VoiceMsg;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.util.AudioManager;
import com.juns.wechat.util.LogUtil;
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

    private VoiceMsg voiceMsg;
    private ImageView ivVoice; //声音图标

    public VoiceMsgViewModel(Context context, MessageBean messageBean) {
        super(context, messageBean);
        voiceMsg = (VoiceMsg) messageBean.getMsgObj();
    }

    @Override
    public View fillView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            int resId = !isShowMyself() ? mResIds[0] : mResIds[1];
            convertView =  mInflater.inflate(resId, viewGroup, false);
        }

        LogUtil.i("start initView!");

        TextView tvDate = ViewHolder.get(convertView, R.id.tv_date);
        ImageView ivAvatar = ViewHolder.get(convertView, R.id.iv_avatar);
        RelativeLayout rlVoiceContaincer = ViewHolder.get(convertView, R.id.rl_voice);
        ivVoice = ViewHolder.get(convertView, R.id.iv_voice);

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
        }

        ivAvatar.setOnClickListener(this);
        rlVoiceContaincer.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_avatar){
            if(isShowMyself()){
                super.onUserPhotoClick(myselfName);
            }else {
                super.onUserPhotoClick(otherName);
            }
        }else if(v.getId() == R.id.rl_voice){
            ChatMediaPlayer chatMediaPlayer = ChatMediaPlayer.getInstance();
            chatMediaPlayer.setVoiceView(ivVoice);
            chatMediaPlayer.setVoiceDir(isShowMyself());
            chatMediaPlayer.playVoice(AudioManager.RECORD_PATH + "/" + voiceMsg.fileName);
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
        return MsgType.MSG_TYPE_VOICE;
    }
}
