package androidx.swift.model

/**
 * 空实体
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class Empty private constructor() {
    companion object {
        fun value() = Empty()
    }
}