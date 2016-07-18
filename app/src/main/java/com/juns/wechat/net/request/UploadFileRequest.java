package com.juns.wechat.net.request;

import android.text.TextUtils;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.net.callback.BaseCallBack;
import com.juns.wechat.net.callback.RefreshTokenCallBack;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.net.response.UploadFileResponse;
import com.juns.wechat.util.LogUtil;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.body.FileBody;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王宗文 on 2016/7/12.
 */
public class UploadFileRequest {

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "uploadAvatar")
    public static class FileParams extends RequestParams{
        private String token;

        public FileParams(File file){
                token = UserManager.getInstance().getToken();
                List<KeyValue> params = new ArrayList<>();
                params.add(new KeyValue("avatar", file));
                MultipartBody multipartBody = new MultipartBody(params, null);
               // FileBody fileBody = new FileBody(file, "multipart/form-data");
                setRequestBody(multipartBody);
               // LogUtil.i("conetentType:" + fileBody.getContentType());


        }
    }

    public static void uploadAvatar(String filePath, BaseCallBack<UploadFileResponse> callBack){
        x.http().post(new FileParams(new File(filePath)), callBack);
    }
}
