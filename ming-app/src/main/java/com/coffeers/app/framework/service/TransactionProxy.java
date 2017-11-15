package com.coffeers.app.framework.service;

import com.coffeers.app.framework.annotation.Transaction;
import com.coffeers.app.framework.aop.Proxy;
import com.coffeers.app.framework.aop.ProxyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Method;

/**
 * 第二步：让它实现Proxy接口，在doProxy方法中完成事务控制的相关逻辑
 * 事务代理切面类
 *
 * 通过ProxyChain对象可获取目标方法，进而判断该方法是否带有Transaction注解。
 * 首先调用DatabaseHelper.beginTransaction方法开启事务，
 * 然后调用ProxyChain对象的doProxyChain方法执行目标方法，
 * 接着调用DatabaseHelper.commintTransaction提交事务，
 * 或者在一次处理中调用DatabaseHelper.rollbackTransaction方法回滚事务，
 * 最后别忘记移除FLAG_HOLDER本地线程变量中的标志
 */
public class TransactionProxy implements Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);
    //本地线程变量，它是一个标志,保证同一线程中事务控制逻辑只会执行一次
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);
            try {
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            } catch (Exception e) {
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            } finally {
                FLAG_HOLDER.remove();
            }

        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
