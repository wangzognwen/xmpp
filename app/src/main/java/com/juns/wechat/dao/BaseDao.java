package com.juns.wechat.dao;

import android.os.Build;
import android.text.TextUtils;

import com.juns.wechat.database.DbUtil;

import net.tsz.afinal.FinalDb;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.SqlInfoBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/7/6.
 */
public class BaseDao<T> implements IDao<T> {
    private DbManager dbManager;
    private Class<T> clazz;

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
        if(results != null){
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
    public void saveOrUpdate(T t) {

    }

    @Override
    public void saveOrUpdate(List<T> list) {
        try {
            beginTransaction();
            if (list == null || list.isEmpty()) return;
            TableEntity<?> table = null;
            try {
                table = dbManager.getTable(list.get(0).getClass());
            } catch (DbException e) {
                e.printStackTrace();
            }

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> result = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);// 0表示获得第一个泛型的具体类型
        return result;
    }

    private void beginTransaction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && dbManager.getDatabase().isWriteAheadLoggingEnabled()) {
            dbManager.getDatabase().beginTransactionNonExclusive();
        } else {
            dbManager.getDatabase().beginTransaction();
        }
    }

    private void setTransactionSuccessful() {
        dbManager.getDatabase().setTransactionSuccessful();
    }

    private void endTransaction() {
        dbManager.getDatabase().endTransaction();
    }

    private void createTableIfNotExist(TableEntity<?> table) throws DbException {
        if (!table.tableIsExist()) {
            synchronized (table.getClass()) {
                if (!table.tableIsExist()) {
                    SqlInfo sqlInfo = SqlInfoBuilder.buildCreateTableSqlInfo(table);
                    dbManager.execNonQuery(sqlInfo);
                    String execAfterTableCreated = table.getOnCreated();
                    if (!TextUtils.isEmpty(execAfterTableCreated)) {
                        dbManager.execNonQuery(execAfterTableCreated);
                    }
                    DbManager.TableCreateListener listener = dbManager.getDaoConfig().getTableCreateListener();
                    if (listener != null) {
                        listener.onTableCreated(dbManager, table);
                    }
                }
            }
        }
    }
}
