package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.JsonResponseParser;
import com.juns.wechat.net.response.BaseResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class LoginResponse extends BaseResponse {
    public static final int FAILED = 1;

    public String token;
    public UserBean userBean;
}
