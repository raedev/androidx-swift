package androidx.swift.sword.env

/**
 * 接口环境地址
 * @author RAE
 * @date 2022/08/21
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class ApiEnvironmentUrl(

    /** 环境 */
    val environment: ApiEnvironment,

    /** 基础路径 */
    val baseUrl: String,

    /** 主机列表,比如一个环境下使用了多个域名，通过主机名称来对应所在路径 */
    val hosts: Map<String, String> = mutableMapOf()
)