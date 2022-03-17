package androidx.swift;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.swift.util.Logger;
import androidx.swift.util.Utils;

import java.util.Objects;

/**
 * 全局的入口
 * @author RAE
 * @date 2020/10/17
 */
public final class AppSwift {

    private static final AppSwift S_INSTANCE = new AppSwift();

    private AppSwift() {
    }

    /**
     * 初始化
     * @param application Application
     */
    public static AppSwift init(Application application) {
        // 初始化工具类
        Utils.init(application);
        return S_INSTANCE;
    }

    /**
     * 获取当前上下文信息
     */
    @NonNull
    public static Context getContext() {
        return Objects.requireNonNull(Utils.getApp(), "Context can't null");
    }

    /**
     * 设置日志默认
     * @param tag Tag
     * @param debug 是否调试模式, True 将输出更多信息
     */
    public void setLog(String tag, boolean debug) {
        Logger.DEBUG = debug;
        Logger.TAG = tag;
    }

}
