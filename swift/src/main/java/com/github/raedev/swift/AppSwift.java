package com.github.raedev.swift;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.github.raedev.swift.config.AppConfig;
import com.github.raedev.swift.session.SessionManager;

/**
 * 全局的入口
 * @author RAE
 * @date 2020/10/17
 */
public final class AppSwift {

    /**
     * 全局的应用程序
     */
    private static Application sApplication;

    public static void init(Application application) {
        // 持有一个全局Application
        sApplication = application;
        // 初始化工具类
        Utils.init(application);
    }

    @NonNull
    public static Application getApplication() {
        if (sApplication == null) {
            throw new RuntimeException("请在AppSwift里面初始化Application");
        }
        return sApplication;
    }

    /**
     * 获取当前上下文信息
     */
    @NonNull
    public static Context getContext() {
        return getApplication();
    }

    /**
     * 获取会话管理器
     */
    public static SessionManager getSessionManager() {
        return SessionManager.getDefault();
    }

    /**
     * 获取独立的配置信息
     * @param cls 配置类，信息保存在单个文件
     */
    public static <T> T getConfig(Class<T> cls) {
        return AppConfig.create(getContext(), null, cls);
    }

    /**
     * 获取应用程序的配置信息
     * @param cls 配置类，信息都保存在应用程序的同一个配置文件
     */
    public static <T> T getAppConfig(Class<T> cls) {
        return AppConfig.create(getContext(), getContext().getPackageName(), cls);
    }

}
