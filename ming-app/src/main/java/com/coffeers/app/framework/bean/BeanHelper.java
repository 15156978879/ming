package com.coffeers.app.framework.bean;

import com.coffeers.app.framework.loadclass.ClassHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jack on 2017/5/23.
 * bean 助手类,获取类的实例
 * 我们需要获取所有被smart框架管理的bean类，此时需要调用ClassHelper类的getBeanClassSet方法，随后需要循环调用ReflectionUtil的newInstance方法，根据类来实例化对象，最后将每次创建的对象保存在一个静态的Map<Class<?>,Object>中，我们需要随时获取该Map，还需要根据该Map的key（类名）去获取对应的value
 */
public class BeanHelper {
    /**
     * 定义bean映射，（用于存放bean类与bean实例的映射关系）
     * key : class
     * value:对应的实例
     * 单例？
     */
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    private static final Map<Class<?>,Object> BEAN_MAP_INTERFACE = new HashMap<Class<?>, Object>();
    /**
     * 静态代码块
     */
    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet) {
            //创建所有class的实例(自己定义的注解类)
            Object object = ReflectionUtil.newInstance(beanClass);
            //给接口注入实现类
            if(object.getClass().getName().contains("impl")||object.getClass().getName().contains("Impl")){
                String interfaceString = object.getClass().getName().substring(0,object.getClass().getName().length()-4);
                interfaceString = interfaceString.replaceAll( ".impl","");
                Class cla = null;
                try {
                    cla = Class.forName(interfaceString);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                BEAN_MAP_INTERFACE.put(cla,object);
            }else{
                BEAN_MAP_INTERFACE.put(beanClass,object);
            }
            BEAN_MAP.put(beanClass,object);

        }
    }

    /**
     * 获取Bean映射容器
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 获取Bean映射容器
     */
    public static Map<Class<?>,Object> getBeanMapINTERFACE(){
        return BEAN_MAP_INTERFACE;
    }

    /**
     * 获取Bean实例
     */
    public static <T> T getBean(Class<?> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class:"+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置Bean实例
     * @param cls
     * @param obj
     */
    public static void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }
}