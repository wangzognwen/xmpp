package com.juns.wechat.net.request;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.BaseCallBack;
import com.juns.wechat.net.response.SyncFriendResponse;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.x;

/**
 * Created by 王者 on 2016/7/26.
 */
public class FriendRequest {

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "getMyFriends")
    public static class SyncFriendParams extends RequestParams {
        private long modifyDate;
        private String token;

        public SyncFriendParams(long modifyDate){
            this.modifyDate = modifyDate;
            token = AccountManager.getInstance().getToken();
        }
    }

    public static void syncFriendData(long lastModifyDate, BaseCallBack<SyncFriendResponse> callBack){
        x.http().post(new SyncFriendParams(lastModifyDate), callBack);
    }
}
