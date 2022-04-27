/*
 * Copyright 2022 RAE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.swift.session;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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
