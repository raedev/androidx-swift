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

package androidx.swift.config;

import io.reactivex.rxjava3.annotations.Nullable;

public interface IConfiguration {

    /**
     * 快速设置值
     * @param key 键
     * @param value 值
     */
    void setValue(String key, String value);

    /**
     * 获取配置值
     * @param key 键
     * @param defValue 默认值
     * @return
     */
    String getValue(String key, @Nullable String defValue);

    /**
     * 删除键
     * @param key
     */
    void remove(String key);

    /**
     * 清除所有
     */
    void clear();
}
