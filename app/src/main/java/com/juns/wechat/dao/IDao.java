package com.juns.wechat.dao;

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
    void saveOrUpdate(T t);
    void saveOrUpdate(List<T> list);
}
