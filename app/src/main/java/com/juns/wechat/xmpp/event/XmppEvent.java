package com.juns.wechat.xmpp.event;

/**
 * Created by 王宗文 on 2016/7/5.
 */
public class XmppEvent {
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    private int resultCode;
    private Exception exception;

    public XmppEvent(int resultCode, Exception exception) {
        this.resultCode = resultCode;
        this.exception = exception;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
