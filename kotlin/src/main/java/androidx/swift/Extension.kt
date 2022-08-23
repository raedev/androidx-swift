/**
 * 扩展方法
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
package androidx.swift

import java.math.RoundingMode

/**
 * 四舍五入
 * @param scale 保留位数，默认是保留小数后2位
 */
fun Double.round(scale: Int = 2): Double {
    return this.toBigDecimal().setScale(scale, RoundingMode.HALF_UP).toDouble()
}