package com.coffeers.app.framework.aop;

import com.coffeers.app.framework.annotation.Aspect;
import com.coffeers.app.framework.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/*
*   控制器切面
*   可以在目标方法执行前后添加其他需要执行的代码
* */
@Aspect(Service.class)
public class ServiceAspect extends AspectProxy{
    private static final Logger LOG = LoggerFactory.getLogger(ServiceAspect.class);
    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOG.debug("------------begin--------------");
        LOG.debug(String.format("class: %s",cls.getName()));
        LOG.debug(String.format("method: %s",method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOG.debug(String.format("time: %s",System.currentTimeMillis()-begin));
        LOG.debug("------------end--------------");
    }
}