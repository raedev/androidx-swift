package androidx.swift

import android.app.Application
import androidx.swift.util.Utils

/**
 * 初始化入口
 * @author RAE
 * @date 2022/06/12
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object AndroidSwift {

    val context: Application = Utils.getApp()

    /**
     *  请在[Application]中初始化
     */
    fun init(context: Application) {
        Utils.init(context)
    }

}