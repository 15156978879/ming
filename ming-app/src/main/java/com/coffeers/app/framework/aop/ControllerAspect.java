package com.coffeers.app.framework.aop;

import com.coffeers.app.framework.annotation.Aspect;
import com.coffeers.app.framework.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

/*
*   控制器切面
*   可以在目标方法执行前后添加其他需要执行的代码
* */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy{
    private static final Logger LOG = LoggerFactory.getLogger(ControllerAspect.class);
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