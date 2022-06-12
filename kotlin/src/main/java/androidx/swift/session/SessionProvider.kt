package androidx.swift.session

/**
 * @author RAE
 * @date 2022/06/12
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface SessionProvider {


    /** 获取当前登录的用户 */
    fun <T> getUser(): T?

    /** 是否已经登录 */
    fun <T> isLogin(): Boolean = getUser<T>() != null
}