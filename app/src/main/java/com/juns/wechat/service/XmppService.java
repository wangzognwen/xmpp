package com.juns.wechat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.util.ThreadPoolUtil;
import com.juns.wechat.xmpp.XmppExceptionHandler;
import com.juns.wechat.xmpp.XmppManager;
import com.juns.wechat.xmpp.XmppManagerImpl;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;


/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：定义一个Service来处理登录及注册Xmpp账户
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/20
 *******************************************************/
public class XmppService extends Service {
    private XmppManager xmpp;
    /**
     * 登录Action,由于登录时这个用户的用户和密码可以从{@link UserBean}中取得，所以不需要传用户名和密码
     */
    public static final String ACTION_LOGIN = "login";

    private volatile boolean mIsLogging = false; //是否正在登录

    @Override
    public void onCreate() {
        super.onCreate();
        xmpp = XmppManagerImpl.getInstance();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new XmppBinder();
    }

    public class XmppBinder extends Binder{
        public XmppService getService(){
            return XmppService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       handleAction(intent);
        return START_STICKY;
    }

    /**
     * 处理各种action
     * @param intent
     */
    private void handleAction(Intent intent){
        if(intent == null) return;
        String action = intent.getAction();
        if(TextUtils.isEmpty(action)) return;
        if(ACTION_LOGIN.equals(action)){
            UserBean user = UserManager.getInstance().getUser();
            login(user.getUserName(), user.getPassword());
        }
    }

    /**
     * 开启一个线程执行登录
     * 更多详情请查看{@link XmppManagerImpl#login(String, String)}
     * @param accName
     * @param passWord
     */
    public void login(final String accName, final String passWord){
        if(mIsLogging) return;
        mIsLogging = true;
        if(!UserManager.getInstance().isLogin()){
            return;
        }
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    xmpp.login(accName, passWord);
                } catch (IOException e) {
                    XmppExceptionHandler.handleIOException(e);
                } catch (XMPPException e) {
                    XmppExceptionHandler.handleXmppExecption(e);
                } catch (SmackException e) {
                    XmppExceptionHandler.handleSmackException(e);
                }
                mIsLogging = false;
            }
        });
    }

    public static void login(Context context){
        Intent service = new Intent(context, XmppService.class);
        service.setAction(ACTION_LOGIN);
        context.startService(service);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp = null;
    }
}
