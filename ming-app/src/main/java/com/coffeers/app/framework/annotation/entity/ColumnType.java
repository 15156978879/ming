package com.coffeers.app.framework.annotation.entity;

import com.coffeers.app.framework.enums.ColType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性类型及长度
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnType {
    ColType type() default ColType.AUTO;
    int width() default 0;
}