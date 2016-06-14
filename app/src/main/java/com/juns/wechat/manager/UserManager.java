package com.juns.wechat.manager;

import android.content.Context;

import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.bean.User;
import com.juns.wechat.util.SharedPreferencesUtil;

/**
 * Created by 王宗文 on 2016/6/8.
 */
public class UserManager {
    private static UserManager instance;
    private User user;
    private Context context = App.getInstance();

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
        user = new User();
        user.setId(SharedPreferencesUtil.getValue(context, User.ID));
        user.setUserName(SharedPreferencesUtil.getValue(context, User.USERNAME));
        user.setPassword(SharedPreferencesUtil.getValue(context, User.PASSWORD));
    }

    public User getUser(){
        return user;
    }

    public boolean isLogin(){
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), Constants.LoginState);
    }

    public void setLogin(boolean login){
        SharedPreferencesUtil.putBooleanValue(context, Constants.LoginState, login);
    }

    public void setId(String userId){
        SharedPreferencesUtil.putValue(context, User.ID, userId);
        user.setId(userId);
    }

    public void setUserName(String userName){
        SharedPreferencesUtil.putValue(context, User.USERNAME, userName);
        user.setUserName(userName);
    }

    public void setPassword(String password){
        SharedPreferencesUtil.putValue(context, User.PASSWORD, password);
        user.setPassword(password);
    }

}
