package com.github.raedev.swift.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>配置文件注解，自动生成配置文件，根据接口生成get、set字段。</p>
 * <p>命名格式：</p>
 * <ul>
 *     <li>获取配置方法定义，如：String getTitle(); 有默认值的定义：String getTitle(String title,String defValue)</li>
 *     <li>设置配置方法定义，如：void setTitle(String title); 只能一个方法参数。</li>
 *     <li>Boolean 类型配置方法定义，如：boolean isVip(); 设置方法一样： void setVip(boolean isVip)</li>
 * </ul>
 * @author RAE
 * @date 2020/10/17
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface Configuration {

    /**
     * 配置文件名称，如果不配置，默认为类名
     */
    String value() default "";

    /**
     * 是否根据版本来改变
     */
    boolean changed() default false;
}
