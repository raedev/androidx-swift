package androidx.swift.mvp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.swift.rxjava3.core.Composer;

import java.util.Objects;

import io.reactivex.rxjava3.core.ObservableTransformer;


/**
 * MVP Presenter
 * @author RAE
 * @date 2018/4/18
 */
public abstract class BasePresenter<V extends IPresenterView> implements IPresenter, ILoadMore, LifecycleEventObserver {

    private V mView;

    public BasePresenter(@NonNull V view) {
        this.bindView(view);
    }

    public void bindView(@NonNull V view) {
        this.mView = view;
        view.getLifecycle().addObserver(this);
    }

    @NonNull
    public V getView() {
        return Objects.requireNonNull(mView);
    }

    protected Context getContext() {
        return mView.getContext();
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

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.getLifecycle().removeObserver(this);
            onDestroy();
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
