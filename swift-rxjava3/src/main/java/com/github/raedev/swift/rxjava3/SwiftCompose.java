package com.github.raedev.swift.rxjava3;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @author RAE
 * @since 2020/12/30
 */
public class SwiftCompose {

    /**
     * IO线程和主线程切换
     * @param <T> 类型
     */
    public static <T> ObservableTransformer<T, T> io2Main() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
