package androidx.swift.annotation

import androidx.swift.text.IParser
import kotlin.reflect.KClass

/**
 * 解析器
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Parser<T>(

    /**
     * 解析器对应的处理器
     */
    val value: KClass<IParser<T>>,

    /**
     * 解析器的类型，如: JSON、XML...
     */
    val type: Int = -1
)
