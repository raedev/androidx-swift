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

import android.util.Log;

public final class Logger {

    public static boolean DEBUG = true;
    public static String TAG = "Swift";

    private static String newTag(String tag) {
        return TAG.concat(".").concat(tag);
    }

    public static void e(String tag, String msg, Throwable e) {
        Log.e(newTag(tag), msg, e);
        writeToFile(newTag(tag), msg, e);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String msg, Throwable e) {
        e(TAG, msg, null);
    }

    public static void e(String msg) {
        e(msg, (Throwable) null);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(newTag(tag), msg);
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(newTag(tag), msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(TAG.concat(".").concat(tag), msg);
        }
    }

    private static void writeToFile(String tag, String msg, Throwable e) {
        if (e == null) {
            LogUtils.file(LogUtils.E, tag, msg);
        } else {
            LogUtils.file(LogUtils.E, tag, msg + "\r\n" + Log.getStackTraceString(e));
        }
    }
}
