package com.juns.wechat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.dao.DataEvent;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.util.LogUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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
    private RelativeLayout rlSignature;
    @Id
    private TextView tvSignature;

    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        EventBus.getDefault().register(this);
    }

    private void setData(){
        userBean = UserManager.getInstance().getUser();
        tvNickName.setText(userBean.getNickName() == null ? "" : userBean.getNickName());
    }

    @Click(viewId = R.id.rlNickName)
    private void modifyNickName(View v){
        Utils.start_Activity(this, ModifyNameActivity.class);
    }

    @Subscriber
    private void onDbDataChanged(DataEvent<UserBean> event){
        if(event.action == DataEvent.REPLACE_ONE && event.data != null){
            if(event.data.getUserName().equals(userBean.getUserName())){
                setData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
