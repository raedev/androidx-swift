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

package androidx.swift.content;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.swift.AppSwift;

public final class AppResource {

    private static Context context() {
        return AppSwift.getContext();
    }

    /**
     * 获取字符串
     * @param resId 字符串Id
     */
    public static String getString(@StringRes int resId) {
        return context().getString(resId);
    }

    /**
     * 获取字符串
     * @param resId 字符串Id
     * @param args 参数信息
     */
    public static String getString(@StringRes int resId, Object... args) {
        return context().getString(resId, args);
    }

    /**
     * 获取图片资源
     * @param resId 图片资源Id
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        return ActivityCompat.getDrawable(context(), resId);
    }

    /**
     * 获取图片资源
     * @param name 图片资源名称
     */
    @Nullable
    public static Drawable getDrawable(String name) {
        int resId = context().getResources().getIdentifier(name, "drawable", context().getPackageName());
        return resId > 0 ? ActivityCompat.getDrawable(context(), resId) : null;
    }

    /**
     * 获取颜色
     * @param resId 颜色Id
     */
    public static int getColor(@ColorRes int resId) {
        return ActivityCompat.getColor(context(), resId);
    }

}
