package com.github.raedev.swift.session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public class SessionManagerImpl extends SessionManager {

    private final Context mContext;
    private final String mSessionName;
    // 用户信息获取比较频繁，作为一个字段去维护
    protected ISessionInfo mUserInfo;

    protected SessionManagerImpl(Context context, String sessionName) {
        this.mContext = context;
        this.mSessionName = sessionName;
    }

    private SharedPreferences getSharedPreferences() {
        return this.mContext.getSharedPreferences(this.mSessionName, Context.MODE_PRIVATE);
    }

    @Override
    public void forgot() {
        mUserInfo = null; // 清除本地缓存字段
        getSharedPreferences().edit().clear().apply();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends ISessionInfo> T getUserInfo() {
        if (mUserInfo != null) return (T) mUserInfo; // 缓存用户信息
        String json = getSharedPreferences().getString("userInfo", null);
        if (json == null) return null;
//        mUserInfo = fromJson(json);
        return (T) mUserInfo;
    }

    @Override
    protected <T extends ISessionInfo> void onSaveUserInfo(T userInfo) {
        if (userInfo == null) return;
        String json = toJson(userInfo);
        getSharedPreferences().edit().putString("userInfo", json).apply();
        mUserInfo = userInfo;
    }

    private <T extends ISessionInfo> String toJson(T user) {
        JSONObject object = new JSONObject();

        return "";
    }

}
