package androidx.swift.sword.utils

import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.swift.util.ConvertUtils
import java.net.URLEncoder
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 请求签名加密工具
 * @author ChenRui
 * @date 2017/5/23 0023 15:14
 */
object RequestSignUtils {
    private const val ENCODE_TYPE_HEX = 0
    private const val ENCODE_TYPE_BASE64 = 1
    private const val DEFAULT_CHARSET = "UTF-8"

    // 默认HMAC 算法
    private const val DEFAULT_HMAC = "HmacSHA256"

    /**
     * 密码加密
     * @param password 密码
     * @return 加密后的密码
     */
    fun encrypt(password: String): String {
        return Base64.encodeToString(password.toByteArray(), Base64.DEFAULT)
    }

    /**
     * AES CBC模式加密
     * @param content 加密内容
     * @param key 密码 16位
     * @param iv 偏移量 16位
     */
    fun encryptAES(content: String, key: String, iv: String): String {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(key) || TextUtils.isEmpty(iv)) {
            return content
        }
        if (key.length < 16 || iv.length < 16) {
            return content
        }
        try {
            val raw = key.toByteArray()
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") //"算法/模式/补码方式"
            val ivParam = IvParameterSpec(iv.toByteArray()) //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParam)
            val encrypted = cipher.doFinal(content.toByteArray())
            return Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return content
    }

    /**
     * AES CBC模式解密
     * @param content 解密文本
     * @param key 16位密码
     * @param iv 16位偏移量
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptAES(content: String?, key: String, iv: String): String {
        val raw = key.toByteArray()
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") //"算法/模式/补码方式"
        val ivParam = IvParameterSpec(iv.toByteArray()) //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParam)
        val encrypted = Base64.decode(content, Base64.DEFAULT) //先用base64解密
        val original = cipher.doFinal(encrypted)
        return String(original)
    }

    /**
     * byte to hex
     * @param bytes
     * @return
     */
    fun bytesToHex(bytes: ByteArray): String {
        return ConvertUtils.bytes2HexString(bytes, true)
    }

    /**
     * HMAC加密
     * @param KEY_MAC 算法类型，参考常量KEY_MAC_*
     * @param key 私钥
     * @param content 加密内容
     * @param encodeType [.ENCODE_TYPE_HEX]
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptHMAC(KEY_MAC: String?, key: String, content: String, encodeType: Int): String {
        val mac = Mac.getInstance(KEY_MAC)
        val secretKey: SecretKey =
            SecretKeySpec(key.toByteArray(charset(DEFAULT_CHARSET)), mac.algorithm)
        mac.init(secretKey)
        val data = mac.doFinal(content.toByteArray(charset(DEFAULT_CHARSET)))
        return if (encodeType == ENCODE_TYPE_BASE64) {
            Base64.encodeToString(data, Base64.NO_WRAP)
        } else bytesToHex(
            data
        )
    }

    /**
     * 默认HMAC加密
     * @param key 私钥
     * @param content 加密内容
     * @return
     */
    fun encryptHMAC(key: String, content: String, encodeType: Int): String {
        return try {
            encryptHMAC(DEFAULT_HMAC, key, content, encodeType)
        } catch (ex: Exception) {
            content
        }
    }


    /**
     * 组合参数
     * @param map
     * @return 如：key1Value1Key2Value2....
     */
    fun groupStringParam(map: Map<String, String?>?): String? {
        if (map == null) {
            return null
        }
        val sb = StringBuilder()
        var i = 0
        for ((key, value) in map) {
            if (value == null || TextUtils.isEmpty(value)) {
                continue
            }
            if (i != 0) {
                sb.append("&")
            }
            sb.append(key).append("=").append(value)
            i++
        }
        return sb.toString()
    }
    /**
     * 转成URL 参数
     * @param map
     * @return 如：key1=value1&key2=value2
     */
    /**
     * 转成URL 参数
     * @param map
     * @return
     */
    @JvmOverloads
    fun toStringParams(map: Map<String?, String?>, enableUrlEncode: Boolean = false): String? {
        val builder = Uri.Builder()
        for (item in map.entries) {
            if (TextUtils.isEmpty(item.key) || TextUtils.isEmpty(item.value)) {
                continue
            }
            var value = item.value
            if (enableUrlEncode) {
                value = URLEncoder.encode(value)
            }
            builder.appendQueryParameter(item.key, value)
        }
        return builder.build().query
    }

    /**
     * 对map 参数签名后返回，采用HMAC加密方式
     * @return 签好名的sign
     */
    fun generateSign(appSecret: String, map: Map<String, String?>?): String? {
        return generateSign(false, ENCODE_TYPE_BASE64, appSecret, map)
    }

    /**
     * 对map 参数签名后返回，采用HMAC加密方式
     * @param debugable TRUE输出调试信息
     * @param encodeType 编码类型
     * @param appSecret 密钥
     * @param map 参数信息
     * @return 签好名的sign
     */
    fun generateSign(
        debugable: Boolean,
        encodeType: Int,
        appSecret: String,
        map: Map<String, String?>?
    ): String? {
        if (map == null || TextUtils.isEmpty(appSecret)) {
            return null
        }

        // 1、对data参数进行重新排序
        //  按照红黑树（Red-Black tree）字母大小排序的 NavigableMap 实现
        val sortMap = TreeMap<String, String?> { o1: Any, o2: Any ->
            o1.toString().compareTo(o2.toString())
        }

        sortMap.putAll(map)


        // 2、拼接参数：key1=Value1&key2=Value2
        val urlParams = groupStringParam(sortMap)
        if (debugable) {
            Log.i("rae", "------> 签名密钥：$appSecret")
            Log.i("rae", "------> 1、排序后拼接参数：$urlParams")
        }

        // 3、拼接准备排序： stringURI + stringParams + AppSecret
        val signString = "$urlParams&secret=$appSecret"
        if (debugable) {
            Log.i("rae", "------> 2、签名的字符串：$signString")
        }

        // 4、私钥签名
        var sign = encryptHMAC(appSecret, signString, encodeType)

        // HEX 转成小写
        if (!TextUtils.isEmpty(sign) && encodeType == ENCODE_TYPE_HEX) {
            sign = sign.lowercase(Locale.getDefault())
        }
        if (debugable) {
            val sb = StringBuilder()
            for ((key, value) in sortMap) {
                sb.append(key)
                sb.append(":")
                sb.append(value)
                sb.append("\n")
            }
            Log.i("rae", "------> 3、签名参数：\n$sb")
            Log.i("rae", "------> 4、签名结果：$sign")
        }
        return sign
    }
}