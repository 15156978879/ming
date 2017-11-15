package com.coffeers.app.modular.index.modules.services.impl;

import com.coffeers.app.framework.annotation.Inject;
import com.coffeers.app.framework.annotation.Service;
import com.coffeers.app.framework.dao.Cnd;
import com.coffeers.app.modular.index.modules.dao.HelloDao;
import com.coffeers.app.modular.index.modules.models.Hello;
import com.coffeers.app.modular.index.modules.services.HelloService;

/**
 * Created by Administrator on 2017/8/18 0018.
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Inject
    private HelloDao helloDao;


    @Override
    public Hello select(Hello hello, Cnd cnd) {
        System.out.print(helloDao);
        return helloDao.select1(hello,cnd);
    }

    @Override
    public Hello insert(Hello hello, Cnd cnd) {
        return helloDao.insert1(hello,cnd);
    }
}
