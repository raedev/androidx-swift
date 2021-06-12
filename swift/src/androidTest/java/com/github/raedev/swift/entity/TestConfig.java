package com.github.raedev.swift.entity;

import com.github.raedev.swift.config.SwiftConfig;

/**
 * 测试配置文件
 * Created by RAE on 2020/10/17.
 */
public interface TestConfig extends SwiftConfig {

    boolean isLogin();

    void setLogin(boolean value);

    void setUserInfo(UserInfo userInfo);

    UserInfo getUserInfo();
}
