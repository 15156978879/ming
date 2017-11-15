package com.coffeers.app.framework.dao;


import java.util.List;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
public interface BaseDao<T> {

    void delete(T t,Cnd cnd);
    void update(T t,Cnd cnd);
    T select(T t,Cnd cnd);

    void add(T t);
    void delete(T t);
    void update(T t);
    T select(T t);

    //////////////////////////////////////////////////

    T fetch(T t);//查询单个
    T fetch(T t,Cnd cnd);//有条件查询单个

    List<T> query(T t);//查询多个
    List<T> query(T t,Cnd cnd);//有条件查询多个

    //T update(T t);//更新单个
    //T update(T t,Cnd cnd);//有条件更新单个

    //void delete(T t);//删除单个
    //void delete(T t,Cnd cnd);//有条件删除单个

    //分页排序

    //原生sql
    List<T> nativeSql(String sql);
}
