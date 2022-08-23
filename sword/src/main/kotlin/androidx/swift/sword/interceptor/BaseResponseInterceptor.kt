package androidx.swift.sword.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 响应拦截器
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class BaseResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return onResponseIntercept(chain, request, response)
    }

    protected open fun onResponseIntercept(
        chain: Interceptor.Chain,
        request: Request,
        response: Response
    ): Response {
        return response
    }
}