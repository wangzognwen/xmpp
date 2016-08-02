package com.juns.wechat.xmpp.process;

import android.content.Context;

import com.coco.yunpian.app.R;
import com.coco.yunpian.app.entity.chat.MessageEntity;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：文本消息处理类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/27
 *******************************************************/
public class ActiveIntroMessageProcess extends MessageProcess {

    public ActiveIntroMessageProcess(Context context) {
        super(context);
    }

    @Override
    public void processedMessage(MessageEntity entity) {
        if(isMsgExist(entity.getPacketId())){
            return;
        }
        saveMessageToDB(entity);
        ringDone();
        noticeShow(entity, mContext.getResources().getString(R.string.msg_intro));
    }
}
