package com.juns.wechat.xmpp.listener;


import android.text.TextUtils;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.xmpp.iq.IQUserInfo;
import com.juns.wechat.xmpp.provider.UserInfoProvider;
import com.juns.wechat.xmpp.util.UserInfoManager;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.StreamError;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public class XmppConnectionListener implements ConnectionListener {
    @Override
    public void connected(XMPPConnection connection) {
        LogUtil.i("connected!");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        initInfo();
    }

    @Override
    public void connectionClosed() {
        LogUtil.i("connectionClosed!");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        if(e != null && !TextUtils.isEmpty(e.getMessage())){
            LogUtil.i("connectionClose!OnError : " + e.getMessage());
        }
        if(e instanceof XMPPException.StreamErrorException){
            XMPPException.StreamErrorException exception = (XMPPException.StreamErrorException) e;
            StreamError streamError = exception.getStreamError();
            StreamError.Condition condition = streamError.getCondition();
            if(condition.toString().equals("conflict")){

            }
        }
    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    private void initInfo(){
        UserInfoManager.queryUserInfo(null); //拉取个人信息mk
    }
}
