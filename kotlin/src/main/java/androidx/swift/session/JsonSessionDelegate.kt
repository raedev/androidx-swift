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
open class JsonSessionDelegate<T : Any>(
    context: Context,
    name: String?,
    userClass: Class<T>
) : DefaultSessionDelegate<T>(context, name, userClass) {

    companion object {
        val LOCK: Any = Any()
    }

    /**
     * JSON文件路径
     */
    protected val filePath: String by lazy {
        val sessionName = name ?: "${context.packageName}.session"
        File(context.cacheDir, if (enableEncrypt) sessionName else "$sessionName.json").path
    }

    private val jsonBean: MutableMap<String, Any> by lazy {
        var json = FileIOUtils.readFile2String(filePath) ?: return@lazy mutableMapOf()
        json = if (enableEncrypt) decrypt(json) else json
        val typeToken = object : TypeToken<MutableMap<String, Any>>() {}
        gson.fromJson(json, typeToken.type) ?: mutableMapOf()
    }

    @Synchronized
    protected open fun persisting() {
        thread {
            synchronized(LOCK) {
                var json = gson.toJson(jsonBean)
                json = if (enableEncrypt) encrypt(json) else json
                FileIOUtils.writeFileFromString(filePath, json)
            }
        }
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
        val json = gson.toJson(jsonBean[key])
        return gson.fromJson(json, cls)
    }

    @Synchronized
    override fun clear() {
        thread {
            synchronized(LOCK) {
                FileUtils.delete(filePath)
            }
        }
    }

    override fun remove(key: String) {
        jsonBean.remove(key)
        persisting()
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return if (jsonBean[key] == null) defaultValue else jsonBean[key].toString()
    }

    override fun getInt(key: String, defaultValue: Int?): Int? {
        return jsonBean[key]?.let { (it as Double).toInt() } ?: defaultValue
    }

    override fun getFloat(key: String, defaultValue: Float?): Float? {
        return jsonBean[key]?.let { (it as Double).toFloat() } ?: defaultValue
    }

    override fun getLong(key: String, defaultValue: Long?): Long? {
        return jsonBean[key]?.let { (it as Double).toLong() } ?: defaultValue
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return jsonBean[key]?.toString().toBoolean()
    }
}