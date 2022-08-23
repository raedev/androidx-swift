package androidx.swift.exception

/**
 * 应用程序运行时异常
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class AppRuntimeException : RuntimeException {

    /**
     * 业务错误代码
     */
    var errorCode: Int = 500
        private set

    constructor(message: String) : super(message)
    constructor(message: String, exception: Exception) : super(message, exception)
    constructor(errorCode: Int, message: String) : super(message) {
        this.errorCode = errorCode
    }
}