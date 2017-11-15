package com.coffeers.app.modular.index.modules.dao;

import com.coffeers.app.framework.dao.Cnd;
import com.coffeers.app.modular.index.modules.models.Hello;


/**
 * Created by Administrator on 2017/8/18 0018.
 */

public interface HelloDao {

    Hello select1(Hello hello, Cnd cnd);
    Hello insert1(Hello hello, Cnd cnd);
}
