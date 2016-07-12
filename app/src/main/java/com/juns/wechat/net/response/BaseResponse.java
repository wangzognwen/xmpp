package com.juns.wechat.net.response;

import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/7.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class BaseResponse {
    public static final int SUCCESS = 0;

    public int code;
    public String msg;
}
