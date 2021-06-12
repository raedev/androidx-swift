package com.github.raedev.swift.utils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;

import java.util.List;

/**
 * Json工具类，基于google gson解析
 * Created by RAE on 2020/10/17.
 * @author rae
 */
public final class JsonUtils {

    /**
     * Converts object to json string.
     */
    public static String toJson(Object object) {
        return GsonUtils.getGson().toJson(object);
    }

    /**
     * Json to Object
     */
    public static <T> T fromJson(String json, Class<T> cls) {
        return GsonUtils.getGson().fromJson(json, cls);
    }

    /**
     * Converts json to list type.
     * @param json The json string.
     * @param cls the list type.
     * @return instance of type
     */
    public static <T> List<T> toList(String json, Class<T> cls) {
        return GsonUtils.getGson().fromJson(json, GsonUtils.getListType(cls));
    }

    /**
     * 将一个对象转换成另外一个对象，多应用于继承类，父类转子类，子类转父类的场景
     * 复制公共字段信息
     * @param source 源对象
     * @param cls 目标对象类型
     * @return 模板对象
     */
    @Nullable
    public static <T> T toObject(Object source, Class<T> cls) {
        String json = toJson(source);
        return fromJson(json, cls);
    }
}
