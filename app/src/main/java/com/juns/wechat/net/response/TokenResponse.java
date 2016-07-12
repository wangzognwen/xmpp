package com.juns.wechat.net.response;

import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/12.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class TokenResponse extends BaseResponse{
    public static final int TOKEN_EXPIRED = 1;
    public static final int TOKEN_INVALID = 2;

    public String token;
}
