package com.github.raedev.swift.config;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 配置文件动态代理
 * Created by RAE on 2020/10/17.
 */
class ConfigInvocationHandler implements InvocationHandler {

    private final SharedPreferences mPreference;
    private final Gson mGson;

    ConfigInvocationHandler(SharedPreferences preference) {
        this.mPreference = preference;
        mGson = GsonUtils.getGson();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String methodName = method.getName().toUpperCase();
        // 清除配置文件
        if (methodName.equalsIgnoreCase("clear")) {
            mPreference.edit().clear().apply();
        }
        // 移除配置项处理
        if (methodName.equalsIgnoreCase("remove") && args != null) {
            String key = args[0].toString().toUpperCase();
            mPreference.edit().remove(key).apply();
            return null;
        }
        // getValue方法
        if (methodName.equalsIgnoreCase("getValue")) {
            return getValue(args[0].toString(), method, args.length > 1 ? new Object[]{args[1]} : null);
        }
        // setValue方法
        if (methodName.equalsIgnoreCase("setValue")) {
            setValue(args[0].toString(), method, new Object[]{args[1]});
            return null;
        }
        if (methodName.equalsIgnoreCase("setObject") && args.length == 2) {
            mPreference.edit().putString(args[0].toString(), mGson.toJson(args[1])).apply();
            return null;
        }
        if (methodName.equalsIgnoreCase("getObject") && args.length == 2) {
            String json = mPreference.getString(args[0].toString(), null);
            if (json == null) return null;
            return mGson.fromJson(json, (Class<?>) args[1]);
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
        if (args == null) throw new IllegalArgumentException("set方法必须提供参数");
        if (args.length != 1) throw new IllegalArgumentException("set方法的方法参数只允许一个");
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> parameterType = parameterTypes[0];
        Object arg = args[0];
        SharedPreferences.Editor editor = mPreference.edit();
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
        } else if (type == long.class) {
            return mPreference.getLong(name, defaultValue == null ? 0 : (long) defaultValue);
        } else {
            // 其他值默认使用Json字符串
            String json = mPreference.getString(name, null);
            return mGson.fromJson(json, type);
        }
    }
}
