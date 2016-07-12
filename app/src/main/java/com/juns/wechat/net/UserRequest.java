package com.juns.wechat.net;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.net.callback.BaseCallBack;
import com.juns.wechat.net.callback.LoginCallBack;
import com.juns.wechat.net.response.RegisterResponse;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.x;

/**
 * Created by 王宗文 on 2016/7/7.
 */

public class UserRequest extends RequestParams {
    private static final String URL = ConfigUtil.REAL_API_URL;

    public static void register(String userName, String passWord, BaseCallBack<RegisterResponse> callBack){
        x.http().post(new RegisterParams(userName, passWord), callBack);
    }


    @HttpRequest(host = URL, path = "addUser")
    public static class RegisterParams extends RequestParams{
        private String userName;
        private String passWord;


        public RegisterParams(String userName, String passWord){
            this.userName = userName;
            this.passWord = passWord;
        }
    }

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "login")
    public static class LoginParams extends RequestParams{
        private String userName;
        private String passWord;

        public LoginParams(String userName, String passWord){
            this.userName = userName;
            this.passWord = passWord;
        }
    }

    public static void login(String userName, String passWord, LoginCallBack callBack){
        x.http().post(new LoginParams(userName, passWord), callBack);
    }

}
