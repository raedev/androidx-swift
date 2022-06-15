package androidx.swift.session

import android.content.Context
import androidx.swift.util.FileIOUtils
import androidx.swift.util.FileUtils
import com.google.gson.reflect.TypeToken
import java.io.File
import kotlin.concurrent.thread

/**
 * Json的Session委托实现
 * @author RAE
 * @date 2022/06/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class JsonSessionDelegate<T>(
    context: Context,
    name: String?,
    userClass: Class<T>
) : DefaultSessionDelegate<T>(context, name, userClass) {

    private var cacheJsonBean: MutableMap<String, Any>? = null

    private val filePath: String
        get() {
            val sessionName = name ?: "${context.packageName}.session"
            return File(context.cacheDir, "$sessionName.json").path
        }


    private val jsonBean: MutableMap<String, Any>
        get() {
            if (cacheJsonBean == null) {
                var json = FileIOUtils.readFile2String(filePath) ?: return mutableMapOf()
                json = if (enableEncrypt) decrypt(json) else json
                val typeToken = object : TypeToken<MutableMap<String, Any>>() {}
                cacheJsonBean = gson.fromJson(json, typeToken.type) ?: mutableMapOf()
            }
            return cacheJsonBean!!
        }

    @Synchronized
    private fun persisting() {
        thread {
            var json = gson.toJson(jsonBean)
            json = if (enableEncrypt) encrypt(json) else json
            FileIOUtils.writeFileFromString(filePath, json)
        }.run()
    }

    override fun onSaveUserInfo(user: T?) {
        when (user) {
            null -> remove(userKey)
            else -> put(userKey, user)
        }
    }

    override fun onLoadUser(): T? {
        return get(userKey, userClass)
    }

    override fun put(key: String, value: Any) {
        jsonBean[key] = value
        persisting()
    }

    override fun <V> get(key: String, cls: Class<V>): V? {
        val json = getString(key)
        return gson.fromJson(json, cls)
    }

    private inline fun <reified V> get(key: String): V {
        val value = jsonBean[key]
        return value as V
    }

    override fun getString(key: String, defaultValue: String?): String? = get(key)
    override fun getInt(key: String, defaultValue: Int?): Int = get(key)
    override fun getBoolean(key: String, defaultValue: Boolean?): Boolean = get(key)
    override fun getFloat(key: String, defaultValue: Float?): Float = get(key)
    override fun getLong(key: String, defaultValue: Long?): Long = get(key)

    @Synchronized
    override fun clear() {
        thread {
            FileUtils.delete(filePath)
        }.run()
    }

    override fun remove(key: String) {
        jsonBean.remove(key)
        persisting()
    }
}