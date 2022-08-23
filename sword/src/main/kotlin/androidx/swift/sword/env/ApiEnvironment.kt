package androidx.swift.sword.env

/**
 * 接口环境
 * @author RAE
 * @date 2022/08/21
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
enum class ApiEnvironment {

    /** 本地调试环境 */
    DEBUG,

    /** 测试环境 */
    TEST,

    /** 准生产环境 */
    FAT,

    /** 正式环境 */
    PROD,

}