package com.juns.wechat.xmpp;

import com.juns.wechat.util.ThreadPoolUtil;
import com.juns.wechat.xmpp.listener.XmppManagerListener;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by 王宗文 on 2016/6/12.
 */
public class XmppManagerUtil {
    private static final XmppManager XMPP_MANAGER = XmppManagerImpl.getInstance();

    public static void login(String userName, String passWord, XmppManagerListener listener){
        try {
            XMPP_MANAGER.login(userName, passWord);
            listener.onLoginSuccess();
        } catch (IOException e) {
            listener.onLoginFailed(e);
        } catch (XMPPException e) {
            listener.onLoginFailed(e);
        } catch (SmackException e) {
            listener.onLoginFailed(e);
        }
    }

    public static void asyncLogin(final String userName, final String passWord, final XmppManagerListener listener){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                login(userName, passWord, listener);
            }
        });
    }

    public static void regNewUser(final String accountName, final String passWord, final XmppManagerListener listener){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    XMPP_MANAGER.regNewUser(accountName, passWord);
                    listener.onRegisterSuccess();
                } catch (SmackException.NotConnectedException e) {
                    listener.onRegisterFailed(e);
                } catch (XMPPException.XMPPErrorException e) {
                    listener.onRegisterFailed(e);
                } catch (SmackException.NoResponseException e) {
                    listener.onRegisterFailed(e);
                }
            }
        });
    }

    public static void search(final String name){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    XMPP_MANAGER.searchUser(name);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
