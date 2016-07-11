package com.juns.wechat.manager;

import android.content.Context;

import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.util.SharedPreferencesUtil;

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
        String userName = getUserName();
        Map<String, Object> params = new HashMap<>();
        params.put(UserBean.USERNAME, userName);
        user = userDao.findByParams(params);
    }

    public UserBean getUser(){
        return user;
    }

    public void saveOrUpdateUser(UserBean userBean){
        userDao.replace(userBean);
        setLogin(true);
        setUserName(userBean.getUserName());
        user = userBean;
    }

    public boolean isLogin(){
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), Constants.LoginState);
    }

    public void setLogin(boolean login){
        SharedPreferencesUtil.putBooleanValue(context, Constants.LoginState, login);
    }

    public void setId(int userId){
        SharedPreferencesUtil.putIntValue(context, UserBean.ID, userId);
        user.setId(userId);
    }

    public void setUserName(String userName){
        SharedPreferencesUtil.putValue(context, UserBean.USERNAME, userName);
    }

    public String getUserName(){
        return SharedPreferencesUtil.getValue(context, UserBean.USERNAME);
    }

    public void setToken(String token){
        SharedPreferencesUtil.putValue(context, "token", token);
    }

    public String getToken(){
        return SharedPreferencesUtil.getValue(context, "token");
    }

    public void setPassword(String password){
        SharedPreferencesUtil.putValue(context, UserBean.PASSWORD, password);
        user.setPassWord(password);
    }

}
