package com.coffeers.app.modular.index.modules.dao.impl;

import com.coffeers.app.framework.annotation.Dao;
import com.coffeers.app.framework.dao.Cnd;
import com.coffeers.app.framework.dao.impl.BaseDaoImpl;
import com.coffeers.app.modular.index.modules.dao.HelloDao;
import com.coffeers.app.modular.index.modules.models.Hello;


/**
 * Created by Administrator on 2017/8/18 0018.
 */
@Dao
public class HelloDaoImpl extends BaseDaoImpl<Hello> implements HelloDao {

    @Override
    public Hello select1(Hello hello,Cnd cnd) {
        return select(hello,cnd);
    }
    @Override
    public Hello insert1(Hello hello,Cnd cnd) {
        //add(hello);
        return select(hello, cnd);
    }
}
