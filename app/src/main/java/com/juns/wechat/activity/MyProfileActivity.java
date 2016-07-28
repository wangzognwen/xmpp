package com.juns.wechat.activity;

import android.content.Intent;
import android.net.Uri;
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
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.dialog.SelectPhotoDialog;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ImageUtil;
import com.juns.wechat.util.PhotoUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * create by 王者 on 2016/7/14
 */
@Content(R.layout.activity_my_profile)
public class MyProfileActivity extends ToolbarActivity implements SelectPhotoDialog.OnClickListener{
    @Id
    private RelativeLayout rlAvatar;
    @Id
    private ImageView ivAvatar;
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

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private UserBean userBean;

    private SelectPhotoDialog selectPhotoDialog;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        EventBus.getDefault().register(this);
    }

    private void setData(){
        userBean = AccountManager.getInstance().getUser();
        tvNickName.setText(userBean.getNickName() == null ? "" : userBean.getNickName());
        ImageUtil.loadImage(ivAvatar, userBean.getHeadUrl());
    }

    @Click(viewId = R.id.rlAvatar)
    private void modifyAvatar(View v){
        if(selectPhotoDialog == null){
            selectPhotoDialog = SelectPhotoDialog.createDialog(this, this);
        }
        selectPhotoDialog.show();
    }


    @Click(viewId = R.id.rlNickName)
    private void modifyNickName(View v){
        Utils.start_Activity(this, ModifyNameActivity.class);
    }

    @Subscriber
    private void onDbDataChanged(DbDataEvent<UserBean> event){
        if(event.action == DbDataEvent.REPLACE_ONE && event.data != null){
            if(event.data.getUserName().equals(userBean.getUserName())){
                setData();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                 /*   startPhotoZoom(
                            Uri.fromFile(PhotoUtil.getFile(imageName)),
                            480);*/
                    Uri uri = Uri.fromFile(PhotoUtil.getFile(imageName));
                    PhotoUtil.cropView(uri, 480, MyProfileActivity.this, PHOTO_REQUEST_CUT, imageName);
                    break;

                case PHOTO_REQUEST_GALLERY:
                    if (data != null)
                        PhotoUtil.cropView(data.getData(), 480, MyProfileActivity.this, PHOTO_REQUEST_CUT, imageName);
                    break;

                case PHOTO_REQUEST_CUT:

                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void takePhoto(View v) {
        imageName = getNowTime() + ".png";
        PhotoUtil.takePhoto(this, PHOTO_REQUEST_TAKEPHOTO, imageName);
        selectPhotoDialog.dismiss();
    }

    @Override
    public void openAlbum(View v) {
        imageName = getNowTime() + ".png";
        PhotoUtil.openAlbum(this, PHOTO_REQUEST_GALLERY);
        selectPhotoDialog.dismiss();
    }

    private String getNowTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
