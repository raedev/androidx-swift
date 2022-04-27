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

import androidx.swift.AppSwift;

public final class ConfigurationManager {
    private ConfigurationManager() {
    }

    /**
     * 获取配置文件Context
     * @param cls 配置文件类
     * @param <T> 类型
     * @return 配置文件Context
     */
    private static <T> ConfigurationHandler<T> getConfigurationContext(Class<T> cls) {
        return new ConfigurationHandler<>(AppSwift.getContext(), cls);
    }

    /**
     * 创建配置文件
     * @param cls 配置文件类
     * @param <T> 类型
     * @return 配置文件实体
     */
    public static <T> T create(Class<T> cls) {
        return getConfigurationContext(cls).getConfig();
    }
}
