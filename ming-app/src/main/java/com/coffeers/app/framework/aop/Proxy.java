package com.coffeers.app.framework.aop;

/**
 * 第一步：在框架中添加一个名为Proxy的接口
 * 代理接口
 */
public interface Proxy {
    //可将多个代理通过一条链子串起来，一个个地去执行，执行顺序取决于添加到链式上的先后顺序
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}