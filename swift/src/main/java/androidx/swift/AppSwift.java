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

package androidx.swift;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.swift.util.Logger;
import androidx.swift.util.Utils;

import java.util.Objects;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public final class AppSwift {

    private static final AppSwift S_INSTANCE = new AppSwift();

    private AppSwift() {
    }

    /**
     * 初始化
     * @param application Application
     */
    public static AppSwift init(Application application) {
        // 初始化工具类
        Utils.init(application);
        return S_INSTANCE;
    }

    /**
     * 获取当前上下文信息
     */
    @NonNull
    public static Context getContext() {
        return Objects.requireNonNull(Utils.getApp(), "Context can't null");
    }

    /**
     * 设置日志默认
     * @param tag Tag
     * @param debug 是否调试模式, True 将输出更多信息
     */
    public void setLog(String tag, boolean debug) {
        Logger.DEBUG = debug;
        Logger.TAG = tag;
    }

    /**
     * 处理RxJava未处理异常
     */
    public void handleRxJavaError() {
        RxJavaPlugins.setErrorHandler(error -> {
            Logger.e("RX未处理异常：" + error.getMessage(), error);
        });
    }
}
