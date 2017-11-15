package com.coffeers.app.framework.annotation.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 建表约束
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraints {
    // 是否主键
    boolean primaryKey() default false;
    // 是否为空
    boolean allowNull() default true;
    // 是否唯一
    boolean unique() default false;
}


