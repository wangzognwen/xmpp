package com.juns.wechat.xmpp.util;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.xmpp.XmppManagerImpl;
import com.juns.wechat.xmpp.iq.IQUserInfo;

/**
 * Created by 王宗文 on 2016/6/30.
 */
public class UserInfoManager {

    /**
     * 查询用户信息，如果是查询自己的信息，用户名可以为空
     * @param userName 被查询用户的用户名
     */
    public static void queryUserInfo(String userName){
        UserBean userBean = new UserBean();
        userBean.setUserName(userName);
        IQUserInfo iqUserInfo = new IQUserInfo();
        iqUserInfo.setUserBean(userBean);
        XmppManagerImpl.getInstance().sendPacket(iqUserInfo);
    }

}
