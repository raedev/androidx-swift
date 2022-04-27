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

import androidx.swift.mvp.ILoadMore;

import java.util.List;

@SuppressWarnings("unused")
public interface PageableObservable<T> extends ILoadMore {

    /**
     * 开始加载第一页
     */
    void start();

    /**
     * 获取当前分页数据
     * @return 数据
     */
    List<T> getDataList();

    /**
     * 获取当前分页
     * @return 当前分页
     */
    int getCurrentPage();

    /**
     * 是否为加载更多
     * @return 是否为加载更多
     */
    boolean isLoadMore();

    /**
     * 是否没有数据
     * @return 是否没有数据
     */
    boolean isEmptyData();
}
