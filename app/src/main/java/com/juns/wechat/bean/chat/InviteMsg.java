package com.juns.wechat.bean.chat;


import org.json.JSONException;
import org.json.JSONObject;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：普通的文本消息的msg字段转换后对应的类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/24
 *******************************************************/
public class InviteMsg extends Msg{
    public static final String USERNAME = "userName";
    public static final String REASON = "reason";
    public static final String REPLY = "reply";

    public String name;
    public String reason;
    public int reply;

    @Override
    public String toSendJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(USERNAME, name);
            jsonObject.put(REASON, reason);
            if(reply != 0){
                jsonObject.put(REPLY, reply);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public enum Reply{
        ACCEPT(1), REJECT(2);
        private int value;

        Reply(int value){
            this.value = value;
        }
    }
}
