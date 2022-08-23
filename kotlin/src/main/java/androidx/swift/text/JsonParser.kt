package androidx.swift.text

import androidx.swift.util.BeanUtils
import kotlin.reflect.KClass

/**
 * 默认的JSON解析器
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class JsonParser<T : Any>(protected val cls: KClass<T>) : IParser<T> {

    override fun parse(text: String): T {
        return BeanUtils.toBean(text, cls.java)
    }
}