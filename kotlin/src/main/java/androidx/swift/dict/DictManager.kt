package androidx.swift.dict

/**
 * 字典管理器，字典是一个节点树，有父节点也有子节点。
 * @author RAE
 * @date 2022/06/28
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class DictManager private constructor(name: String) {
    companion object {
        operator fun get(name: String): DictManager {
            return DictManager(name)
        }
    }
}