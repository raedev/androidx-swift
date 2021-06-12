package com.github.raedev.swift.session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.github.raedev.swift.utils.JsonUtils;

/**
 * 偏好保存用户信息
 * @author rae
 */
public class SharedPreferencesSessionManager extends SessionManager {

    /**
     * 用户信息获取比较频繁，作为一个字段去维护
     */
    protected Object mUserInfo;
    protected final SharedPreferences mSharedPreferences;
    protected final Class<?> mUserClass;

    protected SharedPreferencesSessionManager(Context context, String sessionName, Class<?> userClass) {
        super(sessionName);
        this.mSharedPreferences = context.getSharedPreferences(getSessionName(), Context.MODE_PRIVATE);
        this.mUserClass = userClass;
    }

    @Override
    public void forgot() {
        // 清除本地缓存字段
        mUserInfo = null;
        mSharedPreferences.edit().clear().apply();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getUserInfo() {
        if (mUserInfo != null) {
            // 缓存用户信息
            return (T) mUserInfo;
        }
        String json = mSharedPreferences.getString("userInfo", null);
        if (json == null) {
            return null;
        }
        mUserInfo = JsonUtils.fromJson(json, mUserClass);
        return (T) mUserInfo;
    }

    @Override
    protected <T> void onSaveUserInfo(T userInfo) {
        if (userInfo == null) {
            return;
        }
        String json = JsonUtils.toJson(userInfo);
        mSharedPreferences.edit().putString("userInfo", json).apply();
        mUserInfo = userInfo;
    }
}
