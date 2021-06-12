package com.github.raedev.swift.rxjava3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;

/**
 * 分页请求
 * @author RAE
 * @since 2020/12/30
 */
public abstract class SwiftPageRequest<T> {

    /**
     * 分页View回调
     * @param <T> 实体
     */
    public interface PageView<T> extends LifecycleOwner {

        /**
         * 没有更多数据
         */
        void onNoMorePageData();

        /**
         * 加载数据错误
         * @param message 错误信息
         * @param e 错误异常
         */
        void onLoadPageDataError(@NonNull String message, @Nullable Throwable e);

        /**
         * 加载分页数据成功
         * @param data 数据集合
         */
        void onLoadPageDataSuccess(@NonNull List<T> data);
    }

    private final int mStartPageIndex;
    private final List<T> mDataList;
    private final WeakReference<PageView<T>> mViewWeakReference;
    private int mPage;

    public static <T> SwiftPageRequest<T> create(PageObservableOnSubscribe<T> source) {
        PageView<T> pageView = source.getPageView();
        return new SwiftPageRequest<T>(pageView) {
            @Override
            protected Observable<List<T>> onCreateObserver(PageView<T> view, int page) {
                return source.flatMapObservable(page);
            }
        };
    }

    public SwiftPageRequest(PageView<T> view) {
        this(view, 1);
    }

    public SwiftPageRequest(PageView<T> view, int startIndex) {
        this.mDataList = new ArrayList<>();
        this.mStartPageIndex = startIndex;
        this.mPage = startIndex;
        this.mViewWeakReference = new WeakReference<>(view);
    }

    @Nullable
    private PageView<T> getView() {
        return mViewWeakReference.get();
    }

    public void start() {
        this.mPage = this.mStartPageIndex;
        this.onLoadData(this.mPage);
    }

    public void loadMore() {
        this.onLoadData(this.mPage);
    }

    public void complete(List<T> dataList) {
        if (this.canResetData()) {
            this.onClearData();
        }

        if (this.mPage <= this.mStartPageIndex || !isEmpty(dataList)) {
            this.mDataList.addAll(dataList);
            this.onLoadDataComplete(this.mDataList);
            ++this.mPage;
        } else {
            this.onNoMoreData();
        }
    }

    private boolean isEmpty(List<T> dataList) {
        return dataList == null || dataList.isEmpty();
    }

    protected void onClearData() {
        this.mDataList.clear();
    }

    public void destroy() {
        this.mDataList.clear();
        this.mViewWeakReference.clear();
    }

    public boolean canResetData() {
        return this.mPage <= this.mStartPageIndex;
    }

    protected void onNoMoreData() {
        PageView<T> view = getView();
        if (view != null) {
            view.onNoMorePageData();
        }
    }

    protected void onLoadData(int page) {
        this.onCreateObserver(getView(), page).subscribe(this.createObserver());
    }

    protected DisposableObserver<List<T>> createObserver() {
        return new SwiftObserver<List<T>>() {
            @Override
            public void onError(@NonNull Throwable e) {
                notifyError(e.getMessage(), e);
            }

            @Override
            protected void onNext(@NonNull LifecycleOwner view, @NonNull List<T> data) {
                notifyData(data);
            }
        };
    }

    protected void notifyError(String message, Throwable e) {
        PageView<T> view = getView();
        if (view != null) {
            if (this.mPage > this.mStartPageIndex) {
                view.onNoMorePageData();
            } else {
                view.onLoadPageDataError(message, e);
            }
        }
    }

    protected void notifyData(List<T> data) {
        PageView<T> view = getView();
        if (view != null) {
            if (data != null && data.size() > 0) {
                this.complete(data);
            } else {
                this.notifyError("暂无数据", null);
            }
        }
    }

    /**
     * 请求观察者
     * @param view View视图
     * @param page 分页
     * @return 观察者
     */
    protected abstract Observable<List<T>> onCreateObserver(PageView<T> view, int page);

    protected void onLoadDataComplete(List<T> dataList) {
        PageView<T> view = getView();
        if (view != null) {
            view.onLoadPageDataSuccess(dataList);
        }
    }

    public int getCurrentPage() {
        return this.mPage;
    }

    public List<T> getDataList() {
        return this.mDataList;
    }
}
