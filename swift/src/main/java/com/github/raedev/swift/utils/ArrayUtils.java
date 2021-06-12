package com.github.raedev.swift.utils;

import java.util.List;

/**
 * 数组、集合处理工具类
 * @author RAE
 * @since 2020/12/30
 */
public final class ArrayUtils {

    /**
     * 判断集合是否为空
     * @param list 集合
     * @param <T> 类型
     * @return 是否为空
     */
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 集合数量
     * @param list 集合
     * @return 数组为空返回-1，否则返回数量
     */
    public static <T> int count(List<T> list) {
        return isEmpty(list) ? -1 : list.size();
    }

}
