package com.github.raedev.swift.rxjava3;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.observers.DisposableObserver;

/**
 * 回调观察者
 * @author RAE
 * @date 2020/12/30
 */
public abstract class SwiftObserver<T> extends DisposableObserver<T> {

    private WeakReference<LifecycleOwner> mWeakReference;

    @Override
    public void onNext(@NonNull T t) {
        if (mWeakReference == null || mWeakReference.get() == null) {
            Log.e("Swift", "SwiftObserver View 为空");
            return;
        }
        onNext(mWeakReference.get(), t);
        // 清除View 引用
        mWeakReference.clear();
        mWeakReference = null;
    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.e("Swift", "SwiftObserver内部异常：" + e.getMessage(), e);
        onError(e.getMessage());
    }

    protected void onError(String message) {

    }

    /**
     * 观察者回调
     * @param view 视图
     * @param t 数据
     */
    protected abstract void onNext(@NonNull LifecycleOwner view, @NonNull T t);

    /**
     * 附着View
     * @param view 回调视图
     */
    public void attachView(LifecycleOwner view) {
        if (mWeakReference != null) {
            mWeakReference.clear();
        }
        mWeakReference = new WeakReference<>(view);
    }

    @Override
    public void onComplete() {

    }
}
