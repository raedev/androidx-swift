package com.github.raedev.swift.config;

import androidx.annotation.Nullable;

/**
 * 配置文件接口，规范说明：
 * <p>1、必须是Set\Get方法，比如：setName(), getName();</p>
 * <p>2、Set方法只支持一个参数设置；</p>
 * <p>3、Is前缀的无参方法默认是Boolean类型，比如：isVip();</p>
 * <p>Created by RAE on 2020/10/17.</p>
 */
public interface SwiftConfig {

    /**
     * 清除所有配置信息
     */
    void clear();

    /**
     * 移除配置
     *
     * @param key 配置键
     */
    void remove(String key);

    /**
     * 通过KEY获取值
     *
     * @param key 配置键
     */
    String getValue(String key, String defValue);

    /**
     * 设置配置
     *
     * @param key   配置键
     * @param value 配置值
     */
    void setValue(String key, String value);

    /**
     * 获取对象
     *
     * @param key 配置键
     * @param cls 对象类型
     */
    @Nullable
    <T> T getObject(String key, Class<T> cls);

    /**
     * 保存对象
     *
     * @param key   配置键
     * @param value 配置对象
     */
    void setObject(String key, Object value);

}
