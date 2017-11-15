package com.coffeers.app.framework.annotation;

import java.lang.annotation.*;

/**
 * 切面注解
 */
@Target(ElementType.TYPE)//设置该注解只能应用在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect{
    /**
     * 注解
     * @return
     */
    Class<? extends Annotation> value();
}
