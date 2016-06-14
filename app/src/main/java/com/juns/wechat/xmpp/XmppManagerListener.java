package com.juns.wechat.xmpp;

/**
 * Created by 王宗文 on 2016/6/12.
 */
public interface XmppManagerListener {

    void onLoginSuccess();
    void onLoginFailed(Exception e);
    void onRegisterSuccess();
    void onRegisterFailed(Exception e);
}
