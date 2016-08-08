package com.juns.wechat.dao;

import java.util.List;

/**
 * Created by 王宗文 on 2016/7/14.
 */
public class DbDataEvent<T> {

    public static final int SAVE_ONE = 11;
    public static final int SAVE_MANY = SAVE_ONE + 1;
    public static final int UPDATE_ONE = 21;
    public static final int UPDATE_MANY = UPDATE_ONE + 1;
    public static final int REPLACE_ONE = 31;
    public static final int REPLACE_MANY = REPLACE_ONE + 1;
    public static final int DELETE_ONE = 41;
    public static final int DELETE_MANY = DELETE_ONE + 1;

    public int action;

    public T data;

    public List<T> datas;
}
