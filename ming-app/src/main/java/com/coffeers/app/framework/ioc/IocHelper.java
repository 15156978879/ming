package com.coffeers.app.framework.ioc;

import com.coffeers.app.framework.annotation.Inject;
import com.coffeers.app.framework.bean.BeanHelper;
import com.coffeers.app.framework.bean.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by jack on 2017/5/23.
 * 依赖注入助手类，单例
 */
public class IocHelper {
    static {
        //获取所有的Bean类与Bean实例之间的关系（简称Bean Map）
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        Map<Class<?>, Object> beanMap_INTERFACE = BeanHelper.getBeanMapINTERFACE();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            //遍历beanMap
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                //从beanMap中获取bean类与bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取Bean类定义的所有成员变量（简称Bean Field）
                Field [] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)){
                    //遍历beanField
                    for (Field beanField : beanFields) {
                        //判断当前的Bean Field是否带有Inject注解
                        if (beanField.isAnnotationPresent(Inject.class)){
                            //在Bean Map中获取Bean Field对应的实例
                            Class<?> beanFieldClass = beanField.getType();//声明类型 成员变量的Class
                            Object beanFieldInstance = beanMap_INTERFACE.get(beanFieldClass);// 获取该类型的实例
                            if (beanFieldInstance != null){
                                //通过放射初始化beanField值
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);// 把对应的成员变量属性 赋值
                            }
                        }else{

                        }
                    }
                }


            }
        }
    }
}
