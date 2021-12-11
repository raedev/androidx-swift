package com.github.raedev.swift.session;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户会话管理器
 * <p>用于管理用户的整个生命状态，如：登录、登出、获取用户信息;</p>
 * <p>如果一个应用有多个会话管理器的情况，请手动管理默认SessionManager获取方式，比如新建一个App1SessionManager、App2SessionManager来初始化</p>
 * @author rae
 * @since 2020/12/30
 */
public abstract class SessionManager {

    @Nullable
    protected static SessionManager sSessionManager;

    public static class Builder {

        private final Context mContext;
        private String mSessionName;
        private Class<?> mUserClass;
        private boolean mIsJsonMode;

        public Builder(Context context) {
            mContext = context.getApplicationContext();
        }

        public Builder setSessionName(String name) {
            mSessionName = name;
            return this;
        }

        public Builder setUserClass(Class<?> cls) {
            mUserClass = cls;
            return this;
        }

        public Builder setJsonMode(boolean isJsonMode) {
            mIsJsonMode = isJsonMode;
            return this;
        }

        public SessionManager build() {
            if (mUserClass == null) {
                throw new NullPointerException("session user class can't null");
            }
            if (TextUtils.isEmpty(mSessionName)) {
                mSessionName = mContext.getPackageName() + ".session";
            }
            if (mIsJsonMode) {
                return new JsonFileSessionManager(mContext, mSessionName, mUserClass);
            }
            return new SharedPreferencesSessionManager(mContext, mSessionName, mUserClass);
        }
    }

    /**
     * 初始化默认的用户信息
     * @param context Context
     * @param userInfoClass 用户信息类
     * @deprecated 请使用 {@link Builder} 来初始化，调用{@link #setSessionManager(SessionManager)} 设置默认会话管理器
     */
    @Deprecated
    public static void initDefaultSessionManager(Context context, Class<?> userInfoClass) {
        SessionManager sessionManager = new Builder(context).setUserClass(userInfoClass).build();
        setSessionManager(sessionManager);
    }

    public static void init(Builder builder) {
        setSessionManager(builder.build());
    }

    /**
     * 设置默认的会话管理器
     * @param sessionManager 会话管理器
     */
    public static void setSessionManager(SessionManager sessionManager) {
        if (sSessionManager == null) {
            synchronized (SessionManager.class) {
                if (sSessionManager == null) {
                    sSessionManager = sessionManager;
                }
            }
        }
    }

    /**
     * 获取默认的会话管理器
     */
    @NonNull
    public static SessionManager getDefault() {
        if (sSessionManager == null) {
            throw new NullPointerException("default session manager is null.");
        }
        return sSessionManager;
    }


    @Nullable
    private List<SessionStateListener> mSessionStateListeners;
    private final String mSessionName;

    protected SessionManager(String sessionName) {
        this.mSessionName = sessionName;
    }

    /**
     * 获取当前会话管理器名称
     * @return 名称
     */
    public String getSessionName() {
        return mSessionName;
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
     * @return 用户信息
     */
    @Nullable
    public abstract <T> T getUserInfo();

    /**
     * 设置用户信息
     * @param userInfo 用户信息
     * @param <T> 用户实体
     */
    protected abstract <T> void onSaveUserInfo(T userInfo);

    /**
     * 设置当前用户信息
     */
    public <T> void setUserInfo(T user) {
        this.onSaveUserInfo(user);
        this.notifyUserInfoChanged();
    }

    /**
     * 添加Session状态改变通知，在不用时候记得移除掉，避免内存泄漏
     */
    public void addSessionStateListener(@NonNull SessionStateListener listener) {
        if (mSessionStateListeners == null) {
            mSessionStateListeners = new ArrayList<>();
        }
        mSessionStateListeners.add(listener);
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
        if (mSessionStateListeners == null) {
            return;
        }
        for (SessionStateListener listener : mSessionStateListeners) {
            listener.onUserInfoChanged(this);
        }
    }

}
