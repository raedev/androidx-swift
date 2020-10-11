package com.github.raedev.swift.session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class SessionManager {

    protected static WeakReference<SessionManager> managerWeakReference;

//    /**
//     * 获取默认的会话管理器
//     */
//    public static SessionManager getDefault() {
//
//        if (managerWeakReference == null || managerWeakReference.get() == null) {
//            synchronized (SessionManager.class) {
//                if (managerWeakReference == null || managerWeakReference.get() == null) {
//                    managerWeakReference = new WeakReference<SessionManager>(new PreferencesSessionManager(sConfig));
//                }
//            }
//        }
//
//        return managerWeakReference.get();
//    }


    @Nullable
    private List<SessionStateListener> mSessionStateListeners;

    protected SessionManager() {
    }

    /**
     * 是否登录
     */
    public boolean isLogin() {
        return getUserInfo() != null;
    }


    /**
     * 清除会话信息，即退出登录。
     */
    public abstract void forgot();

    /**
     * 获取当前登录的用户信息，在调用该方法之前请先调用{@link #isLogin()}来判断是否登录
     */
    @Nullable
    public abstract <T extends ISessionInfo> T getUserInfo();

    /**
     * 设置用户信息
     */
    protected abstract <T extends ISessionInfo> void onSaveUserInfo(T userInfo);

    /**
     * 设置当前用户信息
     */
    public <T extends ISessionInfo> void setUserInfo(T user) {
        this.onSaveUserInfo(user);
        this.notifyUserInfoChanged();
    }

    /**
     * 添加Session状态改变通知，在不用时候记得移除掉，避免内存泄漏
     */
    public void addSessionStateListener(@NonNull SessionStateListener listener) {
        if (mSessionStateListeners != null) {
            mSessionStateListeners.add(listener);
        }
    }

    /**
     * 移除Session状态改变通知
     */
    public void removeSessionStateListener(@NonNull SessionStateListener listener) {
        if (mSessionStateListeners != null) {
            mSessionStateListeners.remove(listener);
        }
    }

    protected void notifyUserInfoChanged() {
        if (mSessionStateListeners == null) return;
        for (SessionStateListener listener : mSessionStateListeners) {
            listener.onUserInfoChanged(this);
        }
    }

}
