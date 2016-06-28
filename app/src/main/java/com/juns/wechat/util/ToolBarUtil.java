package com.juns.wechat.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;


/*******************************************************
 * description ：加载toolbar的工具类
 *
 * @since 1.5.5
 * Created by 王宗文 on 2015/9/14
 *******************************************************/
public class ToolBarUtil {
    public static final int TAG_TEXT = 1;
    public static final int TAG_IMG = 2;
    /**
     * 自定义toolbar的显示
     *
     * @param activity
     */
    public static void setToolBar(final AppCompatActivity activity) {
        Toolbar toolBar = (Toolbar) activity.findViewById(R.id.toolbar);
        if(toolBar == null){   // 一些activity并没有使用toolbar
            return;
        }
        setTitle(activity, AppUtil.getActivityLabel(activity));

        ImageView ivReturn = (ImageView) toolBar.findViewById(R.id.ivReturn);
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        activity.setSupportActionBar(toolBar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    /**
     * 自定义标题栏文字，需要在{@link ToolBarUtil#setToolBar(AppCompatActivity)}方法后调用，否则会被覆盖
     *
     * @param activity
     * @param text
     */
    public static void setTitle(AppCompatActivity activity, String text) {
        Toolbar toolBar = (Toolbar) activity.findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) toolBar.findViewById(R.id.tvTitle);
        if(!TextUtils.isEmpty(text)){
            tvTitle.setText(text);
        }
    }

    /**
     * 自定义标题栏文字，需要在{@link ToolBarUtil#setToolBar(AppCompatActivity)}方法后调用，否则会被覆盖
     *
     * @param activity
     * @param resId
     */
    public static void setTitle(AppCompatActivity activity, int resId) {
        setTitle(activity, activity.getString(resId));
    }



}
