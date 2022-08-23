package androidx.swift.dict

/**
 * 字典节点
 * @author RAE
 * @date 2022/06/28
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class DictNode(
    // 字典编码
    open var code: String,
    // 字典值
    open var value: String
) {
    // 所在节点
    var level: Int = 0

    // 字典标题名称
    var name: String? = null

    // 其他数据
    var metaData: Any? = null

    // 父节点
    var parentNode: DictNode? = null

    // 子节点
    var children: List<DictNode>? = null
}