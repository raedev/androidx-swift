package androidx.swift.session

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences 保存实现
 * @author RAE
 * @date 2022/06/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class SharedPreferencesSessionDelegate<T>(
    context: Context,
    name: String?,
    userClass: Class<T>
) : DefaultSessionDelegate<T>(context, name, userClass) {


    private val preferences: SharedPreferences
        get() {
            // com_your_name_session.xml
            val sessionName = name ?: "${context.packageName.replace(".", "_")}_session"
            return context.getSharedPreferences(sessionName, Context.MODE_PRIVATE)
        }

    override fun onSaveUserInfo(user: T?) {
        when (user) {
            null -> preferences.edit().remove(userKey).apply()
            else -> {
                val value = if (enableEncrypt) encrypt(gson.toJson(user)) else gson.toJson(user)
                put(userKey, value)
            }
        }
    }

    override fun onLoadUser(): T? {
        val json = getString(userKey) ?: return null
        return gson.fromJson(if (enableEncrypt) decrypt(json) else json, userClass)
    }

    override fun clear() {
        preferences.edit().clear().apply()
    }

    override fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    override fun put(key: String, value: Any) {
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

    override fun <V> get(key: String, cls: Class<V>): V {
        return gson.fromJson(preferences.getString(key, null), cls)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    override fun getInt(key: String, defaultValue: Int?): Int {
        return preferences.getInt(key, defaultValue ?: 0)
    }

    override fun getBoolean(key: String, defaultValue: Boolean?): Boolean {
        return preferences.getBoolean(key, defaultValue ?: false)
    }

    override fun getFloat(key: String, defaultValue: Float?): Float {
        return preferences.getFloat(key, defaultValue ?: 0f)
    }

    override fun getLong(key: String, defaultValue: Long?): Long {
        return preferences.getLong(key, defaultValue ?: 0L)
    }
}