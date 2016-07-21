package com.juns.wechat.common;


import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.bean.UserBean;

public class UserUtils {
	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static UserBean getUserModel(Context context) {
		UserBean user = null;
		String jsondata = Utils.getValue(context, Constants.UserInfo);
		// Log.e("", jsondata);
		if (!TextUtils.isEmpty(jsondata))
			//user = JSON.parseObject(jsondata, UserBean.class);
		return user;
        return null;
	}

	/**
	 * 获取用户ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserID(Context context) {
		UserBean user = getUserModel(context);
		if (user != null)
			return user.getTelephone();
		else
			return "";
	}

	/**
	 * 获取用户名字
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		UserBean user = getUserModel(context);
		if (user != null)
			return user.getUserName();
		else
			return "";
	}

	/**
	 * 获取用户
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserPwd(Context context) {
		UserBean user = getUserModel(context);
		if (user != null)
			return user.getPassWord();
		else
			return "";
	}

	public static void getLogout(Context context) {
	//	EMChatManager.getDbManager().logout();// 退出环信聊天
		Utils.RemoveValue(context, Constants.LoginState);
		Utils.RemoveValue(context, Constants.UserInfo);
		App.getInstance2().exit();
	}



}
