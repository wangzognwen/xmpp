package com.juns.wechat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.Utils;

/**
 * create by 王者 on 2016/7/14
 */
@Content(R.layout.activity_my_profile)
public class MyProfileActivity extends ToolbarActivity {
    @Id
    private TextView tvNickName;
    @Id
    private TextView tvUserName;
    @Id
    private RelativeLayout rlQrCode;
    @Id
    private ImageView ivQrCode;
    @Id
    private RelativeLayout rlSex;
    @Id
    private TextView tvSex;
    @Id
    private RelativeLayout rlRegion;
    @Id
    private TextView tvRegion;
    @Id
    private RelativeLayout rlSingature;
    @Id
    private TextView tvSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click(viewId = R.id.rlNickName)
    private void onNicknameContainerClicked(View v){
        Utils.start_Activity(this, ModifyNameActivity.class);
    }
}
