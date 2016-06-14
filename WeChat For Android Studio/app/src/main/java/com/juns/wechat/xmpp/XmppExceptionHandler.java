package com.juns.wechat.xmpp;

import com.juns.wechat.bean.User;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.util.NetWorkUtil;
import com.juns.wechat.util.ThreadPoolUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLError;
import org.jivesoftware.smack.sasl.SASLErrorException;

import java.io.IOException;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：各种XMPP异常的统一处理类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/12/2
 *******************************************************/
public class XmppExceptionHandler {

    public static void handleSmackException(SmackException e){
        e.printStackTrace();
        if(e instanceof SmackException.NoResponseException || e instanceof SmackException.NotConnectedException
                || e instanceof SmackException.NotLoggedInException) {
            reLoginToXmpp();
        }
    }

    public static void handleXmppExecption(XMPPException e){
        e.printStackTrace();

    }

    public static void handleIOException(IOException e){

    }

    /**
     * 连接超时或者没有连接的情况下，如果在有网络的情况下，应该让其重新连接并登录。因为在抛出异常时，XMPP并不会自动重连
     */
    private static void reLoginToXmpp(){
        if(!NetWorkUtil.isNetworkAvailable()) return;
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                User user = UserManager.getInstance().getUser();
                XmppManager xmppManager = XmppManagerImpl.getInstance();

            }
        });
    }
}
