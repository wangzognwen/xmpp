package com.juns.wechat.dao;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/7/6.
 */
public interface IDao<T> {
    T findById(Integer id);
    T findByParams(Map<String, Object> params);
    List<T> findAllByParams(Map<String, Object> params);
    void save(T t);
    void save(List<T> list);
    boolean replace(T t);
    boolean replace(List<T> list);
    boolean saveOrUpdate(T t);
    boolean update(WhereBuilder whereBuilder, KeyValue... keyValuePairs);
}
