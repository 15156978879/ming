package com.coffeers.app.modular.index.modules.services;

import com.coffeers.app.framework.dao.Cnd;
import com.coffeers.app.modular.index.modules.models.Hello;


/**
 * Created by Administrator on 2017/8/18 0018.
 */

public interface HelloService {

    Hello select(Hello hello, Cnd cnd);
    Hello insert(Hello hello, Cnd cnd);
}
