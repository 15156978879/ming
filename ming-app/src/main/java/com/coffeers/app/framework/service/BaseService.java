package com.coffeers.app.framework.service;

/**
 * Created by Administrator on 2017/8/18 0018.
 */
public interface BaseService<T> {

    Object count(Object o);

    int count(String tableName);

}
