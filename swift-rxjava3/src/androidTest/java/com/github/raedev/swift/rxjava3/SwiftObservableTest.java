package com.github.raedev.swift.rxjava3;

import android.util.Log;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @author RAE
 * @since 2020/12/30
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class SwiftObservableTest {

    private CountDownLatch mCountDownLatch = new CountDownLatch(1);

    private static class DemoModel {

    }

    @Test
    public void testOne() throws InterruptedException {

        SwiftObservable.create(new ObservableOnSubscribe<DemoModel>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<DemoModel> emitter) throws Throwable {
                Log.i("rae", "onNext");
                emitter.onNext(new DemoModel());
                emitter.onComplete();
            }
        }).doFinally(new Action() {
            @Override
            public void run() throws Throwable {
                Log.i("rae", "doFinally");
                mCountDownLatch.countDown();
            }
        }).subscribe(new Consumer<DemoModel>() {
            @Override
            public void accept(DemoModel demoModel) throws Throwable {
                Log.i("rae", "accept subscribe");
            }
        });

        mCountDownLatch.await();
        Log.i("rae", "测试结束");
    }

    @Test
    public void testTwo() throws InterruptedException {

        mCountDownLatch.await();
        Log.i("rae", "测试结束");
    }
}
