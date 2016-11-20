package com.juns.wechat.xmpp.util;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.xmpp.XmppManagerImpl;
import com.juns.wechat.xmpp.XmppManagerUtil;
import com.juns.wechat.xmpp.iq.DialIQ;

/**
 * Created by 王者 on 2016/11/20.
 */

public class DialManager {
    private static DialManager mInstance;

    public void startDial(String toUserName){
        String myselfName = AccountManager.getInstance().getUserName();
        String fromAddress = "xmpp:" + ConfigUtil.getXmppJid(myselfName);
        String toAddress = "xmpp:" + ConfigUtil.getXmppJid(toUserName);
        DialIQ dialIQ = new DialIQ(fromAddress, toAddress);
        dialIQ.setFrom(myselfName + "@rayo.wangzhe/XMPP");
        dialIQ.setTo("rayo.wangzhe");
        XmppManagerUtil.sendPacket(dialIQ);
    }

    public static DialManager getInstance(){
        if(mInstance == null){
            mInstance = new DialManager();
        }
        return mInstance;
    }
}
