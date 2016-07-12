package com.juns.wechat.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.util.SharedPreferencesUtil;
import com.juns.wechat.view.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/6/8.
 */
public class UserManager {
    private static UserManager instance;
    private UserBean user;
    private Context context = App.getInstance();
    private UserDao userDao = UserDao.getInstance();

    private static final String CURRENT_LOGIN_USER = "current_login_user";

    public synchronized static UserManager getInstance(){
        if(instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager(){
        initUser();
    }

    private void initUser(){
        String userName = getCurrentLoginUserName();
        Map<String, Object> params = new HashMap<>();
        params.put(UserBean.USERNAME, userName);
        user = userDao.findByParams(params);
    }

    public void setCurrentLoginUser(UserBean userBean){
        if(userDao.replace(userBean)){
            setLogin(true);
            setCurrentLoginUserName(userBean.getUserName());
            user = userBean;
        }
    }

    public UserBean getCurrentLoginUser(){
        return user;
    }

    public void logOut(Context context){
        setLogin(false);
        setToken(null);
        setCurrentLoginUserName(null);
        user = null;
        if(context != null){
            Intent intent  = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if(context instanceof  Activity){
                ((Activity) context).finish();
            }
        }
    }

    public boolean isLogin(){
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), Constants.LoginState);
    }

    public void setLogin(boolean login){
        SharedPreferencesUtil.putBooleanValue(context, Constants.LoginState, login);
    }

    public void setCurrentLoginUserName(String userName){
        SharedPreferencesUtil.putValue(context, CURRENT_LOGIN_USER, userName);
    }

    public String getCurrentLoginUserName(){
        return SharedPreferencesUtil.getValue(context, CURRENT_LOGIN_USER);
    }

    public void setToken(String token){
        SharedPreferencesUtil.putValue(context, "token", token);
        setTokenRefreshTime(System.currentTimeMillis());
    }

    public String getToken(){
        return SharedPreferencesUtil.getValue(context, "token");
    }

    private void setTokenRefreshTime(long time){
        SharedPreferencesUtil.putLongValue(context, "token_refresh_time", time);
    }

    public long getTokenRefreshTime(){
        return SharedPreferencesUtil.getLongValue(context, "token_refresh_time", 0);
    }

}
