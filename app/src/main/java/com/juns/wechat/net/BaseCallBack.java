package com.juns.wechat.net;

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
    public void onError(Throwable ex, boolean isOnCallback) {
        httpEvent.setResultCode(HttpEvent.ERROR);
        httpEvent.setException(new Exception(ex));
        EventBus.getDefault().post(httpEvent);
    }

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
