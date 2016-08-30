package com.juns.wechat.view.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.BaseCallBack;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.callback.LoginCallBack;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.NetWorkUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 用户注册
 * create by 王者 on 2061/2/7
 */
@Content(R.layout.activity_register)
public class RegisterActivity extends ToolbarActivity implements OnClickListener {
    @ViewInject(R.id.btnRegister)
	private Button btn_register;
    @ViewInject(R.id.btn_send)
    private Button btn_send;
    @ViewInject(R.id.etInputName)
	private EditText etInputName;
    @ViewInject(R.id.etPassWord)
    private EditText etPassword, et_code;
	private MyCount mc;
    private Handler handler = new Handler();

    private String userName;
    private String passWord;
    private String verifyCode;
    private int validCodeResult = 0; //校验二维码结果
    private static final int VALID_CODE_SUCCESS = 1;
    private static final int VALID_CODE_FAILED = -1;

    private EventHandler eventHandler; //短信验证码的事件监听


	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControl();
        setListener();
	}

	protected void initControl() {
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_register = (Button) findViewById(R.id.btnRegister);
		etInputName = (EditText) findViewById(R.id.etInputName);
		etPassword = (EditText) findViewById(R.id.etPassWord);
		et_code = (EditText) findViewById(R.id.etVerifyCode);
	}

	protected void setListener() {
		btn_send.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		etInputName.addTextChangedListener(new TelTextChange());
		etPassword.addTextChangedListener(new TextChange());
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        validCodeResult = VALID_CODE_SUCCESS;
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    try {
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if(status == 468){ //验证码错误
                            validCodeResult = VALID_CODE_FAILED;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			CommonUtil.finish(RegisterActivity.this);
			break;
		case R.id.btn_send:
			if (mc == null) {
				mc = new MyCount(60000, 1000); // 第一参数是总的时间，第二个是间隔时间
			}
			mc.start();
            sendVerifyCode();
			break;

		case R.id.btnRegister:
			checkInput();
			break;
		default:
			break;
		}
	}

    private void sendVerifyCode(){
        userName = etInputName.getText().toString().trim();
        if (!CommonUtil.isMobileNO(userName)) {
            CommonUtil.showLongToast(RegisterActivity.this, "请使用手机号码注册账户！ ");
            return;
        }
        SMSSDK.getVerificationCode("86", userName);
    }

	private void checkInput() {
		userName = etInputName.getText().toString().trim();
		passWord = etPassword.getText().toString();
		verifyCode = et_code.getText().toString().trim();
		if (!CommonUtil.isMobileNO(userName)) {
			CommonUtil.showLongToast(RegisterActivity.this, "请使用手机号码注册账户！ ");
			return;
		}
		if (TextUtils.isEmpty(verifyCode)) {
			CommonUtil.showLongToast(RegisterActivity.this, "请填写手机号码，并获取验证码！");
			return;
		}
		if (TextUtils.isEmpty(passWord) || passWord.length() < 6) {
			CommonUtil.showLongToast(RegisterActivity.this, "密码不能少于6位！");
			return;
		}

        getLoadingDialog("正在注册...  ").show();

        validCodeResult = 0;
        SMSSDK.submitVerificationCode("86", userName, verifyCode);
        while (validCodeResult == 0){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         if(validCodeResult == VALID_CODE_FAILED){
           CommonUtil.showLongToast(RegisterActivity.this, "验证码错误！");
           getLoadingDialog("正在注册...").dismiss();
           return;
        }

        register(userName, passWord);
	}

    private void register(String name, String pwd){
		if(!NetWorkUtil.isNetworkAvailable()){
			showToast(R.string.toast_network_unavailable);
			getLoadingDialog("正在注册...").dismiss();
			return;
		}
        UserRequest.register(name, pwd, registerCallBack);
    }

    private BaseCallBack<BaseResponse.RegisterResponse> registerCallBack = new BaseCallBack<BaseResponse.RegisterResponse>(){

        @Override
        protected void handleSuccess(BaseResponse.RegisterResponse result) {
            login();
        }

        @Override
        protected void handleFailed(BaseResponse.RegisterResponse result) {
            if(result.code == 1){  //参数错误
                getLoadingDialog("正在注册...").dismiss();
                if(result.errField.equalsIgnoreCase(UserBean.USERNAME)){
                    showToast("用户名不合法");
                }else if(result.errField.equalsIgnoreCase(UserBean.PASSWORD)){
                    showToast("密码长度不能小于6位");
                }
            }else if(result.code == 2){
                getLoadingDialog("正在注册...").dismiss();
                showToast("该用户已注册，可以直接登录");
            }
        }

		@Override
		public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
			getLoadingDialog("正在注册...").dismiss();
		}
	};

    private void login(){
		UserRequest.login(userName, passWord, loginCallBack);
    }

    private LoginCallBack loginCallBack = new LoginCallBack() {

        @Override
        protected void handleSuccess(BaseResponse.LoginResponse result) {
            super.handleSuccess(result);
            AccountManager.getInstance().setUserPassWord(passWord);
            CommonUtil.startActivity(RegisterActivity.this, MainActivity.class);
            CommonUtil.finish(RegisterActivity.this);
        }

        @Override
        protected void handleFailed(BaseResponse.LoginResponse result) {
            showToast("用户名或密码错误");
            getLoadingDialog("正在注册...").dismiss();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showToast(R.string.toast_network_error);
            getLoadingDialog("正在注册...").dismiss();
        }
    };

	// 手机号 EditText监听器
	class TelTextChange implements TextWatcher {

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
			String phone = etInputName.getText().toString();
			if (phone.length() == 11) {
				if (CommonUtil.isMobileNO(phone)) {
					btn_send.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.btn_bg_green));
					btn_send.setTextColor(0xFFFFFFFF);
					btn_send.setEnabled(true);
					btn_register.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.btn_bg_green));
					btn_register.setTextColor(0xFFFFFFFF);
					btn_register.setEnabled(true);
				} else {
					etInputName.requestFocus();
					showToast("请输入正确的手机号码！");
				}
			} else {
				btn_send.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_send.setTextColor(0xFFD0EFC6);
				btn_send.setEnabled(false);
				btn_register.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_register.setTextColor(0xFFD0EFC6);
				btn_register.setEnabled(true);
			}
		}
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
			boolean Sign1 = et_code.getText().length() > 0;
			boolean Sign2 = etInputName.getText().length() > 0;
			boolean Sign3 = etPassword.getText().length() > 0;

			if (Sign1 & Sign2 & Sign3) {
				btn_register.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_register.setTextColor(0xFFFFFFFF);
				btn_register.setEnabled(true);
			} else {
				btn_register.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_register.setTextColor(0xFFD0EFC6);
				btn_register.setEnabled(false);
			}
		}
	}

	/* 定义一个倒计时的内部类 */
	private class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			btn_send.setEnabled(true);
			btn_send.setText("发送验证码");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_send.setEnabled(false);
			btn_send.setText("(" + millisUntilFinished / 1000 + ")秒");
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
