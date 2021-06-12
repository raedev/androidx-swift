package com.github.raedev.swift.rxjava3;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * 分页请求接口
 * @author RAE
 * @since 2020/12/30
 */
public interface PageObservableOnSubscribe<T> {

    /**
     * 获取视图
     * @return 视图
     */
    SwiftPageRequest.PageView<T> getPageView();

    /**
     * 订阅
     * @param page 分页
     * @return 观察者
     */
    Observable<List<T>> flatMapObservable(int page);
}
