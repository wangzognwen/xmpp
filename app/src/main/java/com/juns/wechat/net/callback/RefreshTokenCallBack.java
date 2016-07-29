package com.juns.wechat.net.callback;

import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.TokenResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
public abstract class RefreshTokenCallBack extends BaseCallBack<TokenResponse>{

    @Override
    protected void handleSuccess(TokenResponse result) {
        resetToken(result.token);
    }

    private void resetToken(String token){
        AccountManager.getInstance().setToken(token);
    }

}
