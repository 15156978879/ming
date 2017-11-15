package com.coffeers.app.framework.annotation;

import java.lang.annotation.*;

/**
 * 返回视图（默认beetl）
 * json,jsp,beetl,freemarker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Result {
    String value() default "";
}
