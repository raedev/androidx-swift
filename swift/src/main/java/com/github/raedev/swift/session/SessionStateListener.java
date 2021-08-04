package com.github.raedev.swift.session;

/**
 * 会话状态改变
 */
public interface SessionStateListener {

    /**
     * 用户信息改变
     * @param sessionManager 用户管理器
     */
    void onUserInfoChanged(SessionManager sessionManager);
}
