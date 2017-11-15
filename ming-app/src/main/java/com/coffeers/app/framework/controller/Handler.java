package com.coffeers.app.framework.controller;

import java.lang.reflect.Method;

/**
 * Created by jack on 2017/5/24.
 * 封装At信息
 * 处理对象 class 与 对应的 处理方法
 */
public class Handler {
    /**
     * Controller类
     */
    private Class<?> controllerClass;
    /**
     * At方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}