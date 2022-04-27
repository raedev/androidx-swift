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

package androidx.swift.rxjava3.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.swift.mvp.IPageView;
import androidx.swift.rxjava3.core.Composer;
import androidx.swift.util.ArrayUtils;
import androidx.swift.util.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Function;

public class PageObservable<E> implements PageableObservable<E>, LifecycleEventObserver {

    protected int mStartIndex;
    protected int mCurrentPage;
    protected final List<E> mDataList = new ArrayList<>();
    private final Function<Integer, Observable<List<E>>> mMapper;
    @Nullable
    private Disposable mDisposable;
    private WeakReference<IPageView<E>> mViewReference;
    private Action mOnComplete;

    protected PageObservable(Function<Integer, Observable<List<E>>> mapper) {
        mMapper = mapper;
    }

    public static <E> PageableObservable<E> create(IPageView<E> view, Function<Integer, Observable<List<E>>> mapper) {
        return new PageObservable<>(mapper).compose(view);
    }

    public PageObservable<E> compose(@NonNull IPageView<E> view) {
        if (mViewReference != null && mViewReference.get() != null) {
            throw new UnsupportedOperationException("current view is not null.");
        }
        mViewReference = new WeakReference<>(view);
        return this;
    }

    @Override
    public void start() {
        mCurrentPage = mStartIndex;
        doSubscribe();
    }

    @Override
    public void loadMore() {
        doSubscribe();
    }

    public void doOnComplete(Action onComplete) {
        mOnComplete = onComplete;
    }

    private void doSubscribe() {
        if (!canSubscribe()) {
            return;
        }
        final IPageView<E> view = mViewReference.get();
        if (view == null) {
            throw new NullPointerException("the view has been destroyed");
        }
        try {
            Observable<List<E>> observable = mMapper.apply(mCurrentPage).compose(Composer.uiThread(view));
            if (mOnComplete != null) {
                observable = observable.doFinally(mOnComplete);
            }
            mDisposable = observable
                    .subscribe(data -> {
                        if (ArrayUtils.isEmpty(data)) {
                            view.onNoMoreData(this);
                            return;
                        }
                        mDataList.addAll(data);
                        view.onLoadData(this, mDataList);
                        mCurrentPage++;
                    }, throwable -> {
                        view.onLoadDataError(this, throwable.getMessage(), throwable);
                    }, () -> {
                        if (mDisposable != null) {
                            mDisposable.dispose();
                            mDisposable = null;
                        }
                    });
        } catch (Throwable throwable) {
            view.onLoadDataError(this, throwable.getMessage(), throwable);
        }
    }

    @Override
    public List<E> getDataList() {
        return mDataList;
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public boolean isLoadMore() {
        return mCurrentPage > mStartIndex;
    }

    @Override
    public boolean isEmptyData() {
        return ArrayUtils.isEmpty(mDataList);
    }

    private boolean canSubscribe() {
        if (mDisposable != null) {
            Logger.w("PageObservable", "It can't start when the Page Observable is running.");
            return false;
        }
        Objects.requireNonNull(mViewReference, "you must call this compose() method before invoking this method.");
        return true;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            // 释放资源
            mDataList.clear();
            mOnComplete = null;
            if (mViewReference != null) {
                mViewReference.clear();
                mViewReference = null;
            }
            if (mDisposable != null) {
                mDisposable.dispose();
                mDisposable = null;
            }
        }
    }
}
