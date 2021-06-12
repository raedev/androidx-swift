package com.github.raedev.swift.test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.raedev.swift.AppSwift;
import com.github.raedev.swift.entity.MyConfig;
import com.github.raedev.swift.entity.TestConfig;
import com.github.raedev.swift.entity.UserInfo;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ConfigTest extends BaseSwiftTest {


    @Test
    public void testAppConfig() {
        TestConfig config = AppSwift.getAppConfig(TestConfig.class);
        log("是否登录：%s", config.isLogin());
        config.setLogin(true);
        log("是否登录：%s", config.isLogin());
        log("用户信息：%s", config.getUserInfo());
        config.setUserInfo(new UserInfo("CONFIG_USER_NAME"));
        log("用户信息：%s", config.getUserInfo());
    }

    @Test
    public void testConfig() {
        TestConfig config = AppSwift.getConfig(TestConfig.class);
        log("用户信息：%s", config.getUserInfo());
        config.setUserInfo(new UserInfo("TestConfig"));
        log("用户信息：%s", config.getUserInfo());

        MyConfig myConfig = AppSwift.getConfig(MyConfig.class);
        myConfig.setName("自定义配置文件");
        log("名称：%s", myConfig.getName());
    }

    @Test
    public void testGetSetValue() {
        TestConfig config = AppSwift.getConfig(TestConfig.class);
        config.clear();
        config.setValue("MyKey", "OK！");
        config.setValue("MyKey1", "xxxxx");
        log("自定义值：%s", config.getValue("MyKey", "def"));

        UserInfo userInfo = new UserInfo("测试用户");
        config.setObject("MyObject", userInfo);
        UserInfo object = config.getObject("MyObject", UserInfo.class);
        log("自定义对象：%s", object);
    }

}