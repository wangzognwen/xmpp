package com.juns.wechat.dao;

import java.util.List;

/**
 * Created by 王宗文 on 2016/7/14.
 */
public class DbDataEvent<T> {

    public static final int REPLACE_ONE = 30;
    public static final int REPLACE_MANY = REPLACE_ONE + 1;

    public int action;

    public T data;

    public List<T> datas;
}
