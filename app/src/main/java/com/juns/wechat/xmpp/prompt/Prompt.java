package com.juns.wechat.xmpp.prompt;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * <p/>
 * This file is part of YY Cube project.
 * <p/>
 * It can not be copied and/or distributed without the express
 * permission of Yunyun Network
 * Created by wangshuai on 2015/8/31
 *******************************************************/
public abstract class Prompt {

    public Context mContext;

    public Prompt(Context context){
        mContext = context;
    }

    /**
     * 查看当前是否正在运行
     * @return
     */
    public final boolean isOnForeGround(){
        ActivityManager activityManager = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (taskInfos.size() > 0
                && TextUtils.equals(mContext.getPackageName(),
                taskInfos.get(0).topActivity.getPackageName())) {
            return true;
        }
        return false;
    }

    public final boolean isOnChatActivity(){
        ActivityManager activityManager = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (taskInfos.size() ==0){
            return false;
        }
        String className = taskInfos.get(0).topActivity.getClassName();
        if (className.contains("ChatActivity")){
            return true;
        }
        return false;
    }
}
