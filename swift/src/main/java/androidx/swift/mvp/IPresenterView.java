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
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

/**
 * @author rae
 */
public interface IPresenterView extends LifecycleOwner {

    /**
     * 获取生命周期
     * @return 生命周期
     */
    @NonNull
    @Override
    Lifecycle getLifecycle();

    /**
     * 获取Context
     * @return Context
     */
    @Nullable
    Context getContext();
}
