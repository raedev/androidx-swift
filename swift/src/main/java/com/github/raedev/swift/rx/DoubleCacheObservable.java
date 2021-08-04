package com.github.raedev.swift.rx;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 本地和网络双缓存观察者
 * @author RAE
 * @date 2020/10/27
 */
public final class DoubleCacheObservable {

    /**
     * 创建双缓存观察者，哪个数据源在先先加载。
     * 默认创建的观察者会再IO线程运行，回调在主线程运行。
     * @param dataSourceA 数据源A
     * @param dataSourceB 数据源B
     */
    public static <T> Observable<T> create(Observable<T> dataSourceA, Observable<T> dataSourceB) {
        return Observable
                .create(new DoubleCacheObservableOnSubscribe<T>(dataSourceA, dataSourceB))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建双缓存观察者
     */
    private static class DoubleCacheObservableOnSubscribe<T> implements ObservableOnSubscribe<T> {

        private final Observable<T> mDataSourceA;
        private final Observable<T> mDataSourceB;
        private final CompositeDisposable mDisposable;

        public DoubleCacheObservableOnSubscribe(Observable<T> dataSourceA, Observable<T> dataSourceB) {
            mDisposable = new CompositeDisposable();
            mDataSourceA = dataSourceA;
            mDataSourceB = dataSourceB;
        }

        private void onComplete(@NonNull ObservableEmitter<T> emitter) {
            emitter.onComplete();
            mDisposable.dispose();
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<T> emitter) {
            try {
                T data = mDataSourceA
                        .doOnSubscribe(mDisposable::add)
                        .onErrorResumeWith(mDataSourceB)
                        .blockingFirst();
                emitter.onNext(data);
                onComplete(emitter);
                return;
            } catch (Throwable e) {
                emitter.onError(e);
            }
            onComplete(emitter);
        }
    }
}
