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

import androidx.annotation.Nullable;
import androidx.swift.util.FileIOUtils;
import androidx.swift.util.FileUtils;
import androidx.swift.util.JsonUtils;

import java.io.File;

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
