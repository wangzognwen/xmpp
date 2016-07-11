package com.juns.wechat.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.juns.wechat.bean.UserBean;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 王宗文 on 15/11/5.
 */
public class JsonResponseParser implements ResponseParser {
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").
            registerTypeAdapter(UserBean.class, new MyJsonDeserializer()).create();



    class MyJsonDeserializer implements JsonDeserializer<UserBean>{

        @Override
        public UserBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            UserBean userBean = new UserBean();
            JsonObject jsonObject = json.getAsJsonObject();
            userBean.setUserName(jsonObject.get(UserBean.USERNAME).getAsString());
            userBean.setPassWord(jsonObject.get(UserBean.PASSWORD).getAsString());
            long createTime = jsonObject.get(UserBean.CREATE_DATE).getAsLong();
            userBean.setCreateDate(new Date(createTime));
            long modifyTime = jsonObject.get(UserBean.MODIFY_DATE).getAsLong();
            userBean.setModifyDate(new Date(modifyTime));
            return userBean;
        }
    }

    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (resultClass == List.class) {
            return gson.fromJson(result, resultType);
            // return JSON.parseArray(result, (Class<?>) ParameterizedTypeUtil.getParameterizedType(resultType, List.class, 0));
        } else {
            return gson.fromJson(result, resultClass);
        }

    }
}
