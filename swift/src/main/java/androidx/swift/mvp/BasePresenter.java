/*
 * Copyright 2022 RAE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.swift.mvp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.swift.rxjava3.core.Composer;

import java.lang.ref.WeakReference;
import java.util.Objects;

import io.reactivex.rxjava3.core.ObservableTransformer;


/**
 * BasePresenter
 * @author rae
 */
public abstract class BasePresenter<V extends IPresenterView> implements IPresenter, ILoadMore, LifecycleEventObserver {

    private WeakReference<V> mViewReference;

    public BasePresenter(@NonNull V view) {
        this.bindView(view);
    }

    public void bindView(@NonNull V view) {
        this.mViewReference = new WeakReference<>(view);
        view.getLifecycle().addObserver(this);
    }

    protected V getView() {
        return mViewReference.get();
    }

    protected V requireView() {
        return Objects.requireNonNull(getView());
    }

    @Override
    public void start() {
        this.onStart();
    }

    protected Context getContext() {
        return requireView().getContext();
    }

    /**
     * 释放数据
     */
    protected void onDestroy() {
    }

    /**
     * 加载数据
     */
    protected void onStart() {
    }

    /**
     * 加载数据
     */
    protected void onResume() {
    }

    /**
     * 加载数据
     */
    protected void onPause() {
    }

    @Override
    public void loadMore() {
        this.onLoadMore();
    }

    /**
     * 加载更多数据
     */
    protected void onLoadMore() {

    }

    protected String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    protected String getString(@StringRes int resId, Object... args) {
        return getContext().getString(resId, args);
    }

    protected <T> ObservableTransformer<T, T> uiThread() {
        return Composer.uiThread(getView());
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return getView().getLifecycle();
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.getLifecycle().removeObserver(this);
            onDestroy();
            // 解除View引用实例
            if (mViewReference != null) {
                mViewReference.clear();
                mViewReference = null;
            }
        }
        if (event == Lifecycle.Event.ON_START) {
            onStart();
        }
        if (event == Lifecycle.Event.ON_RESUME) {
            onResume();
        }
        if (event == Lifecycle.Event.ON_PAUSE) {
            onPause();
        }
    }
}
