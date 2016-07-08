package com.juns.wechat.net;

import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.util.NetWorkUtil;
import com.juns.wechat.util.ToastUtil;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.x;

/**
 * Created by 王宗文 on 2016/7/7.
 */

public class UserRequest extends RequestParams {

    public static void register(String userName, String passWord, BaseCallBack<BaseResponse> callBack){
        if(!NetWorkUtil.isNetworkAvailable()){
            ToastUtil.showToast(R.string.toast_network_unavailable, Toast.LENGTH_LONG);
            return;
        }
        x.http().post(new RegisterParams(userName, passWord), callBack);
    }


    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "addUser")
    public static class RegisterParams extends RequestParams{
        private String userName;
        private String passWord;

        public RegisterParams(String userName, String passWord){
            this.userName = userName;
            this.passWord = passWord;
        }
    }

}
