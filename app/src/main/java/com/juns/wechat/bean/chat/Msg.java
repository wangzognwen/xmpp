package com.juns.wechat.bean.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juns.wechat.config.MsgType;

/**
 * Created by 王者 on 2016/8/3.
 */
public abstract class Msg {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    public String toJson(){
        return gson.toJson(this);
    }

    public  String toSendJson(){
        return toJson();
    }

    public static Msg fromJson(String json, int type){
        Msg msg = null;
        switch (type){
            case MsgType.MSG_TYPE_SEND_INVITE:
                msg = gson.fromJson(json, InviteMsg.class);
                break;
            default:
                break;
        }
        return msg;
    }
}
