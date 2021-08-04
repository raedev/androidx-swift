package com.github.raedev.swift.mvp;


import androidx.lifecycle.LifecycleObserver;

/**
 * 公共的Presenter接口
 * @author RAE
 * @date 2018/4/18
 */
public interface IPresenter extends LifecycleObserver {

    /**
     * 开始加载数据
     */
    void loadData();

}
