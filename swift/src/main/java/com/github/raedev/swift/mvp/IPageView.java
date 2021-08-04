package com.github.raedev.swift.mvp;

import androidx.lifecycle.LifecycleOwner;

import java.util.List;

/**
 * 分页请求
 * @author rae
 * @date 2021/06/14
 */
public interface IPageView<T> extends LifecycleOwner {

    /**
     * 加载数据
     * @param pageable 分页
     * @param data 数据
     */
    void onLoadData(Pageable<T> pageable, List<T> data);

    /**
     * 加载数据错误
     * @param pageable 分页
     * @param message 错误消息
     */
    void onLoadDataError(Pageable<T> pageable, String message);

    /**
     * 没有更多数据
     * @param pageable 分页
     */
    void onNoMoreData(Pageable<T> pageable);

}
