package androidx.swift.session

/**
 * Session状态监听
 * @author RAE
 * @date 2022/08/21
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface SessionStateListener {

    /**
     * 用户信息发生改变
     * @param delegate Session管理器
     * @param oldUser 旧的用户
     * @param newUser 新的用户
     */
    fun <T : Any> onUserInfoChanged(delegate: SessionDelegate, oldUser: T?, newUser: T?)
}