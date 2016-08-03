package com.juns.wechat.xmpp.util;


import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.Msg;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.xmpp.extensionelement.TimeElement;

import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/2.
 */
public class ConvertUtil {

    public static MessageBean convertToMessageBean(Message message){
        MessageBean messageBean = new MessageBean();
        messageBean.setMyselfName(ConfigUtil.getUserName(message.getFrom()));
        messageBean.setOtherName(ConfigUtil.getUserName(message.getTo()));
        TimeElement time = TimeElement.from(message);
        messageBean.setDate(time.getTime());
        messageBean.setDirection(MessageBean.Direction.INCOMING.value);

        try {
            JSONObject jsonObject = new JSONObject(message.getBody());
            String msg = jsonObject.optString(MessageBean.MSG);
            int type = jsonObject.optInt(MessageBean.TYPE);
            String typeDesc = jsonObject.optString(MessageBean.TYPE_DESC);
            messageBean.setMsg(msg);
            messageBean.setType(type);
            messageBean.setTypeDesc(typeDesc);

            messageBean.setMsgObj(Msg.fromJson(msg, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBean;
    }
}
