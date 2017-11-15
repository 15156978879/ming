package com.coffeers.app.framework.aop;

import com.coffeers.app.framework.annotation.Aspect;
import com.coffeers.app.framework.annotation.Service;
import com.coffeers.app.framework.bean.BeanHelper;
import com.coffeers.app.framework.loadclass.ClassHelper;
import com.coffeers.app.framework.service.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.annotation.Annotation;
import java.util.*;
/**
 * 第5步：加载AOP框架，然后将其添加到HelperLoader类中
 *
 * 在AopHelper中我们需要获取所有的目标类及其被拦截的切面类实例，
 * 并通过ProxyManager的createProxy方法来创建代理对象，最后将其放入Bean Map中
 */
public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);
    /*
    * 4.静态块来初始化整个AOP框架
    * 获取代理类及其目标类集合的映射关系，进一步获取目标类与代理对象列表的映射关系，
    * 进而遍历这个映射关系，从中获取目标类与代理对象列表，调用ProxyManager.createProxy方法获取代理对象，
    * 调用BeanHelper.setBean方法，将该代理对象重新放入Bean Map中
    * */
    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("load aop failure", e);
        }
    }

    //2.创建map:获取需要被代理的 所有 class 对象
    // 紧接着我们需要获取代理类及其目标类集合之间的映射关系，一个代理类可对应一个或多个目标类。
    // 需要强调的是，这里所说的代理类指的是切面类
    /*
    * 代理类需要扩展AspectProxy抽象类，还需要带有Aspect注解，
    * 只有满足这两个条件，才能根据Aspect注解中所定义的注解属性去获取该注解所对应的目标类集合，
    * 然后才能建立代理类与目标类集合之间的映射关系，最终返回这个映射关系
    * */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
//        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
//        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySupper(AspectProxy.class);
//        for (Class<?> proxyClass: proxyClassSet) {
//            // 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，
//            // 或是否是其超类或超接口。如果是则返回 true；否则返回 false
//            LOGGER.info(":::",proxyClass.isAssignableFrom(Aspect.class));
//            if (proxyClass.isAssignableFrom(Aspect.class)){
//                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
//                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
//                proxyMap.put(proxyClass,targetClassSet);
//            }
//        }
//        addAspectProxy(proxyMap);
//        addTransactionProxy(proxyMap);
//        return proxyMap;
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<>();
        // 获取 所有的切面代理类 的 子类
        Set<Class<?>> aspectProxySet = ClassHelper.getClassSetBySupper(AspectProxy.class);
        for (Class<?> cls : aspectProxySet) {
            if(cls.isAnnotationPresent(Aspect.class)){// 判断该class 是否有 aspect注解(是否是一个切面标注)
                Aspect aspect = cls.getAnnotation(Aspect.class); // 获取注解类
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect); // 由于 aspect 的值是接收一个 注解类,所以这里是获取到 使用该注解类的所有class
                proxyMap.put(cls,targetClassSet);
            }
        }
        return proxyMap;
    }
    //添加切面
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySupper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    //在框架中添加事务代理机制
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

    //1.编写一个带有Aspect注解的所有类
    //获取Aspect注解中设置的注解类，若该注解类不是Aspect类，则可调用ClassHelper的getClassSetByAnnotation方法获取相关的类，并把这些类放入目标类集合中，最终返回这个集合。
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }


    /**
     * 3.根据这个关系分析出目标类与代理对象列表之间的映射关系
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) targetClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }




}