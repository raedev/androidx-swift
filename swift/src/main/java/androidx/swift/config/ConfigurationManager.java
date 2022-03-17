package androidx.swift.config;

import androidx.swift.AppSwift;

/**
 * 配置管理器
 * @author RAE
 * @date 2021/06/13
 * Copyright (c) https://github.com/raedev All rights reserved.
 * @see #create(Class) 创建配置文件
 */
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
