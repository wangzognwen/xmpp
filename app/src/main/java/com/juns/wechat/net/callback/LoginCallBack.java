package com.juns.wechat.net.callback;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.net.response.LoginResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
public abstract class LoginCallBack extends BaseCallBack<LoginResponse>{
    @Override
    public void onSuccess(LoginResponse result) {
        if(result.code == 0){
           saveUserInfo(result.userBean, result.token);
           handleLoginSuccess();
        }else if(result.code == 1){
            handleLoginFailed();
        }
    }



    private void saveUserInfo(UserBean userBean, String token){
        UserManager.getInstance().setCurrentLoginUser(userBean);
        UserManager.getInstance().setToken(token);
    }

    protected abstract void handleLoginSuccess();

    protected abstract void handleLoginFailed();

}
