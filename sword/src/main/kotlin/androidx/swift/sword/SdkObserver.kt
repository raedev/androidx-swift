package androidx.swift.sword

import androidx.swift.exception.SdkException
import io.reactivex.rxjava3.observers.DisposableObserver
import java.net.ConnectException

/**
 * SDK回调
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class SdkObserver<T> : DisposableObserver<T>() {

    override fun onError(e: Throwable) {
        var message = e.message ?: return
        when (e) {
            is ConnectException -> message = "网络连接错误，请检查网络设置"
            is SdkException -> {
                onError(message, e.errorCode)
                return
            }
            is RuntimeException -> {
                // 解析Rx包装多了的一层
                message = e.cause!!.message ?: message
            }
        }
        onError(message)
    }

    protected open fun onError(message: String) = Unit

    protected open fun onError(message: String, errorCode: Int) = Unit

    override fun onComplete() = Unit
}