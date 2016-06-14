package com.juns.wechat.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.juns.wechat.App;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ToastUtil {
    /**居中显示Toast提示(来自res) **/
    public static void showCenterToast(int resId, int time) {
        Context context = App.getInstance();
        Toast toast = Toast.makeText(context, context.getResources().getString(resId), time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /** 默认位置(屏幕下部)显示Toast提示(来自res) **/
    public static void showToast(int resId, int time) {
        Context context = App.getInstance();
        Toast toast = Toast.makeText(context, context.getResources().getString(resId), time);
        toast.show();
    }

    /** 默认位置(屏幕下部)显示Toast提示(来自res) **/
    public static void showToast(String prompt, int time) {
        Context context = App.getInstance();
        Toast toast = Toast.makeText(context, prompt, time);
        toast.show();
    }
}
