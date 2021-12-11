package com.github.raedev.swift.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.github.raedev.swift.annotation.Configuration;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 配置文件上下文
 * 用于管理配置类和配置文件之间的映射关系
 * @author RAE
 * @date 2021/06/13
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class ConfigurationContext<T> implements InvocationHandler {

    private final SharedPreferences mPreference;
    private final Gson mGson;
    private final T mEntity;

    /**
     * 创建配置
     * @param context 上下文
     * @param entityClass 配置实体类，类名必须为Config结尾如：MyConfig、AppConfig、MapConfig
     */
    @SuppressWarnings("unchecked")
    public ConfigurationContext(Context context, Class<T> entityClass) {
        mGson = GsonUtils.getGson();
        String name = entityClass.getName();
        String suffix = "Config";
        if (!entityClass.getSimpleName().endsWith(suffix)) {
            // 规范类命名
            throw new IllegalArgumentException(name + "配置类名必须以Config结尾");
        }
        if (!entityClass.isInterface()) {
            // 规范类命名
            throw new IllegalArgumentException(name + "配置类必须是接口");
        }
        // 检查注解
        Configuration configuration = entityClass.getAnnotation(Configuration.class);
        if (configuration == null || !entityClass.isAnnotationPresent(Configuration.class)) {
            throw new IllegalArgumentException(name + "配置类必须添加@Configuration注解");
        }
        // 配置文件名
        String configName = TextUtils.isEmpty(configuration.value()) ? entityClass.getName().replace(".", "_") : configuration.value();
        if (configuration.changed()) {
            // xxxConfig1.xml 、 xxxConfig2.xml
            configName += AppUtils.getAppVersionCode();
        }
        // 使用偏好文件保存配置
        mPreference = context.getSharedPreferences(configName, Context.MODE_PRIVATE);
        // 创建动态代理
        mEntity = (T) Proxy.newProxyInstance(entityClass.getClassLoader(), new Class[]{entityClass}, this);

    }

    /**
     * 获取配置文件
     * @return 配置文件
     */
    public T getConfig() {
        return mEntity;
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String methodName = method.getName().toUpperCase();
        if (methodName.equalsIgnoreCase("setValue")) {
            mPreference.edit().putString(String.valueOf(args[0]), String.valueOf(args[1])).apply();
            return null;
        }
        if (methodName.equalsIgnoreCase("getValue")) {
            return mPreference.getString(String.valueOf(args[0]), args[1] == null ? null : String.valueOf(args[1]));
        }
        if (methodName.equalsIgnoreCase("REMOVE")) {
            mPreference.edit().remove(String.valueOf(args[0])).apply();
            return null;
        }
        if (methodName.equalsIgnoreCase("CLEAR")) {
            mPreference.edit().clear().apply();
            return null;
        }
        // Get方法处理
        if (methodName.startsWith("SET")) {
            setValue(methodName.replaceFirst("SET", ""), method, args);
            return null;
        }
        // Get方法处理
        if (methodName.startsWith("GET")) {
            return getValue(methodName.replaceFirst("GET", ""), method, args);
        }
        // Remove方法处理
        if (methodName.startsWith("REMOVE")) {
            mPreference.edit().remove(methodName.replaceFirst("REMOVE", "")).apply();
            return null;
        }
        // Is方法处理，比如：isLogin()、isVip()，这类的布尔值
        if (methodName.startsWith("IS")) {
            return mPreference.getBoolean(methodName.replaceFirst("IS", ""), false);
        }
        return null;
    }


    /**
     * 设置配置值
     */
    private void setValue(String name, Method method, @Nullable Object[] args) {
        if (args == null) {
            throw new IllegalArgumentException("set方法必须提供参数");
        }
        if (args.length != 1) {
            throw new IllegalArgumentException("set方法的方法参数只允许一个");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> parameterType = parameterTypes[0];
        Object arg = args[0];
        SharedPreferences.Editor editor = mPreference.edit();

        if (arg == null) {
            // 移除值
            editor.remove(name).apply();
            return;
        }
        if (parameterType == String.class) {
            editor.putString(name, (String) arg);
        } else if (parameterType == int.class) {
            editor.putInt(name, (int) arg);
        } else if (parameterType == boolean.class) {
            editor.putBoolean(name, (boolean) arg);
        } else if (parameterType == float.class) {
            editor.putFloat(name, (float) arg);
        } else if (parameterType == long.class) {
            editor.putLong(name, (long) arg);
        } else if (parameterType == double.class) {
            editor.putFloat(name, (long) arg);
        } else {
            // 其他值默认使用Json字符串
            String json = mGson.toJson(arg);
            editor.putString(name, json);
        }
        editor.apply();
    }

    /**
     * 获取配置值
     */
    private Object getValue(String name, Method method, Object[] args) {
        Class<?> type = method.getReturnType();
        Object defaultValue = args == null ? null : args[0];
        if (type == String.class) {
            return mPreference.getString(name, (String) defaultValue);
        } else if (type == int.class) {
            return mPreference.getInt(name, defaultValue == null ? 0 : (int) defaultValue);
        } else if (type == boolean.class) {
            return mPreference.getBoolean(name, defaultValue != null && (boolean) defaultValue);
        } else if (type == float.class) {
            return mPreference.getFloat(name, defaultValue == null ? 0 : (float) defaultValue);
        } else if (type == double.class) {
            return (double) mPreference.getFloat(name, defaultValue == null ? 0f : (float) defaultValue);
        } else if (type == long.class) {
            return mPreference.getLong(name, defaultValue == null ? 0 : (long) defaultValue);
        } else {
            // 其他值默认使用Json字符串
            String json = mPreference.getString(name, null);
            return mGson.fromJson(json, type);
        }
    }

    // region  扩展方法

    /**
     * 清除所有配置
     */
    public void clear() {
        mPreference.edit().clear().apply();
    }

    /**
     * 移除字段
     * @param key 字段名称
     */
    public void remove(String key) {
        mPreference.edit().remove(key).apply();
    }

    // endregion
}
