package androidx.swift.sword.interceptor

import android.os.Build
import androidx.swift.sword.http.JsonBody
import androidx.swift.sword.utils.RequestSignUtils
import androidx.swift.util.AppUtils
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 请求拦截器
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class BaseRequestInterceptor : Interceptor {

    protected lateinit var request: Request

    /** 是否启动签名 */
    open var enableSign = false

    override fun intercept(chain: Interceptor.Chain): Response {
        this.request = chain.request()
        val builder = this.request.newBuilder()
        // 添加版本号
        builder.addHeader("AppVersionCode", AppUtils.getAppVersionCode().toString())
        builder.addHeader("AppVersionName", AppUtils.getAppVersionName())
        builder.addHeader("AppPackageName", AppUtils.getAppPackageName())
        builder.addHeader("AppSdkVersion", Build.VERSION.SDK_INT.toString())
        // 授权信息
        getAuthorization()?.let { token ->
            builder.addHeader("Authorization", token)
        }
        // 签名
        if (enableSign) {
            generateSign()?.let { sign -> builder.addHeader("AppSign", sign) }
        }
        // 自定义处理
        onHttpBuilder(builder)
        return chain.proceed(builder.build())
    }

    protected open fun onHttpBuilder(builder: Request.Builder) {

    }

    protected open fun generateSign(password: String = "androidx.swift.sword"): String? {
        val maps = mutableMapOf<String, String>()
        request.url.queryParameterNames.forEach { name ->
            maps[name] = request.url.queryParameter(name)!!
        }
        when (val body = request.body) {
            is FormBody -> {
                for (i in 0 until body.size) {
                    val name = body.encodedName(i)
                    val value = body.encodedValue(i)
                    maps[name] = value
                }
            }
            is JsonBody -> {
                maps.putAll(body.toMap())
            }
        }
        // 执行签名
        return RequestSignUtils.generateSign(password, maps)
    }

    protected open fun getAuthorization(): String? = null
}