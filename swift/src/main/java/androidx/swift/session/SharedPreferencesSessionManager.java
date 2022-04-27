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
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.swift.util.JsonUtils;


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

    private void entry() {

    }
}
