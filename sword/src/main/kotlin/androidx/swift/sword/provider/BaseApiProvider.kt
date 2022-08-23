package androidx.swift.sword.provider

import retrofit2.Retrofit

/**
 * 接口提供者
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class BaseApiProvider(
    protected val retrofit: Retrofit
)