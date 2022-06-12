package androidx.swift.session

/**
 * Session管理器
 * @author RAE
 * @date 2022/06/12
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class SessionManager(provider: SessionProvider) : SessionProvider by provider {

    companion object {
        lateinit var sessionManager: SessionManager
            private set;
    }

    class Builder<in T> {

        fun build(): SessionManager {

            throw NotImplementedError("暂未实现")
        }
    }


}