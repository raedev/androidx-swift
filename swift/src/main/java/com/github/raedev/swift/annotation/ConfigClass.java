package com.github.raedev.swift.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 配置文件注解
 * Created by RAE on 2020/10/17.
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigClass {
    /**
     * 配置文件名称，如果不配置，默认为类名
     */
    String value() default "";
}
