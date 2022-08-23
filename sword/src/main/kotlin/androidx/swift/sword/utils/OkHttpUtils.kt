package androidx.swift.sword.utils

import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http.RealResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * OKHttp帮助类
 * @author RAE
 * @date 2022/08/18
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object OkHttpUtils {

    /**
     * 复制响应流数据
     */
    fun copyBufferBody(body: ResponseBody?): Buffer? {
        try {
            body ?: return null
            val source = body.source()
            source.request(Long.MAX_VALUE)
            return source.buffer.clone()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 复制字符串流
     */
    fun copyStringBody(body: ResponseBody?): String? {
        return copyBufferBody(body)?.readString(StandardCharsets.UTF_8)
    }

    /**
     * 创建新的响应
     */
    fun newResponse(source: Response, content: String): Response {
        val contentType = source.body?.contentType().toString()
        val buffer = Buffer().buffer.writeString(content, StandardCharsets.UTF_8)
        val length = content.length.toLong()
        return source.newBuilder()
            .body(RealResponseBody(contentType, length, buffer))
            .build()
    }

}