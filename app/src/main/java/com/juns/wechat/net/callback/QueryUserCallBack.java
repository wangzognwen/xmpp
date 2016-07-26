package com.juns.wechat.net.callback;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.net.response.BaseResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
public abstract class QueryUserCallBack extends BaseCallBack<BaseResponse.QueryUserResponse>{
    @Override
    protected void handleSuccess(BaseResponse.QueryUserResponse result) {
        if(result.userBean != null){
            saveUserInfo(result.userBean);
        }
    }

    private void saveUserInfo(UserBean userBean){
        UserDao.getInstance().replace(userBean);
    }


}
