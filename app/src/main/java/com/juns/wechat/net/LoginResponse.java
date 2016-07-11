package com.juns.wechat.net;

import com.juns.wechat.bean.UserBean;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class LoginResponse extends BaseResponse{
    public String token;
    public UserBean userBean;
}
