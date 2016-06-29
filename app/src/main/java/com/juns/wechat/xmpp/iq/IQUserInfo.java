package com.juns.wechat.xmpp.iq;

import com.juns.wechat.bean.UserBean;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by 王宗文 on 2016/6/29.
 */
public class IQUserInfo extends IQ{
    public static final String ELEMENT = "userinfo";
    public static final String NAME_SPACE = "xmpp:custom:userinfo";

    private UserBean userBean;
    public IQUserInfo() {
        super(ELEMENT, NAME_SPACE);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        if(userBean != null){
            if(getType() == Type.get){
                xml.attribute(UserBean.USERNAME, userBean.getUserName() == null ? "" : userBean.getUserName());
            }
        }
        xml.rightAngleBracket();
        return xml;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
