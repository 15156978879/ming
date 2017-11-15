package com.coffeers.app.framework.aop;

import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 第二步：
 * 代理链
 *
 * targetClass（目标类），targetObject（目标对象），
 * targetMethod（目标方法），methodProxy（方法代理），
 * methodParams（方法参数），此外还包括了proxyList（代理列表），
 * proxyIndex（代理索引），这些成员变量在构造中进行初始化，并提供了几个重要的获值方法。
 *
 * 需要注意的是MethodProxy这个类，
 * 它是CGLib开源项目为我们提供的一个方法代理对象在doProxyChain方法中被使用。
 *   添加cglib类库，实现动态代理
     <dependency>
         <groupId>cglib</groupId>
         <artifactId>cglib</artifactId>
         <version>2.2.2</version>
     </dependency>
 */
public class ProxyChain {
    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;
    private List<Proxy> proxyList = new ArrayList<Proxy>();
    private int proxyIndex = 0; //代理索引

    /**
     * 构造函数
     * @param targetClass  目标类
     * @param targetObject 目标对象
     * @param targetMethod 目标方法
     * @param methodProxy  方法代理
     * @param methodParams 方法参数
     * @param proxyList    代理列表
     */
    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }


    /*
    * 在该方法中，我们通过proxyIndex来充当代理对象的计数器，
    * 若未达到proxyList的上限，则从proxyList中取出相应的Proxy对象，并调用其doProxy方法。
    * 在Proxy接口的实现中会提供相应的横切逻辑，并调用doProxyChain方法，
    * 随后将再次调用当前ProxyChain对象的doProxyChain方法，
    * 直到proxyIndex达到proxyList的上限为止，最后调用methodProxy的invokeSuper方法，
    * 执行目标对象的业务逻辑
    * */
    public Object doProxyChain()throws Throwable{
        Object methodResult;
        if (proxyIndex < proxyList.size()){
            //调用其doProxy方法
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        }else {
            methodResult = methodProxy.invokeSuper(targetObject,methodParams);
        }
        return methodResult;
    }
}
