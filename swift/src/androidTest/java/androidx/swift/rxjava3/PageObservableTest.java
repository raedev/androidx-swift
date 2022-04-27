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

package androidx.swift.rxjava3;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.swift.mvp.IPageView;
import androidx.swift.rxjava3.paging.PageObservable;
import androidx.swift.rxjava3.paging.PageableObservable;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

@RunWith(AndroidJUnit4ClassRunner.class)
public class PageObservableTest {

    private Observable<List<DemoModel>> demoPageObservable(int page) {
        return Observable.create(emitter -> {
            Log.w("Rae", "调用业务里面的分页逻辑");
            List<DemoModel> data = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                data.add(new DemoModel("分页_" + page + "_" + i));
            }
            emitter.onNext(data);
            emitter.onComplete();
        });
    }

    @Test
    public void testPage() {
        IPageView<DemoModel> pageView = new IPageView<DemoModel>() {
            final Lifecycle sLifecycle = new TestLifecycleOwner().getLifecycle();

            @Override
            public void onLoadData(PageableObservable<DemoModel> page, List<DemoModel> data) {
                for (DemoModel item : data) {
                    Log.i("rae", "加载数据成功：" + item.name);
                }
            }

            @Override
            public void onLoadDataError(PageableObservable<DemoModel> pageable, String message, Throwable e) {
                Log.e("rae", "加载数据异常：" + message);
            }

            @Override
            public void onNoMoreData(PageableObservable<DemoModel> pageable) {
                Log.e("rae", "没有数据了");
            }

            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                return sLifecycle;
            }
        };
        final PageObservable<DemoModel> observable = (PageObservable<DemoModel>) PageObservable.create(pageView, this::demoPageObservable);
        observable.doOnComplete(() -> {
            if (observable.getCurrentPage() <= 3) {
                observable.loadMore();
            }
        });
        observable.start();
    }

    static class TestLifecycleOwner implements LifecycleOwner {

        private final Lifecycle mLifecycle = LifecycleRegistry.createUnsafe(this);

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycle;
        }
    }

    static class DemoModel {
        public String name;

        public DemoModel(String name) {
            this.name = name;
        }
    }
}
