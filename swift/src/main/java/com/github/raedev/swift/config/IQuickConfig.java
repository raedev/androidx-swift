package com.github.raedev.swift.config;

import io.reactivex.rxjava3.annotations.Nullable;

/**
 * @author RAE
 * @date 2021/11/15
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface IQuickConfig {

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
