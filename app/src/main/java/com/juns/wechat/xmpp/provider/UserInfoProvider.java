package com.juns.wechat.xmpp.provider;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.xmpp.iq.IQUserInfo;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by 王宗文 on 2016/6/29.
 */
public class UserInfoProvider extends IQProvider<IQUserInfo> {
    public static final UserInfoProvider INSTANCE = new UserInfoProvider();

    @Override
    public IQUserInfo parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
        int eventType = parser.getEventType();
        UserBean userBean = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                if(parser.getNamespace().equals(IQUserInfo.NAME_SPACE)){
                    userBean = new UserBean();

                }
            }
            eventType = parser.next();
        }
        IQUserInfo iqUserInfo = new IQUserInfo();
        iqUserInfo.setUserBean(userBean);
        return iqUserInfo;
    }
}
