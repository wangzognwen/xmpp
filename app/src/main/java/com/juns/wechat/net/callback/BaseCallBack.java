package com.juns.wechat.net.callback;

import android.widget.Toast;

import com.juns.wechat.App;
import com.juns.wechat.R;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.net.HttpEvent;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ToastUtil;

import org.simple.eventbus.EventBus;
import org.xutils.common.Callback;

/**
 * Created by 王宗文 on 2016/7/7.
 */
public abstract class BaseCallBack<T> implements Callback.CommonCallback<T>{
    private HttpEvent httpEvent;

    public BaseCallBack(){
        httpEvent = new HttpEvent();
    }

    @Override
    public final void onSuccess(T result) {
        BaseResponse response = (BaseResponse) result;
        if(response.code == 0){
            handleSuccess(result);
        }else if(response.code == BaseResponse.SERVER_ERROR){
            ToastUtil.showToast("服务器出错了", Toast.LENGTH_SHORT);
            handleFailed(result);
        }else if(response.code == BaseResponse.TOKEN_EXPIRED || response.code == BaseResponse.TOKEN_INVALID){
            handleTokenError();
        }else {
            handleFailed(result);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);
    }

    protected abstract void handleSuccess(T result);


    private void handleTokenError(){
        UserManager.getInstance().logOut(App.getInstance());
    }

    protected abstract void handleFailed(T result);

    @Override
    public void onCancelled(CancelledException cex) {
        httpEvent.setResultCode(HttpEvent.CANCEL);
        EventBus.getDefault().post(httpEvent);
    }

    @Override
    public void onFinished() {
        httpEvent.setResultCode(HttpEvent.FINISH);
        EventBus.getDefault().post(httpEvent);
    }
}
