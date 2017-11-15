package com.coffeers.app.framework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 第三步：使用CGLib提供的Enhance的create方法来创建代理对象
 *        将intercept的参数传入ProxyChain的构造器中即可
 *
 * 代理管理器
 *  现在我们需要些一个类，让它提供一个创建代理对象的方法，
 *  输入一个目标类和一组Proxy接口实现，输出一个代理对象，
 *  将该类命名为ProxyManager，让它来创建所有的代理对象
 */
public class ProxyManager {
    public static <T> T createProxy(final Class<T> targetClass, final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObject,targetMethod,methodProxy,methodParams,proxyList).doProxyChain();
            }
        });
    }
}