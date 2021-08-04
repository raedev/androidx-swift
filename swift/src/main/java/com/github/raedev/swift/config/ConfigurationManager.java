package com.github.raedev.swift.config;

import com.github.raedev.swift.AppSwift;

/**
 * 配置管理器
 * @author RAE
 * @date 2021/06/13
 * Copyright (c) https://github.com/raedev All rights reserved.
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
    public static <T> ConfigurationContext<T> getConfigurationContext(Class<T> cls) {
        return new ConfigurationContext<>(AppSwift.getContext(), cls);
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
