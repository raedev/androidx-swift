package androidx.swift.sword.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 *
 * @author RAE
 * @date 2022/08/05
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class BaseLogInterceptor(var tag: String) : Interceptor, HttpLoggingInterceptor.Logger {

    var enable: Boolean = true

    private val source = HttpLoggingInterceptor(this)

    init {
        source.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (enable) source.intercept(chain) else chain.proceed(chain.request())
    }

    override fun log(message: String) {
        Log.d(tag, message)
    }
}