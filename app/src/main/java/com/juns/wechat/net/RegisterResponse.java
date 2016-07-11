package com.juns.wechat.net;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class RegisterResponse extends BaseResponse{
    public String errField;
}
