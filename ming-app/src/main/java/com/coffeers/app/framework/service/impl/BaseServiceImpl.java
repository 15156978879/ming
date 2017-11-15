package com.coffeers.app.framework.service.impl;

import com.coffeers.app.framework.annotation.Service;
import com.coffeers.app.framework.service.BaseService;

/**
 * Created by Administrator on 2017/8/18 0018.
 */
@Service
public class BaseServiceImpl<T> implements BaseService<T> {

    private T t;

    @Override
    public Object count(Object o) {
        return null;
    }

    @Override
    public int count(String tableName) {
        System.out.println(t.getClass().getName());
        return 0;//baseDao.getForList("select count(*) from "+tableName,null).size();
    }

}
