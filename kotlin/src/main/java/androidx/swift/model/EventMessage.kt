package androidx.swift.model

/**
 * EventBus 携带的消息体
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class EventMessage(
    var code: Int,
    var data: Any
)