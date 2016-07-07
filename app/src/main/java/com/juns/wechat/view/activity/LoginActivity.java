package com.juns.wechat.view.activity;

import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.sasl.SASLError;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juns.wechat.Constants;
import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.common.BaseActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ToastUtil;
import com.juns.wechat.xmpp.event.XmppEvent;
import com.juns.wechat.xmpp.XmppManagerUtil;

import java.io.IOException;

//登陆
public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_back;
	private Button btn_login, btn_register;
	private EditText et_usertel, et_password;
    private String userName, password;

    private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initControl();
        setListener();
    }

	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("登陆");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_qtlogin);
		et_usertel = (EditText) findViewById(R.id.etInputName);
		et_password = (EditText) findViewById(R.id.etPassWord);
	}


    protected void initView() {

    }

    protected void initData() {

    }

    protected void setListener() {
		img_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		findViewById(R.id.tv_wenti).setOnClickListener(this);
		et_usertel.addTextChangedListener(new TextChange());
		et_password.addTextChangedListener(new TextChange());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(LoginActivity.this);
			break;
		case R.id.tv_wenti:
			Utils.start_Activity(LoginActivity.this, WebViewActivity.class,
					new BasicNameValuePair(Constants.Title, "帮助"),
					new BasicNameValuePair(Constants.URL,
							"http://weixin.qq.com/"));
			break;
		case R.id.btn_qtlogin:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		case R.id.btn_login:
			startLogin();
			break;
		default:
			break;
		}
	}

	private void startLogin() {
		userName = et_usertel.getText().toString().trim();
		password = et_password.getText().toString().trim();
		getLoadingDialog("正在登录...").show();
		startLogin(userName, password);
	}

	private void startLogin(final String userName, final String password) {
		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            loginToXmpp(userName, password);

		} else {
			Utils.showLongToast(LoginActivity.this, "请填写账号或密码！");
		}
	}

    private void loginToXmpp(String userName, String password){
        EventBus.getDefault().register(this);
        XmppManagerUtil.asyncLogin(userName, password);
    }

    @Subscriber
    private void onLoginFinish(XmppEvent event){
        EventBus.getDefault().unregister(this);
        LogUtil.i("resultCode: " + event.getResultCode());
        if(event.getResultCode() == XmppEvent.SUCCESS){
            onLoginSuccess();
        }else {
            onLoginFailed(event.getException());
        }
    }

    public void onLoginSuccess() {
        getLoadingDialog("正在登录...").dismiss();
        UserManager.getInstance().setLogin(true);
        UserManager.getInstance().setUserName(userName);
        UserManager.getInstance().setPassword(password);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        Utils.finish(this);
    }

    public void onLoginFailed(Exception e) {
        getLoadingDialog("正在登录...").dismiss();
        if(e instanceof IOException || e instanceof SmackException){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast("网络可能存在问题，请检查网络设置", Toast.LENGTH_LONG);
                }
            });

        }
        if(e instanceof SASLErrorException){
            SASLErrorException saslError = (SASLErrorException) e;
            if(SASLError.not_authorized == saslError.getSASLFailure().getSASLError()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("用户名或密码错误，请重试", Toast.LENGTH_LONG);
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // EditText监听器
	class TextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			boolean Sign2 = et_usertel.getText().length() > 0;
			boolean Sign3 = et_password.getText().length() > 4;
			if (Sign2 & Sign3) {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_login.setEnabled(true);
			} else {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_login.setTextColor(0xFFD0EFC6);
				btn_login.setEnabled(false);
			}
		}
	}

}
