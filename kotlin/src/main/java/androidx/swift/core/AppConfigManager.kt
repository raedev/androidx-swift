package androidx.swift.core

import android.content.Context
import android.content.SharedPreferences
import androidx.swift.AndroidSwift
import androidx.swift.util.GsonUtils
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

/**
 * 配置管理
 * @author RAE
 * @date 2022/06/18
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object AppConfigManager {

    /**
     *  获取配置文件
     * @param T
     * @param cls
     */
    fun <T : Any> getConfig(kcls: KClass<T>, name: String? = null): T {
        val cls = kcls.java
        val context = AndroidSwift.context
        val configName = name ?: cls.name.replace(".", "_")
        val preferences = context.getSharedPreferences(configName, Context.MODE_PRIVATE)
        if (!cls.isInterface) {
            throw ClassCastException("配置类${cls.name}必须定义为接口")
        }

        @Suppress("UNCHECKED_CAST")
        return Proxy.newProxyInstance(
            cls.classLoader, arrayOf(cls), ConfigInvocationHandler(preferences)
        ) as T
    }

    private class ConfigInvocationHandler(
        private val preferences: SharedPreferences
    ) : InvocationHandler {

        private val gson = GsonUtils.getGson()

        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
            val methodName = method.name
            // GET
            if (methodName.startsWith("get")) {
                return invokeGetMethod(methodName.substring(3), args?.get(0), method.returnType)
            }
            // SET
            if (methodName.startsWith("set")) {
                invokeSetMethod(methodName.substring(3), args?.get(0))
                return null
            }
            // default method
            when (methodName) {
                "remove" -> preferences.edit().remove(args!![0].toString()).apply()
                "clear" -> preferences.edit().clear().apply()
                "toString" -> return gson.toJson(preferences.all)
            }

            throw IllegalStateException("配置接口不支持定义方法：$methodName")
        }

        private fun invokeSetMethod(key: String, value: Any?) {
            val editor = preferences.edit()
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Float -> editor.putFloat(key, value)
                is Long -> editor.putLong(key, value)
                is Boolean -> editor.putBoolean(key, value)
                else -> editor.putString(key, gson.toJson(value))
            }
            editor.apply()
        }

        private fun invokeGetMethod(name: String, defaultValue: Any?, returnType: Class<*>): Any? {
            return when (returnType) {
                String::class.java -> preferences.getString(name, defaultValue as String?)
                Int::class.java -> preferences.getInt(name, (defaultValue ?: 0) as Int)
                Float::class.java -> preferences.getFloat(name, (defaultValue ?: 0f) as Float)
                Long::class.java -> preferences.getLong(name, (defaultValue ?: 0) as Long)
                Boolean::class.java -> preferences.getBoolean(name, defaultValue as Boolean)
                else -> gson.fromJson(preferences.getString(name, null), returnType)
            }
        }

    }
}

