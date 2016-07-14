package com.juns.wechat.net.callback;

import com.juns.wechat.manager.UserManager;
import com.juns.wechat.net.response.TokenResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
public abstract class RefreshTokenCallBack extends BaseCallBack<TokenResponse>{
    @Override
    public void onSuccess(TokenResponse result) {
        if(result.code == 0){
            resetToken(result.token);
            onTokenValid();
        }else{
            resetToken(null);
            onTokenInvalid();
        }
    }

    private void resetToken(String token){
        UserManager.getInstance().setToken(token);
    }

    protected abstract void onTokenValid();

    protected abstract void onTokenInvalid();

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ex.printStackTrace();  //ignore
    }
}
