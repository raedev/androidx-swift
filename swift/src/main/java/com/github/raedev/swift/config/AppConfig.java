package com.github.raedev.swift.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.github.raedev.swift.annotation.ConfigClass;

import java.lang.reflect.Proxy;

/**
 * 程序配置文件
 * Created by RAE on 2020/10/17.
 */
public final class AppConfig {

    /**
     * 创建程序配置代理类
     *
     * @param cls 配置类的Class，必须是接口类型，可以注解为{@link ConfigClass}
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(@NonNull Context context, String configName, @NonNull Class<T> cls) {
        if (!cls.isInterface()) {
            throw new IllegalArgumentException("配置类必须定义为接口");
        }
        // 没有指定配置文件，从注解里面获取
        if (TextUtils.isEmpty(configName)) {
            if (cls.isAnnotationPresent(ConfigClass.class)) {
                ConfigClass config = cls.getAnnotation(ConfigClass.class);
                configName = config == null ? null : config.value();
            }
        }
        //  注解没有配置，默认获取类名作为配置文件名
        if (TextUtils.isEmpty(configName)) {
            configName = cls.getName();
        }
        // 使用偏好文件保存配置
        SharedPreferences preferences = context.getSharedPreferences(configName, Context.MODE_PRIVATE);
        // 创建动态代理
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new ConfigInvocationHandler(preferences));
    }
}
