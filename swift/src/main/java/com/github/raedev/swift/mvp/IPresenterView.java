package com.github.raedev.swift.mvp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

/**
 * 公共的Presenter接口
 * @author RAE
 * @date 2018/4/18
 */
public interface IPresenterView extends LifecycleOwner {

    /**
     * 获取生命周期
     * @return 生命周期
     */
    @NonNull
    @Override
    Lifecycle getLifecycle();

    /**
     * 获取Context
     * @return Context
     */
    @Nullable
    Context getContext();
}
