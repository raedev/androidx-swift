package com.github.raedev.swift.rx;

import com.github.raedev.swift.AppLogger;
import com.github.raedev.swift.mvp.IPageView;
import com.github.raedev.swift.mvp.Pageable;
import com.github.raedev.swift.utils.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;

/**
 * 分页请求
 * @author RAE
 * @date 2020/11/2
 */
public abstract class PageObservable<T> implements Pageable<T> {

    private static final String TAG = "PageObservable";

    protected final int mStartPageIndex;
    protected int mPage;
    protected IPageView<T> mView;
    protected final List<T> mDataList = new ArrayList<>();

    public PageObservable(IPageView<T> view) {
        this(view, 1);
    }

    public PageObservable(IPageView<T> view, int startIndex) {
        this.mStartPageIndex = startIndex;
        this.mPage = startIndex;
        this.mView = view;
    }

    public void start() {
        this.mPage = this.mStartPageIndex;
        this.onLoadData(this.mPage);
    }

    public void loadMore() {
        this.onLoadData(this.mPage);
    }

    public void complete(List<T> dataList) {
        if (canResetData()) {
            onClearData();
        }

        // 没有更多数据
        if (isEmpty(dataList) && this.mPage > mStartPageIndex) {
            this.onNoMoreData();
            return;
        }

        // 加载数据完成
        this.onLoadDataComplete(dataList);
    }

    private boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    protected void onClearData() {
        this.mDataList.clear();
    }

    /**
     * 释放资源
     */
    public void destroy() {
        this.mDataList.clear();
        this.mView = null;
    }

    /**
     * 检查是否满足清空数据的条件
     * 1、当分页处于第一页的时候
     * @return 是否满足
     */
    protected boolean canResetData() {
        return this.mPage <= mStartPageIndex;
    }

    protected void onNoMoreData() {
        this.mView.onNoMoreData(this);
    }

    protected void onLoadData(int page) {
        try {
            onCreateObserver(mView, page)
                    .compose(Composer.uiThread(mView))
                    .subscribe(makeObserver());
        } catch (IOException e) {
            notifyError(e);
        }
    }

    /**
     * 创建回调监听
     */
    protected DisposableObserver<List<T>> makeObserver() {
        return new DisposableObserver<List<T>>() {
            @Override
            public void onNext(@NonNull List<T> data) {
                notifyData(data);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                notifyError(e);
            }

            @Override
            public void onComplete() {
            }
        };
    }

    /**
     * 通知错误消息
     */
    protected void notifyError(Throwable e) {
        AppLogger.e(TAG, "分页请求发生错误：" + e.getMessage(), e);
        if (mView == null) {
            return;
        }
        if (mPage > mStartPageIndex) {
            mView.onNoMoreData(this);
        } else {
            mView.onLoadDataError(this, e.getMessage());
        }
    }

    protected void notifyData(List<T> data) {
        if (mView == null) {
            return;
        }
        if (data == null || data.size() <= 0) {
            notifyError(new NullPointerException("暂无数据"));
        } else {
            complete(data);
        }
    }

    /**
     * 创建分页请求，已经自动关联生命周期
     * @param view 视图
     * @param page 页码
     * @return 观察者
     * @throws IOException 异常
     */
    protected abstract Observable<List<T>> onCreateObserver(IPageView<T> view, int page) throws IOException;

    /**
     * 加载数据成功
     * @param dataList 请求返回的数据
     */
    protected void onLoadDataComplete(List<T> dataList) {
        this.mDataList.addAll(dataList);
        this.mView.onLoadData(this, this.mDataList);
        this.mPage += 1;
    }

    @Override
    public int getCurrentPage() {
        return mPage;
    }

    @Override
    public boolean isLoadMore() {
        return mPage > mStartPageIndex;
    }

    @Override
    public boolean isEmpty() {
        return ArrayUtils.isEmpty(mDataList);
    }

    /**
     * 获取当前数据集合
     */
    @Override
    public List<T> getDataList() {
        return mDataList;
    }
}