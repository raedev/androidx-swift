package androidx.swift.text

/**
 * 解析接口
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface IParser<T> {

    /**
     * 将文本转换成一个对象
     */
    fun parse(text: String): T
}