package com.coffeers.app.framework.controller;


import com.coffeers.app.framework.annotation.At;
import com.coffeers.app.framework.ioc.ArrayUtil;
import com.coffeers.app.framework.ioc.CollectionUtil;
import com.coffeers.app.framework.loadclass.ClassHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 通过ClassHelper我们可以获取所有定义了Controller注解的类，
 * 可以通过反射获取该类中所有带有Action注解的方法，
 * 获取Action注解中的请求表达式，进而获取请求方法与请求路径，
 * 封装一个请求对象（Request）和处理对象（Handler），
 * 最后将Request与Handler建立一个映射关系，存入一个Action Map中，
 * 并提供一个可以根据请求方法与请求路径获取处理对象的方法。
 */
public final class ControllerHelper {
    /**
     * 用于存放请求与处理器的映射关系（简称Aciotn Map）
     */
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();
    static {
        /**
         * 获取所有的Controller类
         */
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)){
            //遍历这些Controller
            for (Class<?> controllerClass : controllerClassSet) {
                //获取类上mapping
                String controllerMapping = "";
                if (controllerClass.isAnnotationPresent(At.class)) {
                    At at = controllerClass.getAnnotation(At.class);
                    controllerMapping = at.value();
                }

                //获取Controller类中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)){
                    //遍历Controller类中的方法
                    for (Method method : methods) {
                        //判断当前方法是否有At注解
                        if (method.isAnnotationPresent(At.class)){
                            //从At注解中获取URL映射规则
                            At at = method.getAnnotation(At.class);
                            String mapping = controllerMapping+at.value();
                            String requestMethod = at.method();
                            //验证URL映射规则
                            // ^[/]((([a-zA-Z0-9]|[_-]))+?/)*([^/]([a-zA-Z0-9]|[_-]))+$
                            if (mapping.matches("^[/]((([a-zA-Z0-9]|[_-]))+?/)*([^/]([a-zA-Z0-9]|[_-]))?[a-zA-Z0-9]+$")){
                                if("".equals(requestMethod)){
                                    Request request1 = new Request("GET",mapping);//获取请求方法
                                    Handler handler1 = new Handler(controllerClass,method);//获取请求路径
                                    ACTION_MAP.put(request1,handler1);//初始化Action Map

                                    Request request2 = new Request("POST",mapping);//获取请求方法
                                    Handler handler2 = new Handler(controllerClass,method);//获取请求路径
                                    ACTION_MAP.put(request2,handler2);//初始化Action Map

                                    Request request3 = new Request("PUT",mapping);//获取请求方法
                                    Handler handler3 = new Handler(controllerClass,method);//获取请求路径
                                    ACTION_MAP.put(request3,handler3);//初始化Action Map

                                    Request request4 = new Request("DELETE",mapping);//获取请求方法
                                    Handler handler4 = new Handler(controllerClass,method);//获取请求路径
                                    ACTION_MAP.put(request4,handler4);//初始化Action Map
                                }else{
                                    //获取请求方法与请求路径
                                    Request request = new Request(requestMethod.toUpperCase(),mapping);
                                    Handler handler = new Handler(controllerClass,method);
                                    //初始化Action Map
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 获取Handler
     */
    public static Handler getHandler(String requestMethod,String requstPath){
        Request request = new Request(requestMethod,requstPath);
        return ACTION_MAP.get(request);
    }
}
/*
* 可见，我们在ControllerHelper中封装了一个ActionMap，
* 通过它来存放Request与Handler之间的映射关系，
* 然后通过ClassHelper来获取所有带有Controller注解的类，
* 接着遍历这些Controller类，从Action注解中提取URL，
* 最后初始化Request与Handler之间的映射关系
* */