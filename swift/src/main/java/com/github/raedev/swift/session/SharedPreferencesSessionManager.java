package com.github.raedev.swift.session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.EncryptUtils;
import com.github.raedev.swift.utils.JsonUtils;
import com.github.raedev.swift.utils.TextUtils;

import java.nio.charset.StandardCharsets;

/**
 * 偏好保存用户信息
 * @author rae
 */
public class SharedPreferencesSessionManager extends SessionManager {

    private static final byte[] AES_KEY = "4OM5BxgDQp4uHfwe30i1lzTyY4jeNzBJ".getBytes(StandardCharsets.UTF_8);
    private static final byte[] AES_IV = "7nfFIJNfr1LXtem8".getBytes(StandardCharsets.UTF_8);

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
        // 兼容旧版，移除没有加密的用户信息
        mSharedPreferences.edit().remove("userInfo").apply();
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
        String json = mSharedPreferences.getString("sessionUserInfo", null);
        if (json == null) {
            return null;
        }
        String decrypt = decrypt(json);
//        Log.i("rae", "源数据：" + json);
//        Log.i("rae", "解密后：" + decrypt);
        mUserInfo = JsonUtils.fromJson(decrypt, mUserClass);
        return (T) mUserInfo;
    }

    @Override
    protected <T> void onSaveUserInfo(T userInfo) {
        if (userInfo == null) {
            return;
        }
        String json = encrypt(JsonUtils.toJson(userInfo));
        // 加密用户信息
//        Log.i("Rae", "保存用户信息：" + json);
        mSharedPreferences.edit().putString("sessionUserInfo", json).apply();
        mUserInfo = userInfo;
    }

    private String encrypt(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        return EncryptUtils.encryptAES2HexString(value.getBytes(StandardCharsets.UTF_8), AES_KEY, "AES", AES_IV);
    }

    private String decrypt(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        return new String(EncryptUtils.decryptHexStringAES(value, AES_KEY, "AES", AES_IV), StandardCharsets.UTF_8);
    }
}
