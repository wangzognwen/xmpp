package com.juns.wechat.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.juns.wechat.R;
import com.juns.wechat.common.BaseActivity;
import com.juns.wechat.common.Utils;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class AddFriendActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setListener();
    }

    private void setListener(){
        findViewById(R.id.rl_search_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.start_Activity(AddFriendActivity.this, SearchActivity.class);
            }
        });
    }
}
