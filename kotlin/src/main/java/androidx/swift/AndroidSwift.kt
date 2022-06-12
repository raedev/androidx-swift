package androidx.swift

import android.app.Application

/**
 * 初始化入口
 * @author RAE
 * @date 2022/06/12
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
object AndroidSwift {

    lateinit var context: Application
        private set;

    /**
     *  请在[Application]中初始化
     */
    fun init(context: Application) {
        this.context = context;
    }

}