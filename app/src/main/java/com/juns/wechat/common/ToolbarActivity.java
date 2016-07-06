package com.juns.wechat.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.juns.wechat.R;
import com.juns.wechat.annotation.AnnotationUtil;
import com.juns.wechat.util.ToastUtil;
import com.juns.wechat.util.ToolBarUtil;

public class ToolbarActivity extends BaseActivity {
    protected Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = ToolBarUtil.setToolBar(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        toolbar = ToolBarUtil.setToolBar(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        toolbar = ToolBarUtil.setToolBar(this);
    }

    protected void setToolbarTitle(String text){
        ToolBarUtil.setTitle(this, text);
    }

    protected void setToolbarRight(int type, int resId){
        ToolBarUtil.setToolbarRight(this, type, resId);
    }

    @Override
    public void onBackPressed() {
        Utils.finish(this);
    }
}
