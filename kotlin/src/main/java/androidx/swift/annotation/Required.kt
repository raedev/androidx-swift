package androidx.swift.annotation

/**
 * 必填项
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Required(

    /**
     * 是否为必填，默认是
     */
    val value: Boolean = true,

    /**
     * 但非必填的时候提示的消息
     */
    val message: String = ""
)
