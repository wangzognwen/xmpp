package com.juns.wechat.config;

import com.juns.wechat.BuildConfig;

/**
 * ****************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * <p/>
 * This file is part of YY Cube project.
 * <p/>
 * It can not be copied and/or distributed without the express
 * permission of Yunyun Network
 * Created by wangshuai on 2015/5/13
 * *****************************************************
 */
public class ConfigUtil {
    private static String REAL_API_URL = "https://www.yycube.com/yuncard_dev";
    private static String DEBUG_API_URL = "http://192.169.1.118/yuncardbak";

    public static String REAL_XMPP_SERVER = "123.206.34.158";
    private static String DEBUG_XMPP_SERVER = "123.206.34.158";

    private final static String  DEBUG_XMPP_DOMAIN = "wangzhe";//"@yycube.com";
    public final static String  REAL_XMPP_DOMAIN = "wangzhe";

    public static final String RESOURCE = "XMPP";

    private static boolean mIsDebug = BuildConfig.DEBUG;

    public static String getXmppServer(){
        if (mIsDebug){
            return DEBUG_XMPP_SERVER;
        }
        return REAL_XMPP_SERVER;
    }

    public static String getXmppDomain(){
        if (mIsDebug){
            return DEBUG_XMPP_DOMAIN;
        }
        return REAL_XMPP_DOMAIN;
    }

    public static String getApiUrl(){
        if (mIsDebug){
            return DEBUG_API_URL;
        }
        return REAL_API_URL;
    }

    public static String getXmppJid(int userId){
        if (mIsDebug){
            return userId + "@" + DEBUG_XMPP_DOMAIN + "/" + RESOURCE;
        }
        return userId + "@" + REAL_XMPP_DOMAIN+ "/" + RESOURCE;
    }

    public static String getUserName(String userJid){
        int index = userJid.indexOf("@");
        if (index < 1){
            return null;
        }
        userJid = userJid.substring(0, index);
        if (mIsDebug){
            if (userJid.contains("office")){
                userJid = userJid.replace("office", "");
            }
        }
        return userJid;
    }
}
