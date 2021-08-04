package com.github.raedev.swift;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

import java.util.Objects;

/**
 * 全局的入口
 * @author RAE
 * @date 2020/10/17
 */
public final class AppSwift {

    public static void init(Application application) {
        // 初始化工具类
        Utils.init(application);
    }

    /**
     * 获取当前上下文信息
     */
    @NonNull
    public static Context getContext() {
        return Objects.requireNonNull(Utils.getApp());
    }

}
