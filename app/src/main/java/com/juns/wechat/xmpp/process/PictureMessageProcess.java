package com.juns.wechat.xmpp.process;

import android.content.Context;

import com.coco.yunpian.app.R;
import com.coco.yunpian.app.dynamic.http.json.JsonUtil;
import com.coco.yunpian.app.entity.chat.MessageEntity;
import com.coco.yunpian.app.entity.chat.PictureMsg;
import com.coco.yunpian.app.xmpp.service.DownLoad;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：图片消息处理类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/30
 *******************************************************/
public class PictureMessageProcess extends MessageProcess implements DownLoad.DownLoadOver {
    public PictureMessageProcess(Context context) {
        super(context);
    }

    private MessageEntity mEntity;

    @Override
    public void processedMessage(MessageEntity entity) {
        if(isMsgExist(entity.getPacketId())) return;
        try {
            mEntity = entity;
            PictureMsg pictureMsg = JsonUtil.fromJson(entity.getMsg(), PictureMsg.class);
            DownLoad downLoad = new DownLoad(pictureMsg.fileUrl, pictureMsg.fileName, this);
            downLoad.startDownLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownLoaded() {
        noticeShow(mEntity, mContext.getResources().getString(R.string.msg_picture));
        ringDone();
        saveMessageToDB(mEntity);
    }
}
