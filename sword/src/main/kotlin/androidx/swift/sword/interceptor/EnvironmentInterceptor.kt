package androidx.swift.sword.interceptor

import androidx.swift.sword.env.ApiEnvironment
import androidx.swift.sword.env.ApiEnvironmentUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.regex.Pattern

/**
 * 环境变量
 * @author RAE
 * @date 2022/08/21
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class EnvironmentInterceptor(var environment: ApiEnvironment = ApiEnvironment.PROD) : Interceptor {

    var environmentList = mutableMapOf<ApiEnvironment, ApiEnvironmentUrl>()
    private val envUrl = "http://retrofit.env/"
    private val pattern = Pattern.compile("@\\{\\w+\\}")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var url = request.url.toString()
        if (!url.startsWith(envUrl)) return chain.proceed(request)
        if (!environmentList.containsKey(environment)) throw NullPointerException("请先添加接口环境:$environment")
        val env = environmentList[environment]!!

        // 匹配主机名 @{host}
        val matcher = pattern.matcher(url)
        // 没有配置主机的情况
        if (!matcher.find() || env.hosts.isEmpty()) {
            url = env.baseUrl + url.substring(envUrl.length)
            return chain.proceed(request.newBuilder().url(url).build())
        }

        val name = matcher.group()
            .replace("@", "")
            .replace("{", "")
            .replace("}", "")

        if (!env.hosts.containsKey(name)) throw NullPointerException("当前接口没有配置主机：$name，接口地址：$url")
        url = env.hosts[name] + url.substring(envUrl.length)

        return chain.proceed(request.newBuilder().url(url).build())
    }
}