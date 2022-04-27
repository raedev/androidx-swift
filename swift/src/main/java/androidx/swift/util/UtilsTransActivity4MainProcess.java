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

import android.app.Activity;
import android.content.Intent;

/**
 * <pre>
 *     author: blankj
 *     blog  : http://blankj.com
 *     time  : 2020/03/19
 *     desc  :
 * </pre>
 */
public class UtilsTransActivity4MainProcess extends UtilsTransActivity {

    public static void start(final TransActivityDelegate delegate) {
        start(null, null, delegate, UtilsTransActivity4MainProcess.class);
    }

    public static void start(final androidx.swift.util.Utils.Consumer<Intent> consumer,
                             final TransActivityDelegate delegate) {
        start(null, consumer, delegate, UtilsTransActivity4MainProcess.class);
    }

    public static void start(final Activity activity,
                             final TransActivityDelegate delegate) {
        start(activity, null, delegate, UtilsTransActivity4MainProcess.class);
    }

    public static void start(final Activity activity,
                             final Utils.Consumer<Intent> consumer,
                             final TransActivityDelegate delegate) {
        start(activity, consumer, delegate, UtilsTransActivity4MainProcess.class);
    }
}
