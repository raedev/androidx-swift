package androidx.swift.sword.ssl

import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory

/**
 * 忽略错误证书工厂
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object SSLCertificateFactory {

    /**
     * 忽略域名校验
     */
    val verifier = HostnameVerifier { _, _ -> true }

    /**
     * 默认忽略证书
     */
    val factory: SSLSocketFactory
        get() {
            val context = SSLContext.getInstance("TLS")
            context.init(null, arrayOf(SSLTrustManager()), SecureRandom())
            return context.socketFactory
        }

    /**
     * 自签名证书
     */
    fun getSelfFactory(inputStream: InputStream): SSLSocketFactory {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificate = certificateFactory.generateCertificate(inputStream)
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("trust", certificate)
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, null)
        return sslContext.socketFactory
    }
}