package androidx.swift.sword.http

import androidx.swift.util.BeanUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.nio.charset.StandardCharsets

/**
 * JSON请求Body
 * @author RAE
 * @date 2022/08/05
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class JsonBody(private val obj: Any) : RequestBody() {

    override fun contentType(): MediaType? {
        return "application/json".toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        val json = BeanUtils.toJson(obj)
        sink.writeString(json, StandardCharsets.UTF_8)
    }

    /**
     * 转成FormBody
     */
    fun toMap(): Map<String, String> {
        val json = BeanUtils.toJson(obj)
        val map = BeanUtils.toStringMap(json)
        val result = mutableMapOf<String, String>()
        map.forEach {
            it.value?.let { value ->
                result[it.key] = value
            }
        }
        return result
    }
}