package androidx.swift.session

import android.content.Context
import androidx.swift.util.EncryptUtils
import androidx.swift.util.GsonUtils
import com.google.gson.Gson

/**
 * 默认的实现
 * @author RAE
 * @date 2022/06/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
abstract class DefaultSessionDelegate<T>(
    internal val context: Context,
    internal val name: String?,
    internal val userClass: Class<T>
) :
    SessionDelegate {

    protected val gson: Gson = GsonUtils.getGson()

    protected val userKey = "UserInfo"

    /** 缓存当前用户 */
    private var currentUser: T? = null

    /** DES加密的Key */
    var encryptKey = "NK7FH9&clDtMZgtC".toByteArray()

    /** DES加密偏移量 */
    var encryptIV = "D97WGpbc".toByteArray()

    /** 是否启用DES加密 */
    var enableEncrypt: Boolean = false

    override fun <V> setUser(user: V) {
        onSaveUserInfo(user as T)
        currentUser = user
    }

    override fun <V> getUser(): V? {
        return when (currentUser) {
            null -> {
                currentUser = onLoadUser()
                return currentUser as V
            }
            else -> currentUser as V
        }
    }

    override fun forgot() {
        currentUser = null
        onSaveUserInfo(null)
    }

    /** 加载用户 */
    protected abstract fun onLoadUser(): T?

    /** 持久化当前用户 */
    protected abstract fun onSaveUserInfo(user: T?)

    /** 加密 */
    protected fun encrypt(value: String): String {
        return EncryptUtils.encryptDES2HexString(
            value.toByteArray(), encryptKey, "CBC", encryptIV
        ).ifEmpty { value }
    }

    /** 解密 */
    protected fun decrypt(value: String): String {
        return EncryptUtils.encryptDES2HexString(
            value.toByteArray(), encryptKey, "CBC", encryptIV
        ).ifEmpty { value }
    }

}