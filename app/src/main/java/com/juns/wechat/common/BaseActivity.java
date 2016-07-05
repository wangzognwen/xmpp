package com.juns.wechat.common;

import android.app.NotificationManager;
import android.content.Context;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.juns.wechat.annotation.AnnotationUtil;
import com.juns.wechat.dialog.FlippingLoadingDialog;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ToastUtil;


public class BaseActivity extends AppCompatActivity {
    private FlippingLoadingDialog mLoadingDialog;
    private boolean idResumed = false;


	private static final int notifiId = 11;
	protected NotificationManager notificationManager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        AnnotationUtil.initAnnotation(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        AnnotationUtil.initAnnotation(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        AnnotationUtil.initAnnotation(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        idResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        idResumed = false;
    }

    protected void showToast(int resId){
        showToast(getString(resId));
    }

    protected void showToast(String prompt){
        if(idResumed){
            ToastUtil.showToast(prompt, Toast.LENGTH_LONG);
        }
    }

    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        return mLoadingDialog;
    }

    protected boolean isIdResumed(){
        return idResumed;
    }

}
