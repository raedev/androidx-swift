package com.github.raedev.swift.utils;

/**
 * 文本处理
 * @author RAE
 * @date 2021/01/19
 */
public final class TextUtils {

    /**
     * 文本是否为空
     * @param text 文本
     */
    public static boolean isEmpty(CharSequence text) {
        return android.text.TextUtils.isEmpty(text);
    }

    /**
     * 文本为空取默认值
     * @param text 文本
     */
    public static CharSequence getString(CharSequence text, CharSequence defaultValue) {
        return isEmpty(text) ? defaultValue : text;
    }

    /**
     * 转换成整型
     * @param text 文本
     */
    public static int parseInt(CharSequence text) {
        return parseInt(text, 0);
    }

    /**
     * 转换成整型
     * @param text 文本
     * @param defaultValue 转换失败的默认值
     */
    public static int parseInt(CharSequence text, int defaultValue) {
        try {
            return Integer.parseInt(text.toString());
        } catch (Exception ignored) {
        }
        return defaultValue;
    }

}
