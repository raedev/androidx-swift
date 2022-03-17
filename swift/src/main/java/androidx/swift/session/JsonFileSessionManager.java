package androidx.swift.session;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.swift.util.FileIOUtils;
import androidx.swift.util.FileUtils;
import androidx.swift.util.JsonUtils;

import java.io.File;

/**
 * Json文件保存用户信息
 * @author RAE
 * @date 2021/11/15
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JsonFileSessionManager extends SessionManager {

    private final File mJsonFile;
    private final Class<?> mUserClass;
    protected Object mUserInfo;


    protected JsonFileSessionManager(Context context, String sessionName, Class<?> userClass) {
        super(sessionName);
        mUserClass = userClass;
        mJsonFile = new File(context.getExternalCacheDir(), getSessionName());
    }

    @Override
    public void forgot() {
        mUserInfo = null;
        mJsonFile.delete();
    }

    @Nullable
    @Override
    public <T> T getUserInfo() {
        if (mUserInfo == null && mJsonFile.exists()) {
            // 从JSON文件中加载
            String json = FileIOUtils.readFile2String(mJsonFile);
            mUserInfo = JsonUtils.fromJson(json, mUserClass);
        }
        return (T) mUserInfo;
    }

    @Override
    protected <T> void onSaveUserInfo(T userInfo) {
        mUserInfo = userInfo;
        FileUtils.delete(mJsonFile);
        if (userInfo != null) {
            // 更新文件
            String json = JsonUtils.toJson(userInfo);
            FileIOUtils.writeFileFromString(mJsonFile, json);
        }
    }
}
