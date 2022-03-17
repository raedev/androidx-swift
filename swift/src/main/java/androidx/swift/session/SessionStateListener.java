package androidx.swift.session;

/**
 * 会话状态改变回调监听
 * 当用户信息发生改变时会触发该接口发出通知
 */
public interface SessionStateListener {

    /**
     * 用户信息改变
     * @param sessionManager 用户管理器
     */
    void onUserInfoChanged(SessionManager sessionManager);
}
