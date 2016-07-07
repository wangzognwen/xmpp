package com.juns.wechat.net;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/7.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class BaseResponse {
    public int code;
    public int msg;
}
