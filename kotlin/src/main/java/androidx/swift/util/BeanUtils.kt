package androidx.swift.util

import com.google.gson.reflect.TypeToken

/**
 * 实体Bean工具类
 * @author RAE
 * @date 2022/06/28
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object BeanUtils {

    /** 对象转JSON */
    fun toJson(obj: Any?): String {
        return obj?.let { GsonUtils.toJson(it) } ?: ""
    }

    /** JSON转对象 */
    fun <T> toBean(json: String, cls: Class<T>): T {
        return GsonUtils.fromJson(json, cls)
    }

    /** JSON转集合 */
    fun <T> toList(json: String, cls: Class<T>): List<T> {
        val type = object : TypeToken<List<T>>() {}.type
        return GsonUtils.fromJson(json, type)
    }

    /** JSON转字典 */
    fun <T> toMap(json: String): Map<String, Any?> {
        val type = object : TypeToken<Map<String, Any?>>() {}.type
        return GsonUtils.fromJson(json, type)
    }

    fun toStringMap(json: String): Map<String, String?> {
        val type = object : TypeToken<Map<String, String?>>() {}.type
        return GsonUtils.fromJson(json, type)
    }

    /** 复制一份对象，新的对象可以是其他类型，会将对象的值赋值到新的对象中去 */
    fun <T : Any> copy(obj: Any, cls: Class<T>): T {
        val json = toJson(obj)
        return toBean(json, cls)
    }

    /** 复制一份当前对象 */
    fun <T : Any> copy(obj: T): T {
        return copy(obj, obj.javaClass)
    }

}