package androidx.swift.sword.provider

import androidx.swift.sword.env.ApiEnvironment
import androidx.swift.sword.env.ApiEnvironmentUrl
import androidx.swift.sword.interceptor.BaseLogInterceptor
import androidx.swift.sword.interceptor.EnvironmentInterceptor
import androidx.swift.sword.ssl.SSLCertificateFactory
import androidx.swift.sword.ssl.SSLTrustManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 *  Retrofit 生产工厂，支持多环境多域名动态切换，URL格式：@{host}
 *  示例代码：
 *  比如用户微服务：@{user}/user/login
 *  然后通过ApiEnvironmentUrl.hosts 添加环境主机： hosts['user'] = http://api.user.com/
 * @author RAE
 * @date 2022/08/21
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object RetrofitFactory {

    /**
     * 缓存的Retrofit
     */
    private val cacheRetrofitMap = mutableMapOf<String, Retrofit>()

    class Builder internal constructor(
        /** 基础路径 */
        private val baseUrl: String,
        /** 是否启动环境拦截 */
        private val enableEnvironment: Boolean
    ) {

        private val okb = OkHttpClient.Builder()
        private val rfb = Retrofit.Builder()
        private val logInterceptor = BaseLogInterceptor("Retrofit.Http")

        private val environmentInterceptor = EnvironmentInterceptor()

        /**
         * 是否调试模式，当设置为TRUE时自动将当前环境设置为
         */
        fun debug(debug: Boolean) = apply {
            logInterceptor.enable = debug
            if (debug) environment(ApiEnvironment.DEBUG) else environment(ApiEnvironment.PROD)
        }

        /**
         * 日志输出
         */
        fun tag(tag: String) = apply {
            logInterceptor.tag = tag
        }

        /**
         * 切换接口环境
         */
        fun environment(environment: ApiEnvironment) = apply {
            environmentInterceptor.environment = environment
        }

        /**
         * 添加接口环境
         */
        fun addEnvironment(env: ApiEnvironmentUrl) = apply {
            environmentInterceptor.environmentList[env.environment] = env
        }

        /**
         * 连接超时时间（秒）
         */
        fun connectTimeout(timeout: Long): Builder = apply {
            okb.connectTimeout(timeout, TimeUnit.SECONDS)
        }

        /**
         * 读流超时时间（秒）
         */
        fun readTimeout(timeout: Long): Builder = apply {
            okb.readTimeout(timeout, TimeUnit.SECONDS)
        }

        /**
         * 写流超时时间（秒）
         */
        fun writeTimeout(timeout: Long): Builder = apply {
            okb.writeTimeout(timeout, TimeUnit.SECONDS)
        }

        /**
         * 忽略证书错误
         */
        fun ignoreSSL() = apply {
            okb.sslSocketFactory(SSLCertificateFactory.factory, SSLTrustManager())
            okb.hostnameVerifier(SSLCertificateFactory.verifier)
        }

        /**
         *  添加拦截器
         */
        fun addInterceptor(interceptor: Interceptor): Builder = apply {
            okb.addInterceptor(interceptor)
        }

        /**
         * CallAdapterFactory
         */
        fun addCallAdapterFactory(factory: CallAdapter.Factory) = apply {
            rfb.addCallAdapterFactory(factory)
        }

        /**
         * ConverterFactory
         */
        fun addConverterFactory(factory: Converter.Factory) = apply {
            rfb.addConverterFactory(factory)
        }

        /**
         * OkHttpClient
         */
        fun client(): OkHttpClient {
            // 启用环境拦截
            if (enableEnvironment) okb.addInterceptor(environmentInterceptor)
            // 默认添加日志拦截器
            okb.addInterceptor(logInterceptor)
            return okb.build()
        }

        /**
         * 构建
         * @param cacheName 缓存的名称
         */
        fun build(cacheName: String? = null): Retrofit {
            return rfb.baseUrl(baseUrl).client(client()).build().also {
                cacheRetrofitMap[cacheName ?: abs(it.hashCode()).toString()] = it
            }
        }
    }

    /**
     * 创建Builder
     */
    fun newBuilder(baseUrl: String): Builder {
        return Builder(baseUrl, false)
    }

    /**
     * 创建自动检查环境的Builder
     */
    fun newBuilder(): Builder {
        return Builder("http://retrofit.env/", true)
    }

    /**
     * 缓存缓存的Retrofit
     */
    fun getRetrofit(name: String): Retrofit? {
        return cacheRetrofitMap[name]
    }

    /**
     * 重新配置接口环境
     */
    fun resetEnvironment(retrofit: Retrofit, env: ApiEnvironmentUrl) {
        val client = retrofit.callFactory() as OkHttpClient
        client.interceptors.forEach {
            if (it !is EnvironmentInterceptor) return@forEach
            it.environmentList[env.environment] = env
        }
    }

    /**
     * 重新配置接口环境
     */
    fun resetEnvironment(name: String, env: ApiEnvironmentUrl) {
        getRetrofit(name)?.let { resetEnvironment(it, env) }
    }

    /**
     * 重新配置所有接口环境
     */
    fun resetEnvironment(env: ApiEnvironmentUrl) {
        cacheRetrofitMap.forEach { map ->
            val interceptors = (map.value.callFactory() as OkHttpClient).interceptors
            val item = interceptors.singleOrNull { it is EnvironmentInterceptor } ?: return@forEach
            item as EnvironmentInterceptor
            item.environmentList[env.environment] = env
        }
    }

    /**
     * 获取所有的缓存
     */
    fun getRetrofits(): Map<String, Retrofit> {
        return cacheRetrofitMap
    }

    /**
     * 从缓存中创建
     */
    inline fun formCache(name: String, block: () -> Builder): Retrofit {
        return getRetrofit(name) ?: block.invoke().build(name)
    }

}