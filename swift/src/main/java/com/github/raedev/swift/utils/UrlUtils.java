package com.github.raedev.swift.utils;

import java.util.Map;

/**
 * {DESC}
 * @author RAE
 * @date 2021/08/{2021/8/6 0006}
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public final class UrlUtils {

    /**
     * Map转换为请求参数，格式：a=1&b=2&c=3
     * @param map 处理数据
     * @return 请求参数
     */
    public static String toQueryParams(Map<String, String> map) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (i != 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            i++;
        }
        return sb.toString();
    }
}
