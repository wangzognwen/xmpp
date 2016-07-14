package com.juns.wechat.net.callback;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.manager.UserManager;
import com.juns.wechat.net.response.UpdateUserResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
public abstract class UpdateUserCallBack extends BaseCallBack<UpdateUserResponse>{

    @Override
    public void onSuccess(UpdateUserResponse result) {
        if(result.code == 0){
           saveUserInfo(result.userBean);
           onUpdateSuccess();
        }else{
            onUpdateFailed();
        }
    }



    private void saveUserInfo(UserBean userBean){
        UserManager.getInstance().setCurrentLoginUser(userBean);
    }

    protected abstract void onUpdateSuccess();

    protected abstract void onUpdateFailed();

}
