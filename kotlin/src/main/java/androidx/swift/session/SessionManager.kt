package androidx.swift.session

import android.content.Context

/**
 * Session管理器
 * @author RAE
 * @date 2022/06/12
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class SessionManager {

    companion object {
        /** 默认的Session提供者，请通过[Builder]进行初始化。 */
        lateinit var default: SessionDelegate
            private set
    }

    @Suppress("UNCHECKED_CAST")
    class Builder<T : Any>(private val context: Context, private val userClass: Class<T>) {

        private var isJsonProvider: Boolean = false
        private var enableEncrypt: Boolean = false
        private var sessionName: String? = null


        /**
         * 配置名称，默认为包名+session。
         * 当应用中有多个Session管理器的时候，使用SessionName进行区分。
         * @param name
         * @return
         */
        fun sessionName(name: String): Builder<T> {
            sessionName = name
            return this;
        }


        /**
         * 是否加密Session内容，防止root用户查看导致用户信息泄露
         * @param enableEncrypt
         * @return
         */
        fun enableEncrypt(enableEncrypt: Boolean): Builder<T> {
            this.enableEncrypt = enableEncrypt
            return this;
        }

        /**
         * Session管理器提供者使用JSON存储，默认是[SharedPreferencesSessionDelegate]
         * 除非一些特殊场景使用到（例如：Session信息保存到存储卡中）一般情况使用默认的即可。
         * @return
         */
        fun jsonProvider(): Builder<T> {
            this.isJsonProvider = true;
            return this;
        }

        /**
         *  构建默认的管理器并且对默认的[default]对象赋值。
         */
        fun build() {
            default = when (isJsonProvider) {
                true -> JsonSessionDelegate(context, sessionName, userClass)
                else -> SharedPreferencesSessionDelegate(context, sessionName, userClass)
            }
            val delegate = default as DefaultSessionDelegate<T>
            delegate.enableEncrypt = this.enableEncrypt
        }
    }

}