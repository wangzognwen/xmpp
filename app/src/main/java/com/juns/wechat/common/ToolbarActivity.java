package com.juns.wechat.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.juns.wechat.R;

public class ToolbarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        initView();
        initData();
        setListener();
    }

    /**
     * 初始化控件
     */
    protected  void initView(){

    }

    /**
     * 初始化数据
     */
    protected  void initData(){

    }

    /**
     * 设置监听
     */
    protected  void setListener(){

    }

    @Override
    public void onBackPressed() {
        Utils.finish(this);
    }
}
