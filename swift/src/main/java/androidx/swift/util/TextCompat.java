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

package androidx.swift.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 扩展文本处理
 * @author RAE
 * @date 2021/01/19
 */
public final class TextCompat {

    /**
     * 文本是否为空
     * @param text 文本
     */
    public static boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    /**
     * 文本为空取默认值
     * @param text 文本
     */
    public static CharSequence getString(CharSequence text, CharSequence defaultValue) {
        return isEmpty(text) ? defaultValue : text;
    }

    /**
     * 字符串分割成数组
     * @param text 字符串
     * @param separator 分割符号，比如逗号
     * @return 数组
     */
    public static List<String> explode(String text, String separator) {
        String[] split = text.split(separator);
        return new ArrayList<>(Arrays.asList(split));
    }

}
