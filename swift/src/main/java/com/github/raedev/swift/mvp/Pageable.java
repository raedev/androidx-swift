package com.github.raedev.swift.mvp;

import java.util.List;

/**
 * 可分页的
 * @author RAE
 * @date 2020/11/2
 */
public interface Pageable<T> {

    /**
     * 获取当前分页数据
     * @return 数据
     */
    List<T> getDataList();

    /**
     * 获取当前分页
     * @return 当前分页
     */
    int getCurrentPage();

    /**
     * 是否为加载更多
     * @return 是否为加载更多
     */
    boolean isLoadMore();

    /**
     * 是否没有数据
     * @return 是否没有数据
     */
    boolean isEmpty();
}
