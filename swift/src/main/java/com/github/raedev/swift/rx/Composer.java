package com.github.raedev.swift.rx;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleTransformer;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Rx转换器
 * @author RAE
 * @date 2021/03/02
 */
public final class Composer {

    /**
     * 观察者和订阅者都在IO线程
     */
    public static <T> ObservableTransformer<T, T> ioThread() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    /**
     * 观察在主线程，订阅在IO线程
     */
    public static <T> ObservableTransformer<T, T> mainThread() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 提供更加方便的回调主线程
     */
    public static <T> UiObservableTransformer<T> uiThread(LifecycleOwner owner) {
        return new UiObservableTransformer<>(owner);
    }


    private static class UiObservableTransformer<T> implements ObservableTransformer<T, T>,
            FlowableTransformer<T, T>,
            SingleTransformer<T, T>,
            MaybeTransformer<T, T> {

        private final LifecycleOwner mOwner;

        public UiObservableTransformer(LifecycleOwner owner) {
            mOwner = owner;
        }

        private LifecycleTransformer<T> transformer() {
            return AndroidLifecycle
                    .createLifecycleProvider(mOwner)
                    .bindUntilEvent(Lifecycle.Event.ON_DESTROY);
        }

        @Override
        @NonNull
        public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
            upstream = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            return transformer().apply(upstream);
        }

        @Override
        @NonNull
        public Publisher<T> apply(@NonNull Flowable<T> upstream) {
            upstream = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            return transformer().apply(upstream);
        }

        @Override
        @NonNull
        public SingleSource<T> apply(@NonNull Single<T> upstream) {
            upstream = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            return transformer().apply(upstream);
        }

        @Override
        @NonNull
        public MaybeSource<T> apply(@NonNull Maybe<T> upstream) {
            upstream = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            return transformer().apply(upstream);
        }
    }
}
