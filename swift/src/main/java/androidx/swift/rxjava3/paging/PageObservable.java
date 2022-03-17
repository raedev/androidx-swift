package androidx.swift.rxjava3.paging;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.functions.Function;

/**
 * 分页观察者
 * @author RAE
 * @date 2022/03/14
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PageObservable<T> extends Observable<T> {

    private final int mStartIndex;
    private int mCurrentPage;
    @Nullable
    private Function<Integer, T> mNextPageMapper;


    private PageObservable(int index) {
        this.mStartIndex = index;
    }

    public static <R> PageObservable<R> create(@NonNull Function<Integer, R> mapper) {
        return new PageObservable<R>(0).doOnNextPage(mapper);
    }

    public static <T> PageObservable<T> create(int startIndex) {
        return new PageObservable<>(startIndex);
    }

    public PageObservable<T> doOnNextPage(@NonNull Function<Integer, T> mapper) {
        this.mNextPageMapper = mapper;
        return this;
    }



    @Override
    protected void subscribeActual(@NonNull Observer<? super T> observer) {
        // 开始请求
        try {
            if (mNextPageMapper != null) {
                T data = mNextPageMapper.apply(mCurrentPage);
                mCurrentPage++;
                observer.onNext(data);
                observer.onComplete();
            }
        } catch (Throwable throwable) {
            observer.onError(throwable);
        }
    }
}
