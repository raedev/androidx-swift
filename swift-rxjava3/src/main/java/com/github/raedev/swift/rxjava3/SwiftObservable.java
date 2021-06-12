package com.github.raedev.swift.rxjava3;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.internal.operators.observable.ObservableCreate;
import io.reactivex.rxjava3.observers.DisposableObserver;

/**
 * 敏捷的Observable观察器，搭配@{@link SwiftObserver} 一起来使用
 * @author RAE
 * @since 2020/12/30
 */
public class SwiftObservable<T> extends Observable<T> implements LifecycleEventObserver {

    private Observable<T> mSource;
    private WeakReference<LifecycleOwner> mWeakReference;
    private boolean mIsDisposed;
    private Observer<? super T> mObserver;

    protected SwiftObservable(Observable<T> source) {
        mSource = source;
    }

    public static <T> SwiftObservable<T> create(@NonNull ObservableOnSubscribe<T> source) {
        Objects.requireNonNull(source, "source is null");
        return new SwiftObservable<>(new ObservableCreate<>(source));
    }

    public static <T> SwiftObservable<T> create(@NonNull Observable<T> source) {
        Objects.requireNonNull(source, "source is null");
        return new SwiftObservable<>(source);
    }

    /**
     * 关联生命周期
     * @param view 生命周期View
     * @return
     */
    public SwiftObservable<T> with(LifecycleOwner view) {
        mWeakReference = new WeakReference<>(view);
        view.getLifecycle().addObserver(this);
        return this;
    }

    public SwiftObservable<T> io2Main() {
        mSource = mSource.compose(SwiftCompose.io2Main());
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void subscribeActual(@NonNull Observer<? super T> observer) {
        if (mIsDisposed) {
            return;
        }
        if (observer instanceof SwiftObserver && mWeakReference.get() != null) {
            SwiftObserver<T> swiftObserver = (SwiftObserver<T>) observer;
            swiftObserver.attachView(mWeakReference.get());
        }
        mObserver = observer;
        mSource.subscribe(observer);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_STOP) {
            // 释放
            mIsDisposed = true;
            mWeakReference.clear();
            if (mObserver instanceof DisposableObserver) {
                ((DisposableObserver) mObserver).dispose();
            }
        }

        // 取消监听
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.getLifecycle().removeObserver(this);
        }
    }
}
