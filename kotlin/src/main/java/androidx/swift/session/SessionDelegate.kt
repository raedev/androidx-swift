package androidx.swift.session

/**
 * Session 委托接口
 * @author RAE
 * @date 2022/06/12
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface SessionDelegate {

    /** 获取当前登录的用户 */
    fun <T> getUser(): T?

    /** 设置当前登录用户 */
    fun <T> setUser(user: T)

    /** 获取当前登录的用户，为空会报异常，请调用[isLogin]进行前置判断 */
    fun <T> requireUser(): T = getUser<T>()!!

    /** 是否已经登录 */
    fun <T> isLogin(): Boolean = getUser<T>() != null

    /** 退出登录 */
    fun forgot()


    /** 清除所有信息 */
    fun clear()

    /**
     *  存储Session额外的值
     * @param key 存储的键
     * @param value 存储的值，自动推断类型进行保存
     */
    fun put(key: String, value: Any)

    /**
     * 获取Session额外的值
     *
     * @param key 存储的键
     */
    fun <V> get(key: String, cls: Class<V>): V?

    /**
     * 移除Session值
     *
     * @param key 存储的键
     */
    fun remove(key: String)

    /**
     * 获取Session额外的值
     *
     * @param key 存储的键
     * @param defaultValue 当为空时返回的默认值
     */
    fun getString(key: String, defaultValue: String? = null): String?

    /**
     * 获取Session额外的值
     *
     * @param key 存储的键
     * @param defaultValue 当为空时返回的默认值
     */
    fun getInt(key: String, defaultValue: Int? = null): Int?

    /**
     * 获取Session额外的值
     *
     * @param key 存储的键
     * @param defaultValue 当为空时返回的默认值
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    /**
     * 获取Session额外的值
     *
     * @param key 存储的键
     * @param defaultValue 当为空时返回的默认值
     */
    fun getFloat(key: String, defaultValue: Float? = null): Float?

    /**
     * 获取Session额外的值
     *
     * @param key 存储的键
     * @param defaultValue 当为空时返回的默认值
     */
    fun getLong(key: String, defaultValue: Long? = null): Long?

    /**
     * 添加用户监听
     */
    fun addSessionListener(listener: SessionStateListener)

    /**
     * 移除用户监听
     */
    fun removeSessionListener(listener: SessionStateListener)
}