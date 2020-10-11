package com.github.raedev.swift.session;

/**
 * 用户信息保存
 */
public interface ISessionInfo {

    /**
     * 实体转换为
     */
    String toJson();

    ISessionInfo fromJson(String json);
}
