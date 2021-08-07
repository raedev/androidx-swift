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
     * @param <T>  类型
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
        return isEmpty(list) ? 0 : list.size();
    }

    /**
     * 数组以字符串分割
     * @param list      数组
     * @param separator 分割字符串
     * @param <T>       类型
     * @return 字符串
     */
    public static <T> String join(List<T> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(separator);
            }
            sb.append(list.get(i).toString());
        }
        return sb.toString();
    }

}
