package androidx.swift.annotation

/**
 * 显示注解
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Display(

    /**
     * 显示名称
     */
    val value: String = "",

    /**
     * 显示的字段
     */
    val name: String = ""
)
