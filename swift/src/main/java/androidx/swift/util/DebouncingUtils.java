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

package androidx.swift.util;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2020/09/01
 *     desc  : utils about debouncing
 * </pre>
 */
public class DebouncingUtils {

    private static final int               CACHE_SIZE               = 64;
    private static final Map<String, Long> KEY_MILLIS_MAP           = new ConcurrentHashMap<>(CACHE_SIZE);
    private static final long              DEBOUNCING_DEFAULT_VALUE = 1000;

    private DebouncingUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the view is not in a jitter state.
     *
     * @param view The view.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isValid(@NonNull final View view) {
        return isValid(view, DEBOUNCING_DEFAULT_VALUE);
    }

    /**
     * Return whether the view is not in a jitter state.
     *
     * @param view     The view.
     * @param duration The duration.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isValid(@NonNull final View view, final long duration) {
        return isValid(String.valueOf(view.hashCode()), duration);
    }

    /**
     * Return whether the key is not in a jitter state.
     *
     * @param key      The key.
     * @param duration The duration.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isValid(@NonNull String key, final long duration) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("The key is null.");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("The duration is less than 0.");
        }
        long curTime = SystemClock.elapsedRealtime();
        clearIfNecessary(curTime);
        Long validTime = KEY_MILLIS_MAP.get(key);
        if (validTime == null || curTime >= validTime) {
            KEY_MILLIS_MAP.put(key, curTime + duration);
            return true;
        }
        return false;
    }

    private static void clearIfNecessary(long curTime) {
        if (KEY_MILLIS_MAP.size() < CACHE_SIZE) return;
        for (Iterator<Map.Entry<String, Long>> it = KEY_MILLIS_MAP.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Long> entry = it.next();
            Long validTime = entry.getValue();
            if (curTime >= validTime) {
                it.remove();
            }
        }
    }
}
