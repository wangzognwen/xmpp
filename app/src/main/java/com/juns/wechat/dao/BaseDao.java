package com.juns.wechat.dao;

import android.database.Cursor;

import com.juns.wechat.database.DbUtil;


import org.simple.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/7/6.
 */
public class BaseDao<T> implements IDao<T> {
    protected DbManager dbManager;
    private Class<T> clazz;
    private DbDataEvent<T> dataEvent;

    public BaseDao(){
        dbManager = DbUtil.getDbManager();
        clazz = getEntityClass();
    }


    @Override
    public T findById(Integer id) {
        try {
            return dbManager.findById(clazz, id);
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T findByParams(Map<String, Object> params) {
        List<T> results = findAllByParams(params);
        if(results != null  && results.size() > 0){
            return results.get(0);
        }
        return null;
    }

    @Override
    public List<T> findAllByParams(Map<String, Object> params) {
        try {
            Selector<T> selector = dbManager.selector(clazz);
            if(params != null){
                int index = 0;
                for(String key : params.keySet()){
                    if(index == 0){
                        selector.where(key, "=", params.get(key));
                    }else {
                        selector.and(key, "=", params.get(key));
                    }
                    index++;
                }
                selector.and("flag", "!=", -1);
            }
            return selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(T t) {
        try {
            dbManager.save(t);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(List<T> list) {
        try {
            dbManager.save(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean replace(T t) {
        try {
            dbManager.replace(t);
            dataEvent = new DbDataEvent();
            dataEvent.action = DbDataEvent.REPLACE_ONE;
            dataEvent.data = t;
            EventBus.getDefault().post(dataEvent);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean replace(List<T> list) {
        try {
            dbManager.replace(list);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(WhereBuilder whereBuilder, KeyValue... keyValuePairs) {
        try {
            dbManager.update(clazz, whereBuilder, keyValuePairs);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> result = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);// 0表示获得第一个泛型的具体类型
        return result;
    }

    protected void closeCursor(Cursor cursor){
        if(cursor != null){
            cursor.close();
        }
    }
}
