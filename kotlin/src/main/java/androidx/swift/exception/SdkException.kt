package androidx.swift.exception

import java.io.IOException

/**
 * 接口异常
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class SdkException : IOException {

    /**
     * Http状态码
     */
    var statusCode: Int = 500
        private set

    /**
     * 业务错误代码
     */
    var errorCode: Int = 500
        private set


    constructor(message: String) : super(message)

    constructor(message: String, exception: Exception) : super(message, exception)

    constructor(httpStatusCode: Int, errorCode: Int, message: String) : super(message) {
        this.statusCode = httpStatusCode
        this.errorCode = errorCode
    }

}