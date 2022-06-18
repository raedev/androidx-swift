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

import androidx.lifecycle.LifecycleOwner;
import androidx.swift.rxjava3.paging.PageableObservable;

import java.util.List;

/**
 * @author rae
 */
public interface IPageView<T> extends LifecycleOwner {

    /**
     * 加载数据
     * @param pageable 分页
     * @param data 数据
     */
    void onLoadData(PageableObservable<T> pageable, List<T> data);

    /**
     * 加载数据错误
     * @param pageable 分页
     * @param message 错误消息
     * @param e 异常信息
     */
    void onLoadDataError(PageableObservable<T> pageable, String message, Throwable e);

    /**
     * 没有更多数据
     * @param pageable 分页
     */
    void onNoMoreData(PageableObservable<T> pageable);

}
